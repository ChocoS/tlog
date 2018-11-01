package com.pwawrzyniak.tlog.web.vaadin;

import com.pwawrzyniak.tlog.backend.model.Bill;
import com.pwawrzyniak.tlog.backend.model.BillItem;
import com.pwawrzyniak.tlog.backend.service.MockDataProvider;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route("vaadintest")
public class MainView extends VerticalLayout {

  public MainView(@Autowired MockDataProvider mockDataProvider) {
    List<Bill> bills = mockDataProvider.getMockData();
    List<BillItem> billItems = bills.stream()
        .flatMap(bill -> bill.getBillItems().stream())
        .collect(Collectors.toList());
    Grid<BillItem> grid = new Grid<>();

    grid.setItems(billItems);
    grid.addColumn(billItem -> billItem.getCost().toString()).setHeader("Cost");
    grid.addColumn(BillItem::getDescription).setHeader("Description");
    grid.addColumn(billItem -> String.join(", ", billItem.getTags())).setHeader("Tags");
    grid.addColumn(BillItem::getExpression).setHeader("Expression");

    add(grid);
  }
}