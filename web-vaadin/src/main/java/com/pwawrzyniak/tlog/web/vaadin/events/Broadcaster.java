package com.pwawrzyniak.tlog.web.vaadin.events;

import com.vaadin.flow.shared.Registration;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Broadcaster {
  static Executor executor = Executors.newSingleThreadExecutor();

  static LinkedList<Consumer<Event>> listeners = new LinkedList<>();

  public static synchronized Registration register(
      Consumer<Event> listener) {
    listeners.add(listener);

    return () -> {
      synchronized (Broadcaster.class) {
        listeners.remove(listener);
      }
    };
  }

  public static synchronized void broadcast(Event event) {
    for (Consumer<Event> listener : listeners) {
      executor.execute(() -> listener.accept(event));
    }
  }
}