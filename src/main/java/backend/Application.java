package backend;

import static spark.Spark.*;

import spark.Spark;

public class Application {

  public static void main(String[] args) {
    Spark.port(8080);
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
    // TODO Auto-generated method stub

  }

  public static void registerBeforeFilters() {
    // TODO Auto-generated method stub

  }

  public static void registerStaticTestRoutes() {
    // TODO Auto-generated method stub

  }

}
