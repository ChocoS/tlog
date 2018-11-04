package com.pwawrzyniak.tlog.web.vaadin;

import com.pwawrzyniak.tlog.backend.data.BillItemRepository;
import com.pwawrzyniak.tlog.backend.data.DataInit;
import com.pwawrzyniak.tlog.backend.model.BillItem;
import com.pwawrzyniak.tlog.backend.model.Tag;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.stream.Collectors;

@Route("vaadintest")
public class MainView extends VerticalLayout {

  private static Logger log = LoggerFactory.getLogger(MainView.class);

  public MainView(@Autowired BillItemRepository billItemRepository) {
    Collection<BillItem> billItems = billItemRepository.findAll();

    log.info("Found {} bill items", billItems.size());

    Grid<BillItem> grid = new Grid<>();

    grid.setItems(billItems);
    grid.addColumn(billItem -> billItem.getId().toString()).setHeader("Id");
    grid.addColumn(billItem -> billItem.getCost().toString()).setHeader("Cost");
    grid.addColumn(BillItem::getDescription).setHeader("Description");
    grid.addColumn(billItem -> String.join(", ", billItem.getTags().stream().map(Tag::getName).collect(Collectors.toList()))).setHeader("Tags");
    grid.addColumn(BillItem::getExpression).setHeader("Expression");

    add(grid);
  }
}