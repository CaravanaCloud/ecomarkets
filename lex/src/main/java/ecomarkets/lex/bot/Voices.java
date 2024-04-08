package ecomarkets.lex.bot;

public class Voices {

    public static String getVoidId(String localeId) {
        var result = switch (localeId) {
            case "en_US" -> "Danielle";
            default -> "Aditi";
        };
        return result;
    }
    
}
