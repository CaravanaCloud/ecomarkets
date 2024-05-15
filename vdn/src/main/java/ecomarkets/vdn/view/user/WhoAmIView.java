package ecomarkets.vdn.view.user;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.router.RouteAlias;
import ecomarkets.core.user.UserDetails;
import ecomarkets.vdn.view.MainLayout;
import ecomarkets.vdn.view.VerticalView;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@Route(value = "user/whoami", layout = MainLayout.class)
@RouteAlias(value = "", layout  = MainLayout.class )
public class WhoAmIView extends VerticalView {

    @Inject
    UserDetails user;

    @PostConstruct
    public void init() {
        addDebug("Who Am I?");
        if (user == null) {
            addDebug("Anonymous");
            return;
        }
        addDebug("Info OK - User Details");
        var roles = user.getRoleNames();
        addDebug("Name:" + user.getName());
        addDebug("Email:" + user.getEmail());
        addDebug("Roles: " + roles);

    }

    private void addDebug(String msg) {
        add(new Paragraph(msg));
    }
}