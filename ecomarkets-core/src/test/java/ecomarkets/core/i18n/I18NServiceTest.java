package ecomarkets.core.i18n;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import ecomarkets.core.i18n.I18NService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class I18NServiceTest {
    @Inject
    I18NService i18n;

    @Test
    public void test() {
        assertNotNull(i18n.format("en", "about.description"));
    }

}
