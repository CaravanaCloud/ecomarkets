package ecomarkets.lex.bot;

import static ecomarkets.lex.StackUtils.resourceId;
import static ecomarkets.lex.StackUtils.resourceName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import ecomarkets.lex.LexStack;
import ecomarkets.lex.StackUtils;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.lex.CfnBot;
import software.amazon.awscdk.services.lex.CfnBot.BotLocaleProperty;
import software.amazon.awscdk.services.lex.CfnBot.DataPrivacyProperty;
import software.amazon.awscdk.services.lex.CfnBot.VoiceSettingsProperty;

@ApplicationScoped
public class Bots {
    @Inject
    BotsConfig cfg;

    public BotLocaleProperty buildLocale(String localeId) {
            var intents = new ArrayList<>();
            var fallbackIntent = CfnBot.IntentProperty.builder()
                    .name("FallbackIntent")
                    .description("Default intent when no other intent matches")
                    .parentIntentSignature("AMAZON.FallbackIntent")
                    .build();
            intents.add(fallbackIntent);
            var voice = buildVoice(localeId);
            var slotTypes = List.of();
            return CfnBot.BotLocaleProperty.builder()
                    .localeId(localeId)
                    .nluConfidenceThreshold(cfg.getNLUConfidenceThreshold())
                    .voiceSettings(voice)
                    .intents(intents)
                    .slotTypes(slotTypes)
                    .build();
        }

    private VoiceSettingsProperty buildVoice(String localeId) {
        var voice = CfnBot.VoiceSettingsProperty.builder()
                .voiceId(Voices.getVoidId(localeId))
                .build();
        return voice;
    }

    public BotsConfig getConfig() {
        return cfg;
    }

    public void synth(LexStack stack) {
          Log.info("********** Initializing Stack ***********");
        var role = buildRole(stack, getConfig());
        var privacy = buildPrivacy(stack);
        var localeCodes = Stream.of("en_US");
        var locales = localeCodes.map(this::buildLocale).toList();
        var sessionTtl = getConfig().getSessionTTL();
        buildBot(stack, getConfig(), locales, role, privacy, sessionTtl);
    }

    private static void buildBot(LexStack stack, BotsConfig cfg, List<BotLocaleProperty> locales, Role role, DataPrivacyProperty privacy, int sessionTtl) {
        var bot = CfnBot.Builder.create(stack, StackUtils.resourceId("RetailerBot"))
                .name(resourceName("RetailerBot"))
                .botLocales(locales)
                .autoBuildBotLocales(true)
                .roleArn(role.getRoleArn())
                .dataPrivacy(privacy)
                .idleSessionTtlInSeconds(sessionTtl)
                .build();
        Log.info("Bot created "+bot);
    }



    private static DataPrivacyProperty buildPrivacy(LexStack stack) {
        return CfnBot.DataPrivacyProperty.builder()
                .childDirected(false)
                .build();
    }

    private static Role buildRole(LexStack stack, BotsConfig cfg) {
        var managedPolicyArn = cfg.getManagedPolicyArn();
        var adminPolicy = ManagedPolicy.fromManagedPolicyArn(stack, resourceId("RetailerBotPolicy"), managedPolicyArn);

        var role = Role.Builder.create(stack,  resourceId("RetailerBotRole"))
                    .assumedBy(ServicePrincipal.Builder.create("lexv2.amazonaws.com").build())
                .managedPolicies(List.of(adminPolicy))
                .build();
        return role;
    }
    
}
