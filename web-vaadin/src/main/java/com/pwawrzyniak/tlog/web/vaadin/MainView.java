package com.pwawrzyniak.tlog.web.vaadin;

import com.pwawrzyniak.tlog.backend.data.BillItemRepository;
import com.pwawrzyniak.tlog.backend.model.BillItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Route("vaadintest")
public class MainView extends VerticalLayout {

  public MainView(@Autowired BillItemRepository billItemRepository) {
    Collection<BillItem> billItems = billItemRepository.findAll();
    Grid<BillItem> grid = new Grid<>();

    grid.setItems(billItems);
    grid.addColumn(billItem -> billItem.getId().toString()).setHeader("Id");
    grid.addColumn(billItem -> billItem.getCost().toString()).setHeader("Cost");
    grid.addColumn(BillItem::getDescription).setHeader("Description");
    grid.addColumn(billItem -> String.join(", ", billItem.getTags())).setHeader("Tags");
    grid.addColumn(BillItem::getExpression).setHeader("Expression");

    add(grid);
  }
}