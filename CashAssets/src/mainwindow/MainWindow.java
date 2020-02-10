package mainwindow;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The Class which Objects display the Application's Window.
 * @author Haeldeus
 * @version 1.0
 */
public class MainWindow extends Application {
  
  /**
   * The Current version of the Application.
   */
  private final double version = 0.9;
  
  private final MainWindow me;
  
  private Label response;
  
  public MainWindow() {
    this.me = this;
  }

  /**
   * The Main-Method to start the Application from a Java Environment without JavaFX.
   * @param args  The Arguments to be passed.
   * @since 1.0
   */
  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    /*
     * Basic Scenery Settings.
     */
    primaryStage.setTitle("Weyher-Calculator");
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10, 10, 10, 10));
   
    /*
     * Adds a Menu Bar to the Scene.
     */
    MenuBar menu = new MenuBar();
    final Menu settingsMenu = new Menu("Settings");
    final MenuItem settingsItem = new MenuItem("Settings");
    settingsItem.setOnAction(new SettingsHandler(primaryStage));
    settingsMenu.getItems().addAll(settingsItem);
    menu.getMenus().addAll(settingsMenu);
    
    Button btCashAssets = new Button("Kassenbestand");
    btCashAssets.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        Path path = Paths.get("Settings.stg");
        if (!Files.exists(path)) {
          PrintWriter writer;
          try {
            writer = new PrintWriter(path.toString(), "UTF-8");
            writer.println("simpleDesign = 1");
            writer.println("openFolder = 1");
            writer.close();
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }
        }
        try {
          Stage stage = new Stage();
          stage.initModality(Modality.APPLICATION_MODAL);
          new kasse.MainWindow().start(stage);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    btCashAssets.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    GridPane.setFillHeight(btCashAssets, true);
    GridPane.setFillWidth(btCashAssets, true);
    GridPane.setVgrow(btCashAssets, Priority.ALWAYS);
    GridPane.setHgrow(btCashAssets, Priority.ALWAYS);
    grid.add(btCashAssets, 0, 0);
    
    Button btTip = new Button("Tip Rechner");
    //TODO: Add Handler.
    btTip.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    GridPane.setFillHeight(btTip, true);
    GridPane.setFillWidth(btTip, true);
    GridPane.setVgrow(btTip, Priority.ALWAYS);
    GridPane.setHgrow(btTip, Priority.ALWAYS);
    grid.add(btTip, 0, 1);
    
    Button btOutdoor = new Button("Außentheken-Bestand");
    //TODO: Add Handler.
    btOutdoor.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    GridPane.setFillHeight(btOutdoor, true);
    GridPane.setFillWidth(btOutdoor, true);
    GridPane.setVgrow(btOutdoor, Priority.ALWAYS);
    GridPane.setHgrow(btOutdoor, Priority.ALWAYS);
    grid.add(btOutdoor, 1, 0);
    
    Button btTodo = new Button("Mögliche Ergänzungen");
    btTodo.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    btTodo.setDisable(true);
    GridPane.setFillHeight(btTodo, true);
    GridPane.setFillWidth(btTodo, true);
    GridPane.setVgrow(btTodo, Priority.ALWAYS);
    GridPane.setHgrow(btTodo, Priority.ALWAYS);
    grid.add(btTodo, 1, 1);
    
    /*
     * Adds a BorderPane to the Scene, so the Grid will always be displayed at the right Place.
     */
    BorderPane bp = new BorderPane();
    bp.setTop(menu);
    bp.setCenter(grid);
    
    BorderPane bp2 = new BorderPane();
    Insets insets = new Insets(5);
    Label version = new Label("Version: " + getVersion());
    version.setMaxHeight(15.0);
    bp2.setLeft(version);
    BorderPane.setMargin(version, insets);
    
    response = new Label("");
    response.setMaxHeight(15.0);
    response.setMinWidth(250.0);
    
    GridPane gp = new GridPane();
    gp.add(response, 1, 0);
    GridPane.setHalignment(response, HPos.CENTER);
    
    ProgressBar bar = new ProgressBar();
    bar.setVisible(false);
    gp.add(bar, 0, 0);
    
    bp2.setRight(gp);
    
    Button btCheck = new Button("Update");
    btCheck.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        UpdateTask task = new UpdateTask(bar, me, response);
        bar.setVisible(true);
        response.setText("Suche nach Updates...");
        bar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
      }  
    });
    
    btCheck.setMaxHeight(15.0);
    bp2.setCenter(btCheck);
    BorderPane.setAlignment(btCheck, Pos.CENTER_LEFT);
    
    bp.setBottom(bp2);
    
    
    /*
     * Sets the Size of the Scene, it's restrictions and the Stylesheet. Afterwards, it displays 
     * the primaryStage to the User.
     */
    Scene scene = new Scene(bp, 600, 250);
    scene.getStylesheets().add("controlStyle1.css");
    primaryStage.setScene(scene);
    primaryStage.setMinHeight(270);
    primaryStage.setMinWidth(620);
    primaryStage.show();
  }
  
  protected double getVersion() {
    return version;
  }

  protected void setResponseText(String text) {
    response.setText(text);
  }
  
  protected void addResponseHandler(EventHandler<MouseEvent> handler) {
    response.setOnMouseClicked(handler);
  }
}
