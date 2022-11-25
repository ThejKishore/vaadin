package com.example.application.views.transactionlandingpage;

import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.SamplePersonService;
import com.example.application.views.transaction.TransactionView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.vaadin.erik.SlideMode;
import org.vaadin.erik.SlideTab;
import org.vaadin.erik.SlideTabBuilder;
import org.vaadin.erik.SlideTabPosition;

@PageTitle("TransactionLandingPage")
@Route(value = "transactionLandingPage/:samplePersonID?/:action?(edit)")
@Uses(Icon.class)
public class TransactionLandingPageView extends VerticalLayout {

    private Logger log = LoggerFactory.getLogger(TransactionLandingPageView.class);

    private final String SAMPLEPERSON_ID = "samplePersonID";
    private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "transaction/%s/edit";

    private final Grid<SamplePerson> grid = new Grid<>(SamplePerson.class, false);

    private final SamplePersonService samplePersonService;

    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private TextField phone;
    private DatePicker dateOfBirth;
    private TextField occupation;
    private Checkbox important;
    private Button cancel = new Button("cancel");
    private Button save = new Button("Save");

    private final SplitLayout splitLayout = new SplitLayout();

    @Autowired
    public TransactionLandingPageView(SamplePersonService samplePersonService) {
        this.samplePersonService = samplePersonService;
        addClassNames("transaction-landing-page-view");
        setSizeFull();
        splitLayout.setSizeFull();


        Button filter = new Button(new Icon(VaadinIcon.FILTER));
        filter.addThemeVariants(ButtonVariant.LUMO_ERROR);
        filter.getStyle().set("margin-inline-end", "auto");
        filter.addClickListener(evnt -> {
           splitLayout.getPrimaryComponent().setVisible(true);
           splitLayout.getPrimaryComponent().getElement().getStyle().set("width","40%");
        });

        Button create = new Button("Create Transaction",new Icon(VaadinIcon.PLUS));
        create.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        MenuBar action = new MenuBar();
        action.addThemeVariants(MenuBarVariant.LUMO_ICON,
                MenuBarVariant.LUMO_PRIMARY);
        MenuItem item = action.addItem("Actions");
        SubMenu subItems = item.getSubMenu();
        subItems.addItem("Save as draft");
        subItems.addItem("Save as copy");
        subItems.addItem("Save and publish");


        HorizontalLayout horizontalLayout = new HorizontalLayout(filter,create,action);
        horizontalLayout.setWidthFull();
        horizontalLayout.setJustifyContentMode(JustifyContentMode.END);
        add(horizontalLayout);
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
        splitLayout.addToPrimary(createEditorLayout());
        VerticalLayout gridWrapper = new VerticalLayout();
        gridWrapper.setSizeFull();
        gridWrapper.add(grid);
        grid.setSizeFull();
        splitLayout.addToSecondary(gridWrapper);
        splitLayout.getPrimaryComponent().setVisible(false);

        // Create UI
        add(splitLayout);



        // Configure Grid
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("phone").setAutoWidth(true);
        grid.addColumn("dateOfBirth").setAutoWidth(true);
        grid.addColumn("occupation").setAutoWidth(true);

        grid.addComponentColumn(file -> {
            MenuBar menuBar = new MenuBar();
            menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
            MenuItem menuItem = menuBar.addItem(":");
            menuItem.getElement().setAttribute("aria-label", "More options");
            MenuItem preview = menuItem.getSubMenu().addItem("Preview");
            preview.setVisible(false);
            MenuItem edit = menuItem.getSubMenu().addItem("Edit");
            edit.setVisible(false);
            MenuItem delete = menuItem.getSubMenu().addItem("Delete");
            delete.setVisible(false);
            menuItem.addClickListener(menuItemClickEvent -> {
                int val = new Random().nextInt(4);
                if(val%2 == 0){
                    preview.setVisible(true);
                    edit.setVisible(true);
                    delete.setVisible(false);

                } else{
                    preview.setVisible(true);
                    delete.setVisible(true);
                    edit.setVisible(false);
                }
            });
            return menuBar;
        }).setWidth("70px").setFlexGrow(0);


        grid.setItems(query -> samplePersonService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SAMPLEPERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            }
        });


    }

    private Component createEditorLayout() {
        VerticalLayout editorLayoutDiv = new VerticalLayout();
        editorLayoutDiv.setClassName("editor-layout");

        FormLayout formLayout = new FormLayout();
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new TextField("Email");
        phone = new TextField("Phone");
        dateOfBirth = new DatePicker("Date Of Birth");
        occupation = new TextField("Occupation");
        important = new Checkbox("Important");
        formLayout.add(firstName, lastName, email, phone, dateOfBirth, occupation, important);

        editorLayoutDiv.add(formLayout);
        editorLayoutDiv.add(createButtonLayout());
        return editorLayoutDiv;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addClickListener(l -> {
            splitLayout.getPrimaryComponent().setVisible(false);
        });
        save.addClickListener(l -> {
            splitLayout.getPrimaryComponent().setVisible(false);
        });
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        return buttonLayout;
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName,
                                    String label, String ariaLabel) {
        return createIconItem(menu, iconName, label, ariaLabel, false);
    }
    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName,
                                    String label, String ariaLabel, boolean isChild) {
        Icon icon = new Icon(iconName);

        if (isChild) {
            icon.getStyle().set("width", "var(--lumo-icon-size-s)");
            icon.getStyle().set("height", "var(--lumo-icon-size-s)");
            icon.getStyle().set("marginRight", "var(--lumo-space-s)");
        }

        MenuItem item = menu.addItem(icon, e -> {
        });

        if (ariaLabel != null) {
            item.getElement().setAttribute("aria-label", ariaLabel);
        }

        if (label != null) {
            item.add(new Text(label));
        }

        return item;
    }

}
