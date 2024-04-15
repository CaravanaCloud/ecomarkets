package ecomarkets.vdn.view.product.category;

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
import ecomarkets.core.domain.core.product.category.Category;

public class CategoryForm extends FormLayout {

    TextField name = new TextField("Nome");

    Binder<CategoryDTO> binder = new BeanValidationBinder<>(CategoryDTO.class);

    Button save = new Button("Salvar");
    Button delete = new Button("Deletar");
    Button close = new Button("Cancelar");

    public CategoryForm(){
        addClassName("contact-form");
        binder.bindInstanceFields(this);

        add(name, createButtonsLayout());
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

    public void setCategory(Category category){
        CategoryDTO categoryDTO = new CategoryDTO();

        if(category != null){
            categoryDTO.setId(category.id);
            categoryDTO.setName(category.name);
        }

        binder.setBean(categoryDTO);
    }


    public static abstract class CategoryFormEvent extends ComponentEvent<CategoryForm> {
        private CategoryDTO category;

        protected CategoryFormEvent(CategoryForm source, CategoryDTO category) {
            super(source, false);
            this.category = category;
        }

        public CategoryDTO getCategory() {
            return category;
        }
    }

    public static class SaveEvent extends CategoryFormEvent {
        SaveEvent(CategoryForm source, CategoryDTO category) {
            super(source, category);
        }
    }

    public static class DeleteEvent extends CategoryFormEvent {
        DeleteEvent(CategoryForm source, CategoryDTO category) {
            super(source, category);
        }

    }

    public static class CloseEvent extends CategoryFormEvent {
        CloseEvent(CategoryForm source) {
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
