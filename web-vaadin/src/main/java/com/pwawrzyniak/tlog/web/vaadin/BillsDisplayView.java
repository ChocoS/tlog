package com.pwawrzyniak.tlog.web.vaadin;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.service.BillService;
import com.pwawrzyniak.tlog.web.vaadin.events.Broadcaster;
import com.pwawrzyniak.tlog.web.vaadin.events.Event;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.pwawrzyniak.tlog.web.vaadin.events.Event.Type.DELETE_BILL;
import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@SpringComponent
@UIScope
public class BillsDisplayView extends VerticalLayout { // wrapping layout is needed to mitigate grid height issue

  private static final String BILL_ITEMS_DIV_ID_PREFIX = "tlog_bill_items_";
  private static final String BILL_AUDIT_DIV_ID_PREFIX = "tlog_bill_audit_";

  private Grid<BillDto> billsGrid;
  private BillService billService;
  private BillEditorView billEditorView;
  private Registration broadcasterRegistration;
  private TextField filterTextField;
  private ConfigurableFilterDataProvider<BillDto, Void, String> configurableFilterDataProvider;
  private Grid.Column<BillDto> costColumn;

  public BillsDisplayView(@Autowired BillService billService, @Autowired BillEditorView billEditorView) {
    this.billService = billService;
    this.billEditorView = billEditorView;

    configurableFilterDataProvider = billsGridDataProvider(billService);

    filterTextField = new TextField("Filter");
    filterTextField.addKeyPressListener(Key.ENTER, event -> refreshData());

    billsGrid = new Grid<>();
    billsGrid.setDataProvider(configurableFilterDataProvider);
    billsGrid.addColumn(new LocalDateRenderer<>(BillDto::getDate, DateTimeFormatter.ISO_DATE))
        .setHeader("Date").setFlexGrow(0).setWidth("100px");
    costColumn = billsGrid.addColumn(BillDto::getTotalCost).setHeader("Cost").setFlexGrow(0).setWidth("120px");
    billsGrid.addColumn(new ComponentRenderer<>(this::billItemDisplayComponent)).setHeader("Bill items");
    billsGrid.getStyle().set("border", "1px solid gray");
    billsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);
    billsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
    billsGrid.setItemDetailsRenderer(new ComponentRenderer<>(this::billItemsDetailsComponent));

    HorizontalLayout horizontalLayout = new HorizontalLayout(filterTextField);
    horizontalLayout.setPadding(false);
    horizontalLayout.setAlignItems(Alignment.END);

    add(horizontalLayout);
    add(billsGrid);

    setHeight("500px");
    setPadding(false);
    setSpacing(false);

    refreshTotalCost();
  }

  private void refreshTotalCost() {
    costColumn.setFooter("Total: " + billService.totalCostOfAllNotDeletedBySearchString(filterTextField.getValue()));
  }

  private ConfigurableFilterDataProvider<BillDto, Void, String> billsGridDataProvider(@Autowired BillService billService) {
    DataProvider<BillDto, String> dataProvider = DataProvider.fromFilteringCallbacks(
        query -> billService.findAllNotDeletedBySearchString(query.getOffset(), query.getLimit(), query.getFilter().orElse(null)).stream(),
        query -> billService.findAllNotDeletedBySearchString(query.getOffset(), query.getLimit(), query.getFilter().orElse(null)).size()
    );

    return dataProvider.withConfigurableFilter();
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    UI ui = attachEvent.getUI();
    broadcasterRegistration = Broadcaster.register(myEvent -> ui.access(this::refreshData));
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    broadcasterRegistration.remove();
    broadcasterRegistration = null;
  }

  private void refreshData() {
    configurableFilterDataProvider.setFilter(filterTextField.getValue());
    billsGrid.getDataProvider().refreshAll();
    refreshTotalCost();
  }

  private Component auditDisplayComponent(BillDto billDto) {
    Div parentDiv = new Div();
    parentDiv.setId(BILL_AUDIT_DIV_ID_PREFIX + billDto.getId()); // without this same bills are not displayed
    parentDiv.getStyle().set("white-space", "normal"); // this is for word wrap
    Div createdDiv = new Div();
    createdDiv.setText("Created at " + formatLocalDateTime(billDto.getCreatedAt()) + " by " + billDto.getCreatedBy());
    parentDiv.add(createdDiv);
    if (billDto.getLastModifiedAt() != null) {
      Div lastModifiedDiv = new Div();
      lastModifiedDiv.setText("Last modified at " + formatLocalDateTime(billDto.getLastModifiedAt()) + " by " + billDto.getLastModifiedBy());
      parentDiv.add(lastModifiedDiv);
    }
    return parentDiv;
  }

  private Component billItemDisplayComponent(BillDto billDto) {
    Div parentDiv = new Div();
    parentDiv.setId(BILL_ITEMS_DIV_ID_PREFIX + billDto.getId()); // without this same bills are not displayed
    parentDiv.getStyle().set("white-space", "normal"); // this is for word wrap
    billDto.getBillItems().forEach(billItemDto -> {
      Div billItemDiv = new Div();
      billItemDiv.setText(billItemDto.display());
      parentDiv.add(billItemDiv);
    });
    return parentDiv;
  }

  private Component billItemsDetailsComponent(BillDto billDto) {
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.getStyle().set("border", "1px solid");
    Button deleteButton = new Button(new Icon(VaadinIcon.FILE_REMOVE), clicked -> {
      billService.softDeleteBill(billDto);
      showDeleteConfirmation(billDto);
      fireDeleteBillEvent();
    });
    Button editButton = new Button(new Icon(VaadinIcon.EDIT), clicked -> billEditorView.edit(billDto));
    Button copyButton = new Button(new Icon(VaadinIcon.COPY), clicked -> billEditorView.copy(billDto));
    horizontalLayout.add(auditDisplayComponent(billDto), deleteButton, editButton, copyButton);

    return horizontalLayout;
  }

  private void fireDeleteBillEvent() {
    Broadcaster.broadcast(new Event(DELETE_BILL));
  }

  private void showDeleteConfirmation(BillDto billDto) {
    Notification.show("Bill deleted: " + billDto, 5000, TOP_CENTER);
  }

  private String formatLocalDateTime(LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }
}