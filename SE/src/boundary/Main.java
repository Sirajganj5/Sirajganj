package boundary;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
  private Stage primaryStage;
  private BorderPane rootLayout;

  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.setResizable(false);
    this.primaryStage.setTitle("Dashboard");
    initializeRootLayout();
    showDashboard();
  }

  public void initializeRootLayout() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("RootLayout.fxml"));
      rootLayout = (BorderPane) loader.load();
      Scene scene = new Scene(rootLayout);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void showDashboard() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("Dashboard.fxml"));
      HBox Dashboard = (HBox) loader.load();
      rootLayout.setCenter(Dashboard);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  
  public Stage getPrimaryStage() {
    return primaryStage;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
