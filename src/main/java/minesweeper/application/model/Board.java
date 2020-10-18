package minesweeper.application.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class Board {
	int rows;
	int columns;
	Square[][] squares;

	public void init(int rows, int columns, int mines) {
		this.rows = rows;
		this.columns = columns;
		squares = new Square[rows][columns]; 
		for (int row=0;row<rows;row++) {
			for (int column=0;column<columns;column++) {
				squares[row][column] = new Square();
				squares[row][column].setContent(new EmptySpace());
			}
		}
		for (int plantedMines=0;plantedMines<mines;plantedMines++) {
			while (true) {
				int itsAMine = (int) (Math.random() * ((rows*columns)));
				if ((plantedMines==0)||!squares[itsAMine/rows][itsAMine%rows].getContent().boom()) {
					Mine mine = new Mine();
					squares[itsAMine/rows][itsAMine%rows].setContent(mine);
					break;
				}
			}			
		}
	}	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int row=0;row<rows;row++) {
			sb.append("\n");
			for (int column=0;column<columns;column++) {
				sb.append(squares[row][column].getContent().boom()==false?0:1);
				sb.append(" ");
			}
		}
		return sb.toString();
	}
}
