package minesweeper.application.logic;

import org.springframework.stereotype.Service;

import lombok.Getter;
import minesweeper.application.model.Board;

@Service
@Getter
public class BoardMasterService {

	int remainingSquares;

	public void discoverSquare() {
		remainingSquares--;
	}

	public void initializeBoard(Board board, int rows, int columns, int mines) {
		remainingSquares = (rows * columns) - mines;
		board.init(rows, columns, mines);		
	}

	public int getSquareValue(Board board, int row, int column) {
		int value = getValue(board, row-1, column-1);
		value += getValue(board, row-1, column); 
		value += getValue(board, row-1, column+1);
		value += getValue(board, row, column-1);
		value += getValue(board, row, column+1);
		value += getValue(board, row+1, column-1);
		value += getValue(board, row+1, column);
		value += getValue(board, row+1, column+1);
		return value;
	}

	private int getValue(Board board, int row, int column) {
		int value = 0;
		try {
			value+=board.getSquares()[row][column].getContent().boom()?1:0;
		} catch (RuntimeException e) {
			return value;
		}
		return value;
	}
}