package fanucdir;

import javafx.application.Application;

public class DialogSceneController {

    Main app;

    public void setApp(Main app){
        this.app = app;
    }


    public void cancelDeleteAction(){
        this.app.cancelDeleteAction();
    }

    public void confirmDeleteAction(){
        this.app.deleteFile();
    }
}
