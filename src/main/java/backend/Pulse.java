package main.java.backend;

public class Pulse {

  public static enum Sentiment {
    ANGER, CONTEMPT, DISGUST, FEAR, HAPPINESS, NEUTRAL, SADNESS, SURPRISE
  }

  public static enum Heart {
    BUSINESS, EVENT
  }

  private int intensity;
  private Sentiment sentiment;
  private LatLong location;
  private Heart heart;

  public Pulse() {
    // TODO Auto-generated constructor stub
  }

  public Pulse(int intensity, Sentiment sentiment, LatLong location, Heart heart) {
    super();
    this.intensity = intensity;
    this.sentiment = sentiment;
    this.location = location;
    this.heart = heart;
  }

  public int getIntensity() {
    return intensity;
  }

  public void setIntensity(int intensity) {
    this.intensity = intensity;
  }

  public Sentiment getSentiment() {
    return sentiment;
  }

  public void setSentiment(Sentiment sentiment) {
    this.sentiment = sentiment;
  }

  public LatLong getLocation() {
    return location;
  }

  public void setLocation(LatLong location) {
    this.location = location;
  }

  public Heart getHeart() {
    return heart;
  }

  public void setHeart(Heart heart) {
    this.heart = heart;
  }

}
