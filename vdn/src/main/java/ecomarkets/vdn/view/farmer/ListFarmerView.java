package ecomarkets.vdn.view.farmer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ecomarkets.core.domain.core.farmer.Farmer;
import ecomarkets.core.domain.core.farmer.FarmerId;
import ecomarkets.core.domain.usecase.farmer.FarmerUseCase;
import ecomarkets.vdn.view.MainLayout;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@PageTitle("Agricultores")
@Route(value="farmers", layout = MainLayout.class)
public class ListFarmerView extends VerticalLayout {

    Grid<Farmer> grid = new Grid<>(Farmer.class);

    FarmerForm form;

    @Inject
    FarmerUseCase farmerUseCase;

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
        grid.addColumn(Farmer::getName).setHeader("Nome");
        grid.addColumn(f -> f.getEmail().value()).setHeader("Email");
        grid.addColumn(f -> f.getAddress().city()).setHeader("Cidade");
        grid.addColumn(f -> f.getAddress().state()).setHeader("Estado");
        grid.addColumn(f -> "(%s) %s".formatted(f.getTelephone().areaCode(), f.getTelephone().number())).setHeader("Telefone");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editFarmer(event.getValue()));
    }

    public void editFarmer(Farmer farmer) {
        if (farmer == null) {
            closeEditor();
        } else {
            form.setFarmer(farmer);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private Component getToolbar() {
        Button addFarmerButton = new Button("Cadastrar Agricultor");
        addFarmerButton.addClickListener(click -> addFarmer());

        var toolbar = new HorizontalLayout(addFarmerButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addFarmer() {
        grid.asSingleSelect().clear();
        form.setFarmer(null);
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
        form = new FarmerForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveFarmer); // <1>
        form.addDeleteListener(this::deleteFarmer); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveFarmer(FarmerForm.SaveEvent event) {
        FarmerDTO farmer = event.getFarmer();

        if(farmer.getId() == null){
            farmerUseCase.newFarmer(farmer.parseFarmer());
        }else{
            farmerUseCase.changeFarmer(farmer.getId(), farmer.getName());
        }

        updateList();
        closeEditor();
    }

    private void deleteFarmer(FarmerForm.DeleteEvent event) {
        farmerUseCase.deleteFarmer(FarmerId.of(event.getFarmer().getId()));
        updateList();
        closeEditor();
    }

    private void updateList() {
        grid.setItems(Farmer.findAll().list());
    }

    private void closeEditor() {
        form.setFarmer(null);
        form.setVisible(false);
        removeClassName("editing");
    }
}
