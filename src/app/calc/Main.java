package app.calc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Font.loadFont(getClass().getResource("/resources/fonts/VarelaRound-Regular.ttf").toExternalForm(), 10);
        //This doesn't work for some reason???
        Parent root = FXMLLoader.load(getClass().getResource("view/calcView.fxml"));
        primaryStage.setTitle("OpCalc v1.2 - Damián Adams - March 13, 2016");
        primaryStage.setScene(new Scene(root, 700, 600));
        primaryStage.show();
        primaryStage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
