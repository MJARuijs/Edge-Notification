package mjaruijs.edge_notification.data.cards;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static mjaruijs.edge_notification.data.cards.CardList.getValue;

public class Sublist {

    private List<SubCard> sublist;

    Sublist() {
        sublist = new ArrayList<>();
    }

    void addCard(SubCard subCard) {
        sublist.add(subCard);
    }

    void deleteCard(String cardName) {
        String appName = cardName.replace("_Del_Btn", "");

        for (SubCard subCard : sublist) {
            if (subCard.getItem().equals(appName)) {
                sublist.remove(subCard);
                break;
            }
        }
    }

    public int size() {
        return sublist.size();
    }

    public SubCard get(String name) {
        for (SubCard subCard : sublist) {
            if (subCard.getItem().equals(name)) {
                return subCard;
            }
        }
        return null;
    }

    public SubCard get(int i) {
        return sublist.get(i);
    }

    static Sublist readFromXML(Scanner sc) {
        Sublist list = new Sublist();
        String line;
        int color = 0;
        String subListName = "";

        do {
            line = sc.nextLine();
            if (line.contains("item")) {
                subListName = getValue(line);
            } else if (line.contains("color")) {
                color = Integer.parseInt(getValue(line));
            } else if (line.contains("</sub-card>")) {
                list.addCard(new SubCard(subListName, color));
            }
        } while(!line.contains("</sub-cards>"));

        Log.i("SUBLIST PARSER: ", list.toString());

        return list;
    }

    @Override
    public String toString() {
        StringBuilder fileContent = new StringBuilder();

        if (sublist.size() > 0) {

            for (SubCard subCard : sublist) {
                fileContent.append("\n\t\t\t\t<sub-card>")
                        .append("\n\t\t\t\t\t<item>").append(subCard.getItem()).append("</item>")
                        .append("\n\t\t\t\t\t<color>").append(subCard.getColor()).append("</color>")
                        .append("\n\t\t\t/t</sub-card>");
            }
        }

        return fileContent.toString();
    }

}
