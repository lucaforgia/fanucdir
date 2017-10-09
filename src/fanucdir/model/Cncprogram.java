package fanucdir.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class CncProgram {
    public StringProperty fileName;
    private StringProperty programName;
    private File programFile;

    public CncProgram(File programFile, String fileName, String programName){
        this.fileName = new SimpleStringProperty(fileName);
        this.programName = new SimpleStringProperty(programName);
        this.programFile = programFile;
    }

    public String getFileName(){
        return this.fileName.get();
    }

    public String getProgramName(){
        return this.programName.get();
    }

    public File getProgramFile(){
        return this.programFile;
    }

}
