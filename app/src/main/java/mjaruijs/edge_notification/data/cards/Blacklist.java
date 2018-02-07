package mjaruijs.edge_notification.data.cards;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static mjaruijs.edge_notification.data.cards.CardList.getValue;

public class Blacklist {

    private List<BlackCard> blacklist;

    Blacklist() {
        blacklist = new ArrayList<>();
    }

    void addCard(BlackCard card) {
        blacklist.add(card);
    }

    public boolean contains(String ticker) {
        for (BlackCard blackCard : blacklist) {
            if (ticker.contains(blackCard.getAppName())) {
                return true;
            }
        }
        return false;
    }

    void deleteCard(String cardName) {
        String appName = cardName.replace("_Del_Btn", "");

        for (BlackCard blackCard : blacklist) {
            if (blackCard.getAppName().equals(appName)) {
                blacklist.remove(blackCard);
                break;
            }
        }
    }

    public int size() {
        return blacklist.size();
    }

    public BlackCard get(int i) {
        return blacklist.get(i);
    }

    static Blacklist readFromXML(Scanner sc) {
        Blacklist list = new Blacklist();
        String line;
        String blacklistName = "";

        do {
            line = sc.nextLine();
            if (line.contains("<item>")) {
                blacklistName = getValue(line);
            } else if (line.contains("</black-card>")) {
                list.addCard(new BlackCard(blacklistName));
            }

        } while(!line.contains("</blacklist>"));

        return list;
    }

    @Override
    public String toString() {
        StringBuilder fileContent = new StringBuilder();

        if (blacklist.size() > 0) {

            for (BlackCard blackCard : blacklist) {
                fileContent.append("\n\t\t\t\t<black-card>" + "\n\t\t\t\t<item>")
                        .append(blackCard.getItem())
                        .append("</item>").append("\n\t\t\t</black-card>");
            }
        }

        return fileContent.toString();
    }

}
