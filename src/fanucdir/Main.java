package fanucdir;

import fanucdir.controller.MainSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    private Stage primaryStage;
    private MainSceneController mainController;
    private Dialog confirmDialog;
//    private RemoveDialogController removeDialogController;
//    private Stage cancelDialogStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.print("in start");
//        FXMLLoader loader = FXMLLoader.load(getClass().getResource("mainScene.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("mainScene.fxml"));

        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/mainScene.fxml"));

        Parent root = (Parent)loader.load();
        this.mainController = (MainSceneController)loader.getController();
        this.mainController.setMainStage(primaryStage);
        this.mainController.setApp(this);


//        MainSceneController mainController = (MainSceneController)loader.getController();
        primaryStage.setTitle("FanucDir - cerca programma");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.getScene().getStylesheets().add("style.css");
        primaryStage.show();

//        FXMLLoader removeDialogLoader = new FXMLLoader(getClass().getResource("view/removeDialogScene.fxml"));
//        Parent removeDialogParent = (Parent)removeDialogLoader.load();


//        this.removeDialogController = (RemoveDialogController)removeDialogLoader.getController();
//        this.removeDialogController.setApp(this);
//        this.cancelDialogStage = new Stage();
//
//        this.cancelDialogStage.initModality(Modality.APPLICATION_MODAL);
//        this.cancelDialogStage.initOwner(primaryStage);
//        Scene dialogScene = new Scene(removeDialogParent, 300, 200);
//        this.cancelDialogStage.setTitle("Confermare");
//        dialogScene.getStylesheets().add("style.css");
//        this.cancelDialogStage.setScene(dialogScene);


//        Dialog nuovo = new Dialog(primaryStage, "Canenero"){
//            @Override
//            public void okCallBack() {
//                System.out.println("riscritto");
//            }
//        };
//        nuovo.showDialog();
//        nuovo.setConfirmQuestion("Sei ipersicuro");


//        showDialog();

//        mainController.ratto();
    }


    public void deleteFile(){
        this.mainController.deleteFileSelected();
//        this.cancelDialogStage.hide();
    }

    public void showRemoveDialog() throws Exception{
//        this.cancelDialogStage.show();

        Main app = this;
        String selectedFileName = this.mainController.getFileSelectedName();
        String question = "Sei sicuro di voler cancellare il file " + selectedFileName + "?";
        this.confirmDialog = new Dialog(this.primaryStage, question){

            @Override
            public void okCallBack() {
                app.deleteFile();
                this.hide();
            }
        };

        this.confirmDialog.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
