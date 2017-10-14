package fanucdir.controller;

import fanucdir.Dialog;
import javafx.fxml.FXML;
import javafx.scene.text.Text;



public class DialogController {

    private Dialog dialogOwner;

    @FXML
    private Text confirmQuestion;


    public void okCallBack(){
        this.dialogOwner.okCallBack();
    }

    public void cancelCallBack(){
        this.dialogOwner.cancelCallBack();
    }


    public void setConfirmQuestion(String confirmQuestion){
        this.confirmQuestion.setText(confirmQuestion);
    }

    public void setDialogOwner(Dialog dialogOwner){
        this.dialogOwner = dialogOwner;
    }
}
