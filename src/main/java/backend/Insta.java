package main.java.backend;

import main.java.backend.Pulse.Sentiment;

public class Insta {

  public String caption;
  public String url;
  public double lat;
  public double lng;
  public Sentiment sentiment;
  
  public Insta(String caption, String url, double lat, double lng, Sentiment sentiment) {
    super();
    this.caption = caption;
    this.url = url;
    this.lat = lat;
    this.lng = lng;
    this.sentiment = sentiment;
  }
   
}
