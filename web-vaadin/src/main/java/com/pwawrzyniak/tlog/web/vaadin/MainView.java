package com.pwawrzyniak.tlog.web.vaadin;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.service.BillService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.FormatStyle;
import java.util.List;

import static com.pwawrzyniak.tlog.backend.service.DataInit.bill;
import static com.pwawrzyniak.tlog.backend.service.DataInit.billItem;
import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ofLocalizedDate;

@Route("vaadintest")
public class MainView extends VerticalLayout {

  private static final String BILL_DIV_CLASS_NAME_PREFIX = "tlog_bill_class_";

  private static Logger log = LoggerFactory.getLogger(MainView.class);

  public MainView(@Autowired BillService billService) {
    List<BillDto> bills = billService.findAllBills();

    log.info("Found {} bills", bills.size());

    Grid<BillDto> billsGrid = new Grid<>();

    billsGrid.setItems(bills);
    billsGrid.addColumn(new LocalDateRenderer<>(BillDto::getDate, ofLocalizedDate(FormatStyle.SHORT)))
        .setHeader("Date").setFlexGrow(0).setWidth("100px");
    billsGrid.addColumn(BillDto::getCost).setHeader("Cost").setFlexGrow(0).setWidth("100px");
    billsGrid.addColumn(new ComponentRenderer<>(this::billItemDtoDisplayComponent)).setHeader("Bill items").setFlexGrow(0).setWidth("600px");
    billsGrid.getStyle().set("border", "1px solid gray");
    billsGrid.setSelectionMode(Grid.SelectionMode.NONE);

    VerticalLayout billsGridWrapper = new VerticalLayout(); // needed to mitigate grid height issue
    billsGridWrapper.setHeight("500px");
    billsGridWrapper.add(billsGrid);

    // new bill test
    Button newBillButton = new Button("new bill", (event) -> {
      billService.saveBill(bill(now(), billItem("0.99", "0.99", "new bill", "other")));
      billsGrid.setItems(billService.findAllBills());
    });
    // end

    add(newBillButton, billsGridWrapper);
  }

  private Component billItemDtoDisplayComponent(BillDto billDto) {
    Div parentDiv = new Div();
    parentDiv.setClassName(BILL_DIV_CLASS_NAME_PREFIX + billDto.getId()); // without this same bills are not displayed
    parentDiv.getStyle().set("white-space", "normal"); // this is for word wrap
    billDto.getBillItems().forEach(billItemDto -> {
      Div billItemDiv = new Div();
      billItemDiv.setText(billItemDto.display());
      parentDiv.add(billItemDiv);
    });
    return parentDiv;
  }
}