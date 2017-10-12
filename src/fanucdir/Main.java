package fanucdir;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {


    private Stage primaryStage;
    private MainSceneController mainController;
    private DialogSceneController removeDialogController;
    private Stage cancelDialogStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.print("in start");
//        FXMLLoader loader = FXMLLoader.load(getClass().getResource("mainScene.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("mainScene.fxml"));

        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScene.fxml"));

        Parent root = (Parent)loader.load();
        this.mainController = (MainSceneController)loader.getController();
        this.mainController.setMainStage(primaryStage);
        this.mainController.setApp(this);


//        MainSceneController mainController = (MainSceneController)loader.getController();
        primaryStage.setTitle("FanucDir - cerca programma");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.getScene().getStylesheets().add("style.css");
        primaryStage.show();

//        showDialog();

//        mainController.ratto();
    }


    public void deleteFile(){
        this.mainController.deleteFileSelected();
        this.cancelDialogStage.hide();
    }

    public void cancelDeleteAction(){
        this.cancelDialogStage.hide();
    }

    public void showRemoveDialog() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogScene.fxml"));
        Parent root = (Parent)loader.load();


        this.removeDialogController = (DialogSceneController)loader.getController();
//        controller.setMainStage(primaryStage);
        this.removeDialogController.setApp(this);


        this.cancelDialogStage = new Stage(); // new stage

        this.cancelDialogStage.initModality(Modality.APPLICATION_MODAL);
        this.cancelDialogStage.initOwner(primaryStage);
        Scene dialogScene = new Scene(root, 300, 200);
        this.cancelDialogStage.setTitle("Confermare");
        dialogScene.getStylesheets().add("style.css");
        this.cancelDialogStage.setScene(dialogScene);
        this.cancelDialogStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
