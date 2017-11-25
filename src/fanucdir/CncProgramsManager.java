package fanucdir;

import fanucdir.model.CncProgram;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CncProgramsManager {

    private ArrayList<CncProgram> cncProgramList = new ArrayList<>();
    private String folderPath;
    public final static String ARCHIVE_PATH = Paths.get(System.getProperty("user.dir"),"cnc_programs").toString();
    private final static int MAX_PROGRAMS_NUMBER = 8999;
    private final static int MIN_PROGRAMS_NUMBER = 101;
    public final static int MAX_LINES_SHOWED = 300;
    public final static int CHAR_FOR_LINE = 15;
    public final static int MAX_CHAR_SHOWED = CncProgramsManager.MAX_LINES_SHOWED * CncProgramsManager.CHAR_FOR_LINE;
    public final static String ARCHIVE_TAG = "(__ARCHIVIATO__)";

    public static final List<Integer> PROGRAM_NUMBERS_NOT_AVAILABLE = Collections.unmodifiableList(Arrays.asList(0,1,54));

    public ArrayList<CncProgram> getCncProgramList() {
        return cncProgramList;
    }

    public File getProgram(String fileName){

       File fileToReturn  = cncProgramList.get(0).getProgramFile();
//       cncProgramList.forEach(program -> {if(program.getFileName() == fileName){ return program.getProgramFile(); }});

        int size = cncProgramList.size();

        for(int i = 0; i < size; i++){
            CncProgram temp = cncProgramList.get(i);
            if(temp.getFileName().equals(fileName)){
                fileToReturn = temp.getProgramFile();
                break;
            }
        }
        return fileToReturn;
    }

    private boolean searchProgramTitle(String programTitle, ArrayList<String> words){
        boolean isInside = true;
        int wordsLen = words.size();
        for(int i = 0; i < wordsLen; i++){
            if(!programTitle.contains(words.get(i).toString())){
                isInside = false;
                break;
            }
        }

        return isInside;
    }

    public ArrayList<CncProgram> filterPrograms(String textToSearch){
        String normalizedText = textToSearch.toLowerCase();

        ArrayList<String> words = new ArrayList<>(Arrays.asList(normalizedText.split(" ")));

        ArrayList<CncProgram> filteredList = new ArrayList<>();
        cncProgramList.forEach(program -> {if(searchProgramTitle(program.getProgramTitle().toLowerCase(), words)){ filteredList.add(program); }});
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
        System.out.println(parsedString);
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


    public String getNextFreeFileName(){
        return convertFromFileNumberToFileName(getNextFreeProgramNumber());
    }

    public void deleteProgram(CncProgram programSelected){
        this.removeProgramFromList(programSelected);
        programSelected.getProgramFile().delete();
    }

    public String copyProgramWithTag(CncProgram program)throws Exception{
        return this.copyProgram(program, true,false);
    }

    public String copyProgramIfNoTag(CncProgram program)throws Exception{
        return this.copyProgram(program, false,true);
    }

    private String copyProgram(CncProgram program, boolean addTag, boolean dontCopyIfTag) throws Exception{

        if(dontCopyIfTag && program.getIsArchived()){
            System.out.println("canemorto");
            throw new Exception();
        }

        Charset charset = StandardCharsets.UTF_8;

        String newFileName = this.getNextFreeFileName();

        String newFilePath = Paths.get( this.folderPath ,newFileName).toString();

        File newFile= new File(newFilePath);

        String content = program.getProgramContent().replaceAll(program.getFileName(), newFileName);
        if(addTag){
            content = content.replaceFirst("[)]", ")\n" + CncProgramsManager.ARCHIVE_TAG);
        }

        Files.write(newFile.toPath(), content.getBytes(charset));

        this.addProgramToList(newFile, newFileName, content);

        return newFileName;
    }

}
