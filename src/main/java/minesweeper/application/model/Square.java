package minesweeper.application.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class Square {
	private Content content;
	private boolean flagged;
}
