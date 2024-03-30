package ecomarkets.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class I18NService {

    public String format(String locale_code, String key, Object... params) {
        // Split the key to extract the bundle name and the actual message key
        var parts = key.split("\\.", 2);
        var bundleName = parts[0];
        var messageKey = parts.length > 1 ? parts[1] : "";

        // Load the resource bundle using the extracted bundle name
        var locale = Locale.forLanguageTag(locale_code);
        var resourceBundle = ResourceBundle.getBundle(bundleName, locale);

        // Load the message template from the resource bundle using the message key
        var messageTemplate = resourceBundle.getString(messageKey);

        // Use MessageFormat to replace placeholders in the message template with
        // parameters
        var formattedMessage = MessageFormat.format(messageTemplate, params);

        return formattedMessage;
    }
}
