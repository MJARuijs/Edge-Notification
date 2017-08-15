package mjaruijs.edge_notification.data;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CardList {

    private static List<AppCard> cards;
    private static File appFile;

    private CardList() {
        cards = new ArrayList<>();
    }

    public static void initialize(File file) {
        final String fileName = "app_array.txt";
        appFile = new File(file, fileName);
    }

    public void addCard(AppCard appcard) {
        cards.add(appcard);
    }

    public boolean contains(String appName) {
        for (AppCard card : cards) {
            if (card.getAppName().equals(appName)) {
                return true;
            }
        } return false;
    }

    public static void deleteCard(String name) {
        List<AppCard> newAppList = new ArrayList<>();
        String appName = name.replace("_Del_Btn", "");
        for (int i = 0; i < cards.size(); i++) {
            if (!cards.get(i).getAppName().equals(appName)) {
                newAppList.add(cards.get(i));
            }
        } cards = newAppList;
    }

    public AppCard getByName(String appName) {
        for (AppCard card : cards) {
            if (card.getAppName().equals(appName)) {

                return card;
            }
        } return null;
    }

    public static boolean multipleSelected() {
        int counter = 0;
        for (AppCard card : cards) {
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

    public List<AppCard> getSelectedCards() {
        List<AppCard> selectedCards = new ArrayList<>();
        for (AppCard card : cards) {
            if (card.isSelected()) {
                selectedCards.add(card);
            }
        } return selectedCards;
    }

    public static List<AppCard> getCards() {
        return cards;
    }

    public AppCard get(int i) {
         return cards.get(i);
    }

    public int size() {
        return cards.size();
    }

    public void clear() {
        cards.clear();
    }

    public static CardList readFromXML(IconMap iconMap) {
        CardList cardList = new CardList();
        String line;
        String appName = "";
        Drawable appIcon = null;
        String appColor;
        int colorInt = 0;
        try {
            Scanner sc = new Scanner(appFile);
            sc.next();
            do {
                line = sc.nextLine();
                if (line.contains("<name>")) {
                    appName = getString(line);
                    appIcon = iconMap.getValue(appName);
                } else if (line.contains("<color")) {
                    appColor = getString(line);

                    colorInt = Integer.parseInt(appColor);
                } else if (line.contains("</app-card>")) {
                    cardList.addCard(new
                            AppCard(appName,
                            appIcon,
                            colorInt));
                }
            } while(!line.contains("</resources>"));


            Log.i("XmlParser", cardList.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } return cardList;
    }

    private static String getString(String line) {
        int begin = line.indexOf(">");
        int end = line.indexOf("<", begin);
        String res = "";
        for (int i = begin + 1; i < end; i++) {
            res += line.charAt(i);
        }
        return res;
    }

    public void writeToFile() {
        try {
            FileWriter fileWriter = new FileWriter(appFile);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            String fileContent = "<resources>";
            if (getCards().size() > 0) {
                for (AppCard appCard : getCards()) {
                    fileContent += "\n\t<app-card>"
                            + "\n\t\t<name>" + appCard.getAppName() + "</name>"
                            + "\n\t\t<color>" + appCard.getNotificationColor() + "</color>"
                            + "\n\t</app-card>";
                }
            } fileContent += "\n</resources>";
            Log.i("XmlParser", fileContent);
            printWriter.println(fileContent);
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        String string = "";
        for (AppCard card : getCards()) {
            string += card.getAppName() + " " + card.getAppIcon() + " " + card.getNotificationColor() + " \n";
        } return string;
    }
}
