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
    private boolean dialogAlreadyUsed = false;

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
        primaryStage.setTitle("FanucDir - gestore programmi");
        primaryStage.setScene(new Scene(root, 1000, 800));

        primaryStage.getScene().getStylesheets().add("style.css");
        primaryStage.setMaximized(true);
        primaryStage.show();

    }


    public void deleteFile(){
        this.mainController.deleteProgramSelected();
//        this.cancelDialogStage.hide();
    }

    public void showRemoveDialog() throws Exception{
//        this.cancelDialogStage.show();

        if(dialogAlreadyUsed){
            this.confirmDialog.hide();
        }else{
            dialogAlreadyUsed = true;
        }
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

        this.confirmDialog.setCancelCallBackButtonText("Annulla");
        this.confirmDialog.setOkCallBackButtonText("Sì, cancella!");

        this.confirmDialog.show();
    }

    public void showArchiveDialog(String newFileName) throws Exception{

        if(dialogAlreadyUsed){
            this.confirmDialog.hide();
        }else{
            dialogAlreadyUsed = true;
        }
        Main app = this;
        String question = "File archiviato con nome " + newFileName + "";
        this.confirmDialog = new Dialog(this.primaryStage, question){

            @Override
            public void okCallBack() {
                this.hide();
            }
        };

        this.confirmDialog.hideCancelButton();
        this.confirmDialog.setOkCallBackButtonText("Ricevuto");

        this.confirmDialog.show();
    }

    public void showCopiedDialog(String newFileName) throws Exception{

        if(dialogAlreadyUsed){
            this.confirmDialog.hide();
        }else{
            dialogAlreadyUsed = true;
        }
        Main app = this;
        String question = "File copiato con nome " + newFileName + "";
        this.confirmDialog = new Dialog(this.primaryStage, question){

            @Override
            public void okCallBack() {
                this.hide();
            }
        };

        this.confirmDialog.hideCancelButton();
        this.confirmDialog.setOkCallBackButtonText("Ricevuto");

        this.confirmDialog.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
