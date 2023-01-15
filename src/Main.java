import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {

        String directoryPath = "/Users/semyonsavenkov/IdeaProjects/JavaCoreHW3_Files/Games/savegames/";

        //creating gameProgress and saving it to a file
        GameProgress gameProgress =
                new GameProgress(94, 10, 2, 254.32);
        saveGame(directoryPath,gameProgress);

        GameProgress gameProgress2 =
                new GameProgress(85, 12, 3, 300.50);
        saveGame(directoryPath,gameProgress2);

        GameProgress gameProgress3 =
                new GameProgress(70, 15, 5, 500.50);
        saveGame(directoryPath,gameProgress3);

        //adding saves to a zip archive
        ArrayList listOfFiles = new ArrayList<String>();

        File savesDir = new File(directoryPath);
        if (savesDir.isDirectory()) {
            for (File item : savesDir.listFiles()) {
                if (! item.getName().contains(".zip")) {
                    listOfFiles.add(item.getAbsolutePath());

                }
            }
            zipFiles(directoryPath, listOfFiles);

            //deleting non-zip files after archivation
            for (File item : savesDir.listFiles()) {
                if (! item.getName().contains(".zip")) {
                    item.delete();
                }
            }
        }
    }

    public static void saveGame(String directoryPath, GameProgress currentGameProgress) {

        String formatedDate = getStringDate();

        try (FileOutputStream fos = new FileOutputStream(directoryPath + "save" + formatedDate +
                currentGameProgress.hashCode() +  ".dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(currentGameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String directoryPath, ArrayList<String> listOfFiles) {

        String formatedDate = getStringDate();

        try (ZipOutputStream zout = new ZipOutputStream(
                new FileOutputStream(directoryPath + formatedDate + "archivedsaves.zip"))
        ) {
            for (String currentFile : listOfFiles) {

                try (FileInputStream fis = new FileInputStream(currentFile)) {
                    ZipEntry entry = new ZipEntry(currentFile);
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (IOException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static String getStringDate () {
        SimpleDateFormat myDF = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        return myDF.format(new Date());
    }

}