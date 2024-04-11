package ecomarkets.vdn.view.product;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import ecomarkets.core.domain.core.product.MeasureUnit;
import ecomarkets.core.domain.core.product.Product;
import ecomarkets.core.domain.core.product.category.Category;
import ecomarkets.core.domain.core.product.image.ImageRepository;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.io.InputStream;

public class ProductForm extends FormLayout {

    TextField name = new TextField("Nome");
    TextArea description = new TextArea("Descrição");
    TextArea recipeIngredients = new TextArea("Ingredientes (Receita)");
    ComboBox<MeasureUnit> measureUnit = new ComboBox<>("Unidade");
    ComboBox<Category> category = new ComboBox<>("Categoria");
    TextField priceValue = new TextField("Preço");

    Upload imageFileUpload;

    Image productImage = new Image();

    Button save = new Button("Salvar");
    Button delete = new Button("Deletar");
    Button close = new Button("Cancelar");

    ImageFormData imageFormData;

    ImageRepository imageRepository;

    Binder<ProductDTO> binder = new BeanValidationBinder<>(ProductDTO.class);

    public ProductForm(ImageRepository imageRepository){
        this.imageRepository = imageRepository;

        addClassName("contact-form");
        binder.bindInstanceFields(this);

        category.setItems(Category.findAll().list());
        category.setItemLabelGenerator(c -> c.name);

        measureUnit.setItems(MeasureUnit.values());
        measureUnit.setItemLabelGenerator(MeasureUnit::name);

        processImageComponent();

        add(name, description, recipeIngredients, priceValue, measureUnit, category, productImage, imageFileUpload, createButtonsLayout());
    }

    private void processImageComponent() {
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        imageFileUpload = new Upload(memoryBuffer);

        Button uploadButton = new Button("Upload Imagem...");
        imageFileUpload.setUploadButton(uploadButton);

        Span dropLabel = new Span("Arrastar e soltar imagem");
        Icon dropIcon = VaadinIcon.CLOUD_UPLOAD_O.create();
        imageFileUpload.setDropLabelIcon(dropIcon);

        imageFileUpload.setDropLabel(dropLabel);
        imageFileUpload.addSucceededListener(event -> {
            InputStream fileData = memoryBuffer.getInputStream();
            long contentLength = event.getContentLength();
            String fileName = event.getFileName();
            String mimeType = event.getMIMEType();

            ProductDTO productDTO = binder.getBean();
            imageFormData = new ImageFormData();
            imageFormData.setFile(fileData);
            imageFormData.setFileName(fileName);
            imageFormData.setMimeType(mimeType);
            imageFormData.setContentLength(contentLength);

            productDTO.setImageFormData(imageFormData);

            StreamResource streamResource = new StreamResource(fileName, () -> fileData);
            productImage.setSrc(streamResource);
        });
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

    public void setProduct(Product product){
        ProductDTO dto = new ProductDTO();
        productImage.setSrc("");
        if(product != null){
            dto.setId(product.id);
            dto.setCategory(product.getCategory());
            dto.setDescription(product.getDescription());
            dto.setName(product.getName());
            dto.setMeasureUnit(product.getMeasureUnit());
            dto.setPriceValue(product.getPrice().total().doubleValue());
            dto.setRecipeIngredients(product.getRecipeIngredients() != null ? product.getRecipeIngredients().description() : null);

            if(product.productImage() != null){
                productImage.setSrc(imageRepository.createPresignedGetUrl(product.productImage()));
            }

        }

        binder.setBean(dto);
    }

    public static abstract class ProductFormEvent extends ComponentEvent<ProductForm> {
        private ProductDTO productDTO;

        protected ProductFormEvent(ProductForm source, ProductDTO ProductDTO) {
            super(source, false);
            this.productDTO = ProductDTO;
        }

        public ProductDTO getProduct() {
            return productDTO;
        }
    }

    public static class SaveEvent extends ProductFormEvent {
        SaveEvent(ProductForm source, ProductDTO productDTO) {
            super(source, productDTO);
        }
    }

    public static class DeleteEvent extends ProductFormEvent {
        DeleteEvent(ProductForm source, ProductDTO contact) {
            super(source, contact);
        }

    }

    public static class CloseEvent extends ProductFormEvent {
        CloseEvent(ProductForm source) {
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

