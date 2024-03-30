package ecomarkets.vdn.view;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;

import ecomarkets.core.i18n.I18NService;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;


@Route(value="about", layout = MainLayout.class)
public class AboutView 
    extends VerticalView {
    @Inject
    I18NService i18n;

    @PostConstruct
    void init() {
        var description = new Paragraph(i18n.format("en","about.description"));
        addContent(description);
    }
}
