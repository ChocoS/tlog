package com.pwawrzyniak.tlog.web.vaadin;

import com.pwawrzyniak.tlog.backend.dto.UserDto;
import com.pwawrzyniak.tlog.backend.service.security.UserDetailsServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@Route("main")
@UIScope
@Push
public class MainView extends VerticalLayout {

  public MainView(@Autowired UserDetailsServiceImpl userDetailsService,
                  @Autowired BillsDisplayView billsDisplayView, @Autowired BillEditorView bIllEditorView) {
    Button logoutButton = new Button("Logout");
    logoutButton.getElement().setAttribute("onclick", "window.open('logout', '_self')");
    UserDto userDto = userDetailsService.getLoggedInUser();
    Label label = new Label("Hello " + userDto.getFirstName());

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.add(label, logoutButton);

    add(horizontalLayout, bIllEditorView, billsDisplayView);
    setWidth("100%");
    setHorizontalComponentAlignment(Alignment.END, horizontalLayout);
  }
}