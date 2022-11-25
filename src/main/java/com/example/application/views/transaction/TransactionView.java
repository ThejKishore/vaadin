package com.example.application.views.transaction;

import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.SamplePersonService;
import com.example.application.views.transactionlandingpage.TransactionLandingPageView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.*;
import org.vaadin.tatu.BeanTable;

import java.util.*;

@PageTitle("Transaction")
@Route(value = "transaction/:samplePersonID?/:action?(edit)")
@RouteAlias(value = "")
public class TransactionView extends Div implements BeforeEnterObserver {

    private final String SAMPLEPERSON_ID = "samplePersonID";

    private static final Set<String> states = new LinkedHashSet<>();
    private static final Set<String> countries = new LinkedHashSet<>();



    static {
        states.addAll(Arrays.asList("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut",
                "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
                "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi",
                "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
                "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
                "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington",
                "West Virginia", "Wisconsin", "Wyoming"));

        countries.addAll(Arrays.asList("Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola",
                "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia",
                "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize",
                "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Bouvet Island",
                "Brazil", "British Indian Ocean Territory", "British Virgin Islands", "Brunei Darussalam", "Bulgaria",
                "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
                "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands",
                "Colombia", "Comoros", "Congo", "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Cyprus",
                "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador",
                "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands",
                "Faroe Islands", "Federated States of Micronesia", "Fiji", "Finland", "France", "French Guiana",
                "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana",
                "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea",
                "Guinea-Bissau", "Guyana", "Haiti", "Heard Island and McDonald Islands", "Honduras", "Hong Kong",
                "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Ivory Coast",
                "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
                "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau",
                "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
                "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Moldova", "Monaco", "Mongolia",
                "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands",
                "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue",
                "Norfolk Island", "North Korea", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau",
                "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal",
                "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis",
                "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe",
                "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia",
                "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands",
                "South Korea", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname",
                "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic",
                "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago",
                "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
                "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands",
                "United States Virgin Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City State", "Venezuela",
                "Vietnam", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zaire", "Zambia",
                "Zimbabwe"));
    }

    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private TextField phone;
    private DatePicker dateOfBirth;
    private TextField occupation;
    private Checkbox important;
    private Button cancel = new Button("cancel");
    private Button save = new Button("Save");


    private final SamplePersonService samplePersonService;



    public TransactionView(SamplePersonService samplePersonService) {
        this.samplePersonService = samplePersonService;
        addClassNames("transaction-view", "flex", "flex-col", "h-full");
//        setSizeFull();
//        Main content = new Main();
//        content.addClassNames("grid", "gap-xl", "items-start", "justify-center", "max-w-screen-md", "mx-auto", "pb-l",
//                "px-l");
//
//        content.setSizeFull();

        SplitLayout content  = new SplitLayout();
        content.addToPrimary(createCheckoutForm());
        content.addToSecondary(createAside());
        content.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
        content.getPrimaryComponent().getElement().getStyle().set("width" ,"60%");
        add(content);
    }

    private Component createCheckoutForm() {
        Section checkoutForm = new Section();
        checkoutForm.addClassNames("flex", "flex-col", "flex-grow");

        H2 header = new H2("Checkout");
        header.addClassNames("mb-0", "mt-xl", "text-3xl");
        Paragraph note = new Paragraph("All fields are required unless otherwise noted");
        note.addClassNames("mb-xl", "mt-0", "text-secondary");
        checkoutForm.add(header, note);

        checkoutForm.add(createEditorLayout());
        checkoutForm.add(fromAccntSelection());
        checkoutForm.add(toAccntSelection());
        checkoutForm.add(createPaymentInformationSection());
        checkoutForm.add(new Hr());
        checkoutForm.add(createFooter());
        return checkoutForm;
    }

    Grid<SamplePerson> fromAccntGrid = new Grid<>(SamplePerson.class, false);
    private VerticalLayout createFromAccntLayout() {

        fromAccntGrid.addColumn(SamplePerson::getFirstName).setHeader("First name");
        fromAccntGrid.addColumn(SamplePerson::getLastName).setHeader("Last name");
        fromAccntGrid.addColumn(SamplePerson::getEmail).setHeader("Email");
        fromAccntGrid.addColumn(SamplePerson::getPhone).setHeader("Phone");
        fromAccntGrid.addColumn(SamplePerson::getDateOfBirth).setHeader("DOB");
        fromAccntGrid.setVisible(false);


        VerticalLayout dialogLayout = new VerticalLayout(fromAccntGrid);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("min-width", "300px")
                .set("max-width", "100%").set("height", "100%");

        return dialogLayout;
    }


