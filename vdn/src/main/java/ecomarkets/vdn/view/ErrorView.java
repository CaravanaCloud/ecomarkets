package ecomarkets.vdn.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.Route;

import jakarta.servlet.http.HttpServletResponse;


@Route(value="error")
public class ErrorView extends Composite<VerticalLayout> implements HasErrorParameter<AccessDeniedException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
          ErrorParameter<AccessDeniedException> parameter) {
        getElement().setText(
                "Sorry, you can't access that page.");
        return HttpServletResponse.SC_FORBIDDEN;
    }
}
