package ecomarkets.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class VerticalView
    extends VerticalLayout {
    
    protected VerticalLayout getContent() {
        return this;
    }

    protected void addContent(Component... components) {
        for (Component component : components) {
            getContent().add(component); 
        }
        
    }

}
