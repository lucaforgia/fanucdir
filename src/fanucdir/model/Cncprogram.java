package fanucdir.model;

import fanucdir.CncProgramsManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class CncProgram {
    public StringProperty fileName;
    private StringProperty programTitle;
    private File programFile;
    private String programContent;
    private boolean isArchived;

    public CncProgram(File programFile, String fileName, String programContent){
        this.fileName = new SimpleStringProperty(fileName);
        this.programTitle = new SimpleStringProperty(this.createProgramTitle(programContent));
        this.programFile = programFile;
        this.programContent = programContent;
        this.isArchived = programContent.contains(CncProgramsManager.ARCHIVE_TAG);
    }

    public CncProgram(File programFile){
        try{
            Charset charset = StandardCharsets.UTF_8;
            String content = new String(Files.readAllBytes(programFile.toPath()), charset);
            this.fileName = new SimpleStringProperty(programFile.getName().toString());
            this.programTitle = new SimpleStringProperty(this.createProgramTitle(content));
            this.programFile = programFile;
            this.programContent = content;
            this.isArchived = programContent.contains(CncProgramsManager.ARCHIVE_TAG);

        }catch (IOException ex){
            System.out.print("cazzo");
        }
    }

    public String getFileName(){
        return this.fileName.get();
    }

    public boolean getIsArchived(){return this.isArchived;}

    public String getProgramContent(){
        return this.programContent;
    }
    public String getProgramTitle(){
        return this.programTitle.get();
    }

    public File getProgramFile(){
        return this.programFile;
    }


    private String createProgramTitle(String programContent){
        String rawTitle = programContent.substring(programContent.indexOf("("), programContent.indexOf(")"));
        return rawTitle.replace("(", "");
    }
}
