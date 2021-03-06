package scanners;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Replacement {

    public static boolean replace(List<File> list, String oldName, String newName, String mainFolderPath) {

        boolean alreadyExecuted = false;

        File file;
        Pattern p = Pattern.compile(":\"" + "textures/" + oldName + "\"");

        try {
            for (File f : list) {
                file = f.getAbsoluteFile();
                Scanner scanner = new Scanner(new FileReader(file));
                String replaced;
                while (scanner.hasNextLine()) {

                    String line = scanner.nextLine();
                    Matcher matcher = p.matcher(line);

                    if (matcher.find()) {
                        replaced = line.replaceAll(p.toString(), ":\"" + "textures/" + newName + "\"");

                        if (replacedFile(replaced, file)) {

                            if (!alreadyExecuted) {
                                moveAndRename(mainFolderPath, oldName, newName);
                                alreadyExecuted = true;
                            }

                        } else {
                            return false;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean replacedFile(String s, File file) {

        if (s == null) {
            return false;
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {
            bf.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static boolean moveAndRename(String path, String oldName, String newName) {
        if (newName.contains("/")) {
            createFolder(path + "/textures/" + newName);
        }

        try {
            Files.move(Paths.get(path + "/textures/" + oldName), Paths.get(path + "/textures/" + newName), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean createFolder(String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
