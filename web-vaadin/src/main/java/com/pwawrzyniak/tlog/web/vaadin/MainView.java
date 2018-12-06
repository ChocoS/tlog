package com.pwawrzyniak.tlog.web.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@Route("main")
@UIScope
@Push
public class MainView extends VerticalLayout {

  public MainView(@Autowired BillsDisplayView billsDisplayView, @Autowired BillEditorView bIllEditorView) {
    Button logoutButton = new Button("Logout");
    logoutButton.getElement().setAttribute("onclick", "window.open('logout', '_self')");

    add(logoutButton, bIllEditorView, billsDisplayView);
    setWidth("100%");
    setHorizontalComponentAlignment(Alignment.END, logoutButton);
  }
}