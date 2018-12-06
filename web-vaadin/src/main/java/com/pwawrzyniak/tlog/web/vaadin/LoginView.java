package com.pwawrzyniak.tlog.web.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends VerticalLayout {

  public LoginView() {
    TextField userField = new TextField("User");
    userField.setAutofocus(true);

    PasswordField passwordField = new PasswordField("Password");

    Element form = new Element("form");
    form.setAttribute("method", "post");
    form.setAttribute("action", "/login");
    Element username = new Element("input");
    username.setAttribute("type", "hidden");
    username.setAttribute("name", "username");
    form.appendChild(username);
    Element password = new Element("input");
    password.setAttribute("type", "hidden");
    password.setAttribute("name", "password");
    form.appendChild(password);

    Button submitButton = new Button("Submit", event -> {
      username.setAttribute("value", userField.getValue());
      password.setAttribute("value", passwordField.getValue());
      form.callFunction("submit");
    });

    add(userField, passwordField, submitButton);
    getElement().appendChild(form);
    setAlignItems(Alignment.CENTER);
  }
}