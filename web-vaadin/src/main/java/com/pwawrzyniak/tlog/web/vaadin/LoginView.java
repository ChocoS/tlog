package com.pwawrzyniak.tlog.web.vaadin;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends VerticalLayout {

  private Element form = new Element("form");
  private Element username = new Element("input");
  private Element password = new Element("input");

  private TextField userField = new TextField("User");
  private PasswordField passwordField = new PasswordField("Password");

  public LoginView() {
    userField.setAutofocus(true);
    userField.addKeyPressListener(Key.ENTER, this::submitForm);

    passwordField.addKeyPressListener(Key.ENTER, this::submitForm);

    form.setAttribute("method", "post");
    form.setAttribute("action", "/login");
    username.setAttribute("type", "hidden");
    username.setAttribute("name", "username");
    form.appendChild(username);
    password.setAttribute("type", "hidden");
    password.setAttribute("name", "password");
    form.appendChild(password);

    Button submitButton = new Button("Submit", this::submitForm);

    add(userField, passwordField, submitButton);
    getElement().appendChild(form);
    setHorizontalComponentAlignment(Alignment.CENTER, userField, passwordField, submitButton);
  }

  private void submitForm(Object event) {
    username.setAttribute("value", userField.getValue());
    password.setAttribute("value", passwordField.getValue());
    form.callFunction("submit");
  }
}