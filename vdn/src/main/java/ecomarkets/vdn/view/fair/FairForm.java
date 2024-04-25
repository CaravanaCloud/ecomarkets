package ecomarkets.vdn.view.fair;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import ecomarkets.core.domain.core.fair.Fair;

public class FairForm extends FormLayout {

    DateTimePicker startDate = new DateTimePicker("Início");
    DateTimePicker endDate = new DateTimePicker("Fim");
    Button save = new Button("Salvar");
    Button delete = new Button("Deletar");
    Button close = new Button("Cancelar");

    Binder<FairDTO> binder = new BeanValidationBinder<>(FairDTO.class);

    public FairForm(){
        addClassName("contact-form");
        binder.bindInstanceFields(this);

        add(startDate, endDate, createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave()); // <1>
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean()))); // <2>
        close.addClickListener(event -> fireEvent(new CloseEvent(this))); // <3>

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); // <4>
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean())); // <6>
        }
    }

    public void setFair(Fair fair){
        FairDTO fairDTO = new FairDTO();

        if(fair != null){
            fairDTO.setId(fair.id);
            fairDTO.setStartDate(fair.getShoppingPeriod().startDate());
            fairDTO.setEndDate(fair.getShoppingPeriod().endDate());
        }

        binder.setBean(fairDTO);
    }


    public static abstract class FairFormEvent extends ComponentEvent<FairForm> {
        private FairDTO fair;

        protected FairFormEvent(FairForm source, FairDTO farmer) {
            super(source, false);
            this.fair = farmer;
        }

        public FairDTO getFair() {
            return fair;
        }
    }

    public static class SaveEvent extends FairFormEvent {
        SaveEvent(FairForm source, FairDTO farmer) {
            super(source, farmer);
        }
    }

    public static class DeleteEvent extends FairFormEvent {
        DeleteEvent(FairForm source, FairDTO farmer) {
            super(source, farmer);
        }

    }

    public static class CloseEvent extends FairFormEvent {
        CloseEvent(FairForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
