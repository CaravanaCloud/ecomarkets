package ecomarkets.lex.bot;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@StaticInitSafe
@ConfigMapping(prefix="ecomarkets.bot")
public interface BotsConfig {
    @WithName("managedPolicyArn")
    @WithDefault("arn:aws:iam::aws:policy/AdministratorAccess")
    String getManagedPolicyArn();


    @WithName("sessionTTL")
    @WithDefault("86400")
    int getSessionTTL();

    @WithDefault("0.40")
    double getNLUConfidenceThreshold();
}
