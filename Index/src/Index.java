import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Index extends Thread {
    private String rootPath;
    private List<File> fileList;
    private HashMap<String, HashSet<Integer>> map;

    public Index(String path, List<File> list, int startIndex, int endIndex) {
        rootPath = path;
        fileList = new ArrayList<>(list.subList(startIndex, endIndex));
        map = new HashMap<>();
    }

    public static String parse(File file) {
        String line = "";
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String buff;
            while((buff = br.readLine()) != null) {
                line = line.concat(buff);
            }
            String[] chars = {"/", "\\", "<", ">", ".", ",", ":", "?", "!", "(", ")", "*", "[", "]", ";", "\"" , ""};
            for(String i: chars) {
                line = line.replace(i, "");
            }
        }
        catch(IOException e) {e.getMessage();}
        return line;
    }

    public static int setFileID(File dir, int pathLen) {
        String Path = dir.getAbsolutePath().substring(pathLen);
        String[] folers = Path.split("\\\\");
        folers[folers.length-1] = folers[folers.length-1].split("_")[0];

        HashMap<String, Integer> dict = new HashMap<>();
        dict.put("test", 1);
        dict.put("train", 2);
        dict.put("neg", 1);
        dict.put("pos", 2);
        dict.put("unsup", 3);

        return Integer.parseInt(String.valueOf(dict.get(folers[0])) + String.valueOf(dict.get(folers[1])) + String.valueOf(folers[2]));
    }

    public HashMap<String, HashSet<Integer>> getMap() {
        return map;
    }

    @Override
    public void run() {
        for(File file: fileList) {
            String[] lexemes = Index.parse(file).split(" ");
            int FileID = Index.setFileID(file, rootPath.length());
            for(String word: lexemes) {
                if(!map.containsKey(word)) {
                    map.put(word, new HashSet<>());
                }
                map.get(word).add(FileID);
            }
        }
    }
}