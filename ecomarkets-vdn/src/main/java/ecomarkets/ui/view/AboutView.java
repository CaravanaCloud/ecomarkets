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

import ecomarkets.ui.MainLayout;
import ecomarkets.ui.ParentView;


@Route(value="about", layout = MainLayout.class)
public class AboutView  extends ParentView {
    @PostConstruct
    void init() {
        var txt = new Paragraph("Don't Panic! It's organic :)");
        add(txt);
    }
}
