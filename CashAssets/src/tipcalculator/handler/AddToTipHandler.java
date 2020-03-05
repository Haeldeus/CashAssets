package tipcalculator.handler;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import tipcalculator.TipWindow;
import util.Util;

public class AddToTipHandler implements EventHandler<MouseEvent> {

  private final TipWindow primary;
  
  private GridPane gridPane;
  
  private int rowIndex;
  
  public AddToTipHandler(TipWindow primary, GridPane grid, int rowIndex) {
    this.primary = primary;
    this.gridPane = grid;
    this.rowIndex = rowIndex;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void handle(MouseEvent arg0) {
    gridPane.getChildren().remove(gridPane.getChildren().size() - 1);
    ArrayList<String> usedItems = new ArrayList<String>();
    for (int i = 5; i < gridPane.getChildren().size() - 1; i += 3) {
      usedItems.add(((ComboBox<String>)gridPane.getChildren().get(i)).getSelectionModel()
          .getSelectedItem());
    }
    ArrayList<String> tmpStaff = Util.getStaffMembers();
    ArrayList<String> res = new ArrayList<String>();
    for (String s : tmpStaff) {
      if (!usedItems.contains(s)) {
        res.add(s);
      }
    }
    ComboBox<String> staffMember = new ComboBox<String>(FXCollections
        .observableArrayList(res));
    staffMember.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        String selected = staffMember.getSelectionModel().getSelectedItem();
        ArrayList<String> usedItems = new ArrayList<String>();
        for (int i = 5; i < gridPane.getChildren().size() - 1; i += 3) {
          usedItems.add(((ComboBox<String>)gridPane.getChildren().get(i)).getSelectionModel()
              .getSelectedItem());
        }
        ArrayList<String> tmpStaff = Util.getStaffMembers();
        ArrayList<String> res = new ArrayList<String>();
        for (String s : tmpStaff) {
          if (!usedItems.contains(s)) {
            res.add(s);
          }
        }
        if (selected != null) {
          res.add(selected);
        }
        staffMember.setItems(FXCollections.observableArrayList(res));
      }      
    });
    gridPane.add(staffMember, 0, rowIndex);
    
    GridPane gridTemp = new GridPane();
    gridTemp.setHgap(5);
    TextField tfHours = new TextField();
    tfHours.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldProperty, 
          Boolean newProperty) {
        if (oldProperty && !newProperty) {
          System.out.println("Focus lost");
        } else if (!oldProperty && newProperty) {
          System.out.println("Focus gained");
        }
      }    
    });
    gridTemp.add(tfHours, 0, 0);
    Label labelH = new Label("h");
    gridTemp.add(labelH, 1, 0);
    gridPane.add(gridTemp, 1, rowIndex);
    
    Image img = new Image(getClass().getResourceAsStream("/res/delete.png"));
    ImageView imageview = new ImageView(img);
    Button remove = new Button("", imageview);
    remove.setTooltip(new Tooltip("Aus Liste entfernen"));
    remove.setOnMouseClicked(new DeleteFromTipHandler());
    gridPane.add(remove, 2, rowIndex);
    
    img = new Image(getClass().getResourceAsStream("/res/add.png"));
    imageview = new ImageView(img);
    Button add = new Button("", imageview);
    add.setTooltip(new Tooltip("Hinzufügen"));
    add.setOnMouseClicked(new AddToTipHandler(primary, gridPane, rowIndex + 1));
    gridPane.add(add, 0, rowIndex + 1);
  }
}
