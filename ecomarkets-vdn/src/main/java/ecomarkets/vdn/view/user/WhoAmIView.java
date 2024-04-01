package ecomarkets.vdn.view.user;


import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import ecomarkets.core.user.UserDetails;
import ecomarkets.vdn.user.UserSession;
import ecomarkets.vdn.view.MainLayout;
import ecomarkets.vdn.view.VerticalView;
import io.quarkus.oidc.UserInfo;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@Route(value = "user/whoami", layout = MainLayout.class)
public class WhoAmIView extends VerticalView {
    

    @Inject
    UserSession user;


    @PostConstruct
    public void init() {
        addDebug("Who Am I?");
        if (user == null) {
            addDebug("Anonymous");
            return; 
        }
        addDebug("Info OK");
        addDebug("User Details:" + user.getUserDetails().getName());
        
    }

    private void addDebug(String msg) {
         add(new Paragraph(msg));
    }
}