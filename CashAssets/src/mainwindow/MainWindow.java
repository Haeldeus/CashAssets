package mainwindow;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
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
  
  /**
   * The instance of this Class. Used for the {@link UpdateTask}.
   */
  private final MainWindow me;
  
  /**
   * The Label, that displays responses of the UpdateTask.
   */
  private Label response;
  
  /**
   * The Constructor for all Objects of this Class. Sets the created Instance as {@link #me}.
   * @since 1.0
   */
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
    
    /*
     * Creates a new Button to launch the CashAssets Application and adds a Handler.
     */
    Button btCashAssets = new Button("Kassenbestand");
    btCashAssets.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        /*
         * Checks, if there is a Settings File. If not, this will create a new one with the 
         * default Settings.
         */
        Path path = Paths.get("Settings.stg");
        if (!Files.exists(path)) {
          PrintWriter writer;
          try {
            writer = new PrintWriter(path.toString(), "UTF-8");
            writer.println("simpleDesign = 1");
            writer.println("openFolder = 1");
            writer.close();
          } catch (FileNotFoundException e) {
            /*
             * Just for debugging purposes. This shouldn't be called at any time.
             */
            e.printStackTrace();
          } catch (UnsupportedEncodingException e) {
            /*
             * Just for debugging purposes. Usually this shouldn't be called at any time.
             */
            e.printStackTrace();
          }
        }
        try {
          /*
           * Creates a new Stage for the CashAssets Application. The Modality will be 
           * set to Application_modal to prevent closing the Main Window before the new one.
           */
          Stage stage = new Stage();
          stage.initModality(Modality.APPLICATION_MODAL);
          new kasse.MainWindow().start(stage);
        } catch (Exception e) {
          /*
           * Just for debugging purposes. Usually this shouldn't be called at any time.
           */
          e.printStackTrace();
        }
      }
    });
    /*
     * Ensures, that the Button for the new Window will scale with the Scene's size.
     */
    btCashAssets.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    GridPane.setFillHeight(btCashAssets, true);
    GridPane.setFillWidth(btCashAssets, true);
    GridPane.setVgrow(btCashAssets, Priority.ALWAYS);
    GridPane.setHgrow(btCashAssets, Priority.ALWAYS);
    grid.add(btCashAssets, 0, 0);
    
    /*
     * Creates a new Button to launch the Tip Calculator Application.
     */
    Button btTip = new Button("Tip Rechner");
    //TODO: Add Handler.
    /*
     * Ensures, that the Button for the new Window will scale with the Scene's size.
     */
    btTip.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    GridPane.setFillHeight(btTip, true);
    GridPane.setFillWidth(btTip, true);
    GridPane.setVgrow(btTip, Priority.ALWAYS);
    GridPane.setHgrow(btTip, Priority.ALWAYS);
    grid.add(btTip, 0, 1);
    
    /*
     * Creates a new Button to launch the Outdoor Cash Application.
     */
    Button btOutdoor = new Button("Außentheken-Bestand");
    //TODO: Add Handler.
    /*
     * Ensures, that the Button for the new Window will scale with the Scene's size.
     */
    btOutdoor.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    GridPane.setFillHeight(btOutdoor, true);
    GridPane.setFillWidth(btOutdoor, true);
    GridPane.setVgrow(btOutdoor, Priority.ALWAYS);
    GridPane.setHgrow(btOutdoor, Priority.ALWAYS);
    grid.add(btOutdoor, 1, 0);
    
    /*
     * Creates a disabled Button to fill the Scene and inform the User, that additions are possible.
     */
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
    
    /*
     * Adds a BorderPane, that will contain the lower part of the Scene.
     */
    BorderPane bp2 = new BorderPane();
    Insets insets = new Insets(5);
    /*
     * Creates the Label, that will display the Version.
     */
    Label version = new Label("Version: " + getVersion());
    version.setMaxHeight(15.0);
    bp2.setLeft(version);
    BorderPane.setMargin(version, insets);
    
    /*
     * Creates the Response Label to display responses from the Version Check.
     */
    response = new Label("");
    response.setMaxHeight(15.0);
    response.setMinWidth(250.0);
    
    /*
     * Creates a GridPane that contains the ProgressBar and Response Label used for the Version 
     * Check.
     */
    GridPane gp = new GridPane();
    gp.add(response, 1, 0);
    GridPane.setHalignment(response, HPos.CENTER);
    
    /*
     * Creates the ProgressBar to display the Progress of the Version Check.
     */
    ProgressBar bar = new ProgressBar();
    bar.setVisible(false);
    gp.add(bar, 0, 0);
    
    /*
     * Adds the new GridPane to the lower BorderPane.
     */
    bp2.setRight(gp);
    
    /*
     * Creates a new Button for the Version Check and adds a Handler.
     */
    Button btCheck = new Button("Update");
    btCheck.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        /*
         * Creates a new Update Task for the Version Check.
         */
        UpdateTask task = new UpdateTask(bar, me);
        /*
         * Displays the ProgressBar and the Response Label.
         */
        bar.setVisible(true);
        response.setText("Suche nach Updates...");
        /*
         * Binds the ProhressBar to the progress of the Task.
         */
        bar.progressProperty().bind(task.progressProperty());
        /*
         * Starts the Task in a new Thread.
         */
        new Thread(task).start();
      }  
    });
    
    /*
     * Adds the Button to the lower BorderPane and sets it's alignment.
     */
    btCheck.setMaxHeight(15.0);
    bp2.setCenter(btCheck);
    BorderPane.setAlignment(btCheck, Pos.CENTER_LEFT);
    
    /*
     * Adds the lower BorderPane to the total Pane.
     */
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
  
  /**
   * Returns the Version of this Class. Used for possible Updates.
   * @return  The Version, this Class is on.
   * @since 1.0
   */
  protected double getVersion() {
    return version;
  }

  /**
   * Sets the Text of {@link #response} to the given Text.
   * @param text  The Text to be set as a String.
   * @since 1.0
   */
  protected void setResponseText(String text) {
    response.setText(text);
  }
  
  /**
   * Adds the given handler to {@link #response}.
   * @param handler The {@link EventHandler} for a {@link MouseEvent}, that will be added 
   *     to {@link #response}.
   * @since 1.0
   */
  protected void addResponseHandler(EventHandler<MouseEvent> handler) {
    response.setOnMouseClicked(handler);
  }
  
  /**
   * Removes the EventHandler from {@link #response}.
   * @see #addResponseHandler(EventHandler)
   * @since 1.0
   */
  protected void removeResponseHandler() {
    try {
      response.removeEventHandler(MouseEvent.MOUSE_CLICKED, response.getOnMouseClicked());
    } catch (NullPointerException e) {
      //Do nothing since there are no Handlers to remove
    }
  }
}
