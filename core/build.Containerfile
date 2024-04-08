FROM fedora:39 as build

# Root level
USER root
ENV PATH="/usr/bin:${PATH}"
RUN bash -c "dnf install -y zip unzip nodejs npm"

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
RUN mkdir -p "/home/$USERNAME/ecomarkets"
WORKDIR "/home/$USERNAME/ecomarkets"
COPY --chown=$USERNAME . .

## Build MVN
ARG MVN_XOPTS="-DskipTests -Pproduction -Dquarkus.profile=prod -fn -B -ntp -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn"
ENV QUARKUS_OIDC_PROVIDER="google"
RUN bash -c ". $HOME/.sdkman/bin/sdkman-init.sh \
    && mvn $MVN_XOPTS install"

## Build NPM
WORKDIR "/home/container-user/ecomarkets/app"
RUN npm install
RUN npm run build

