package ecomarkets.vdn.view.dev;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;

import ecomarkets.vdn.view.MainLayout;
import ecomarkets.vdn.view.VerticalView;
import jakarta.annotation.PostConstruct;


@Route(value="sandbox", layout = MainLayout.class)
public class SandboxView 
    extends VerticalView {


    @PostConstruct
    void init() {
        var greeting = new Paragraph(format("about.greeting", getUser().getName()));
        var description = new Paragraph(format("about.description"));
        addContent(greeting, description);
    }
}
