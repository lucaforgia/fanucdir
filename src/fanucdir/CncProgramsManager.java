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

    public static final List<Integer> PROGRAM_NUMBERS_NOT_AVAILABLE = Collections.unmodifiableList(Arrays.asList(0,1,54));

    public ArrayList<CncProgram> getCncProgramList() {
        return cncProgramList;
    }

    public File getProgram(String fileName){

       File fileToReturn = cncProgramList.get(0).getProgramFile();
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

    public ArrayList<CncProgram> filterPrograms(String textToSearch){

        String normalizedText = textToSearch.toLowerCase();
        ArrayList<CncProgram> filteredList = new ArrayList<>();
        cncProgramList.forEach(program -> {if(program.getProgramName().toLowerCase().contains(normalizedText)){ filteredList.add(program); }});
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
        String programName;
        this.cncProgramList.removeAll(this.cncProgramList);
        for (final File fileEntry : folder.listFiles()) {
            fileName = fileEntry.getName();
            if(fileName.startsWith("O")){
                programName = this.createProgramName(fileEntry);
                this.addProgramToList(fileEntry, fileName, programName);
            }
        }
    }

    private String createProgramName(File fileEntry){
        int counter = 0;
        String programName = "UNDEFINED";
        try(BufferedReader br = new BufferedReader(new FileReader(fileEntry))) {
            String fileName = fileEntry.getName();

            counter = 0;
            for(String line; (line = br.readLine()) != null; ) {
                if(counter == 1){

                    programName = line.replace(fileName, "").replace("(", "").replace(")", "");

                    break;
                }
                counter++;
            }
        }catch (IOException ex){
            System.out.print("cazzo");
        }
        return programName;
    }

    private void addProgramToList(File fileEntry, String fileName, String programName){
        cncProgramList.add(new CncProgram(fileEntry, fileName, programName));
    }

    private void removeProgramFromList(File fileEntry){
        int size = cncProgramList.size();
        String fileName = fileEntry.getName();

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


    public String getNextFreeProgramName(){
        return convertFromFileNumberToFileName(getNextFreeProgramNumber());
    }

    public void deleteFile(File fileSelected){
        this.removeProgramFromList(fileSelected);
        fileSelected.delete();
    }

    public String copyProgram(File file) throws Exception{
        String newFileName = this.getNextFreeProgramName();

        String newFilePath = Paths.get( this.folderPath ,newFileName).toString();

        File newFile= new File(newFilePath);
        String fileName = file.getName();

        Charset charset = StandardCharsets.UTF_8;

        String content = new String(Files.readAllBytes(file.toPath()), charset);
        content = content.replaceAll(fileName, newFileName);
        Files.write(newFile.toPath(), content.getBytes(charset));

        this.addProgramToList(newFile, newFileName, this.createProgramName(newFile));

        return newFileName;
    }

}
