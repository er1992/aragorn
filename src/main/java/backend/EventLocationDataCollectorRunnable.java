package backend;

import java.net.URLEncoder;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import backend.EventLocationDataCollector.Event;

public class EventLocationDataCollectorRunnable implements Runnable {

  public static String API_KEY = "AIzaSyAFrfhvQFuEyAnM7rPyVXxUXQxH193UGEc";
  Event event;

  public EventLocationDataCollectorRunnable(Event event) {
    this.event = event;
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
      System.out.println(event.link);
      JSONObject json = new JSONObject(response);
      JSONObject obj = ((JSONObject) json.getJSONArray("results").get(0)).getJSONObject("geometry")
          .getJSONObject("location");
      event.loc = new LatLong(Double.valueOf(obj.get("lat").toString()), Double.valueOf(obj.get("lng").toString()));
    } catch (Exception e) {
      System.out.println("Couldnt get" + event.title);
    }
  }

}
