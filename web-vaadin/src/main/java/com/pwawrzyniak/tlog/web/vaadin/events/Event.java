package com.pwawrzyniak.tlog.web.vaadin.events;

public class Event {

  public enum Type {
    SAVE_BILL, DELETE_BILL
  }

  private Type type;

  public Event(Type type) {
    this.type = type;
  }

  public Type getType() {
    return type;
  }
}