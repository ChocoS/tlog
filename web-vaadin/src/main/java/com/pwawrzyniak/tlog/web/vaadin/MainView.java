package com.pwawrzyniak.tlog.web.vaadin;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@UIScope
@Push
public class MainView extends VerticalLayout {

  private static Logger log = LoggerFactory.getLogger(MainView.class);

  public MainView(@Autowired BillsDisplayView billsDisplayView, @Autowired BillEditorView bIllEditorView) {
    add(bIllEditorView, billsDisplayView);
    setWidth("100%");
  }
}