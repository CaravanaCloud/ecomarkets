package ecomarkets.lex;

import io.quarkus.logging.Log;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awscdk.App;

@ApplicationScoped
public class LexApplication implements QuarkusApplication {
    @Inject
    App app;

    @Inject
    LexStack lex;

    @Override
    public int run(String... args) throws Exception {
        Log.info("Synthetizing " + lex.toString());
        app.synth();
        var code = 0;
        Quarkus.asyncExit(code);
        // TODO: avoid forced exit
        System.exit(code);
        return code;
    }
}
