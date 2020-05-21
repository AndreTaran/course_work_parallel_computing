import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        int NumberOfThreads;
        System.out.println("Enter num of Threads: ");
        Scanner in = new Scanner(System.in);
        NumberOfThreads = in.nextInt();

        System.out.println("Enter path: ");
        in = new Scanner(System.in);
        String path = in.nextLine();

        // String path = "C:\\Users\\tatan\\Downloads\\aclImdb (1)\\aclImdb\\";
        List<File> dirs = new ArrayList<>();
        Files.walk(Paths.get(path), FileVisitOption.FOLLOW_LINKS)
                .parallel()
                .map(Path::toFile)
                .forEach(f -> {
                    if (f.getAbsolutePath().contains("unsup")) {
                        if (f.getName().matches("18[0-9][0-9][0-9]_\\d*.txt"))
                            dirs.add(new File(f.getAbsolutePath()));

                    } else {
                        if (f.getName().matches("4[5-6][0-9][0-9]_\\d*.txt|47[0-4][0-9]_\\d*.txt")) {
//                        if(f.getName().matches("[1-8][0-9][0-9][0-9]_\\d*.txt")) {
                            dirs.add(new File(f.getAbsolutePath()));
                        }
                    }
                });

        ParallelIndexer Indexer = new ParallelIndexer(NumberOfThreads, path, dirs);

        System.out.println("Num of lexemes -> " + Indexer.getDict().size());
        System.out.println("Time -> " + Indexer.getResultTime() + " for num of threads -> " + NumberOfThreads);

        long start = System.currentTimeMillis();
        HashMap<String, HashSet<Integer>> map = new HashMap<>();
        for(File file: dirs) {
            String[] lexemes = Index.parse(file).split(" ");
            int FileID = Index.setFileID(file, path.length());
            for(String word: lexemes) {
                if(!map.containsKey(word)) {
                    map.put(word, new HashSet<>());
                }
                map.get(word).add(FileID);
            }
        }
        long end = System.currentTimeMillis();

        System.out.println();
        System.out.println("Sequence time -> " + (end - start));
        System.out.println("Num of lexemes -> " + map.size());

        System.out.println();
        System.out.println("seq and par is the same? " + map.equals(Indexer.getDict()));
    }
}
