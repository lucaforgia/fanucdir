package fanucdir;

import fanucdir.model.Cncprogram;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private ObservableList<Cncprogram> cncPrograms = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainScene.fxml"));
        primaryStage.setTitle("FanucDir - cerca programma");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();


        cncPrograms.add(new Cncprogram(1,"came"));

    }


    public static void main(String[] args) {
        launch(args);
    }
}
