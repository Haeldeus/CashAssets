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
  
  private double latestVersion;

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
    Label version = new Label("Version: 0.9");
    version.setMaxHeight(15.0);
    bp2.setLeft(version);
    BorderPane.setMargin(version, insets);
    
    GridPane gp = new GridPane();
    
    Label response = new Label("");
    response.setMaxHeight(15.0);
    response.setMinWidth(250.0);
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
        bar.setVisible(true);
        response.setText("Suche nach Updates...");
        Task<Void> task = new Task<Void>() {
          @Override 
          public Void call() {
            try {
              // Make a URL to the web page
              URL url = new URL("https://github.com/Haeldeus/CashAssets/blob/master/version.txt");
          
              // Get the input stream through URL Connection
              URLConnection con = url.openConnection();
              InputStream is = con.getInputStream();
          
              // Once you have the Input Stream, it's just plain old Java IO stuff.
          
              // For this case, since you are interested in getting plain-text web page
              // I'll use a reader and output the text content to System.out.
          
              // For binary content, it's better to directly read the bytes from stream and write
              // to the target file.
          
          
              BufferedReader br = new BufferedReader(new InputStreamReader(is));
          
              String line = null;
              
              int i = 0;
              int max = 10000;
              
              String s = "";
              
              // read each line and write to System.out
              while ((line = br.readLine()) != null) {
                s = s.concat(line);
                i++;
                updateProgress(i, max);
              }
              System.out.println(s.replaceAll("<" + "\\w+" + ">", ""));
              latestVersion = 0.9;
            } catch (IOException e) {
              e.printStackTrace();
              latestVersion = 0.0;
            }
            bar.setVisible(false);
            return null;
          }
        };
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
    Scene scene = new Scene(bp, 500, 250);
    scene.getStylesheets().add("controlStyle1.css");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  private double versionCheck() {
    try {
      // Make a URL to the web page
      URL url = new URL("https://github.com/Haeldeus/CashAssets/blob/master/version.txt");
  
      // Get the input stream through URL Connection
      URLConnection con = url.openConnection();
      InputStream is = con.getInputStream();
  
      // Once you have the Input Stream, it's just plain old Java IO stuff.
  
      // For this case, since you are interested in getting plain-text web page
      // I'll use a reader and output the text content to System.out.
  
      // For binary content, it's better to directly read the bytes from stream and write
      // to the target file.
  
  
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
  
      String line = null;
      
      int i = 0;
      int max = 10000;
      
      // read each line and write to System.out
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
      return 0.9;
    } catch (IOException e) {
      e.printStackTrace();
      return 0.0;
    }
  }
}
