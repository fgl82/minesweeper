package minesweeper.application.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import minesweeper.application.model.Board;
import net.rgielen.fxweaver.core.FxmlView;

@Getter
@Component
@FxmlView("main-stage.fxml")
public class MainController {

	@Autowired
	private BoardMasterService boardMasterService;

	@Autowired
	private Board board;

	@FXML
	private Label boardLabel;

	@FXML
	private GridPane boardPanel;

	private Node getNodeFromGridPane(int col, int row) {
		ObservableList<Node> children = boardPanel.getChildren().filtered(i -> i instanceof ImageView);
		for (Node node : children) {						
			if ((GridPane.getColumnIndex(node) == col) && (GridPane.getRowIndex(node) == row)) {				
				return node;
			}
		}
		return null;
	}	

	private void fireNode(Node node) {
		if(node!=null) {
			ImageView result = (ImageView)node;
			if(result.isVisible()) {
				result.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, false, false, null));
			}
		}
	}

	public void initializeBoard() {
		boardPanel.getParent().setStyle("-fx-background-color: #333333");
		((Pane)boardPanel.getParent()).setPrefSize(40*board.getRows(), 40*board.getColumns());
		boardMasterService.initializeBoard(board, 20, 20, 50);
		for (int row=0;row<board.getRows();row++) {
			final int finalRow = row;
			for (int column=0;column<board.getColumns();column++) {
				final int finalColumn = column;
				//Button button = new Button();
				//button.setStyle("-fx-focus-color: transparent;");
				//button.setStyle("-fx-faint-focus-color: transparent;");
				//button.setFocusTraversable(false);
				//button.setPrefSize(40, 40);
				Label label = new Label();
				label.setPrefSize(40, 40);
				label.setMaxSize(40, 40);
				label.setStyle("-fx-font-weight: bold");
				Image image = new Image("file:////home/bittboy/Downloads/minecraft-icon.png");
				ImageView view = new ImageView(image);
				view.setFitHeight(40);
				view.setFitWidth(40);				
				if (!board.getSquares()[row][column].getContent().boom()) {
					int value = boardMasterService.getSquareValue(board, row, column);
					Integer finalValue = value;
					if (finalValue==1) {
						label.setTextFill(Color.web("#00CCFF"));	
					} else if (finalValue==2) {
						label.setTextFill(Color.web("#00FF00"));
					} else if (finalValue==3) {
						label.setTextFill(Color.web("#FFFF00"));	
					} else if (finalValue==4) {
						label.setTextFill(Color.web("#FFA500"));
					} else if (finalValue==5) {
						label.setTextFill(Color.web("#EE82EE"));	
					} else if (finalValue==6) {
						label.setTextFill(Color.web("#FF2965"));	
					} else if (finalValue>=7) {
						label.setTextFill(Color.web("#FF0000"));	
					}
					if (finalValue>0) {
						label.setText(Integer.toString(finalValue));
					}
					label.setTextAlignment(TextAlignment.CENTER);
					label.setAlignment(Pos.CENTER);
					view.setOnMouseClicked(e -> {
						boardMasterService.discoverSquare();	
						view.setVisible(false);
						if (boardMasterService.getRemainingSquares()==0) {
							Alert a = new Alert(AlertType.INFORMATION);
							a.setContentText("You made it!");
							a.setTitle("All mines discovered");
							a.setHeaderText(null);
							a.show();
							return;
						} 						
						if(finalValue==0) {
							Node node = getNodeFromGridPane(finalColumn-1, finalRow-1);
							fireNode(node);
							node = getNodeFromGridPane(finalColumn, finalRow-1);
							fireNode(node);
							node = getNodeFromGridPane(finalColumn+1, finalRow-1);
							fireNode(node);
							node = getNodeFromGridPane(finalColumn-1, finalRow);
							fireNode(node);
							node = getNodeFromGridPane(finalColumn+1, finalRow);
							fireNode(node);
							node = getNodeFromGridPane(finalColumn-1,finalRow+1);
							fireNode(node);
							node = getNodeFromGridPane(finalColumn, finalRow+1);
							fireNode(node);
							node = getNodeFromGridPane(finalColumn+1, finalRow+1);
							fireNode(node);
						}
					});					
				} else {
					view.setOnMouseClicked(e -> {
						Alert a = new Alert(AlertType.ERROR);
						a.setContentText("You are dead");
						a.setTitle("Boom!!!");
						a.setHeaderText(null);
						a.show();
					});
				}
				this.boardPanel.add(label, column, row);			
				this.boardPanel.add(view, column, row);
				//button.setGraphic(view);
				//this.boardPanel.add(button, column, row);
			}
		}
	}

}
