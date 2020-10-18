package minesweeper.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class MineSweeper {
	public static void main(String[] args) {
		Application.launch(MineSweeperRunner.class, args);
		//		SpringApplication.run(MineSweeper.class, args);
	}


}
