package mjaruijs.edge_notification.data.cards;

import java.util.ArrayList;
import java.util.List;

public abstract class CardList {

    private static List<Card> list;

    CardList() {
        list = new ArrayList<>();
    }

    void addCard(Card card) {
        list.add(card);
    }

    public boolean contains(String appName) {

        for (Card card : list) {

            if (card.getAppName().equals(appName)) {
                return true;
            }

        }
        return false;
    }

    public void deleteCard(String name) {

        String appName = name.replace("_Del_Btn", "");

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getAppName().equals(appName)) {
                list.remove(i);
                break;
            }

        }
    }

    public Card getByName(String appName) {
        for (Card card : list) {
            if (card.getAppName().equals(appName)) {
                return card;
            }
        } return null;
    }

    public boolean multipleSelected() {
        int counter = 0;
        for (Card card : list) {
            if (card.isSelected()) {
                counter++;
            }
            if (counter > 1) {
                return true;
            }
        }
        return false;
    }

    public Card get(int i) {
         return list.get(i);
    }

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    static String getValue(String line) {
        int begin = line.indexOf(">");
        int end = line.indexOf("<", begin);
        StringBuilder res = new StringBuilder();
        for (int i = begin + 1; i < end; i++) {
            res.append(line.charAt(i));
        }
        return res.toString();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Card card : list) {
            string.append(card.getAppName()).append(" ").append(card.getAppIcon()).append(" ").append(" \n");
        }
        return string.toString();
    }
}
