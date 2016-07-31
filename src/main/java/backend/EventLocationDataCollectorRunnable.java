package main.java.backend;

import java.net.URLEncoder;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class EventLocationDataCollectorRunnable implements Runnable {

  public static String API_KEY = "AIzaSyCmova-f1LfsLRspT6BWT-5t_xJVVezT_Y";
  Event event;
  public boolean done;

  public EventLocationDataCollectorRunnable(Event event) {
    this.event = event;
    done = false;
  }

  @Override
  public void run() {

    Document doc = null;
    try {
      doc = Jsoup.connect(event.link).timeout(10 * 1000).get();

      String location = doc.select(".quick-info").select(".val:eq(4)").text();

      String apiUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + URLEncoder.encode(location)
          + "&key=" + API_KEY;

      HttpRestConnection http = new HttpRestConnection();
      String response = http.sendGet(apiUrl);
      JSONObject json = new JSONObject(response);
      System.out.println(location + " " + json.getString("status"));
      JSONObject obj = ((JSONObject) json.getJSONArray("results").get(0)).getJSONObject("geometry")
          .getJSONObject("location");
      event.loc = new LatLong(Double.valueOf(obj.get("lat").toString()), Double.valueOf(obj.get("lng").toString()));
    } catch (Exception e) {
      e.printStackTrace();
    }
    done = true;
  }

}
