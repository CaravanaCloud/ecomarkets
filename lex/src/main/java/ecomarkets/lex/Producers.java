package ecomarkets.lex;

import static ecomarkets.lex.StackUtils.resourceId;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;

@ApplicationScoped
public class Producers {
    @Inject
    LexConfig cfg;

    @Produces
    @ApplicationScoped
    public App newApp(){
        Log.info("Creating CDK Stack App");
        var app = new App();
        return app;
    }

    @Produces
    @ApplicationScoped
    public StackProps newProps(){
        Log.info("Creating CDK Stack Props");
        var props = StackProps.builder().build();
        return props;
    }

    @Produces
    @ApplicationScoped
    public LexStack newLexStack(App app, StackProps props){
        Log.info("Creating CDK Lex Stack");
        var stack = new LexStack(app, resourceId("LexStack"), props, cfg);
        return stack;
    }


}
