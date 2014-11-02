package pwfcurry.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Main");
		Pane pane = FXMLLoader.load(getClass().getResource("layout.fxml"));
		primaryStage.setScene(new Scene(pane));
		primaryStage.show();
	}

}
