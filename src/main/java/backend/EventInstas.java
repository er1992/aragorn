package main.java.backend;

import java.util.ArrayList;
import java.util.List;

public class EventInstas {

  public Event event;
  public List<Insta> instas;
  
  public EventInstas(Event event) {
    this.event = event;
    this.instas = new ArrayList<>();
  }
  
  public void addInstas(Insta insta) {
    this.instas.add(insta);
  }
  
}
