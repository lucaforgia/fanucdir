package fanucdir.controller;

import fanucdir.CncProgramsManager;
import fanucdir.Main;
import fanucdir.model.CncProgram;
import fanucdir.model.Tool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable{

    private ObservableList<CncProgram> cncProgramObservableList = FXCollections.observableArrayList();
    private ObservableList<Tool> programToolsObsList = FXCollections.observableArrayList();
    private final CncProgramsManager archiveCncProgramsManager = new CncProgramsManager();
    private CncProgramsManager showedCncProgramsManager = archiveCncProgramsManager;
    private Stage mainStage;

    private Main app;
    private String programsFolderSelected;
    private CncProgram programSelected;
    private String nextFreeFileNameInfoText;
    private String nextFreeFixedFileNameInfoText;


    @FXML
    private TableView<CncProgram> programTable;

    @FXML
    private TableColumn<CncProgram, String> programTitleColumn;

    @FXML
    private TableColumn<CncProgram, String> fileNameColumn;

    @FXML
    private TextField textToSearch;

    @FXML
    private TextArea programText;

    @FXML
    private VBox centerPanel;

    @FXML
    private Text currentFolder;

    @FXML
    private  Text currentProgram;

    @FXML
    private BorderPane mainPanel;

    @FXML
    private Button deleteButton;

    @FXML
    private Button copyButton;

    @FXML
    private Button archiveAllButton;

    @FXML
    private Button archiveButton;

    @FXML
    private TableView<Tool> toolsTable;

    @FXML
    private TableColumn<Tool, String> toolsCodeColumn;

    @FXML
    private TableColumn<Tool, String> toolsCommentColumn;

    @FXML
    private Text programsQtText;

    @FXML
    private ObservableList<String> infoObsArrayList;

    @FXML
    private Button deleteAllProgramsButton;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        System.out.print("in inzialize");

        programTitleColumn.setCellValueFactory(new PropertyValueFactory<CncProgram, String>("programTitle"));
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<CncProgram, String>("fileName"));
        programTable.getColumns().setAll(programTitleColumn, fileNameColumn);
        programText.setEditable(false);
        programTable.setItems(cncProgramObservableList);

        programTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
//                selectProgram(newSelection.getFileName(), newSelection.getProgramTitle());
                selectProgram(newSelection);
            }
        });

        toolsCodeColumn.setCellValueFactory(new PropertyValueFactory<Tool, String>("toolCode"));
        toolsCommentColumn.setCellValueFactory(new PropertyValueFactory<Tool, String>("toolComment"));
        toolsTable.getColumns().setAll(toolsCodeColumn, toolsCommentColumn);
        toolsTable.setItems(programToolsObsList);

        toolsCodeColumn.prefWidthProperty().bind(toolsTable.widthProperty().multiply(0.24));
        toolsCommentColumn.prefWidthProperty().bind(toolsTable.widthProperty().multiply(0.75));

        deleteButton.setVisible(false);
        copyButton.setVisible(false);
        archiveButton.setVisible(false);
        archiveAllButton.setVisible(false);
        deleteAllProgramsButton.setVisible(false);
        programsFolderSelected = showedCncProgramsManager.setFolderPath();
        currentFolder.setText(programsFolderSelected);
        showPrograms();

//        centerPanel.setMaxWidth(100);

