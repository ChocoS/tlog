package com.pwawrzyniak.tlog.web.vaadin;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.service.BillService;
import com.pwawrzyniak.tlog.web.vaadin.events.Broadcaster;
import com.pwawrzyniak.tlog.web.vaadin.events.Event;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import static com.pwawrzyniak.tlog.web.vaadin.events.Event.Type.DELETE_BILL;
import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;
import static java.time.format.DateTimeFormatter.ofLocalizedDate;

@SpringComponent
@UIScope
public class BillsDisplayView extends VerticalLayout { // wrapping layout is needed to mitigate grid height issue

  private static Logger log = LoggerFactory.getLogger(BillsDisplayView.class);
  private static final String BILL_ITEMS_DIV_ID_PREFIX = "tlog_bill_items_";
  private static final String BILL_AUDIT_DIV_ID_PREFIX = "tlog_bill_audit_";

  private Grid<BillDto> billsGrid;
  private BillService billService;
  private Registration broadcasterRegistration;

  public BillsDisplayView(@Autowired BillService billService) {
    this.billService = billService;
    List<BillDto> bills = billService.findAllNotDeletedBills();

    billsGrid = new Grid<>();
    billsGrid.setItems(bills);
    billsGrid.addColumn(new LocalDateRenderer<>(BillDto::getDate, ofLocalizedDate(FormatStyle.SHORT)))
        .setHeader("Date").setFlexGrow(0).setWidth("100px");
    billsGrid.addColumn(BillDto::getTotalCost).setHeader("Cost").setFlexGrow(0).setWidth("100px");
    billsGrid.addColumn(new ComponentRenderer<>(this::billItemDisplayComponent)).setHeader("Bill items").setFlexGrow(0).setWidth("600px");
    billsGrid.addColumn(new ComponentRenderer<>(this::auditDisplayComponent)).setHeader("Audit").setFlexGrow(0).setWidth("400px");
    billsGrid.addColumn(new ComponentRenderer<>(this::operationsComponent)).setHeader("Operations").setFlexGrow(0).setWidth("100px");
    billsGrid.getStyle().set("border", "1px solid gray");
    billsGrid.setSelectionMode(Grid.SelectionMode.NONE);

    add(billsGrid);

    setHeight("500px");
    setPadding(false);
    setSpacing(false);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    UI ui = attachEvent.getUI();
    broadcasterRegistration = Broadcaster.register(myEvent -> {
      ui.access(this::refresh);
    });
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    broadcasterRegistration.remove();
    broadcasterRegistration = null;
  }

  public void refresh() {
    List<BillDto> bills = billService.findAllNotDeletedBills();
    billsGrid.setItems(bills);
    billsGrid.getDataProvider().refreshAll();
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

  private Component operationsComponent(BillDto billDto) {
    return new Button("Delete", clicked -> {
      billService.softDeleteBill(billDto);
      showDeleteConfirmation(billDto);
      fireDeleteBillEvent();
    });
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