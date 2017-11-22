package fanucdir.controller;

import fanucdir.CncProgramsManager;
import fanucdir.Main;
import fanucdir.model.CncProgram;
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
import java.util.ResourceBundle;

public class MainSceneController implements Initializable{

    private ObservableList<CncProgram> cncProgramObservableList = FXCollections.observableArrayList();
    private final CncProgramsManager archiveCncProgramsManager = new CncProgramsManager();
    private CncProgramsManager showedCncProgramsManager = archiveCncProgramsManager;
    private Stage mainStage;

    private Main app;
    private String filePathSelected;
    private String programsFolderSelected;
    private File fileSelected;


    @FXML
    private TableView<CncProgram> programTable;

    @FXML
    private TableColumn<CncProgram, String> programNameColumn;

    @FXML
    private TableColumn<CncProgram, String> fileNameColumn;

    @FXML
    private TextField textToSearch;

    @FXML
    private TextArea programText;

    @FXML
    private TableView infoTable;

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
    private Button archiveButton;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        System.out.print("in inzialize");

        programNameColumn.setCellValueFactory(new PropertyValueFactory<CncProgram, String>("programName"));

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<CncProgram, String>("fileName"));

//        programTable.getItems().setAll(parseUserList());

        programTable.getColumns().setAll(programNameColumn, fileNameColumn);
        programText.setEditable(false);

        programTable.setItems(cncProgramObservableList);

//        cncProgramObservableList.add(new CncprogramOld("o9999", "Fast merda bla bla bla"));
//        cncProgramObservableList.add(new CncprogramOld("oskkdjd", "Fast puzza"));


        programTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectProgram(newSelection.getFileName(), newSelection.getProgramName());
            }
        });

        deleteButton.setVisible(false);
        copyButton.setVisible(false);
        archiveButton.setVisible(false);
        programsFolderSelected = showedCncProgramsManager.setFolderPath();
        currentFolder.setText(programsFolderSelected);
        showPrograms();


//        centerPanel.setMaxWidth(100);

//        programTable.setMinWidth(500);

    }

    private void clearProgramText(){
        programText.clear();
    }

    private void selectProgram(String fileName, String programName){
        programText.setScrollTop(0);
        File fileSelected = showedCncProgramsManager.getProgram(fileName);
        this.fileSelected = fileSelected;
        filePathSelected = fileSelected.getPath();


        try(BufferedReader br = new BufferedReader(new FileReader(fileSelected))) {
            programText.clear();
            for(String line; (line = br.readLine()) != null; ) {
                programText.appendText(line + "\n");
            }
            programText.positionCaret(0);
            currentProgram.setText(fileName + " / " + programName);
            showProgramTextButtons(true);

        }catch (IOException ex){

        }
    }

    private void showProgramTextButtons(boolean ohYes){
        boolean isArchiveSelected = this.programsFolderSelected.equals(CncProgramsManager.ARCHIVE_PATH);
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

    }

    public String getFileSelectedName(){
        return this.fileSelected.getName();
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

        showProgramTextButtons(false);
    }

    public void askIfDelete() throws Exception{
//        app.cacca();
        app.showRemoveDialog();
//        this.fileSelected.delete();
    }

    public void copyProgram() throws Exception{
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Directory dove copiare");

        File selectedDirectory = chooser.showDialog(mainStage);

        String newPath = selectedDirectory.getPath();
        CncProgramsManager copyFolderCncProgramsManager = new CncProgramsManager();
        copyFolderCncProgramsManager.setFolderPath(newPath);

        String newFileName = copyFolderCncProgramsManager.copyProgram(this.fileSelected);

        app.showCopiedDialog(newFileName);

    }

    public void archiveProgram() throws Exception{
       String newFileName = archiveCncProgramsManager.copyProgram(this.fileSelected);
       app.showArchiveDialog(newFileName);
    }

    public void deleteFileSelected(){
        this.showedCncProgramsManager.deleteFile(this.fileSelected);
        this.reloadFolder();
        showProgramTextButtons(false);
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
//        File defaultDirectory = new File("c:/dev/javafx");
//        chooser.setInitialDirectory(defaultDirectory);
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

    public void getNextFreeProgramName(){
        String nextFreeProgramName = showedCncProgramsManager.getNextFreeProgramName();
        System.out.println(nextFreeProgramName);

    }

    public void setMainStage(Stage mainStage){
        this.mainStage = mainStage;
    }

}
