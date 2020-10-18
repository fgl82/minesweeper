package minesweeper.application.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mine implements Content{

	@Override
	public boolean boom () {
		return true;
	}
}
