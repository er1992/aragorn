package backend;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

//

public class EventLocationDataCollector {

  public static String API_KEY = "AIzaSyAFrfhvQFuEyAnM7rPyVXxUXQxH193UGEc";

  public List<Event> events;

  public EventLocationDataCollector() {
    events = new ArrayList<>();
  }

  public void getEntries() throws Exception {

    URL url = new URL("http://www.adelaidecitycouncil.com/Ajax/whats_on_rss_feed");
    HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
    // Reading the feed
    SyndFeedInput input = new SyndFeedInput();
    SyndFeed feed = input.build(new XmlReader(httpcon));
    List<SyndEntry> entries = feed.getEntries();
    Iterator<SyndEntry> itEntries = entries.iterator();

    while (itEntries.hasNext()) {
      SyndEntry entry = (SyndEntry) itEntries.next();
      Event event = new Event(entry.getTitle(), entry.getLink(), entry.getAuthor(), entry.getPublishedDate(),
          entry.getDescription().getValue(), null);
      Document doc = Jsoup.connect(event.link).get();
      String location = doc.select("label label-where").text();

      String apiUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + location + "&key="
          + API_KEY;
      HttpRestConnection http = new HttpRestConnection();
      String response = http.sendGet(apiUrl);
      JSONObject json = new JSONObject(response);
      JSONObject obj = ((JSONObject) json.getJSONArray("results").get(0)).getJSONObject("geometry");
      event.loc = new LatLong(Double.valueOf(obj.get("lat").toString()), Double.valueOf(obj.get("long").toString()));
      events.add(event);
    }
  }
  
  public static class Event {
    public String title;
    public String link;
    public String author;
    public Date date;
    public String description;
    public LatLong loc;

    public Event(String title, String link, String author, Date date, String description, LatLong loc) {
      super();
      this.title = title;
      this.link = link;
      this.author = author;
      this.date = date;
      this.description = description;
      this.loc = loc;
    }

  }
}