    Grid<SamplePerson> toAccntGrid = new Grid<>(SamplePerson.class, false);
    private VerticalLayout createToAccntLayout() {

        toAccntGrid.addColumn(SamplePerson::getFirstName).setHeader("First name");
        toAccntGrid.addColumn(SamplePerson::getLastName).setHeader("Last name");
        toAccntGrid.addColumn(SamplePerson::getEmail).setHeader("Email");
        toAccntGrid.addColumn(SamplePerson::getPhone).setHeader("Phone");
        toAccntGrid.addColumn(SamplePerson::getDateOfBirth).setHeader("DOB");
        toAccntGrid.setVisible(false);


        VerticalLayout dialogLayout = new VerticalLayout(toAccntGrid);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("min-width", "300px")
                .set("max-width", "100%").set("height", "100%");

        return dialogLayout;
    }

    private Section fromAccntSelection() {
        Section shippingDetails = new Section();
        shippingDetails.addClassNames("flex", "flex-col", "mb-xl", "mt-m");

        H3 header = new H3("From");
        header.addClassNames("mb-m", "mt-s", "text-2xl");

        ComboBox<String> countrySelect = new ComboBox<>("Country");
        countrySelect.setRequiredIndicatorVisible(true);
        countrySelect.addClassNames("mb-s");
        countrySelect.setItems(countries);

        countrySelect.addValueChangeListener( val -> {
            fromAccntGrid.setVisible(true);
            List<SamplePerson> people = this.samplePersonService.findAll();
            fromAccntGrid.setItems(people);
//            fromGrid.setItems(query -> samplePersonService.list(
//                            PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
//                    .stream());
        });


        shippingDetails.add(header, countrySelect, createFromAccntLayout());
//        shippingDetails.add(header, countrySelect,createFromGrid());
        return shippingDetails;
    }

    private Section toAccntSelection() {
        Section shippingDetails = new Section();
        shippingDetails.addClassNames("flex", "flex-col", "mb-xl", "mt-m");

        H3 header = new H3("Send To");
        header.addClassNames("mb-m", "mt-s", "text-2xl");

        ComboBox<String> countrySelect = new ComboBox<>("Country");
        countrySelect.setRequiredIndicatorVisible(true);
        countrySelect.addClassNames("mb-s");
        countrySelect.setItems(countries);

        countrySelect.addValueChangeListener( val -> {
            List<SamplePerson> toAccnt = this.samplePersonService.findAll();
            toAccntGrid.setVisible(true);
            toAccntGrid.setItems(toAccnt);
//            toGrid.setDataProvider(DataProvider.ofCollection(samplePersonService.findAll()));
        });

//        shippingDetails.add(header, countrySelect,createToSection());
        shippingDetails.add(header, countrySelect,createToAccntLayout());
        return shippingDetails;
    }





    BeanTable<SamplePerson> fromGrid = new BeanTable<>(SamplePerson.class,false);
    BeanTable<SamplePerson> toGrid = new BeanTable<>(SamplePerson.class,false);
    private BeanTable createFromGrid(){
        fromGrid.setClassName("fl-from-table");
        fromGrid.setHtmlAllowed(true);
        fromGrid.addColumn("first",SamplePerson::getFirstName);
        fromGrid.addColumn("last",SamplePerson::getLastName);
        fromGrid.addComponentColumn(null , file -> {
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
        });
        return fromGrid;
    }

    private Div createToSection(){
        Div div = new Div();
        div.setClassName("table-wrapper");
        div.add(createToGrid());
        return div;
    }

    private BeanTable createToGrid(){
        toGrid.setClassName("fl-to-table");
        toGrid.setWidthFull();
        toGrid.addColumn("first",SamplePerson::getFirstName);
        toGrid.addColumn("last",SamplePerson::getLastName);
        toGrid.addComponentColumn(null , file -> {
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
        });
        return toGrid;
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
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        return buttonLayout;
    }

