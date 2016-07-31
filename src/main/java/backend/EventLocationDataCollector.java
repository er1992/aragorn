package main.java.backend;

import java.util.ArrayList;
import java.util.List;

import main.java.backend.Feed;
import main.java.backend.FeedMessage;
import main.java.backend.LatLong;
import main.java.backend.RSSFeedParser;

//

public class EventLocationDataCollector {

  public List<Event> events;

  public EventLocationDataCollector() {
    events = new ArrayList<>();
  }

  public void getEntries() throws Exception {

    // URL url = new
    // URL("http://www.adelaidecitycouncil.com/Ajax/whats_on_rss_feed");
    // HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
    // // Reading the feed
    // SyndFeedInput input = new SyndFeedInput();
    // SyndFeed feed = input.build(new XmlReader(httpcon));
    // List<SyndEntry> entries = feed.getEntries();
    // Iterator<SyndEntry> itEntries = entries.iterator();
    // while (itEntries.hasNext()) {
    // SyndEntry entry = (SyndEntry) itEntries.next();
    // System.out.println(entry.getPublishedDate());
    // System.out.println(entry.getUpdatedDate());
    // Event event = new Event(entry.getTitle(), entry.getLink(),
    // entry.getAuthor(), entry.getUpdatedDate(),
    // entry.getDescription().getValue(), null);
    // events.add(event);
    // }
    
    RSSFeedParser parser = new RSSFeedParser("http://www.adelaidecitycouncil.com/Ajax/whats_on_rss_feed");
    Feed feed = parser.readFeed();

    for (FeedMessage message : feed.getMessages()) {
      
      Event event = new Event(message.getTitle(), message.getLink(), message.getAuthor(), message.getGuid(),
          message.getDescription(), null);
      events.add(event);
    }


  }

  public static class Event {
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

  }
}
