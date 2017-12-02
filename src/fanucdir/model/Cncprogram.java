package fanucdir.model;

import fanucdir.CncProgramsManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CncProgram {
    public StringProperty fileName;
    private StringProperty programTitle;
    private File programFile;
    private String programContent;
    private boolean replaceIfExist;
    public final static String OVERWRITE_TAG = "(--OVERWRITE--)";

    public CncProgram(File programFile, String fileName, String programContent){
        this.fileName = new SimpleStringProperty(fileName);
        this.programTitle = new SimpleStringProperty(this.createProgramTitle(programContent));
        this.programFile = programFile;
        this.programContent = programContent;
        this.replaceIfExist = programContent.contains(CncProgram.OVERWRITE_TAG);
    }

    public CncProgram(File programFile){
        try{
            Charset charset = StandardCharsets.UTF_8;
            String content = new String(Files.readAllBytes(programFile.toPath()), charset);
            this.fileName = new SimpleStringProperty(programFile.getName().toString());
            this.programTitle = new SimpleStringProperty(this.createProgramTitle(content));
            this.programFile = programFile;
            this.programContent = content;
            this.replaceIfExist = this.programContent.contains(CncProgram.OVERWRITE_TAG);

        }catch (IOException ex){
            System.out.print("cazzo");
        }
    }

    public String getFileName(){
        return this.fileName.get();
    }

    public String getProgramContent(){
        return this.programContent;
    }
    public String getProgramTitle(){
        return this.programTitle.get();
    }

    public File getProgramFile(){
        return this.programFile;
    }

    public boolean getReplaceIfExist(){
        return this.replaceIfExist;
    }

    private String createProgramTitle(String programContent){
        String rawTitle = "";
        try{
            rawTitle = programContent.substring(programContent.indexOf("(")+1, programContent.indexOf(")"));
        }catch (Exception e){
            rawTitle = "UNDEFINED";
        }
        return rawTitle;
    }

    public ArrayList<Tool> getTools(){
        ArrayList<Tool> tools = new ArrayList<>();
        ArrayList<String> stringsFinded = new ArrayList<>();
        Matcher m = Pattern.compile("^T[0-9]+.*$+", Pattern.MULTILINE).matcher(this.programContent);
        while (m.find()) {

            String finded = m.group();

            if(!stringsFinded.contains(finded)){
                stringsFinded.add(finded);
                tools.add(new Tool(finded));

            }
        }

        return tools;
    }
}
