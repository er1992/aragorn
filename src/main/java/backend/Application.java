package main.java.backend;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import main.java.backend.Pulse.Sentiment;
import spark.Spark;

public class Application {

  public static Logger log = LoggerFactory.getLogger(Application.class);
  public static EventLocationDataCollector eventCollector = new EventLocationDataCollector();
  public static List<Event> newEvents;
  public static List<EventInstas> eventInstasList;

  public static void main(String[] args) {
    Spark.port(8080);
    Spark.threadPool(10);

    registerStaticTestRoutes();

    registerBeforeFilters();
    registerGetRoutes();

    Properties props = new Properties();
    String getJsonFromRemote = "true";
    try {
      props.load(new FileInputStream("api.properties"));
      getJsonFromRemote = props.getProperty("GET_JSON_FROM_REMOTE");
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (getJsonFromRemote.equalsIgnoreCase("true")) {
      try {
        eventCollector.getEntries();
      } catch (Exception e) {
        e.printStackTrace();
      }

      List<Event> events = eventCollector.events;
      newEvents = new ArrayList<>();

      List<EventLocationDataCollectorRunnable> runnables = new ArrayList<>();
      for (Event event : events) {
        EventLocationDataCollectorRunnable runnable = new EventLocationDataCollectorRunnable(event);
        runnables.add(runnable);
      }

      System.out.println(runnables.size() + "THREADS LEFT");

      Iterator<EventLocationDataCollectorRunnable> i = runnables.iterator();
      while (i.hasNext()) {
        EventLocationDataCollectorRunnable runnable = i.next();
        runnable.run();
        newEvents.add(runnable.event);
        i.remove();
        System.out.println(runnables.size() + "THREADS LEFT");
      }

      while (runnables.size() != 0) {
        try {
          Thread.sleep(100);
          System.out.println("STILL WAITING ON THREADS TO SHUTDOWN");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      System.out.println("ALL THREADS FINISHED");

      try (Writer writer = new FileWriter("events.json")) {
        Gson gson = new GsonBuilder().create();
        gson.toJson(newEvents, writer);
      } catch (IOException e1) {
        e1.printStackTrace();
      }

    } else {
      try {
        TypeToken<List<Event>> unmarshallingType = new TypeToken<List<Event>>(){};
        newEvents = new Gson().fromJson(new JsonReader(new FileReader("events.json")), unmarshallingType.getType());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    List<Insta> instagramData = new ArrayList<>();
    String sql = "SELECT * FROM insta";
    Statement stmt = null;
    try {
      stmt = Utils.getConnection().createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while (!rs.next()) {
        instagramData.add(getInstaFromResultSet(rs));
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("Instas from DB: " + instagramData.size());
    
    Map<String, String> headers = new HashMap<>();
    headers.put("Ocp-Apim-Subscription-Key", "8d30aab7c387402bb70c1797720e6ba8");
    headers.put("Content-Type", "application/json");
    try {
      for (Insta insta : instagramData) {
        HttpRestConnection client = new HttpRestConnection();
        String response = client.sendPost("https://api.projectoxford.ai/emotion/v1.0/recognize",
            "{\"url\":\"" + insta.url + "\"}", headers);
        JSONObject obj = ((JSONObject) new JSONArray(response).get(0)).getJSONObject("scores");
        Iterator<?> keys = obj.keys();
        Double prevHighestVal = 0d;
        while (keys.hasNext()) {
          String key = (String) keys.next();
          Double val = Double.parseDouble(obj.getString("key"));
          if (val > prevHighestVal) {
            insta.sentiment = Sentiment.valueOf(key);
            prevHighestVal = val;
          }
        }
        String sqlInsert = "UPDATE insta SET sentiment=" + insta.sentiment + "WHERE caption=" + insta.caption;
        Utils.getConnection().createStatement().execute(sqlInsert);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    for (Insta insta : instagramData) {
      System.out.println("Sentiment: " + insta.sentiment);
    }

    eventInstasList = new ArrayList<>();

    for (Event event : newEvents) {
      EventInstas eventInstas = new EventInstas(event);
      for (Insta insta : instagramData) {
        // Add another if statement for cognitive decision making (Bluemix
        // Watson) on whether the caption is related to the event or not
        if (distance(insta.lat, event.loc.latitude, insta.lng, event.loc.longitude, 0d, 0d) < 50) {
          eventInstas.addInstas(insta);
        }
      }
      eventInstasList.add(eventInstas);
    }
    
    System.out.println("Events from DB: " + newEvents.size());

  }

  public static Insta getInstaFromResultSet(ResultSet rs) throws Exception {
    return new Insta(rs.getString("caption"), rs.getString("url"), rs.getDouble("lat"), rs.getDouble("lng"),
        Sentiment.valueOf(rs.getString("sentiment")));
  }

  public static double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {
    final int R = 6371; // Radius of the earth
    Double latDistance = Math.toRadians(lat2 - lat1);
    Double lonDistance = Math.toRadians(lon2 - lon1);
    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
        * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c * 1000; // convert to meters
    double height = el1 - el2;
    distance = Math.pow(distance, 2) + Math.pow(height, 2);

    return Math.sqrt(distance);
  }

  public static void registerBeforeFilters() {
    // TODO Auto-generated method stub

  }

  public static void registerStaticTestRoutes() {
    Spark.get("/test", (request, response) -> {
      return "TESTING COMPLETED";
    });

  }

  public static void registerGetRoutes() {
    Spark.get("/events", (request, response) -> {
      response.type("application/json");
      // response.header("Access-Control-Allow-Origin", "*");
      return new Gson().toJson(newEvents);
    });

    Spark.get("/eventsAggregate", (request, response) -> {
      response.type("application/json");
      return new Gson().toJson(eventInstasList);
    });

  }

}
