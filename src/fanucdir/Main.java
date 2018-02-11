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
    }

    public void copyToProgramsManager(CncProgramsManager programManager) throws Exception{
        this.mainController.copyToProgramsManager(programManager);
//        this.cancelDialogStage.hide();
    }

    public void deleteFileFromManager(CncProgramsManager programManagerWithFile){
        this.mainController.deleteFileFromManager(programManagerWithFile);
    }

    private void deleteAllPrograms(){
        this.mainController.deleteAllPrograms();
    }

    public void showRemoveAllDialog() throws Exception{
//        this.cancelDialogStage.show();

        if(dialogAlreadyUsed){
            this.confirmDialog.hide();
        }else{
            dialogAlreadyUsed = true;
        }
        Main app = this;
        String question = "Sei sicuro di voler cancellare tutti i programmi?";
        this.confirmDialog = new Dialog(this.primaryStage, question){

            @Override
            public void okCallBack() {
                app.deleteAllPrograms();
                this.hide();
            }
        };

        this.confirmDialog.setCancelCallBackButtonText("Annulla");
        this.confirmDialog.setOkCallBackButtonText("Sì, cancella!");

        this.confirmDialog.show();
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

    public void showCopiedAllDialog(String filesError) throws Exception{

        if(dialogAlreadyUsed){
            this.confirmDialog.hide();
        }else{
            dialogAlreadyUsed = true;
        }
        Main app = this;
        String question = "Files copiati con successo ";

        if(!filesError.equals("")){
            question = "Alcuni file non sono stati copiati correttamente: " + filesError;
        }
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


    public void showAlreadyExistDialog(CncProgramsManager programManagerWithFile) throws Exception{
//        this.cancelDialogStage.show();

        if(dialogAlreadyUsed){
            this.confirmDialog.hide();
        }else{
            dialogAlreadyUsed = true;
        }
        Main app = this;
        String selectedFileName = this.mainController.getFileSelectedName();
        String question = "Il file " + selectedFileName + " esiste già.";
        this.confirmDialog = new Dialog(this.primaryStage, question){

            @Override
            public void okCallBack() throws Exception{
                app.deleteFileFromManager(programManagerWithFile);
                app.copyToProgramsManager(programManagerWithFile);
                this.hide();
            }
        };

        this.confirmDialog.setCancelCallBackButtonText("Annulla");
        this.confirmDialog.setOkCallBackButtonText("Sovrascrivi");

        this.confirmDialog.show();
    }

    public static void log(String text){
        System.out.println(text);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
