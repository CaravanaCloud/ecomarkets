package ecomarkets.lex;

import ecomarkets.lex.bot.Bots;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

public class LexStack extends Stack {

    private LexStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);
    }

    public static LexStack of(Construct scope,
            String id,
            StackProps props,
            Bots bots) {
        var stack = new LexStack(scope, id, props);
        bots.synth(stack);
        return stack;
    }

}
