package ecomarkets.lex;

import static ecomarkets.lex.StackUtils.resourceId;
import static ecomarkets.lex.StackUtils.resourceName;

import java.util.List;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.lex.CfnBot;
import software.amazon.awscdk.services.lex.CfnBot.BotLocaleProperty;
import software.constructs.Construct;

@ApplicationScoped
public class LexStack extends Stack {

    public LexStack(final Construct scope, final String id, final StackProps props, final LexConfig cfg) {
        super(scope, id, props);

        var managedPolicyArn = cfg.getManagedPolicyArn();
        
        var adminPolicy = ManagedPolicy.fromManagedPolicyArn(this, resourceId("RetailerBotPolicy"), managedPolicyArn);

        var role = Role.Builder.create(this,  resourceId("RetailerBotRole"))
                    .assumedBy(ServicePrincipal.Builder.create("lexv2.amazonaws.com").build())
                .managedPolicies(List.of(adminPolicy))
                .build();

        var privacy = CfnBot.DataPrivacyProperty.builder()
                .childDirected(false)
                .build();

        BotLocaleProperty retailerEN = null;
        List<BotLocaleProperty> locales = List.of();
        var sessionTtl = cfg.getSessionTTL();

        var bot = CfnBot.Builder.create(this, StackUtils.resourceId("RetailerBot"))
                .name(resourceName("RetailerBot"))
                .botLocales(locales)
                .autoBuildBotLocales(true)
                .roleArn(role.getRoleArn())
                .dataPrivacy(privacy)
                .idleSessionTtlInSeconds(sessionTtl)
                .build();
    }
}
