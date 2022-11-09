package dad.ahorcado;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
// David Alejandro Hernández Alonso 2º DAM A
public class AhorcadoApp extends Application {

	public static Stage primaryStage;
	
	private RootController rootController = new RootController();
	@Override
	public void start(Stage primaryStage) throws Exception {
			
		primaryStage.setTitle("Juego del Ahorcado");
		primaryStage.setScene(new Scene(rootController.getView()));
		primaryStage.getIcons().add(new Image(AhorcadoApp.class.getResourceAsStream("/hangman/9.png")));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
