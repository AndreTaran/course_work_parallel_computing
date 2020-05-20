import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
         String path = "C:\\Users\\tatan\\Downloads\\aclImdb (1)\\aclImdb\\";
        List<File> dirs = new ArrayList<>();
        Files.walk(Paths.get(path), FileVisitOption.FOLLOW_LINKS)
                .parallel()
                .map(Path::toFile)
                .forEach(f -> {
                    if(f.getAbsolutePath().contains("unsup")) {
                        if(f.getName().matches("18[0-9][0-9][0-9]_\\d*.txt"))
                            dirs.add(new File(f.getAbsolutePath()));

                    }
                    else{
                        if(f.getName().matches("4[5-6][0-9][0-9]_\\d*.txt|47[0-4][0-9]_\\d*.txt")) {
//                        if(f.getName().matches("[1-8][0-9][0-9][0-9]_\\d*.txt")) {
                            dirs.add(new File(f.getAbsolutePath()));
                        }
                    }
                });
    }
}
