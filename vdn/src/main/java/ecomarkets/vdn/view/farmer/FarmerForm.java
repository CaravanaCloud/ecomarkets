package ecomarkets.vdn.view.farmer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import ecomarkets.core.domain.core.farmer.Farmer;

public class FarmerForm extends FormLayout {

    TextField name = new TextField("Nome");
    TextField email = new TextField("Email");
    TextField telephone = new TextField("Telefone");
    TextField country = new TextField("País");

    TextField state = new TextField("Estado");
    TextField city = new TextField("City");
    TextField houseNumber = new TextField("Número");
    TextField addOn = new TextField("Complemento");
    TextField reference = new TextField("Referência");
    TextField postCode = new TextField("CEP");

    Binder<FarmerDTO> binder = new BeanValidationBinder<>(FarmerDTO.class);

    Button save = new Button("Salvar");
    Button delete = new Button("Deletar");
    Button close = new Button("Cancelar");

    public FarmerForm(){
        addClassName("contact-form");
        binder.bindInstanceFields(this);

        add(name, email, telephone, country, state, city, houseNumber, addOn, reference, postCode, createButtonsLayout());
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

    public void setFarmer(Farmer farmer){
        FarmerDTO farmerDTO = new FarmerDTO();

        if(farmer != null){
            farmerDTO.setId(farmer.id);
            farmerDTO.setName(farmer.getName());
            farmerDTO.setEmail(farmer.getEmail().value());
            farmerDTO.setTelephone(farmer.getTelephone().number());
            farmerDTO.setCountry(farmer.getAddress().country());
            farmerDTO.setState(farmer.getAddress().state());
            farmerDTO.setCity(farmer.getAddress().city());
            farmerDTO.setHouseNumber(farmer.getAddress().houseNumber());
            farmerDTO.setAddOn(farmer.getAddress().addOn());
            farmerDTO.setReference(farmer.getAddress().reference());
            farmerDTO.setPostCode(farmer.getAddress().postCode());
        }

        binder.setBean(farmerDTO);
    }


    public static abstract class FarmerFormEvent extends ComponentEvent<FarmerForm> {
        private FarmerDTO farmer;

        protected FarmerFormEvent(FarmerForm source, FarmerDTO farmer) {
            super(source, false);
            this.farmer = farmer;
        }

        public FarmerDTO getFarmer() {
            return farmer;
        }
    }

    public static class SaveEvent extends FarmerFormEvent {
        SaveEvent(FarmerForm source, FarmerDTO farmer) {
            super(source, farmer);
        }
    }

    public static class DeleteEvent extends FarmerFormEvent {
        DeleteEvent(FarmerForm source, FarmerDTO farmer) {
            super(source, farmer);
        }

    }

    public static class CloseEvent extends FarmerFormEvent {
        CloseEvent(FarmerForm source) {
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
