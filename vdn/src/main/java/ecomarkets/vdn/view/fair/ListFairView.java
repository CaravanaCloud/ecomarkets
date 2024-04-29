package ecomarkets.vdn.view.fair;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ecomarkets.core.domain.core.fair.Fair;
import ecomarkets.core.domain.core.fair.FairId;
import ecomarkets.core.domain.core.fair.ShoppingPeriod;
import ecomarkets.core.domain.usecase.fair.FairUseCase;
import ecomarkets.vdn.view.MainLayout;
import io.quarkus.panache.common.Sort;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

import java.time.format.DateTimeFormatter;

@PageTitle("Feira")
@Route(value="fair", layout = MainLayout.class)
public class ListFairView extends VerticalLayout {

    Grid<Fair> grid = new Grid<>(Fair.class);

    FairForm form;

    @Inject
    FairUseCase fairUseCase;

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

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        grid.addColumn(f -> f.id).setHeader("Id");
        grid.addColumn(f -> f.getShoppingPeriod().startDate().format(formatter)).setHeader("Início");
        grid.addColumn(f -> f.getShoppingPeriod().endDate().format(formatter)).setHeader("Fim");
        grid.addColumn(f -> f.getCreationDate().format(formatter)).setHeader("Dt. Criação");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editFair(event.getValue()));
    }

    public void editFair(Fair fair) {
        if (fair == null) {
            closeEditor();
        } else {
            form.setFair(fair);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private Component getToolbar() {
        Button addFarmerButton = new Button("Cadastrar Feira");
        addFarmerButton.addClickListener(click -> addFair());

        var toolbar = new HorizontalLayout(addFarmerButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addFair() {
        grid.asSingleSelect().clear();
        form.setFair(null);
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
        form = new FairForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveFair); // <1>
        form.addDeleteListener(this::deleteFair); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveFair(FairForm.SaveEvent event) {
        FairDTO fair = event.getFair();

        if(fair.getId() == null){
            fairUseCase.newFair(ShoppingPeriod.of(fair.getStartDate(), fair.getEndDate()));
        }else{
            fairUseCase.changeFair(FairId.of(fair.getId()), ShoppingPeriod.of(fair.getStartDate(), fair.getEndDate()));
        }

        updateList();
        closeEditor();
    }

    private void deleteFair(FairForm.DeleteEvent event) {
        fairUseCase.deleteFair(FairId.of(event.getFair().getId()));
        updateList();
        closeEditor();
    }

    private void updateList() {
        grid.setItems(Fair.findAll(Sort.descending("creationDate")).list());
    }

    private void closeEditor() {
        form.setFair(null);
        form.setVisible(false);
        removeClassName("editing");
    }
}
