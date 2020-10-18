package minesweeper.application.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter

public class EmptySpace implements Content {

	@Override
	public boolean boom () {
		return false;
	}
}
