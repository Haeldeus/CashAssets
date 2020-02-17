package tipcalculator;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A Handler for the Staff Button in the Menu of the Tip-Window.
 * @author Haeldeus
 * @version 1.0
 */
public class StaffHandler implements EventHandler<ActionEvent> {

  /**
   * The Stage, this Handler was called from.
   */
  private final Stage primaryStage;
  
  /**
   * The Path to the Staff File.
   */
  private Path path;
  
  /**
   * A Constructor for the Handler. Will set the given Stage as {@link #primaryStage}. 
   * @param primaryStage  The Stage, that will be set as primary Stage.
   * @since 1.0
   */
  public StaffHandler(Stage primaryStage) {
    this.primaryStage = primaryStage;
    path = Paths.get("data" + File.separator + "Staff.stg");
  }
  
  @Override
  public void handle(ActionEvent arg0) {
    /*
     * Creates a new Stage to display the Settings Scene.
     */
    final Stage dialog = new Stage();
    dialog.initOwner(primaryStage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    /*
     * Creates a new GridPane, that will contain all fields of this Scene.
     */
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    
    Label infoLabel = new Label("Hier können Sie die Mitarbeiter Ihres Teams bearbeiten.");
    grid.add(infoLabel, 0, 0);
    GridPane.setColumnSpan(infoLabel, 10);
    
    GridPane staffMemberPane = new GridPane();
    ScrollPane sp = new ScrollPane();
    sp.setContent(staffMemberPane);
    
    String[] staff = getStaffMembers();
    
    for (int i = 0; i < staff.length; i++) {
      String s = staff[i];
      Label l = new Label(s);
      staffMemberPane.add(l, 0, i);
      Image img = new Image(getClass().getResourceAsStream("/res/delete.png"));
      ImageView imageview = new ImageView(img);
      Button b = new Button("", imageview);
      staffMemberPane.add(b, 1, i);
    }
    
    
    BorderPane bp = new BorderPane();
    bp.setTop(grid);
    bp.setCenter(sp);
    
    Scene dialogScene = new Scene(bp, 400, 200);
    dialogScene.getStylesheets().add("controlStyle1.css");
    dialog.setScene(dialogScene);
    dialog.setResizable(false);
    dialog.setTitle("Mitarbeiter");
    dialog.show();
    
    System.out.println("Has to be handled...");
  }

  private String[] getStaffMembers() {
    //TODO: Do actual file reading
    String[] res = new String[20];
    for (int i = 1; i <= 20; i++) {
      res[i - 1] = "SM" + i;
    }
    return res;
  }

}
