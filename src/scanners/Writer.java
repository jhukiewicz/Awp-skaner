package scanners;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

public class Writer {




    public static boolean writeTextures(Map<String,Integer> map ){

        System.out.println("in write textures");
         String filename = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss.'txt'").format( Calendar.getInstance().getTime());

         File file = new File("output\\" + filename);

        if (map==null){
            System.out.println("Fail!");
            return false;
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))){
            map.forEach((key,value)-> {
                try {
                    bf.write(key + " : " + value);
                    bf.newLine();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            });
            return true;
        }
        catch (IOException e ){
            e.printStackTrace();
        }
        return false;

    }

    public static boolean writeAwpFiles(Set<String > set, String textureName){

        if (set.isEmpty()){
            return false;
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter("output\\" + textureName +".txt"))){

            bf.write(textureName + ": "+"\n");

            for (String s : set){
                bf.write(s);
                bf.newLine();
            }

        }catch (IOException e ){
            e.printStackTrace();
        }
        return true;
    }

}
