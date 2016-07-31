package main.java.backend;

import java.util.List;

public class Event {
  public String title;
  public String link;
  public String author;
  public String date;
  public String description;
  public LatLong loc;

  public Event(String title, String link, String author, String date, String description, LatLong loc) {
    super();
    this.title = title;
    this.link = link;
    this.author = author;
    this.date = date;
    this.description = description;
    this.loc = loc;
  }

  public Event() {
    // TODO Auto-generated constructor stub
  }

}