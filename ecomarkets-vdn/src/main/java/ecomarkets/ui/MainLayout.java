package ecomarkets.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

import ecomarkets.ui.view.AboutView;
import ecomarkets.ui.view.MainView;
import jakarta.annotation.PostConstruct;

public class MainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver { 
    @Override
	public void beforeEnter(BeforeEnterEvent event) {
        var location = event.getLocation();
        var path = location.getPath();
        if (! isAccessGranted(path)) {
            throw new AccessDeniedException();
        }
    }

    private boolean isAccessGranted(String path) {
       return true;
    }

    @PostConstruct
    void init() {
        
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        var logo = new Paragraph("Welcome to ecomarkets!");
        logo.addClassNames(
            LumoUtility.FontSize.LARGE, 
            LumoUtility.Margin.MEDIUM);

        var header = new HorizontalLayout(new DrawerToggle(), logo); 

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER); 
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header); 

    }

    private void createDrawer() {
        var drawer = new VerticalLayout();
        drawer.add(new RouterLink("Home", MainView.class));
        drawer.add(new RouterLink("About", AboutView.class));
        addToDrawer(drawer);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Close the drawer when the layout is attached to the UI
        Page page = attachEvent.getUI().getPage();
        page.executeJs("document.querySelector('vaadin-app-layout').drawerOpened = false;");
    }
}
