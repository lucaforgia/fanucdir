package fanucdir;

import fanucdir.model.Cncprogram;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable{

    private ObservableList<Cncprogram> cncPrograms = FXCollections.observableArrayList();
    private ArrayList<Cncprogram> allCncPrograms;
    private Stage mainStage;

    private Application app;
    private String filePathSelected;
    private String programsFolderSelected;


    @FXML
    private TableView<Cncprogram> programTable;

    @FXML
    private TableColumn<Cncprogram, String> programNameColumn;

    @FXML
    private TableColumn<Cncprogram, String> fileNameColumn;

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

    @Override
    public void initialize(URL location, ResourceBundle resources){
        System.out.print("in inzialize");

        programNameColumn.setCellValueFactory(new PropertyValueFactory<Cncprogram, String>("programName"));

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<Cncprogram, String>("fileName"));

//        programTable.getItems().setAll(parseUserList());

        programTable.getColumns().setAll(programNameColumn, fileNameColumn);
        programText.setEditable(false);

        programTable.setItems(cncPrograms);

//        cncPrograms.add(new Cncprogram("o9999", "Fast merda bla bla bla"));
//        cncPrograms.add(new Cncprogram("oskkdjd", "Fast puzza"));


        programTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectProgram(newSelection.getFileName(), newSelection.getProgramName());
            }
        });

        programsFolderSelected = System.getProperty("user.dir") + "\\cnc_programs";
        currentFolder.setText(programsFolderSelected);
        getAllPrograms();
        showAllPrograms();

//        programTable.setMinWidth(500);

    }

    private void selectProgram(String fileName, String programName){
        programText.setScrollTop(0);
        filePathSelected = programsFolderSelected + "\\" + fileName;
        File fileSelected = new File(filePathSelected);
        try(BufferedReader br = new BufferedReader(new FileReader(fileSelected))) {
            programText.clear();
            for(String line; (line = br.readLine()) != null; ) {
                programText.appendText(line + "\r\n");
            }
            programText.positionCaret(0);
            currentProgram.setText(fileName + " / " + programName);


        }catch (IOException ex){

        }
    }

    public void setApp(Application app){
        this.app = app;
    }

    public void openFile(){
        app.getHostServices().showDocument(filePathSelected);
    }

    private void showAllPrograms(){
        cncPrograms.removeAll(cncPrograms);
        allCncPrograms.forEach(program -> cncPrograms.add(program));
    }

    private void getAllPrograms(){

        String pathName = programsFolderSelected;


        allCncPrograms = new ArrayList<Cncprogram>();
        System.out.print("\n");
        System.out.print(pathName);
        File folder = new File(pathName);
        int counter = 0;
        String fileName;
        String programName;
        for (final File fileEntry : folder.listFiles()) {
            counter = 0;
            try(BufferedReader br = new BufferedReader(new FileReader(fileEntry))) {
                fileName = fileEntry.getName();
                if(fileName.startsWith("O")){
                    for(String line; (line = br.readLine()) != null; ) {
                        if(counter == 1){

                            programName = line.replace(fileName, "").replace("(", "").replace(")", "");
                            allCncPrograms.add(new Cncprogram(fileName, programName));
                            break;
                        }
                        counter++;
                    }
                }
            }catch (IOException ex){
                System.out.print("cazzo");
            }

        }


    }

    public void searchProgram(){

        String text = textToSearch.getText().toLowerCase();
        cncPrograms.removeAll(cncPrograms);
        allCncPrograms.forEach(program -> {if(program.getProgramName().toLowerCase().contains(text)){ cncPrograms.add(program); }});

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
        getAllPrograms();
        showAllPrograms();
    }

    public void setMainStage(Stage mainStage){
        this.mainStage = mainStage;
    }

}
