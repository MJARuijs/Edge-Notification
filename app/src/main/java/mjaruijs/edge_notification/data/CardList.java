package mjaruijs.edge_notification.data;

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

    private List<AppCard> cards;
    private final String fileName = "app_array.txt";
    private File appFile;

    public CardList(File file) {
        cards = new ArrayList<>();
        //this.context = context;
        appFile = new File(file, fileName);
    }

    public void addCard(AppCard appcard) {
        cards.add(appcard);
    }

    public List<AppCard> getCards() {
        return cards;
    }

    public AppCard get(int i) {
         return cards.get(i);
    }

    public int size() {
        return cards.size();
    }

    public CardList readFromXML() {
        CardList cardList = new CardList(appFile);
        String line;
        String appName = "";
        String appIcon;
        String appColor;
        int iconInt = 0;
        int colorInt = 0;
        try {
            Scanner sc = new Scanner(appFile);
            sc.next();
            do {
                line = sc.nextLine();
                if (line.contains("<name>")) {
                    appName = getString(line);
                    Log.i("APPNAME", appName);
                } else if (line.contains("<icon>"))  {
                    appIcon = getString(line);
                    iconInt = Integer.getInteger(appIcon);
                    Log.i("APPICON", appIcon);
                } else if (line.contains("<color")) {
                    appColor = getString(line);
                    colorInt = Integer.getInteger(appColor);
                    Log.i("APPCOLOR", appColor);
                } else if (line.contains("</app-card>")) {

                    cardList.addCard(new
                            AppCard(appName,
                            iconInt,
                            colorInt));
                }
            } while(!line.contains("</resources>"));


            Log.i("XMLPARSER", cardList.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } return cardList;
    }

    private String getString(String line) {
        int begin = line.indexOf(">");
        int end = line.indexOf("<", begin);
        String res = "";
        for (int i = begin + 1; i < end; i++) {
            res += line.charAt(i);
        }
        Log.i("GETSTRING", res);
        return res;
    }


    public void writeToFile() {


        try {
            //FileOutputStream fos = new FileOutputStream(fileName, false);
            FileWriter fileWriter = new FileWriter(appFile);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            String fileContent = "<resources>";
            if (getCards().size() > 0) {
                for (AppCard appCard : getCards()) {
                    fileContent += "\n\t<app-card>"
                            + "\n\t\t<name>" + appCard.getAppName() + "</name>"
                            + "\n\t\t<icon>" + appCard.getAppIcon() + "</icon>"
                            + "\n\t\t<color>" + appCard.getNotificationColor() + "</color>"
                            + "\n\t</app-card>";
                }
            } fileContent += "\n</resources>";
            Log.i("XMLPARSER", fileContent);
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
