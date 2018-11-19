package com.pwawrzyniak.tlog.web.vaadin;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.service.BillService;
import com.pwawrzyniak.tlog.backend.service.TagService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

  private static Logger log = LoggerFactory.getLogger(MainView.class);

  public MainView(@Autowired BillService billService, @Autowired TagService tagService) {
    List<BillDto> bills = billService.findAllBills();
    log.info("Found {} bills", bills.size());
    Component newBillForm = new BIllEditorView(tagService.findAllTagsSorted());
    Component billsDisplayComponent = new BillsDisplayView(bills);
    add(newBillForm, billsDisplayComponent);
    setWidth("100%");
  }
}