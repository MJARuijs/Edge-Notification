package mjaruijs.edge_notification.data;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import mjaruijs.edge_notification.data.cards.AppCardList;
import mjaruijs.edge_notification.data.cards.Blacklist;

public class Data {

    private static AppCardList appCardList;
    private static Blacklist blacklist;
    private static File file;

    public static void initialize(File appFile, IconMap iconMap) {
        final String fileName = "app_array.txt";
        file = new File(appFile, fileName);
        AppCardList.initialize(file);
        Blacklist.initialize(file);
        appCardList = AppCardList.readFromXML(iconMap);
        blacklist = Blacklist.readFromXML(iconMap);
    }

    public static AppCardList getCards() {
        return appCardList;
    }

    public static Blacklist getBlacklist() {
        return blacklist;
    }

    public static void writeToFile()  {

        try {
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            String fileContent = "<resources>";
            fileContent += appCardList.toString() + blacklist.toString();
            fileContent += "\n</resources>";

            Log.i("Data", fileContent);

            printWriter.write(fileContent);
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Data", "EXCEPTION " + e);
        }


    }

}
