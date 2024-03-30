package ecomarkets.ui.view;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import ecomarkets.i18n.I18NService;
import ecomarkets.ui.MainLayout;
import ecomarkets.ui.VerticalView;


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
