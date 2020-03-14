package tipcalculator.handler;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

import javafx.application.Platform;
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
  
  private boolean exact;
  
  /**
   * 
   * @param primary
   * @param grid
   * @param rowIndex
   */
  public AddToTipHandler(TipWindow primary, GridPane grid, int rowIndex) {
    this.primary = primary;
    this.gridPane = grid;
    this.rowIndex = rowIndex;
    exact = false;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void handle(MouseEvent arg0) {
    gridPane.getChildren().remove(gridPane.getChildren().size() - 1);
    ArrayList<String> usedItems = new ArrayList<String>();
    for (int i = 0; i < gridPane.getChildren().size() - 1; i += 3) {
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
        for (int i = 0; i < gridPane.getChildren().size() - 1; i += 3) {
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
    tfHours.setMaxWidth(50);
    tfHours.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldProperty, 
          Boolean newProperty) {
        if (oldProperty && !newProperty) {
          String str = tfHours.getText();
          str = str.replaceAll("[\\D&&[^,]&&[^\\.]]", "");
          str = str.replaceAll(",", "\\.");
          if (!exact) {
            if (str.indexOf('.') != - 1) {
              str = str.substring(0, str.indexOf('.'));
            }
          }
          tfHours.setText(str.replace('.', ','));
          BigDecimal total = new BigDecimal("0.00");
          for (int i = 1; i < gridPane.getChildren().size(); i += 3) {
            GridPane smallGrid = (GridPane)gridPane.getChildren().get(i);
            TextField tmpTf = (TextField)smallGrid.getChildren().get(0);
            try {
              if (tmpTf.getText() != null && !tmpTf.getText().equals("")) {
                total = total.add(new BigDecimal(tmpTf.getText().replace(',', '.')));
              }
            } catch (NumberFormatException nfe) {
              nfe.printStackTrace();
              System.err.println("Skipping this Error....");
            }
          }
          BigDecimal totalHours = new BigDecimal("0.00");
          totalHours = totalHours.add(total);
          
          totalHours = totalHours.setScale(2, RoundingMode.DOWN);
          primary.getLbHoursTotal().setText(totalHours.toString().replace('.', '.') + "h");
          
          if (primary.getTfTip().getText() != null && !primary.getTfTip().getText().equals("")) {
            String txt = primary.getTfTip().getText();
            txt = txt.replaceAll("[\\D&&[^,]&&[^\\.]]", "");
            txt = txt.replaceAll(",", "\\.");
            BigDecimal totalTip = new BigDecimal(txt);
            totalTip = totalTip.setScale(2, RoundingMode.DOWN);
            
            MathContext mc = new MathContext(10);
            
            if (!primary.getTfKitchen().isDisabled()) {
              if (primary.getTfKitchen().getText() != null && !primary.getTfKitchen().getText()
                  .equals("")) {
                String kitchenCut = primary.getTfKitchen().getText();
                kitchenCut = kitchenCut.replaceAll("[\\D&&[^,]&&[^\\.]]", "");
                kitchenCut = kitchenCut.replaceAll(",", "");
                kitchenCut = kitchenCut.replaceAll("\\.", "");
                BigDecimal tipKitchen = totalTip.multiply(new BigDecimal("0." 
                    + kitchenCut).setScale(2, RoundingMode.HALF_DOWN), mc);
                totalTip = totalTip.subtract(tipKitchen, mc);
                Platform.runLater(new Runnable() {
                  @Override
                  public void run() {
                    primary.getLbKitchenTip().setText("= " + tipKitchen.setScale(2, 
                        RoundingMode.HALF_DOWN).toString() + "€");
                  }                 
                });
              }
            } else {
              primary.getLbKitchenTip().setText("");
            }
            
            BigDecimal tipPerHour = totalTip.divide(totalHours, mc);
            System.out.println("Tip per Hour: " + tipPerHour.toString());
            for (int i = 1; i < gridPane.getChildren().size(); i += 3) {
              GridPane smallGrid = (GridPane)gridPane.getChildren().get(i);
              TextField tmpTf = (TextField)smallGrid.getChildren().get(0);
              try {
                if (tmpTf.getText() != null && !tmpTf.getText().equals("")) {
                  Label lbTmp = (Label)smallGrid.getChildren().get(2);
                  BigDecimal tip = new BigDecimal("0.00");
                  tip = tipPerHour.multiply(new BigDecimal(tmpTf.getText()));
                  tip = tip.setScale(2, RoundingMode.HALF_DOWN);
                  lbTmp.setText("= " + tip.toString() + "€");
                }
              } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                System.err.println("Skipping this Error....");
              }
            }
          }
        } else if (!oldProperty && newProperty) {
          //TODO check if there is something to be done
        }
      }    
    });
    gridTemp.add(tfHours, 0, 0);
    
    Label labelH = new Label("h");
    gridTemp.add(labelH, 1, 0);
    
    Label lbTip = new Label("=");
    lbTip.setMinWidth(50);
    gridTemp.add(lbTip, 2, 0);
    gridPane.add(gridTemp, 1, rowIndex);
    
    Image img = new Image(getClass().getResourceAsStream("/res/delete.png"));
    ImageView imageview = new ImageView(img);
    Button remove = new Button("", imageview);
    remove.setTooltip(new Tooltip("Aus Liste entfernen"));
    remove.setOnMouseClicked(new DeleteFromTipHandler(rowIndex, gridPane, primary));
    gridPane.add(remove, 2, rowIndex);

    img = new Image(getClass().getResourceAsStream("/res/add.png"));
    imageview = new ImageView(img);
    Button add = new Button("", imageview);
    add.setTooltip(new Tooltip("Hinzufügen"));
    add.setOnMouseClicked(new AddToTipHandler(primary, gridPane, rowIndex + 1));
    gridPane.add(add, 0, rowIndex + 1);
  }
}