    private Component createPaymentInformationSection() {
        Section paymentInfo = new Section();
        paymentInfo.addClassNames("flex", "flex-col", "mb-xl", "mt-m");

        H3 header = new H3("Send To");
        header.addClassNames("mb-m", "mt-s", "text-2xl");

        TextField cardHolder = new TextField("Cardholder name");
        cardHolder.setRequiredIndicatorVisible(true);
        cardHolder.setPattern("[\\p{L} \\-]+");
        cardHolder.addClassNames("mb-s");

        Div subSectionOne = new Div();
        subSectionOne.addClassNames("flex", "flex-wrap", "gap-m");

        TextField cardNumber = new TextField("Card Number");
        cardNumber.setRequiredIndicatorVisible(true);
        cardNumber.setPattern("[\\d ]{12,23}");
        cardNumber.addClassNames("mb-s");

        TextField securityCode = new TextField("Security Code");
        securityCode.setRequiredIndicatorVisible(true);
        securityCode.setPattern("[0-9]{3,4}");
        securityCode.addClassNames("flex-grow", "mb-s");
        securityCode.setHelperText("What is this?");

        subSectionOne.add(cardNumber, securityCode);

        Div subSectionTwo = new Div();
        subSectionTwo.addClassNames("flex", "flex-wrap", "gap-m");

        Select<String> expirationMonth = new Select<>();
        expirationMonth.setLabel("Expiration month");
        expirationMonth.setRequiredIndicatorVisible(true);
        expirationMonth.setItems("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");

        Select<String> expirationYear = new Select<>();
        expirationYear.setLabel("Expiration month");
        expirationYear.setRequiredIndicatorVisible(true);
        expirationYear.setItems("22", "23", "24", "25", "26");

        subSectionTwo.add(expirationMonth, expirationYear);

        paymentInfo.add( header, cardHolder, subSectionTwo);
        return paymentInfo;
    }

    private Footer createFooter() {
        Footer footer = new Footer();
        footer.addClassNames("flex", "items-center", "justify-between", "my-m");

        Button cancel = new Button("Cancel order");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button pay = new Button("Pay securely", new Icon(VaadinIcon.LOCK));
        pay.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        pay.addClickListener(evnt -> {
            pay.getUI().ifPresent( ui ->{
                ui.navigate(TransactionLandingPageView.class);
            });
        });
        footer.add(cancel, pay);
        return footer;
    }

    private Aside createAside() {
        Aside aside = new Aside();
        aside.addClassNames("bg-contrast-5", "box-border", "p-l", "rounded-l", "float");
        aside.getStyle().set("position","sticky").set("top","0");
        Header headerSection = new Header();
        headerSection.addClassNames("flex", "items-center", "justify-between", "mb-m");
        H3 header = new H3("Order");
        header.addClassNames("m-0");
        Button edit = new Button("Edit");
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        headerSection.add(header, edit);

        UnorderedList ul = new UnorderedList();
        ul.addClassNames("list-none", "m-0", "p-0", "flex", "flex-col", "gap-m");

        ul.add(createListItem("Vanilla cracker", "With wholemeal flour", "$7.00"));
        ul.add(createListItem("Vanilla blueberry cake", "With blueberry jam", "$8.00"));
        ul.add(createListItem("Vanilla pastry", "With wholemeal flour", "$5.00"));

        aside.add(headerSection, ul);
        return aside;
    }

    private ListItem createListItem(String primary, String secondary, String price) {
        ListItem item = new ListItem();
        item.addClassNames("flex", "justify-between");

        Div subSection = new Div();
        subSection.addClassNames("flex", "flex-col");

        subSection.add(new Span(primary));
        Span secondarySpan = new Span(secondary);
        secondarySpan.addClassNames("text-s text-secondary");
        subSection.add(secondarySpan);

        Span priceSpan = new Span(price);

        item.add(subSection, priceSpan);
        return item;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> samplePersonId = event.getRouteParameters().get(SAMPLEPERSON_ID).map(UUID::fromString);
        if (samplePersonId.isPresent()) {
            Optional<SamplePerson> samplePersonFromBackend = samplePersonService.get(samplePersonId.get());
            if (samplePersonFromBackend.isPresent()) {
                populateForm(samplePersonFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested samplePerson was not found, ID = %s", samplePersonId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
//                event.forwardTo(TransactionLandingPageView.class);
            }
        }
    }

    private void populateForm(SamplePerson value) {
        this.firstName.setValue(value.getFirstName());
        this.lastName.setValue(value.getLastName());
        this.email.setValue(value.getEmail());
        this.phone.setValue(value.getPhone());
        this.dateOfBirth.setValue(value.getDateOfBirth());
        this.occupation.setValue(value.getOccupation());
    }
}
