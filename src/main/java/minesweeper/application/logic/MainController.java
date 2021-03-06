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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import minesweeper.application.model.Board;
import minesweeper.application.model.Square;
import minesweeper.application.utils.Constants;
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

	private void clickOnMineSquare(MouseEvent e, int row, int column, ImageView view) {
		Square square = boardMasterService.getSquare(board, row, column);
		Image image;
		if (e.getButton()==MouseButton.SECONDARY) {							
			if (!square.isFlagged()) {
				boardMasterService.flagSquare(square, true);
				image = new Image(Constants.WARNING_PICTURE);
			} else {
				boardMasterService.flagSquare(square, false);
				image = new Image(Constants.TILE_PICTURE);
			}
			view.setImage(image);
		} else {
			if (square.isFlagged()) {
				return;
			}
			image = new Image("mine.png");
			view.setScaleX(0.75);
			view.setScaleY(0.75);
			view.setImage(image);
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("You are dead");
			a.setTitle("Boom!!!");
			a.setHeaderText(null);
			a.show();
		}
	}

	private void clickOnEmptySquare(MouseEvent e, int value, int row, int column, ImageView view) {
		Square square = boardMasterService.getSquare(board, row, column);
		if (e.getButton() == MouseButton.SECONDARY) {
			Image image1;
			if (!square.isFlagged()) {
				boardMasterService.flagSquare(square, true);
				image1 = new Image(Constants.WARNING_PICTURE);
			} else {
				boardMasterService.flagSquare(square, false);
				image1 = new Image(Constants.TILE_PICTURE);
			}
			view.setImage(image1);
		} else {
			if (square.isFlagged()) {
				return;
			}
			boardMasterService.discoverSquare();
			view.setFitHeight(40);
			view.setFitWidth(40);
			view.setVisible(false);
			if (boardMasterService.getRemainingSquares() == 0) {
				Alert a = new Alert(AlertType.INFORMATION);
				a.setContentText("You made it!");
				a.setTitle("All mines discovered");
				a.setHeaderText(null);
				a.show();
				return;
			}
			if (value == 0) {
				Node node = getNodeFromGridPane(column - 1, row - 1);
				fireNode(node);
				node = getNodeFromGridPane(column, row - 1);
				fireNode(node);
				node = getNodeFromGridPane(column + 1, row - 1);
				fireNode(node);
				node = getNodeFromGridPane(column - 1, row);
				fireNode(node);
				node = getNodeFromGridPane(column + 1, row);
				fireNode(node);
				node = getNodeFromGridPane(column - 1, row + 1);
				fireNode(node);
				node = getNodeFromGridPane(column, row + 1);
				fireNode(node);
				node = getNodeFromGridPane(column + 1, row + 1);
				fireNode(node);
			}
		}
	}		


	public void initializeBoard() {
		boardPanel.getParent().setStyle("-fx-background-color: #333333");
		((Pane)boardPanel.getParent()).setPrefSize((double)40*board.getRows(), (double)40*board.getColumns());
		boardMasterService.initializeBoard(board, 10, 10, 10);
		for (int row=0;row<board.getRows();row++) {
			final int finalRow = row;
			for (int column=0;column<board.getColumns();column++) {
				final int finalColumn = column;
				Label label = new Label();
				label.setPrefSize(40, 40);
				label.setMaxSize(40, 40);
				label.setStyle("-fx-font-weight: bold");
				Image image = new Image(Constants.TILE_PICTURE);
				ImageView view = new ImageView(image);
				view.setFitHeight(40);
				view.setFitWidth(40);				
				if (!board.getSquares()[row][column].getContent().boom()) {
					int value = boardMasterService.getSquareValue(board, row, column);
					Integer finalValue = value;
					label.setTextFill(Color.web(boardMasterService.getColorForValue(finalValue)));					
					if (finalValue>0) {
						label.setText(Integer.toString(finalValue));
					}
					label.setTextAlignment(TextAlignment.CENTER);
					label.setAlignment(Pos.CENTER);
					view.setOnMouseClicked(e -> clickOnEmptySquare(e, finalValue, finalRow, finalColumn, view));

				} else {
					view.setOnMouseClicked(e -> clickOnMineSquare(e, finalRow, finalColumn, view));
				}
				this.boardPanel.add(label, column, row);			
				this.boardPanel.add(view, column, row);
			}
		}
	}

}
