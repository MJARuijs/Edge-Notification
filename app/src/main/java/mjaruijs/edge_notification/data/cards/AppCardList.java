package mjaruijs.edge_notification.data.cards;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import mjaruijs.edge_notification.data.IconMap;

public class AppCardList extends CardList{

    private static List<AppCard> appCardList;
    private static File file;
    private static final String TAG = "AppCardList";

    public static void initialize(File appFile) {
        file = appFile;
        appCardList = new ArrayList<>();
    }

    public static List<AppCard> getCards() {
        return appCardList;
    }

    public void addCard(AppCard card) {
        super.addCard(card);
        appCardList.add(card);
    }


    public void deleteCard(String name) {
        super.deleteCard(name);
        String appName = name.replace("_Del_Btn", "");

        for (int i = 0; i < appCardList.size(); i++) {

            if (appCardList.get(i).getAppName().equals(appName)) {
                appCardList.remove(i);
                break;
            }

        }
    }

    public boolean multipleSelected() {
        int counter = 0;
        for (AppCard card : appCardList) {
            if (card.isSelected()) {
                counter++;
            }
            if (counter > 1) {
                Log.i("CardList", "multiple selected");
                return true;
            }
        }
        Log.i("CardList", "0/1 selected");

        return false;
    }

    public static void clear() {
        Log.i(TAG, "Clearing " + appCardList.size());
        appCardList.clear();
    }

    public static AppCardList readFromXML(IconMap iconMap) {
        AppCardList list = new AppCardList();
        String line;
        String appName = "";
        Drawable icon = null;
        int color = 0;
        try {
            Scanner sc = new Scanner(file);

            if (sc.hasNextLine()) {
                do {

                    line = sc.nextLine();
                    if (line.contains("<name>")) {
                        appName = getValue(line);
                        icon = iconMap.getValue(appName);
                    } else if (line.contains("<color>")) {
                        color = Integer.parseInt(getValue(line));
                    } else if (line.contains("</app-card>")) {
                        list.addCard(new AppCard(appName, icon, color));
                    }

                } while(!line.contains("</app-list>") && sc.hasNextLine());
            }
            Log.i("CARD PARSER: ", list.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String toString() {
        String fileContent = "\n<app-list>";

        if (appCardList.size() > 0) {
            for (AppCard card : appCardList) {
                fileContent += "\n\t\t<app-card>"
                        + "\n\t\t\t<name>" + card.getAppName() + "</name>"
                        + "\n\t\t\t<color>" + card.getColor() + "</color>"
                        + "\n\t\t</app-card>";
            }
        }

        fileContent += "\n</app-list>";
        return fileContent;
    }
}
