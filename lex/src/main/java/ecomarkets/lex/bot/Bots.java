package ecomarkets.lex.bot;

import static ecomarkets.lex.StackUtils.resourceId;
import static ecomarkets.lex.StackUtils.resourceName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import ecomarkets.lex.LexStack;
import ecomarkets.lex.StackUtils;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awscdk.CfnResource;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.lex.CfnBot;
import software.amazon.awscdk.services.lex.CfnBot.BotLocaleProperty;
import software.amazon.awscdk.services.lex.CfnBot.VoiceSettingsProperty;

@ApplicationScoped
public class Bots {
    @Inject
    BotsConfig cfg;

    public Map<String, Object> buildLocale(String localeId) {
        List<Map<String, Object>> intents = new ArrayList<>();
        Map<String, Object> fallbackIntent = new HashMap<>();
        fallbackIntent.put("Name", "FallbackIntent");
        fallbackIntent.put("Description", "Default intent when no other intent matches");
        fallbackIntent.put("ParentIntentSignature", "AMAZON.FallbackIntent");
        intents.add(fallbackIntent);
    
        Map<String, Object> voice = buildVoice(localeId); // This should return a Map representing the voice settings
        
        List<Object> slotTypes = new ArrayList<>(); // Assuming no slotTypes for simplicity
    
        Map<String, Object> locale = new HashMap<>();
        locale.put("LocaleId", localeId);
        locale.put("NluConfidenceThreshold", cfg.getNLUConfidenceThreshold());
        locale.put("VoiceSettings", voice);
        locale.put("Intents", intents);
        locale.put("SlotTypes", slotTypes);
    
        return locale;
    }

    private Map<String, Object> buildVoice(String localeId) {
        Map<String, Object> voice = new HashMap<>();
        voice.put("VoiceId", Voices.getVoidId(localeId));
        return voice;
    }

    public BotsConfig getConfig() {
        return cfg;
    }

    public void synth(LexStack stack) {
          Log.info("********** Initializing Stack ***********");
        var role = buildRole(stack);

        buildBot(stack, role);
    }

    private void buildBot(LexStack stack, Role role) {
        var localeCodes = Stream.of("en_US");
        var locales = localeCodes.map(this::buildLocale).toList();
        var sessionTtl = getConfig().getSessionTTL();
        var privacy = Map.of("ChildDirected", "false");
        CfnResource bot = CfnResource.Builder.create(stack, StackUtils.resourceId("RetailerBot"))
            .type("AWS::Lex::Bot")
            .properties(Map.of(
                    "Name", resourceName("RetailerBot"), // Use the actual method to generate resource name
                    "BotLocales", locales, // Ensure this is formatted correctly for CloudFormation
                    "AutoBuildBotLocales", true,
                    "RoleArn", role.getRoleArn(), // Assuming 'role' is an IAM role construct
                    "DataPrivacy", privacy, // Ensure this matches CloudFormation's expected structure
                    "IdleSessionTTLInSeconds", sessionTtl
            ))
            .build();

        Log.info("Bot created "+bot);
    }



    private Role buildRole(LexStack stack) {
        var managedPolicyArn = cfg.getManagedPolicyArn();
        var adminPolicy = ManagedPolicy.fromManagedPolicyArn(stack, resourceId("RetailerBotPolicy"), managedPolicyArn);

        var role = Role.Builder.create(stack,  resourceId("RetailerBotRole"))
                    .assumedBy(ServicePrincipal.Builder.create("lexv2.amazonaws.com").build())
                .managedPolicies(List.of(adminPolicy))
                .build();
        return role;
    }
    
}
