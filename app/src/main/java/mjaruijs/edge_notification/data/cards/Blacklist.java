package mjaruijs.edge_notification.data.cards;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Blacklist extends CardList {

    private static List<BlackCard> blacklist;
    private static File file;
    private static final String TAG = "Blacklist";

    public static void initialize(File appFile) {
        file = appFile;
        blacklist = new ArrayList<>();
    }

    public void addCard(BlackCard card) {
        super.addCard(card);
        blacklist.add(card);
    }

    public void clear() {

        Log.i(TAG, "Clearing " + blacklist.size());
        blacklist.clear();
    }

//    public static Blacklist readFromXML(IconMap iconMap) {
//        Blacklist list = new Blacklist();
//        String line;
//        String appName = "";
//        Drawable icon = null;
//        String blacklistName = "";
//        try {
//            Scanner sc = new Scanner(file);
//            sc.next();
//            do {
//                line = sc.nextLine();
//                if (line.contains("<blacklist>")) {
//                    do {
//                        line = sc.nextLine();
//                        if (line.contains("<name>")) {
//                            appName = getValue(line);
//                            icon = iconMap.getValue(appName);
//                        } else if (line.contains("<item>")) {
//                            blacklistName = getValue(line);
//                        } else if (line.contains("</black-card>")) {
//                            blacklist.add(new BlackCard(appName, icon, blacklistName));
//                        }
//
//                    } while(!line.contains("</blacklist>"));
//                }
//
//            } while(!line.contains("</resources>"));
//
//            Log.i("BLACKLIST PARSER: ", list.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    @Override
    public String toString() {
        String fileContent = "\n<blacklist>";

        if (blacklist.size() > 0) {
            for (BlackCard blackCard : blacklist) {
                fileContent += "\n\t\t<black-card>"
                    + "\n\t\t\t<name>" + blackCard.getAppName() + "</name>"
                    + "\n\t\t\t<item>" + blackCard.getItem() + "</item>"
                    + "\n\t\t</black-card>";
            }
        }

        fileContent += "\n</blacklist>";
//        Log.i("BLACKLIST TO STRING", fileContent);
        return fileContent;
    }
}
