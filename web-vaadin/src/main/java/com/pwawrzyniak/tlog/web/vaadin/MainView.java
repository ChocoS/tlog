package com.pwawrzyniak.tlog.web.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("vaadintest")
public class MainView extends VerticalLayout {

  public MainView() {
    Label label = new Label("off");
    Button button = new Button("on/off", event -> {
      if (label.getText().equals("on")) {
        label.setText("off");
      } else {
        label.setText("on");
      }
    });

    add(button, label);
  }
}