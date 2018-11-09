package com.pwawrzyniak.tlog.web.vaadin;

import com.pwawrzyniak.tlog.backend.data.BillRepository;
import com.pwawrzyniak.tlog.backend.model.Bill;
import com.pwawrzyniak.tlog.backend.model.BillItem;
import com.pwawrzyniak.tlog.backend.model.Tag;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;

@Route("vaadintest")
public class MainView extends VerticalLayout {

  private static Logger log = LoggerFactory.getLogger(MainView.class);

  public MainView(@Autowired BillRepository billRepository) {
    List<Bill> bills = billRepository.findAll();

    log.info("Found {} bills", bills.size());

    Grid<Bill> billsGrid = new Grid<>();

    billsGrid.setItems(bills);
    billsGrid.addColumn(new LocalDateRenderer<>(Bill::getDate, ofLocalizedDate(FormatStyle.SHORT)))
        .setHeader("Date").setFlexGrow(0).setWidth("100px");
    billsGrid.addColumn(bill -> bill.getBillItems().stream().map(BillItem::getCost)
        .reduce(BigDecimal.ZERO, BigDecimal::add)).setHeader("Cost").setFlexGrow(0).setWidth("100px");
    billsGrid.addColumn(new ComponentRenderer<>(bill -> {
      Grid<BillItem> billItemsGrid = new Grid<>();
      billItemsGrid.setItems(bill.getBillItems());
      billItemsGrid.addColumn(TemplateRenderer.<BillItem>of(
          "<div class='bill-item-details' style='white-space: normal;'>"
              + "<div>[[item.name]]</div>"
              + "</div>")
          .withProperty("name", this::displayBillItem));
      billItemsGrid.setSelectionMode(Grid.SelectionMode.NONE);
      billItemsGrid.setHeightByRows(true);
      return billItemsGrid;
    })).setHeader("Bill items").setFlexGrow(0).setWidth("600px");
    billsGrid.getStyle().set("border", "1px solid gray");
    billsGrid.setSelectionMode(Grid.SelectionMode.NONE);

    VerticalLayout billsGridWrapper = new VerticalLayout(); // needed to mitigate grid height issue
    billsGridWrapper.setHeight("500px");
    billsGridWrapper.add(billsGrid);

    add(billsGridWrapper);
  }

  private String displayBillItem(BillItem billItem) {
    String tags = "(" + String.join(", ", billItem.getTags().stream().map(Tag::getName)
        .collect(Collectors.toList())) + ")";
    if (billItem.getDescription() != null) {
      return billItem.getCost() + " " + tags + " " + billItem.getDescription();
    } else {
      return billItem.getCost() + " " + tags;
    }
  }
}