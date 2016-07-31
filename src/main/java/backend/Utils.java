package main.java.backend;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Utils {

  public static Properties props;
  
  static {
    try {
      loadProperties();
      Class.forName(props.getProperty("DRIVER"));
    } catch (IOException | ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void loadProperties() throws FileNotFoundException, IOException {
    props = new Properties();
    props.load(new FileInputStream("jdbc.properties"));
  }
  
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(props.getProperty("URL"), props.getProperty("USER"), props.getProperty("PASS"));
  }

}
