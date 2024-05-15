package ecomarkets.vdn.view.product.category;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ecomarkets.core.domain.core.product.category.Category;
import ecomarkets.core.domain.usecase.product.CategoryUseCase;
import ecomarkets.vdn.view.MainLayout;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@PageTitle("Categorias")
@Route(value="categories", layout = MainLayout.class)
public class ListCategoryView extends VerticalLayout {

    Grid<Category> grid = new Grid<>(Category.class);

    CategoryForm form;

    @Inject
    CategoryUseCase categoryUseCase;

    @PostConstruct
    public void init(){
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(c -> c.name).setHeader("Nome");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editCategory(event.getValue()));
    }

    public void editCategory(Category category) {
        if (category == null) {
            closeEditor();
        } else {
            form.setCategory(category);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private Component getToolbar() {
        Button addCategoryButton = new Button("Cadastrar categoria");
        addCategoryButton.addClickListener(click -> addCategory());

        var toolbar = new HorizontalLayout(addCategoryButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addCategory() {
        grid.asSingleSelect().clear();
        form.setCategory(null);
        form.setVisible(true);
        addClassName("editing");
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
        form = new CategoryForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveCategory); // <1>
        form.addDeleteListener(this::deleteCategory); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveCategory(CategoryForm.SaveEvent event) {
        CategoryDTO cat = event.getCategory();

        if(cat.getId() == null){
            categoryUseCase.newCategory(cat.getName());
        }else{
            categoryUseCase.changeCategory(cat.getId(), cat.getName());
        }

        updateList();
        closeEditor();
    }

    private void deleteCategory(CategoryForm.DeleteEvent event) {
        categoryUseCase.deleteCategory(event.getCategory().getId());
        updateList();
        closeEditor();
    }


    private void updateList() {
        grid.setItems(Category.findAll().list());
    }

    private void closeEditor() {
        form.setCategory(null);
        form.setVisible(false);
        removeClassName("editing");
    }
}
