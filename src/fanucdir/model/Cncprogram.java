package fanucdir.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class CncProgram {
    public StringProperty fileName;
    private StringProperty programTitle;
    private File programFile;

    public CncProgram(File programFile, String fileName, String programTitle){
        this.fileName = new SimpleStringProperty(fileName);
        this.programTitle = new SimpleStringProperty(programTitle);
        this.programFile = programFile;
    }

    public String getFileName(){
        return this.fileName.get();
    }

    public String getProgramTitle(){
        return this.programTitle.get();
    }

    public File getProgramFile(){
        return this.programFile;
    }

}
