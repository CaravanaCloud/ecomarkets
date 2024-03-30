package ecomarkets.vdn.view.dev;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;

import ecomarkets.core.i18n.I18NService;
import ecomarkets.vdn.user.UserSession;
import ecomarkets.vdn.view.MainLayout;
import ecomarkets.vdn.view.VerticalView;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;


@Route(value="sandbox", layout = MainLayout.class)
public class SandboxView 
    extends VerticalView {
    @Inject
    UserSession session;

    @PostConstruct
    void init() {
        var greeting = new Paragraph(user.format("about.greeting", session.getUser().getUUID()));
        var description = new Paragraph(user.format("about.description"));
        addContent(greeting, description);
    }
}
