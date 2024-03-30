####
# docker build -f api.Containerfile --no-cache --progress=plain -t caravanacloud/ecomarkets-api:$(cat VERSION) .
# docker run -i --rm -p 9090:9090 --network=host  caravanacloud/ecomarkets-api:$(cat VERSION)
# docker login --username=caravanacloud
# docker push caravanacloud/ecomarkets-api:$(cat VERSION)
###

### BUILD STAGE ###
FROM fedora:39 as build

# Root level
USER root
ENV PATH="/usr/bin:${PATH}"
RUN bash -c "dnf install -y zip unzip"

## Create User
ARG USERNAME=container-user
ARG USER_UID=1000
ARG USER_GID=$USER_UID
RUN groupadd --gid $USER_GID $USERNAME 
RUN useradd --uid $USER_UID --gid $USER_GID -m $USERNAME -d "/home/$USERNAME"

# User level
USER $USERNAME

## Setup JDK
ENV JAVA_HOME=/home/$USERNAME/.sdkman/candidates/java/current
ARG JAVA_SDK="21.0.2-graal"
RUN curl -s "https://get.sdkman.io" | bash 
RUN bash -c ". $HOME/.sdkman/bin/sdkman-init.sh \
    && sdk install java $JAVA_SDK \
    && sdk default java $JAVA_SDK \
    && sdk install quarkus \
    && sdk install maven \
    "

## Copy source code
RUN mkdir -p "/home/$USERNAME/quarkus-app"
WORKDIR "/home/$USERNAME/quarkus-app"
COPY --chown=$USERNAME .. .

## Build
ARG MVN_XOPTS="-DskipTests -fn -B -ntp -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn"
ENV QUARKUS_OIDC_PROVIDER="google"
RUN bash -c ". $HOME/.sdkman/bin/sdkman-init.sh \
    && mvn $MVN_XOPTS install"

## Link
# jdeps --ignore-missing-deps --multi-release=21 --list-deps target/ecomarkets-api-runner.jar
# JDK removed internal API/com.sun.tools.javac.code,java.base/sun.security.x509,java.compiler,java.desktop,java.instrument,java.logging,java.management,java.naming,java.rmi,java.scripting,java.security.jgss,java.security.sasl,java.sql,java.transaction.xa,java.xml,jdk.compiler/com.sun.tools.javac.code,jdk.compiler/com.sun.tools.javac.tree,jdk.compiler/com.sun.tools.javac.util,jdk.jconsole,jdk.unsupported
ARG JAVA_MODULES="java.base,java.compiler,java.desktop,java.logging,java.management,java.naming,java.rmi,java.security.sasl,java.sql,java.transaction.xa,java.xml,jdk.compiler,jdk.compiler,jdk.unsupported,org.graalvm.nativeimage,org.graalvm.word"
RUN bash -c ". $HOME/.sdkman/bin/sdkman-init.sh \
    && jlink --add-modules $JAVA_MODULES --compress=zip-2 --no-header-files --no-man-pages --output ./target/jre"

RUN find "/home/container-user/quarkus-app/"

### RUNTIME STAGE ###
FROM fedora:39

USER root
# RUN bash -c "dnf install -y zip unzip"

# Create user
ARG USERNAME=container-user
ARG USER_UID=1000
ARG USER_GID=$USER_UID
RUN groupadd --gid $USER_GID $USERNAME 
RUN useradd --uid $USER_UID --gid $USER_GID -m $USERNAME -d "/home/$USERNAME"
USER $USERNAME

# Copy binary
RUN mkdir -p "/home/$USERNAME/quarkus-app"
WORKDIR "/home/$USERNAME/quarkus-app"

COPY --from=build --chown=$USERNAME "/home/container-user/quarkus-app/target/jre" "/home/container-user/quarkus-app/target/jre"
COPY --from=build --chown=$USERNAME "/home/container-user/quarkus-app/ecomarkets-api/target/ecomarkets-api-runner.jar" "/home/container-user/quarkus-app/ecomarkets-api/target/ecomarkets-api-runner.jar"


EXPOSE 9090

# ARG JAVA_XARGS="-XX:+UseSerialGC -XX:InitialRAMPercentage=95.0"
# ARG JAVA_XARGS="-XX:+UseZGC -XX:InitialRAMPercentage=90.0  -XX:MaxRAMPercentage=95.0"
ARG JAVA_XARGS=""
ENTRYPOINT /home/container-user/quarkus-app/target/jre/bin/java $JAVA_XARGS -jar /home/container-user/quarkus-app/ecomarkets-api/target/ecomarkets-api-runner.jar 