import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        //creating gameProgress and saving it to a file
        GameProgress gameProgress =
                new GameProgress(94, 10, 2, 254.32);
        saveGame(gameProgress);

        GameProgress gameProgress2 =
                new GameProgress(85, 12, 3, 300.50);
        saveGame(gameProgress2);

        GameProgress gameProgress3 =
                new GameProgress(70, 15, 5, 500.50);
        saveGame(gameProgress3);

        //adding saves to a zip archive
        ArrayList listOfFiles = new ArrayList<String>();

        File savesDir = new File("Games/savegames");
        if (savesDir.isDirectory()) {
            for (File item : savesDir.listFiles()) {
                if (! item.getName().contains(".zip")) {
                    listOfFiles.add(item.getAbsolutePath());

                }
            }
            zipFiles(listOfFiles);

            //deleting non-zip files after archivation
            for (File item : savesDir.listFiles()) {
                if (! item.getName().contains(".zip")) {
                    item.delete();
                }
            }
        }
    }

    public static void saveGame(GameProgress currentGameProgress) {

        String formatedDate = getStringDate();

        try (FileOutputStream fos = new FileOutputStream("Games/savegames/save" + formatedDate +
                currentGameProgress.hashCode() +  ".dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(currentGameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(ArrayList<String> listOfFiles) {

        String formatedDate = getStringDate();

        try (ZipOutputStream zout = new ZipOutputStream(
                new FileOutputStream("Games/savegames/save" + formatedDate + "archivedsaves.zip"))
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
