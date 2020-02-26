package tipcalculator.handler;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Util;

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
   * An ArrayList of Strings, where all Staff Members will be saved in after reading the Strings 
   * from the File.
   */
  private ArrayList<String> staff;
  
  /**
   * A Constructor for the Handler. Will set the given Stage as {@link #primaryStage}. 
   * @param primaryStage  The Stage, that will be set as primary Stage.
   * @since 1.0
   */
  public StaffHandler(Stage primaryStage) {
    this.primaryStage = primaryStage;
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
    staffMemberPane.setVgap(5);
    staffMemberPane.setHgap(15);
    ScrollPane sp = new ScrollPane();
    sp.setContent(staffMemberPane);
    
    staff = Util.getStaffMembers();
    for (int i = 0; i < staff.size(); i++) {
      String s = staff.get(i);
      Label l = new Label(s);
      staffMemberPane.add(l, 0, i);
      Image img = new Image(getClass().getResourceAsStream("/res/delete.png"));
      ImageView imageview = new ImageView(img);
      Button b = new Button("", imageview);
      b.setTooltip(new Tooltip("Aus Liste entfernen"));
      b.setOnMouseClicked(new DeleteHandler(this, i, staff, staffMemberPane));
      staffMemberPane.add(b, 1, i);
    }
    
    Image img = new Image(getClass().getResourceAsStream("/res/add.png"));
    ImageView imageview = new ImageView(img);
    Button add = new Button("", imageview);
    add.setOnMouseClicked(new AddHandler(this, staffMemberPane));
    staffMemberPane.add(add, 0, staff.size());
    GridPane.setColumnSpan(add, 2);
    
    BorderPane bottom = new BorderPane();
    Button save = new Button("Speichern");
    save.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        Util.setStaffMembers(staff);
        dialog.close();
      }     
    });
    bottom.setLeft(save);
    
    Button cancel = new Button("Abbrechen");
    cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {   
        dialog.close();
      }    
    });
    bottom.setRight(cancel);
    
    BorderPane bp = new BorderPane();
    Insets insets = new Insets(5);
    bp.setTop(grid);
    BorderPane.setMargin(grid, insets);
    bp.setCenter(sp);
    BorderPane.setMargin(sp, insets);
    bp.setBottom(bottom);
    BorderPane.setMargin(bottom, insets);
    bp.setPadding(new Insets(5, 10, 5, 10));
    
    Scene dialogScene = new Scene(bp, 400, 200);
    dialogScene.getStylesheets().add(Util.getControlStyle());
    dialog.setScene(dialogScene);
    dialog.setResizable(false);
    dialog.setTitle("Mitarbeiter");
    dialog.show();
  }
  
  /**
   * Updates the {@link #staff} Field to the given new ArrayList of Strings. This is called, 
   * whenever a new Staff Member was added or an existing Member was deleted.
   * @param newStaff The new ArrayList of Strings, that contains the updated List of Staff Members.
   * @since 1.0
   */
  protected void updateStaff(ArrayList<String> newStaff) {
    this.staff = newStaff;
  }
  
  /**
   * Returns all Staff Members, that are currently saved in {@link #staff}. This can be different 
   * than {@link Util#getStaffMembers()}, since either adding or deleting a Staff Member in this 
   * Stage won't be saved immediately in the File.
   * @return  The Staff Members, that are currently saved in {@link #staff} as an ArrayList of 
   *     Strings.
   * @since 1.0
   */
  protected ArrayList<String> getStaff() {
    return staff;
  }
}
