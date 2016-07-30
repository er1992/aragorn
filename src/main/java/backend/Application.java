package backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Spark;

public class Application {
  
  public static Logger log = LoggerFactory.getLogger(Application.class);
  public static EventLocationDataCollector evenCollector = new EventLocationDataCollector();
  
  
  public static void main(String[] args) {
    Spark.port(80);
    Spark.threadPool(10);

    registerStaticTestRoutes();

    registerBeforeFilters();
    registerGetRoutes();
    registerPostRoutes();
    registerPutRoutes();
    registerDeleteRoutes();
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
      return evenCollector.events;
    });

  }

  public static void registerBeforeFilters() {
    // TODO Auto-generated method stub

  }

  public static void registerStaticTestRoutes() {
    // TODO Auto-generated method stub

  }

}
