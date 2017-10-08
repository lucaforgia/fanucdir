package fanucdir;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.print("in start");
//        FXMLLoader loader = FXMLLoader.load(getClass().getResource("mainScene.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("mainScene.fxml"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScene.fxml"));

        Parent root = (Parent)loader.load();
        MainSceneController controller = (MainSceneController)loader.getController();
        controller.setMainStage(primaryStage);
        controller.setApp(this);

//        MainSceneController mainController = (MainSceneController)loader.getController();
        primaryStage.setTitle("FanucDir - cerca programma");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.getScene().getStylesheets().add("style.css");
        primaryStage.show();
//        mainController.ratto();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
