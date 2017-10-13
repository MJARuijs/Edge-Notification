package mjaruijs.edge_notification.data.cards;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Blacklist extends CardList {

    private static List<BlackCard> blacklist;
    private static final String TAG = "Blacklist";

    public Blacklist() {
        blacklist = new ArrayList<>();
    }

    public void addCard(BlackCard card) {
        super.addCard(card);
        blacklist.add(card);
    }

    public void clear() {
        super.clear();
        blacklist.clear();
    }

    public static Blacklist readFromXML(Scanner sc) {
        Blacklist list = new Blacklist();
        String line;
        String blacklistName = "";
        sc.next();

        do {
            line = sc.nextLine();
            if (line.contains("<item>")) {
                blacklistName = getValue(line);
            } else if (line.contains("</black-card>")) {
                blacklist.add(new BlackCard(blacklistName));
            }

        } while(!line.contains("</blacklist>"));

        Log.i("BLACKLIST PARSER: ", list.toString());
        return list;
    }

    @Override
    public String toString() {
        String fileContent = "";

        if (blacklist.size() > 0) {

            for (BlackCard blackCard : blacklist) {
                fileContent += "\n\t\t\t\t<black-card>"
                    + "\n\t\t\t\t<item>" + blackCard.getItem() + "</item>"
                    + "\n\t\t\t</black-card>";
            }
        }

        return fileContent;
    }
}
