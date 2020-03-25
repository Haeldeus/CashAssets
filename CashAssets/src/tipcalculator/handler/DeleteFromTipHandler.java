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
    for (int i = 0; i < 3; i++) {
      GridPane.setMargin(grid.getChildren().get(3 * (rowIndex - 1)), null);
      grid.getChildren().remove(3 * (rowIndex - 1));
    }
    ObservableList<Node> tmpChildren = grid.getChildren();
    ArrayList<Node> children = new ArrayList<Node>();
    for (Node n : tmpChildren) {
      children.add(n);
    }
    grid.getChildren().clear();
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        for (int j = 0; j < children.size(); j++) {
          if (j % 3 == 2) {
            Image img = new Image(getClass().getResourceAsStream("/res/delete.png"));
            ImageView iw = new ImageView(img);
            Button btDelete = new Button("", iw);
            btDelete.setOnMouseClicked(new DeleteFromTipHandler((j / 3) + 1, grid, primary));
            grid.add(btDelete, 2, (j / 3));
          } else if (j == children.size() - 1) {
            int size = grid.getChildren().size();
            Image img = new Image(getClass().getResourceAsStream("/res/add.png"));
            ImageView iw = new ImageView(img);
            Button btAdd = new Button("", iw);
            grid.add(btAdd, 0, (size / 3 + 1));
            btAdd.setOnMouseClicked(new AddToTipHandler(primary, grid, size / 3 + 1));
          } else {
            grid.add(children.get(j), (j % 3), (j / 3));
          }
          
          if (j >= children.size() - 3) {
            GridPane.setMargin(grid.getChildren().get(j), null);
          }
        }
        primary.getTfTip().requestFocus();
      }     
    });
  }

}
