package backend;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import backend.EventLocationDataCollector.Event;
import spark.Spark;

public class Application {

  public static Logger log = LoggerFactory.getLogger(Application.class);
  public static EventLocationDataCollector eventCollector = new EventLocationDataCollector();
  public static List<Event> newEvents;

  public static void main(String[] args) {
    Spark.port(8080);
    Spark.threadPool(10);

    registerStaticTestRoutes();

    registerBeforeFilters();
    registerGetRoutes();
    registerPostRoutes();
    registerPutRoutes();
    registerDeleteRoutes();

    try {
      eventCollector.getEntries();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    List<Event> events = eventCollector.events;
    newEvents = new ArrayList<>();

    for (Event event : events) {
      EventLocationDataCollectorRunnable runnable = new EventLocationDataCollectorRunnable(event);
      runnable.run();
      newEvents.add(runnable.event);
    }

  }

  public static void registerDeleteRoutes() {
    Spark.get("/test", (request, response) -> {
      return "TESTING COMPLETED";
    });

  }

  public static void registerPutRoutes() {
    // TODO Auto-generated method stub

  }

  public static void registerPostRoutes() {
    // TODO Auto-generated method stub

  }

  public static void registerGetRoutes() {
    Spark.get("/events", (request, response) -> {
      return new Gson().toJson(newEvents);
    });

  }

  public static void registerBeforeFilters() {
    // TODO Auto-generated method stub

  }

  public static void registerStaticTestRoutes() {
    // TODO Auto-generated method stub

  }

}
