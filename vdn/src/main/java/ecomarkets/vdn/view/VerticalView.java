package ecomarkets.vdn.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import ecomarkets.core.user.UserDetails;
import jakarta.inject.Inject;

public class VerticalView
    extends VerticalLayout {
    
    @Inject
    UserDetails user;

    public UserDetails getUser() {
        return user;
    }

    protected String format(String key, Object... args){
        return user.format(key, args);
    }

    protected VerticalLayout getContent() {
        return this;
    }

    protected void addContent(Component... components) {
        for (Component component : components) {
            getContent().add(component); 
        }
        
    }

}
