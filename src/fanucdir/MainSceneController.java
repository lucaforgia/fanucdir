package fanucdir;

import fanucdir.model.CncProgram;
import javafx.application.Application;
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
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable{

    private ObservableList<CncProgram> cncProgramObservableList = FXCollections.observableArrayList();
    private final CncProgramsManager cncProgramsManager = new CncProgramsManager();
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
    private Text currentFolder;

    @FXML
    private  Text currentProgram;

    @FXML
    private BorderPane mainPanel;

    @FXML
    private Button deleteButton;

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

        System.out.println("cascacacsacsa");

        deleteButton.setVisible(false);
        programsFolderSelected = cncProgramsManager.setFolderPath();
        currentFolder.setText(programsFolderSelected);
        showPrograms();

//        programTable.setMinWidth(500);

    }

    private void clearProgramText(){
        programText.clear();
    }

    private void selectProgram(String fileName, String programName){
        programText.setScrollTop(0);
        File fileSelected = cncProgramsManager.getProgram(fileName);
        this.fileSelected = fileSelected;
        filePathSelected = fileSelected.getPath();


        try(BufferedReader br = new BufferedReader(new FileReader(fileSelected))) {
            programText.clear();
            for(String line; (line = br.readLine()) != null; ) {
                programText.appendText(line + "\r\n");
            }
            programText.positionCaret(0);
            currentProgram.setText(fileName + " / " + programName);
            deleteButton.setVisible(true);

        }catch (IOException ex){

        }
    }

    public void setApp(Main app){
        this.app = app;
    }

    private void showPrograms(){
        cncProgramObservableList.removeAll(cncProgramObservableList);
        cncProgramsManager.getCncProgramList().forEach(program -> cncProgramObservableList.add(program));
    }

    public void askIfDelete() throws Exception{
//        app.cacca();
        app.showRemoveDialog();
//        this.fileSelected.delete();
    }

    public void deleteFileSelected(){
        this.fileSelected.delete();
        this.reloadFolder();
        this.clearProgramText();
        this.currentProgram.setText("");
        deleteButton.setVisible(false);
    }

    public void searchProgram(){
        String text = textToSearch.getText().toLowerCase();

        cncProgramObservableList.removeAll(cncProgramObservableList);

        cncProgramsManager.filterPrograms(text).forEach(program -> cncProgramObservableList.add(program));

    }

    public void chooseDirectory(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("JavaFX Projects");
//        File defaultDirectory = new File("c:/dev/javafx");
//        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(mainStage);


        String newPath = selectedDirectory.getPath();
        currentFolder.setText(newPath);
        programsFolderSelected = newPath;
        cncProgramsManager.setFolderPath(newPath);
//        getAllPrograms();
        showPrograms();
    }

    public void reloadFolder(){
        cncProgramsManager.reloadCncProgramList();
        showPrograms();
    }

    public void getNextFreeProgramName(){
        String nextFreeProgramName = cncProgramsManager.getNextFreeProgramName();
        System.out.println(nextFreeProgramName);

    }

    public void getNextTwoFreeProgramNames(){
        String nextFreeProgramName = cncProgramsManager.getNextTwoFreeProgramNames();
        System.out.println(nextFreeProgramName);
    }

    public void setMainStage(Stage mainStage){
        this.mainStage = mainStage;
    }

}
