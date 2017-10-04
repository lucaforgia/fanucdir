package fanucdir;

import fanucdir.model.Cncprogram;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable{

    private ObservableList<Cncprogram> cncPrograms = FXCollections.observableArrayList();
    private ArrayList<Cncprogram> allCncPrograms = new ArrayList<Cncprogram>();

    @FXML
    private TableView<Cncprogram> programTable;

    @FXML
    private TableColumn<Cncprogram, String> programNameColumn;

    @FXML
    private TableColumn<Cncprogram, String> fileNameColumn;

    @FXML
    private TextField textToSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        System.out.print("in inzialize");

        programNameColumn.setCellValueFactory(new PropertyValueFactory<Cncprogram, String>("programName"));

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<Cncprogram, String>("fileName"));

//        programTable.getItems().setAll(parseUserList());

        programTable.getColumns().setAll(programNameColumn, fileNameColumn);


        programTable.setItems(cncPrograms);

//        cncPrograms.add(new Cncprogram("o9999", "Fast merda bla bla bla"));
//        cncPrograms.add(new Cncprogram("oskkdjd", "Fast puzza"));


        getAllPrograms(allCncPrograms);
        showAllPrograms();
    }

    private void showAllPrograms(){
        cncPrograms.removeAll(cncPrograms);
        allCncPrograms.forEach(program -> cncPrograms.add(program));
    }

    private void getAllPrograms(ArrayList<Cncprogram> cncProgramList){

        String pathName = System.getProperty("user.dir") + "\\cnc_programs";
        File folder = new File(pathName);
        int counter = 0;
        String fileName;
        String programName;
        for (final File fileEntry : folder.listFiles()) {
//            System.out.print("\n file " + fileEntry.getName() + " \n");
            counter = 0;
            try(BufferedReader br = new BufferedReader(new FileReader(fileEntry))) {
                for(String line; (line = br.readLine()) != null; ) {
                    if(counter == 1){
//                        System.out.print(line);
                        fileName = fileEntry.getName();
                        programName = line.replace(fileName, "").replace("(", "").replace(")", "");
                        cncProgramList.add(new Cncprogram(fileName, programName));
                        break;
                    }
                    counter++;
                }
                // line is not visible here.
            }catch (IOException ex){

            }

        }


    }

    public void searchProgram(){

        String text = textToSearch.getText().toLowerCase();


            cncPrograms.removeAll(cncPrograms);
            allCncPrograms.forEach(program -> {if(program.getProgramName().toLowerCase().contains(text)){ cncPrograms.add(program); }});

    }

}
