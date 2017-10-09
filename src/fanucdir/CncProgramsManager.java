package fanucdir;

import fanucdir.model.CncProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CncProgramsManager {

    private ArrayList<CncProgram> cncProgramList = new ArrayList<>();
    private String folderPath;

    public void CncProgramList(){

    }

    public ArrayList<CncProgram> getCncProgramList() {
        return cncProgramList;
    }

    public File getProgram(String fileName){

       File fileToReturn = cncProgramList.get(0).getProgramFile();
//       cncProgramList.forEach(program -> {if(program.getFileName() == fileName){ return program.getProgramFile(); }});

        int size = cncProgramList.size();

        for(int i = 0; i < size; i++){
            CncProgram temp = cncProgramList.get(i);
            if(temp.getFileName() == fileName){
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
        this.folderPath = Paths.get(System.getProperty("user.dir"),"cnc_programs").toString();
        this.setAllPrograms();
        return this.folderPath;
    }

    private void setAllPrograms(){

        File folder = new File(this.folderPath);
        int counter = 0;
        String fileName;
        String programName;
        this.cncProgramList.removeAll(this.cncProgramList);
        for (final File fileEntry : folder.listFiles()) {
            counter = 0;
            try(BufferedReader br = new BufferedReader(new FileReader(fileEntry))) {
                fileName = fileEntry.getName();
                if(fileName.startsWith("O")){
                    for(String line; (line = br.readLine()) != null; ) {
                        if(counter == 1){

                            programName = line.replace(fileName, "").replace("(", "").replace(")", "");
                            cncProgramList.add(new CncProgram(fileEntry, fileName, programName));
                            break;
                        }
                        counter++;
                    }
                }
            }catch (IOException ex){
                System.out.print("cazzo");
            }

        }


    }

}
