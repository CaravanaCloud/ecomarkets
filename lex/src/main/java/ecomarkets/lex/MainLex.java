package ecomarkets.lex;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;


@QuarkusMain
public class MainLex  {

    public static void main(String... args) {
        Quarkus.run(LexApplication.class, args);
    }
}

