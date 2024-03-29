package ecomarkets.ui.view;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteNotFoundError;

import ecomarkets.ui.MainLayout;
import ecomarkets.ui.ParentView;


@Route(value="error")
public class ErrorView extends Composite<VerticalLayout> implements HasErrorParameter<AccessDeniedException> {
    @PostConstruct
    void init() {
        var txt = new Paragraph("Oops ;'(");
        getContent().add(txt);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
          ErrorParameter<AccessDeniedException> parameter) {
        getElement().setText(
                "Sorry, you can't access that page.");
        return HttpServletResponse.SC_FORBIDDEN;
    }
}
