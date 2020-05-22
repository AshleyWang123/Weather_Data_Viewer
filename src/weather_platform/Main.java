package weather_platform;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.lang.String;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("resources/Login.fxml"));
       primaryStage.setTitle("Meteorological Station Query Platform");
        Scene scene = new Scene(root, 900, 1000);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(Login.class.getResource("style.css").toExternalForm());
        primaryStage.show();
}
    public static void main(String[] args) { launch(args); }}
