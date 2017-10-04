package fanucdir.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cncprogram {

    public StringProperty fileName;
    private StringProperty programName;

    public Cncprogram(String fileName, String programName){
        this.fileName = new SimpleStringProperty(fileName);
        this.programName = new SimpleStringProperty(programName);
    }

    public String getFileName(){
        return this.fileName.get();
    }

    public String getProgramName(){
        return this.programName.get();
    }
}
