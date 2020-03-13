package tipcalculator.handler;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import tipcalculator.TipWindow;

public class DeleteFromTipHandler implements EventHandler<MouseEvent> {

  private int rowIndex;
  
  private GridPane grid;
  
  private TipWindow primary;
  
  public DeleteFromTipHandler(int rowIndex, GridPane grid, TipWindow primary) {
    this.rowIndex = rowIndex;
    this.grid = grid;
    this.primary = primary;
  }
  
  @Override
  public void handle(MouseEvent arg0) {
    System.out.println("RowIndex of pressed Delete: " + rowIndex);
    for (int i = 0; i < 3; i++) {
      System.out.println("Remove index: " + (3 * (rowIndex - 1)) 
          + " from total Size: " + grid.getChildren().size() + ". Op " + (i + 1) + "/3");
      GridPane.setMargin(grid.getChildren().get(3 * (rowIndex - 1)), null);
      grid.getChildren().remove(3 * (rowIndex - 1));
    }
    ObservableList<Node> tmpChildren = grid.getChildren();
    ArrayList<Node> children = new ArrayList<Node>();
    for (Node n : tmpChildren) {
      children.add(n);
    }
    System.out.println(children.size());
    grid.getChildren().clear();
    System.out.println(children.size());
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        for (int j = 0; j < children.size(); j++) {
          if (j % 3 == 2) {
            System.out.println("Adding del Button at: " + (j % 3) + "," + (j / 3));
            Image img = new Image(getClass().getResourceAsStream("/res/delete.png"));
            ImageView iw = new ImageView(img);
            Button btDelete = new Button("", iw);
            btDelete.setOnMouseClicked(new DeleteFromTipHandler((j / 3) + 1, grid, primary));
            grid.add(btDelete, 2, (j / 3));
          } else if (j == children.size() - 1) {
            int size = grid.getChildren().size();
            System.out.println("Size after OP: " + size);
            System.out.println("Row to add plus in: " + (size / 3 + 1));
            Image img = new Image(getClass().getResourceAsStream("/res/add.png"));
            ImageView iw = new ImageView(img);
            Button btAdd = new Button("", iw);
            grid.add(btAdd, 0, (size / 3 + 1));
            btAdd.setOnMouseClicked(new AddToTipHandler(primary, grid, size / 3 + 1));
          } else {
            System.out.println("adding new Node at: " + (j % 3) + "," + (j / 3));
            grid.add(children.get(j), (j % 3), (j / 3));
          }
          
          if (j >= children.size() - 3) {
            GridPane.setMargin(grid.getChildren().get(j), null);
          }
        }
      }     
    });
  }

}
