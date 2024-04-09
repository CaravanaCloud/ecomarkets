package ecomarkets.vdn.view.product;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ecomarkets.core.domain.core.product.Price;
import ecomarkets.core.domain.core.product.Product;
import ecomarkets.core.domain.core.product.RecipeIngredients;
import ecomarkets.vdn.view.MainLayout;

@PageTitle("Produtos")
@Route(value="products", layout = MainLayout.class)
public class ListProductView extends VerticalLayout {

    Grid<Product> grid = new Grid<>(Product.class);

    ProductForm form;

    public ListProductView(){
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new ProductForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveProduct); // <1>
        form.addDeleteListener(this::deleteProduct); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveProduct(ProductForm.SaveEvent event) {
        ProductDTO dto = event.getProduct();

        Double integerPart = Math.floor(dto.getPrice());
        Double decimalPart = dto.getPrice() - integerPart;
        Price price = new Price(integerPart.intValue(), decimalPart.intValue());
        Product product = Product.of(dto.getName(), dto.getDescription(),
                RecipeIngredients.of(dto.getRecipeIngredients()),dto.getMeasureUnit(), price, dto.getCategory());

        product.persist();

        updateList();
        closeEditor();
    }

    private void deleteProduct(ProductForm.DeleteEvent event) {
        Product.deleteById(event.getProduct().getId());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("name", "category", "price", "measureUnit", "description");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editProduct(event.getValue()));
    }

    private Component getToolbar() {
        Button addProductButton = new Button("Add product");
        addProductButton.addClickListener(click -> addProduct());

        var toolbar = new HorizontalLayout(addProductButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editProduct(Product product) {
        if (product == null) {
            closeEditor();
        } else {
            form.setProduct(product);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setProduct(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addProduct() {
        grid.asSingleSelect().clear();
        form.setProduct(null);
        form.setVisible(true);
        addClassName("editing");
    }

    private void updateList() {
        grid.setItems(Product.findAll().list());
    }







}