//        programTable.setMinWidth(500);

    }

    private void clearProgramText(){
        programText.clear();
        this.clearTools();
    }



    private void selectProgram(CncProgram program){
        programText.setScrollTop(0);
        this.programSelected = program;
        programText.clear();

        this.showTools(program);

        if(program.getProgramContent().length() > CncProgramsManager.MAX_CHAR_SHOWED){
            programText.appendText(program.getProgramContent().substring(0, CncProgramsManager.MAX_CHAR_SHOWED));
            programText.appendText("... >>>>>>>> CONTINUA <<<<<<<<\n");
        }else{
            programText.appendText(program.getProgramContent());
        }

        programText.positionCaret(0);
        currentProgram.setText(program.getFileName() + " / " + program.getProgramTitle());
        showProgramTextButtons(true);

    }

    private void clearTools(){
        programToolsObsList.removeAll(programToolsObsList);
    }

    private void showTools(CncProgram program){
        this.clearTools();

        this.programToolsObsList.setAll(program.getTools());
    }

    public boolean getIsArchiveSelected(){
        return this.programsFolderSelected.equals(CncProgramsManager.ARCHIVE_PATH);
    }

    private void showProgramTextButtons(boolean ohYes){
        boolean isArchiveSelected = getIsArchiveSelected();
        if(ohYes){
            deleteButton.setVisible(true);
            copyButton.setVisible(isArchiveSelected);
            archiveButton.setVisible(!isArchiveSelected);
        }else{
            this.clearProgramText();
            this.currentProgram.setText("");
            deleteButton.setVisible(false);
            copyButton.setVisible(false);
            archiveButton.setVisible(false);
        }
        archiveAllButton.setVisible(!isArchiveSelected);

    }

    public String getFileSelectedName(){
        return this.programSelected.getFileName();
    }

    public void setApp(Main app){
        this.app = app;
    }

    private void showPrograms(){
        cncProgramObservableList.removeAll(cncProgramObservableList);

        if(!textToSearch.getText().toString().equals("")){
            String text = textToSearch.getText();
            showedCncProgramsManager.filterPrograms(text).forEach(program -> cncProgramObservableList.add(program));
        }
        else{
            showedCncProgramsManager.getCncProgramList().forEach(program -> cncProgramObservableList.add(program));
        }

        programsQtText.setText("" + cncProgramObservableList.size());

        showProgramTextButtons(false);
        deleteAllProgramsButton.setVisible(!getIsArchiveSelected());
        setNextFreeFileNameInfo();
    }

    public void askIfDelete() throws Exception{
//        app.cacca();
        app.showRemoveDialog();
    }

    public void copyProgram() throws Exception{
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Directory dove copiare");

        File selectedDirectory = chooser.showDialog(mainStage);

        String newPath = selectedDirectory.getPath();
        CncProgramsManager copyFolderCncProgramsManager = new CncProgramsManager();
        copyFolderCncProgramsManager.setFolderPath(newPath);

        String newFileName;

        try{
            newFileName = copyFolderCncProgramsManager.copyProgram(this.programSelected, false);
            app.showCopiedDialog(newFileName);
        }catch (FileAlreadyExistsException e){
            app.showAlreadyExistDialog(copyFolderCncProgramsManager);
        }catch (Exception e){
            throw new Exception(e);
        }

        System.out.println("copiato");



    }

    private void setNextFreeFileNameInfo(){
        String nextFileName = this.showedCncProgramsManager.getNextFreeFileName();
        infoObsArrayList.remove(nextFreeFileNameInfoText);
        nextFreeFileNameInfoText = "next free file name: " + nextFileName;
        infoObsArrayList.add(nextFreeFileNameInfoText);

        String nextFixedFileName = this.showedCncProgramsManager.getNextFreeFixedFileName();
        Main.log(nextFixedFileName);
        infoObsArrayList.remove(nextFreeFixedFileNameInfoText);
        nextFreeFixedFileNameInfoText = "next free fixed file name: " + nextFixedFileName;
        infoObsArrayList.add(nextFreeFixedFileNameInfoText);

    }

    public void archiveAllProgram() throws Exception{
        showedCncProgramsManager.reloadCncProgramList();

        ArrayList<CncProgram> programList = showedCncProgramsManager.getCncProgramList();
        int len = programList.size();
        String filesError = "";

        for(int i = 0; i < len; i++){
            try{
                archiveCncProgramsManager.copyProgram(programList.get(i), true);
            }catch (Exception e){
                filesError += "\n" + programList.get(i).getFileName();
            }
        }
        app.showCopiedAllDialog(filesError);
    }
    public void deleteFileFromManager(CncProgramsManager programManagerWithFile){
        Main.log("delete da file manager");
        if(programManagerWithFile.isArchiveManager()){
            programManagerWithFile.copyProgramToTrash(this.programSelected);
        }
        programManagerWithFile.deleteProgram(this.programSelected.getFileName());
    }
    public void copyToProgramsManager(CncProgramsManager programsManager) throws Exception{

//        programsManager.copyProgram(this.programSelected, false);
        String newFileName;
        if(programsManager == archiveCncProgramsManager){
            newFileName = programsManager.copyProgram(this.programSelected, true);
            app.showArchiveDialog(newFileName);
        }else{
            newFileName = programsManager.copyProgram(this.programSelected, false);
            app.showCopiedDialog(newFileName);
        }
    }

    public void archiveProgram() throws Exception{
        try{
            String newFileName = archiveCncProgramsManager.copyProgram(this.programSelected, true);
            app.showArchiveDialog(newFileName);
        }catch (FileAlreadyExistsException e){
            app.showAlreadyExistDialog(archiveCncProgramsManager);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    private void reloadAfterDeleteFile(){
        this.reloadFolder();
        showProgramTextButtons(false);
        setNextFreeFileNameInfo();
    }

    public void deleteProgramSelected(){
        if(this.getIsArchiveSelected()){
            this.showedCncProgramsManager.copyProgramToTrash(this.programSelected);
        }
        this.showedCncProgramsManager.deleteProgram(this.programSelected);
        reloadAfterDeleteFile();
    }

    public void askIfDeleteAllPrograms() throws Exception{
        Main.log("removeallllllll");
        app.showRemoveAllDialog();
    }

    public void deleteAllPrograms(){
        showedCncProgramsManager.deleteAllPrograms();
        reloadAfterDeleteFile();

    }

    public void searchProgramsForName(){
        showPrograms();
    }

    public void resetFilter(){
        textToSearch.setText("");
        this.showPrograms();
    }

    public void chooseDirectory(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("JavaFX Projects");
        File selectedDirectory = chooser.showDialog(mainStage);


        String newPath = selectedDirectory.getPath();

        if(newPath.equals(CncProgramsManager.ARCHIVE_PATH)){
            showedCncProgramsManager = archiveCncProgramsManager;
        }else{
            showedCncProgramsManager = new CncProgramsManager();
            showedCncProgramsManager.setFolderPath(newPath);
        }
        currentFolder.setText(newPath);
        programsFolderSelected = newPath;
//        getAllPrograms();
        showPrograms();
    }

    public void showArchive(){
        String newPath = CncProgramsManager.ARCHIVE_PATH;
        currentFolder.setText(newPath);
        programsFolderSelected = newPath;
        showedCncProgramsManager = archiveCncProgramsManager;
//        getAllPrograms();
        showPrograms();
    }

    public void reloadFolder(){
        showedCncProgramsManager.reloadCncProgramList();
        showPrograms();
    }

    public void setMainStage(Stage mainStage){
        this.mainStage = mainStage;
    }

}
