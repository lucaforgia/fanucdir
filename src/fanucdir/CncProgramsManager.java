package fanucdir;

import fanucdir.model.CncProgram;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class CncProgramsManager {

    private ArrayList<CncProgram> cncProgramList = new ArrayList<>();
    private String folderPath;
    public final static String ARCHIVE_PATH = Paths.get(System.getProperty("user.dir"),"cnc_programs").toString();
    private final static int MAX_PROGRAMS_NUMBER = 7999;
    private final static int MIN_PROGRAMS_NUMBER = 101;
    private final static int MIN_FIXED_PROGRAMS_NUMBER = 8000;
    private final static int MAX_FIXED_PROGRAMS_NUMBER = 8999;
    public final static int MAX_LINES_SHOWED = 300;
    public final static int CHAR_FOR_LINE = 15;
    public final static int MAX_CHAR_SHOWED = CncProgramsManager.MAX_LINES_SHOWED * CncProgramsManager.CHAR_FOR_LINE;


    public static final List<Integer> PROGRAM_NUMBERS_NOT_AVAILABLE = Collections.unmodifiableList(Arrays.asList(0,1,54));

    public ArrayList<CncProgram> getCncProgramList() {
        return cncProgramList;
    }

    private boolean searchProgramTitle(String programTitle, ArrayList<String> words){
        boolean isInside = true;
        int wordsLen = words.size();
        String normalizedTitle = normalizeTitleForSearch(programTitle);
        for(int i = 0; i < wordsLen; i++){
            if(!normalizedTitle.contains(words.get(i).toString())){
                isInside = false;
                break;
            }
        }

        return isInside;
    }

    private String normalizeTitleForSearch(String programTitle){
        return programTitle.replaceAll("[.-/]","");
    }

    private String normalizeTextToSearch(String textToSearch){
        String normalizedText = textToSearch.toUpperCase().replaceAll("\\[", "").replaceAll("]","");
        return normalizedText;
    }

    private boolean isInnerSearch(String text){
        return text.contains("[") && text.contains("]");
    }

    private boolean isFileNameSearch(String text){
        return text.startsWith("O");
    }

    public ArrayList<CncProgram> filterPrograms(String textToSearch){

        String normalizedText = normalizeTextToSearch(textToSearch);
        ArrayList<CncProgram> filteredList = new ArrayList<>();

        if(isInnerSearch(textToSearch)){
            cncProgramList.forEach(program -> {if(program.getProgramContent().contains(normalizedText)){ filteredList.add(program); }});
        }else if(isFileNameSearch(normalizedText)){
            cncProgramList.forEach(program -> {if(program.getFileName().equals(normalizedText)){ filteredList.add(program); }});
        }
        else{
            ArrayList<String> words = new ArrayList<>(Arrays.asList(normalizedText.split(" ")));
            cncProgramList.forEach(program -> {if(searchProgramTitle(program.getProgramTitle(), words)){ filteredList.add(program); }});
        }



        return filteredList;
    }

    private ArrayList<CncProgram> findByTitle(String programTitle){

        ArrayList<CncProgram> filteredList = new ArrayList<>();
        cncProgramList.forEach(program -> {if(program.getProgramTitle().equals(programTitle)){ filteredList.add(program); }});
        return filteredList;
    }

    public String setFolderPath(String path){
        this.folderPath = path;
        this.setAllPrograms();
        return this.folderPath;
    }

    public String setFolderPath(){
        this.folderPath = CncProgramsManager.ARCHIVE_PATH;
        this.setAllPrograms();
        return this.folderPath;
    }

    public void reloadCncProgramList(){
        setFolderPath(folderPath);
    }

    private void setAllPrograms(){

        File folder = new File(this.folderPath);

        String fileName;
        this.cncProgramList.removeAll(this.cncProgramList);
        for (final File fileEntry : folder.listFiles()) {
            fileName = fileEntry.getName();
            if(fileName.startsWith("O") && this.convertFromFileNameToInt(fileName) != 0){
                this.addProgramToList(fileEntry);
            }
        }
    }

    private void addProgramToList(File fileEntry, String fileName, String content){
        cncProgramList.add(new CncProgram(fileEntry, fileName, content));
    }

    private void addProgramToList(File fileEntry){
        cncProgramList.add(new CncProgram(fileEntry));
    }

    private void removeProgramFromList(CncProgram programSelected){
        int size = cncProgramList.size();
        String fileName = programSelected.getFileName();

        for(int i = 0; i < size; i++){
            CncProgram temp = cncProgramList.get(i);

            if(temp.getFileName().equals(fileName)){
                System.out.println("rimosso dalla lista " + fileName);
                cncProgramList.remove(i);
                System.out.println(cncProgramList.size());
                break;
            }
        }
    }

    private int convertFromFileNameToInt(String fileName){
        String parsedString = fileName.substring(1,5);
        int fileNumber = 0;
        try{
            fileNumber = Integer.parseInt(parsedString);
        }catch (NumberFormatException e){

        }

        return fileNumber;
    }

    private String convertFromFileNumberToFileName(int fileNumber){
        if(fileNumber < 10){
            return "O000" + fileNumber;
        }
        if(fileNumber <100){
            return "O00" + fileNumber;
        }
        if(fileNumber < 1000){
            return "O0" + fileNumber;
        }
        else{
            return "O" + fileNumber;
        }
    }

    private int getNextFreeProgramNumber(){

        int size = cncProgramList.size();
        int freeNextProgramNumber = 0;

        ArrayList<Integer> numberList = new ArrayList<>();

        for(int i = 0; i < size; i++){
            int currentProgramNumber = 0;
            currentProgramNumber = convertFromFileNameToInt(cncProgramList.get(i).getFileName());
            numberList.add(currentProgramNumber);
        }

        for(int ii = CncProgramsManager.MIN_PROGRAMS_NUMBER; ii < CncProgramsManager.MAX_PROGRAMS_NUMBER; ii++){
            int nextProgramNumber = ii;
            if(!numberList.contains(nextProgramNumber) && !CncProgramsManager.PROGRAM_NUMBERS_NOT_AVAILABLE.contains(ii)){
                freeNextProgramNumber = nextProgramNumber;
                break;
            }
        }


        return freeNextProgramNumber;
    }

    private int getNextFreeFixedProgramNumber(){

        int size = cncProgramList.size();
        int freeNextProgramNumber = 0;

        ArrayList<Integer> numberList = new ArrayList<>();

        for(int i = 0; i < size; i++){
            int currentProgramNumber = 0;
            currentProgramNumber = convertFromFileNameToInt(cncProgramList.get(i).getFileName());
            numberList.add(currentProgramNumber);
        }

        for(int ii = CncProgramsManager.MIN_FIXED_PROGRAMS_NUMBER; ii < CncProgramsManager.MAX_FIXED_PROGRAMS_NUMBER; ii++){
            int nextProgramNumber = ii;
            if(!numberList.contains(nextProgramNumber) && !CncProgramsManager.PROGRAM_NUMBERS_NOT_AVAILABLE.contains(ii)){
                freeNextProgramNumber = nextProgramNumber;
                break;
            }
        }


        return freeNextProgramNumber;
    }

    private boolean fileAlreadyExist(String fileName){
        int size = cncProgramList.size();
        boolean alreadyExist = false;

        for(int i = 0; i < size; i++){
            if(cncProgramList.get(i).getFileName().equals(fileName)){
                alreadyExist = true;
                break;
            }
        }
        return alreadyExist;
    }

    public String getNextFreeFileName(){
        return convertFromFileNumberToFileName(getNextFreeProgramNumber());
    }

    public String getNextFreeFixedFileName(){
        return convertFromFileNumberToFileName(getNextFreeFixedProgramNumber());
    }

    public void deleteProgram(CncProgram programSelected){
        this.removeProgramFromList(programSelected);
        programSelected.getProgramFile().delete();
    }

    public void deleteProgram(String fileName){
        ArrayList<CncProgram> programs = filterPrograms(fileName);
        if(programs.size() == 1){
            CncProgram program = programs.get(0);
            this.removeProgramFromList(program);
            program.getProgramFile().delete();
        }
    }

    public void deletePrograms(ArrayList<CncProgram> programs){
        programs.forEach(program -> deleteProgram(program));
    }

    public String copyProgram(CncProgram program, boolean addDate) throws Exception{
        String newFileName;
        Charset charset = StandardCharsets.UTF_8;
        String toCopyFileName = program.getFileName();
        int toCopyFileNumber = convertFromFileNameToInt(toCopyFileName);

        if(toCopyFileNumber >= CncProgramsManager.MIN_FIXED_PROGRAMS_NUMBER && toCopyFileNumber <= CncProgramsManager.MAX_FIXED_PROGRAMS_NUMBER){
            if(fileAlreadyExist(toCopyFileName)){
                throw new FileAlreadyExistsException(toCopyFileName);

            }else{
                newFileName = toCopyFileName;
            }
        }else{
            newFileName = this.getNextFreeFileName();
        }

        Main.log(program.getFileName());


        String newFilePath = Paths.get( this.folderPath ,newFileName).toString();

        File newFile= new File(newFilePath);

        String content = program.getProgramContent().replaceAll(program.getFileName(), newFileName);
        if(addDate){
            LocalDateTime date = LocalDateTime.now();
            String formattedDate = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
            content = content.replaceFirst("[)]", ")\n(" + formattedDate + ")");
        }

        if(program.getReplaceIfExist()){
            ArrayList<CncProgram> oldPrograms = this.findByTitle(program.getProgramTitle());
            this.deletePrograms(oldPrograms);

            content = content.replaceAll(Pattern.quote(CncProgram.OVERWRITE_TAG), "");
        }

        Files.write(newFile.toPath(), content.getBytes(charset));

        this.addProgramToList(newFile, newFileName, content);

        return newFileName;
    }

}
