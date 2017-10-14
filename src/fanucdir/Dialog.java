package fanucdir;

import fanucdir.controller.DialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Dialog {

    private DialogController dialogController;
    private Stage dialogStage;

   Dialog(Stage primaryStage, String confirmQuestion) throws Exception{

        FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource("view/dialog.fxml"));
        Parent dialogParent = (Parent)dialogLoader.load();
        this.dialogController = (DialogController) dialogLoader.getController();
        this.dialogController.setDialogOwner(this);
        this.dialogStage = new Stage();

        this.dialogStage.initModality(Modality.APPLICATION_MODAL);
       this.dialogStage.initOwner(primaryStage);
       Scene dialogScene = new Scene(dialogParent, 300, 200);
       this.dialogStage.setTitle("Confermare");
       dialogScene.getStylesheets().add("style.css");
       this.dialogStage.setScene(dialogScene);
       this.setConfirmQuestion(confirmQuestion);
    }

    public void setConfirmQuestion(String text){
        this.dialogController.setConfirmQuestion(text);
    }

    public void okCallBack(){
        System.out.println("ok");
    }

    public void cancelCallBack(){
        System.out.println("cancel");
        this.hide();
    }

    public void hide(){
        this.dialogStage.hide();
    }

    public void show(){
        this.dialogStage.show();
    }

}
