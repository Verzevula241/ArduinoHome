package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("ArduinoHome");
        primaryStage.setScene(new Scene(root, 300, 400));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        Controller.stage = primaryStage;


    }


    public static void main(String[] args) {
        launch(args);
    }
}
