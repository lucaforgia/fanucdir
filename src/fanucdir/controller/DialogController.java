package fanucdir.controller;

import fanucdir.Dialog;
import fanucdir.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;


public class DialogController implements Initializable{

    private Dialog dialogOwner;

    @FXML
    private Text confirmQuestion;

    @FXML
    private TextField textEntry;

    @FXML
    private Button cancelCallBackButton;

    @FXML
    private Button okCallBackButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelCallBackButton.defaultButtonProperty().bind(cancelCallBackButton.focusedProperty());
        okCallBackButton.defaultButtonProperty().bind(okCallBackButton.focusedProperty());
        okCallBackButton.requestFocus();
    }

    public void dialogOnKeyPressed(KeyEvent event){
        if(event.getCode() == KeyCode.ESCAPE) {
            cancelCallBackButton.fire();
            event.consume();
        }
    }

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
