package tipcalculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * A Class which Objects will display the Window for the Tip Calculation.
 * @author Haeldeus
 * @version 1.0
 */
public class TipWindow extends Application {

  //TODO: Delete this later on.
  /**
   * The Main Method for this Class. Used for testing to not start this from the Application's Main 
   * Window.
   * @param args  Unused.
   * @since 1.0
   */
  public static void main(String[] args) {
    TipWindow.launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Tip-Rechner");
    
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10, 10, 10, 10));
    
    MenuBar menu = new MenuBar();
    final Menu settingsMenu = new Menu("Settings");
    final MenuItem settingsItem = new MenuItem("Mitarbeiter...");
    settingsItem.setOnAction(new StaffHandler(primaryStage));
    settingsMenu.getItems().addAll(settingsItem);
    menu.getMenus().addAll(settingsMenu);
    
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(menu);
    borderPane.setCenter(grid);
    
    Scene scene = new Scene(borderPane, 600, 250);
    scene.getStylesheets().add("controlStyle1.css");
    primaryStage.setScene(scene);
    primaryStage.setMinHeight(270);
    primaryStage.setMinWidth(620);
    primaryStage.show();
  }

}
