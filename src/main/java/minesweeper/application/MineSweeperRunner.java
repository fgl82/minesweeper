package minesweeper.application;

import java.io.IOException;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import minesweeper.application.logic.MainController;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.spring.SpringFxWeaver;

@Component
public class MineSweeperRunner extends Application {

	private ConfigurableApplicationContext applicationContext;	

	@Override
	public void init() {
		String[] args = getParameters().getRaw().toArray(new String[0]);
		this.applicationContext = new SpringApplicationBuilder().sources(MineSweeper.class).run(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
		Parent root = fxWeaver.loadView(MainController.class);
		applicationContext.getBean(MainController.class).initializeBoard();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.getIcons().add(new Image("mine.png"));
		stage.show();
	}	

	@Override
	public void stop() {
		this.applicationContext.close();
		Platform.exit();
	}

	@Bean
	public FxWeaver fxWeaver(ConfigurableApplicationContext applicationContext) {
		return new SpringFxWeaver(applicationContext); 
	}	

}
