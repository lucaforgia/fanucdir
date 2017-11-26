package fanucdir.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tool {
    private String toolCode;
    private String toolComment;

//    public StringProperty toolComment;

    public Tool(String textLine){
        int commentStart = textLine.indexOf("(");

        if(commentStart > 0){
            toolCode = textLine.substring(0, commentStart);
            toolComment = textLine.substring(commentStart + 1, textLine.indexOf(")"));
//            toolComment = new SimpleStringProperty(textLine);
        }else{
            toolCode = textLine;
            toolComment = "";
        }

    }

    public String getToolCode(){
        return this.toolCode;
    }

    public String getToolComment(){
        return this.toolComment;
    }
}
