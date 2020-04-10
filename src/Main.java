import java.io.BufferedReader; 
import java.io.File;  
import java.util.Scanner;
import java.io.*; 
import java.nio.file.Files; 
import java.nio.file.*; 
import java.util.Arrays;
import javax.swing.*;  

public class Main extends JFrame{ 
    static JProgressBar relocationProgress;    
    static JLabel progressLabel = new JLabel("File Relocation Progress");    
    static int i=0,num=0; 
    static int size = 4; //Number of folders  
                  
    public static void iterate(int increment){
        int sz = i;
        while (i < (sz+increment)) {   
            i += 1;
            relocationProgress.setValue(i);                  
            try{Thread.sleep(50);}catch(Exception e){}
        }
        if(i == 100)
            System.exit(0);
        return;         
    } 
    public static String[] removeStringFromArr(String[] arr, int index) { 
        if (arr == null || index < 0 || index >= arr.length)  
            return arr; 
        String[] anotherArray = new String[arr.length - 1]; 
        for (int i = 0, k = 0; i < arr.length; i++) { 
            if (i == index)  
                continue;           
            anotherArray[k++] = arr[i]; 
        } 
        return anotherArray; 
    } 
    private static String[] getDownloadsContents () {
        String dirpath = new String(System.getProperty("user.home")+"/Downloads/");
        String dirFiles[];
        String defString[] = {new String("None"), new String("None")};
        File f = new File(dirpath);
        if(f.exists()) {   
            dirFiles = f.list();         
            return dirFiles;
        } 
        return defString;         
    }
    
    private static void printStr (String[] str) {
        int sz = str.length;
        for(int i = 0; i < sz; i++) 
            System.out.println(str[i]);
    }
    
    private static int searchSubString (String[] str, String compareTo) {
        int sz = str.length;
        for(int i = 0; i < sz; i++) {
            String currentStr = str[i];
            if(currentStr.contains(compareTo) && currentStr.equals(compareTo) != true)
               return i;  
        } 
        return 999;    
    }
    
    private static void relocateFiles (String[] content, String extension, String folder) throws IOException {
        int index = 0;
        while(index != 999) {
           index = searchSubString(content, extension);
           if (index != 999) {
              Files.move(Paths.get(System.getProperty("user.home")+"/Downloads/"+content[index]),
                         Paths.get(System.getProperty("user.home")+"/Downloads/"+folder+"/"+content[index]));  
           } 
           content = removeStringFromArr(content, index);                       
        }
        iterate(100/size);             
    }
    
    private static void createDirectory (String name) {
        String path = new String(System.getProperty("user.home")+"/Downloads/"+name);
        File pathFile = new File(path);
        pathFile.mkdir();
    }
    public static void main(String[] args) { 
        JFrame window = new JFrame ("JavaDFM");
        JPanel panel = new JPanel();
        String content[] = getDownloadsContents();
        //Initialize progress bar
        relocationProgress = new JProgressBar(0,100);           
        relocationProgress.setValue(0);    
        relocationProgress.setStringPainted(true); 
        //Add UI to the JPanel
        panel.add(progressLabel);       
        panel.add(relocationProgress);
        //Add the UI Panel to the Window
        window.add(panel);
        window.setVisible(true);
        window.setSize(250, 100);
        //Manage the file relocation
        createDirectory("Texts");
        createDirectory("PDFs");
        createDirectory("Word");
        createDirectory("Compressed");
        //Get text files and relocate them  
        try {relocateFiles (content, ".txt", "Texts");  }
        catch (Exception IOException) {return;}
        //Get compressed files and relocate them
        try {relocateFiles (content, ".pdf", "PDFs");  }
        catch (Exception IOException) {return;}
        //Get PDFs and relocate them
        try {relocateFiles (content, ".zip", "Compressed");  }
        catch (Exception IOException) {return;}
        //Get Word Docs and relocate them
        try {relocateFiles (content, ".docx", "Word");  }
        catch (Exception IOException) {return;}
             
    } 
} 
