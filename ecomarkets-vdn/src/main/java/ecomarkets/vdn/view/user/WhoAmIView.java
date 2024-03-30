package ecomarkets.vdn.view.user;


import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import ecomarkets.vdn.user.UserSession;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@Route("user/whoami")
public class WhoAmIView extends VerticalLayout {
    
    @Inject
    UserSession session;

    @PostConstruct
    public void init() {
        addDebug("Who Am I?");
        if (session == null || session.getUser().isAnonymous()) {
            addDebug("Anonymous");
            return; 
        } 
        addDebug("Authenticated");
        //addDebug("Name: "  + ud.getName());
        //addDebug("Email: " + ud.getEmail());
        //addDebug("Roles: " + ud.getRoleNames());
        //
        //ud.ifAdmin(() -> {
        //    addDebug("MENSAGEM APENAS PARA O ADMIN");
        //},
        //() -> {
        //    throw new RuntimeException("YOU SHOULD NOT BE ON THIS PAGE");
        //}
        // );
        //
    }

    private void addDebug(String msg) {
         add(new Paragraph(msg));
    }
}