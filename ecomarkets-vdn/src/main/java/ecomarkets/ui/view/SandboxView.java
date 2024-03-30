package ecomarkets.ui.view;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;

import ecomarkets.i18n.I18NService;
import ecomarkets.ui.MainLayout;
import ecomarkets.ui.VerticalView;
import ecomarkets.user.UserSession;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;


@Route(value="sandbox", layout = MainLayout.class)
public class SandboxView 
    extends VerticalView {
    @Inject
    UserSession user;

    @PostConstruct
    void init() {
        var greeting = new Paragraph(user.format("about.greeting", user.getUUID()));
        var description = new Paragraph(user.format("about.description"));
        addContent(greeting, description);
    }
}
