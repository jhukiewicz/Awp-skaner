package scanners;

import java.util.*;

public class Counter {

    public static Map <String, Integer> countTextures(String folderPath, String texturesExtension){
        Map<String, Integer> usages = new TreeMap<>();
        try {
            List<String> textures = FileScanner.scanAllTextures(folderPath + "/textures", texturesExtension);

            List<String> awpFiles = (List<String>) FileScanner.search(FileScanner.scanAllAwpFiles(folderPath, ".awp"),FileScanner.USAGE_PATTERN,SearchOptions.SEARCH_FOR_USAGE);

            for (String s : awpFiles) {
                usages.merge(s, 1, Integer::sum);
            }

            for (String s : textures) {
                if (!usages.containsKey(s)) {
                    usages.put(s, 0);
                }
            }
        }catch (NullPointerException e ){
            e.printStackTrace();
        }

        return usages;
    }

}
