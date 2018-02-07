package mjaruijs.edge_notification.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import mjaruijs.edge_notification.data.cards.AppCardList;

public class Data {

    private static AppCardList appCardList;
    private static File file;
    private static boolean initialized = false;

    public static void initialize(File appFile, IconMap iconMap) {
        final String fileName = "app_array.xml";
        file = new File(appFile, fileName);
        AppCardList.initialize(file);
        appCardList = AppCardList.readFromXML(iconMap);
        initialized = true;
    }

    public static AppCardList getCards(File file, IconMap iconMap) {
        if (!initialized) {
            initialize(file, iconMap);
        }
        return appCardList;
    }

    public static void writeToFile()  {

        try {
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            String fileContent = "<resources>";
            fileContent += appCardList.toString();

            fileContent += "\n</resources>";

            printWriter.write(fileContent);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
