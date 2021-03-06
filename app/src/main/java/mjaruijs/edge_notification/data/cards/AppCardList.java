package mjaruijs.edge_notification.data.cards;

import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import mjaruijs.edge_notification.data.IconMap;

public class AppCardList extends CardList {

    private static List<AppCard> appCardList;
    private static File file;

    public static void initialize(File appFile) {
        file = appFile;
        appCardList = new ArrayList<>();
    }

    public static List<AppCard> getCards() {
        return appCardList;
    }

    public void deselectCards() {
        for (AppCard card : appCardList) {
            card.setSelected(false);
        }
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
                return true;
            }

        }
        return false;
    }

    public List<AppCard> getSelectedCards() {
        List<AppCard> selectedCards = new ArrayList<>();

        for (AppCard card : getCards()) {
            if (card.isSelected()) {
                selectedCards.add(card);
            }
        }
        return selectedCards;
    }

    public AppCard get(int i) {
        return appCardList.get(i);
    }

    public AppCard getByName(String appName) {
        super.getByName(appName);
        for (AppCard card : appCardList) {

            if (card.getAppName().equals(appName)) {
                return card;
            }

        }
        return null;
    }

    public void clear() {
        super.clear();
        appCardList.clear();
    }

    public static AppCardList readFromXML(IconMap iconMap) {
        AppCardList list = new AppCardList();
        String line;
        String appName = "";
        Drawable icon = null;
        int color = 0;
        Blacklist blacklist = new Blacklist();
        Sublist sublist = new Sublist();

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
                    } else if (line.contains("<blacklist>")) {
                        blacklist = Blacklist.readFromXML(sc);
                    } else if (line.contains("<sub-cards>")) {
                        sublist = Sublist.readFromXML(sc);
                    } else if (line.contains("</app-card>")) {
                        list.addCard(new AppCard(appName, icon, color, blacklist, sublist));
                    }

                } while(!line.contains("</app-list>") && sc.hasNextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder fileContent = new StringBuilder("\n<app-list>");

        if (appCardList.size() > 0) {

            for (AppCard card : appCardList) {

                fileContent
                        .append("\n\t\t<app-card>" + "\n\t\t\t<name>").append(card.getAppName()).append("</name>")
                        .append("\n\t\t\t<color>").append(card.getColor()).append("</color>")
                        .append("\n\t\t\t<sub-cards>").append(card.getSublist().toString()).append("\n\t\t\t</sub-cards>")
                        .append("\n\t\t\t<blacklist>").append(card.getBlacklist().toString()).append("\n\t\t\t</blacklist>")
                        .append("\n\t\t</app-card>");

            }
        }

        fileContent.append("\n</app-list>");
        return fileContent.toString();
    }
}
