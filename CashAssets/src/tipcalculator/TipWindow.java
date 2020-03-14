package tipcalculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tipcalculator.handler.AddToTipHandler;
import tipcalculator.handler.StaffHandler;
import util.Util;

/**
 * A Class which Objects will display the Window for the Tip Calculation.
 * @author Haeldeus
 * @version 1.0
 */
public class TipWindow extends Application {
  
  private Label lbHoursTotal;
  
  public TipWindow() {
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
    
    Label tipSum = new Label("Summe Tip: ");
    grid.add(tipSum, 0, 0);
    
    GridPane tipPane = new GridPane();
    tipPane.setHgap(5);
    
    TextField tfTip = new TextField();
    tipPane.add(tfTip, 0, 0);
    Label tipEuroLabel = new Label("€");
    tipPane.add(tipEuroLabel, 1, 0);
    
    grid.add(tipPane, 1, 0);
    
    Label totalHours = new Label("Gesamtstunden: ");
    grid.add(totalHours, 0, 1);
    
    lbHoursTotal = new Label("0,00h");
    lbHoursTotal.setMaxWidth(50);
    grid.add(lbHoursTotal, 1, 1);
    grid.getColumnConstraints().add(new ColumnConstraints());
    grid.getColumnConstraints().add(new ColumnConstraints());
    grid.getColumnConstraints().get(1).setMaxWidth(75);
    grid.getColumnConstraints().add(new ColumnConstraints());
    grid.getColumnConstraints().add(new ColumnConstraints());
    grid.getColumnConstraints().get(3).setMinWidth(75);
    
    Separator sep = new Separator();
    grid.add(sep, 0, 2);
    GridPane.setColumnSpan(sep, 2);
    
    GridPane staffGrid = new GridPane();
    staffGrid.setHgap(5);
    staffGrid.setVgap(5);
    
    Image img = new Image(getClass().getResourceAsStream("/res/add.png"));
    ImageView imgView = new ImageView(img);
    Button add = new Button("", imgView);
    add.setTooltip(new Tooltip("Hinzufügen"));
    add.setOnMouseClicked(new AddToTipHandler(this, staffGrid, 1));
    staffGrid.add(add, 0, 0);
    
    ScrollPane sp = new ScrollPane();
    sp.setContent(staffGrid);
    sp.setFitToWidth(true);
    grid.add(sp, 0, 3);
    GridPane.setColumnSpan(sp, 4);
    
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(menu);
    borderPane.setCenter(grid);
    
    Scene scene = new Scene(borderPane, 600, 250);

    scene.getStylesheets().add(Util.getControlStyle());
    primaryStage.setScene(scene);
    primaryStage.setMinHeight(270);
    primaryStage.setMinWidth(310);
    primaryStage.show();
  }
  
  /**
   * Returns the Label for the total amount of hours, so it can be altered by other Objects.
   * @return  The Label for the Total amount of hours.
   * @see #lbHoursTotal
   * @since 1.0
   */
  public Label getLbHoursTotal() {
    return lbHoursTotal;
  }
}
