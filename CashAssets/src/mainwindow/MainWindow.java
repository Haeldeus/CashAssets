package mainwindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mainwindow.handlers.SettingsHandler;
import mainwindow.tasks.NewsTask;
import mainwindow.tasks.UpdateTask;
import util.Util;

/**
 * The Class which Objects display the Application's Window.
 * @author Haeldeus
 * @version 1.0
 */
public class MainWindow extends Application {
  
  /**
   * The Current version of the Application.
   */
  private final double version = 0.96;
  
  /**
   * The instance of this Class. Used for the {@link UpdateTask}.
   */
  private final MainWindow me;
  
  /**
   * The Label, that displays responses of the UpdateTask.
   */
  private Label response;
  
  /**
   * Determines, if the Nightmode of the Application should be used while displaying.
   */
  private final boolean nightmode;
  
  /**
   * The primary Stage of the Application.
   */
  private Stage primary;
  
  /**
   * The Constructor for all Objects of this Class. Sets the created Instance as {@link #me}.
   * @since 1.0
   */
  public MainWindow() {
    this.me = this;
    this.nightmode = Util.checkNightmode();
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
    this.primary = primaryStage;
    primaryStage.setTitle("Weyher-Calculator");
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10, 10, 10, 10));
   
    /*
     * Adds a Menu Bar to the Scene.
     */
    final Menu settingsMenu = new Menu("Settings");
    final MenuItem settingsItem = new MenuItem("Settings");
    settingsItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, 
        KeyCombination.SHIFT_DOWN));
    settingsItem.setOnAction(new SettingsHandler(primaryStage));
    settingsMenu.getItems().addAll(settingsItem);
    MenuBar menu = new MenuBar();
    menu.getMenus().addAll(settingsMenu);
    
    /*
     * Adds the News Item to the MenuBar
     */
    final Menu newsMenu = new Menu("News");
    final MenuItem newsItem = new MenuItem("Änderungen");
    newsItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN, 
        KeyCombination.SHIFT_DOWN));
    newsItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        //new Thread(new NewsTask(me)).start();  
        ExecutorService executor = Executors.newSingleThreadExecutor();
        NewsTask task = new NewsTask(me);
        try {
          executor.submit(task).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
              showUpdate("Zeitüberschreitung");
            }         
          });
          task.cancel();
          executor.shutdownNow();
        }  
      } 
    });
    newsMenu.getItems().addAll(newsItem);
    menu.getMenus().addAll(newsMenu);
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
        Path path = Paths.get("data" + File.separator + "Settings.stg");
        if (!Files.exists(path)) {
          File directory = new File("data");
          if (!directory.exists()) {
            directory.mkdir();
          }
          PrintWriter writer;
          try {
            writer = new PrintWriter(path.toString(), "UTF-8");
            writer.println("simpleDesign = 1");
            writer.println("openFolder = 1");
            writer.println("nightMode = 0");
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
          new cashassets.CashAssetsWindow().start(stage);
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
    btTip.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
          new tipcalculator.TipWindow().start(stage);
        } catch (Exception e) {
          e.printStackTrace();
        }      
      }    
    });
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
     * Creates a new Button to launch the External Register Application.
     */
    Button btExternal = new Button("Außentheken-Bestand");
    btExternal.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
          new externalregister.ExternalWindow().start(stage);
        } catch (Exception e) {
          e.printStackTrace();
        }      
      }    
    });
    
    /*
     * Ensures, that the Button for the new Window will scale with the Scene's size.
     */
    btExternal.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    GridPane.setFillHeight(btExternal, true);
    GridPane.setFillWidth(btExternal, true);
    GridPane.setVgrow(btExternal, Priority.ALWAYS);
    GridPane.setHgrow(btExternal, Priority.ALWAYS);
    grid.add(btExternal, 1, 0);
    
    /*
     * Creates a Button to launch the Window for the monthly Balance.
     */
    Button btMonthlyBalance = new Button("Endabrechnung (Monat)");
    btMonthlyBalance.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
          new monthlybalance.MBalanceWindow().start(stage);
        } catch (Exception e) {
          e.printStackTrace();
        }      
      }    
    });
    /*
     * Ensures, that the Button for the new Window will scale with the Scene's size.
     */
    btMonthlyBalance.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    GridPane.setFillHeight(btMonthlyBalance, true);
    GridPane.setFillWidth(btMonthlyBalance, true);
    GridPane.setVgrow(btMonthlyBalance, Priority.ALWAYS);
    GridPane.setHgrow(btMonthlyBalance, Priority.ALWAYS);
    grid.add(btMonthlyBalance, 1, 1);
    
    /*
     * Creates disabled Buttons to fill the Scene and inform the User, that additions are possible.
     */
    Button btTodo = new Button("Mögliche Ergänzungen");
    btTodo.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    btTodo.setDisable(true);
    GridPane.setFillHeight(btTodo, true);
    GridPane.setFillWidth(btTodo, true);
    GridPane.setVgrow(btTodo, Priority.ALWAYS);
    GridPane.setHgrow(btTodo, Priority.ALWAYS);
    grid.add(btTodo, 2, 0);
    
    btTodo = new Button("Mögliche Ergänzungen");
    btTodo.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    btTodo.setDisable(true);
    GridPane.setFillHeight(btTodo, true);
    GridPane.setFillWidth(btTodo, true);
    GridPane.setVgrow(btTodo, Priority.ALWAYS);
    GridPane.setHgrow(btTodo, Priority.ALWAYS);
    grid.add(btTodo, 2, 1);
    
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
         * Displays the ProgressBar and the Response Label.
         */
        bar.setVisible(true);
        response.setText("Suche nach Updates..."); 
        /*
         * Creates a new Update Task for the Version Check.
         */
        UpdateTask task = new UpdateTask(bar, me);
        /*
         * Binds the ProhressBar to the progress of the Task.
         */
        bar.progressProperty().bind(task.progressProperty());
        /*
         * Starts the Task in a new Thread.
         */
        new Thread(task).start();
        new Thread(() -> {
          try {
            Thread.sleep(5000);  
          } catch (InterruptedException e) {
            //Testing Purposes, shouldn't be called.
            e.printStackTrace();
          }
          task.cancel();
        }).start();
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
    scene.getStylesheets().add(Util.getControlStyle());
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
  public double getVersion() {
    return version;
  }

  /**
   * Sets the Text of {@link #response} to the given Text.
   * @param text  The Text to be set as a String.
   * @since 1.0
   */
  public void setResponseText(String text) {
    response.setText(text);
  }
  
  /**
   * Adds the given handler to {@link #response}.
   * @param handler The {@link EventHandler} for a {@link MouseEvent}, that will be added 
   *     to {@link #response}.
   * @since 1.0
   */
  public void addResponseHandler(EventHandler<MouseEvent> handler) {
    response.setOnMouseClicked(handler);
  }
  
  /**
   * Removes the EventHandler from {@link #response}.
   * @see #addResponseHandler(EventHandler)
   * @since 1.0
   */
  public void removeResponseHandler() {
    try {
      response.removeEventHandler(MouseEvent.MOUSE_CLICKED, response.getOnMouseClicked());
    } catch (NullPointerException e) {
      //Do nothing since there are no Handlers to remove
    }
  }
  
  /**
   * Displays the Stage, where the user gets informed about the latest changes to the Application.
   * @param s The Text, that will be displayed in the Stage.
   * @since 1.0
   */
  public void showUpdate(String s) {
    final Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(primary);
    ScrollPane sp = new ScrollPane();
    Text t = new Text(s);
    sp.setContent(t);
    sp.setFitToWidth(true);
    Scene dialogScene = new Scene(sp, 300, 150);
    t.wrappingWidthProperty().bind(sp.widthProperty().subtract(15));
    dialogScene.getStylesheets().add(Util.getControlStyle());
    if (nightmode) {
      t.setId("text");
    }
    dialog.setTitle("Neuerungen in dieser Version");
    dialog.setScene(dialogScene);
    dialog.setResizable(true);
    dialog.show();
  }
}
