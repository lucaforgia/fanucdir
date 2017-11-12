package fanucdir.controller;

import fanucdir.Dialog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;



public class DialogController {

    private Dialog dialogOwner;

    @FXML
    private Text confirmQuestion;

    @FXML
    private TextField textEntry;

    @FXML
    private Button cancelCallBackButton;

    @FXML
    private Button okCallBackButton;

    public void okCallBack() throws Exception{
        this.dialogOwner.okCallBack();
    }

    public void cancelCallBack(){
        this.dialogOwner.cancelCallBack();
    }

    public void setCancelCallBackButtonText(String text){
        this.cancelCallBackButton.setText(text);
    }

    public void setOkCallBackButtonText(String text){
        this.okCallBackButton.setText(text);
    }

    public void setConfirmQuestion(String confirmQuestion){
        this.confirmQuestion.setText(confirmQuestion);
    }

    public void setDialogOwner(Dialog dialogOwner){
        this.dialogOwner = dialogOwner;
    }

    public void hideTextEntry(){
        this.textEntry.setVisible(false);
    }

    public void showTextEntry(){
        this.textEntry.setVisible(true);
    }

    public void hideCancelButton(){
        this.cancelCallBackButton.setVisible(false);
    }

    public String getTextEntryText(){
        return this.textEntry.getText();
    }
}
