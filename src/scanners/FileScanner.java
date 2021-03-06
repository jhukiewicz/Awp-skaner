package scanners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileScanner {

    final static String USAGE_PATTERN = "\"textures/([^\"]*?([^\"/]+))\"";

    //Scan for files in desired directory and its subdirectories

    public static List<String> scanAllTextures(String path, String extension) {
        List<String> result = null;

        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            result = walk
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .map(File::getPath)
                    .filter(s -> s.endsWith(extension))
                    .map(s -> s.substring(s.indexOf("textures") + 9))
                    .map(s -> s.replace("\\", "/"))
                    .sorted()
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //search for awp files

    public static List<File> scanAllAwpFiles(String path, String extension) {
        List<File> result = null;

        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            result = walk.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(extension))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;


    }


    public static Collection<String> search(List<File> list, String pattern, SearchOptions option) {
        if (list == null) {
            return null;
        }

        Collection<String> collection;

        if (option== SearchOptions.SEARCH_FOR_USAGE){
            collection= new ArrayList<>();
        }else {
            collection = new LinkedHashSet<>();
        }

        try {
            File file;
            Pattern p;
            p = Pattern.compile(pattern);
            for (File f : list) {
                file = f.getAbsoluteFile();

                Scanner scanner = new Scanner(new FileReader(file));


                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    Matcher matcher = p.matcher(line);
                    while (matcher.find()) {
                        if (option== SearchOptions.SEARCH_FOR_USAGE) {
                            collection.add(matcher.group(1));
                        }else {
                            collection.add(file.getPath());
                        }
                    }
                }
            }

        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return collection;
    }

}
