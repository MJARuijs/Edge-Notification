package mjaruijs.edge_notification.data.cards;

import android.util.Log;

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
        } return false;
    }

    public static void deleteCard(String name) {
        List<Card> newAppList = new ArrayList<>();
        String appName = name.replace("_Del_Btn", "");
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getAppName().equals(appName)) {
                newAppList.add(list.get(i));
            }
        }
        list = newAppList;
    }

    public Card getByName(String appName) {
        for (Card card : list) {
            if (card.getAppName().equals(appName)) {
                return card;
            }
        } return null;
    }

    public static boolean multipleSelected() {
        int counter = 0;
        for (Card card : list) {
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

    public List<Card> getSelectedCards() {
        List<Card> selectedCards = new ArrayList<>();
        for (Card card : list) {
            if (card.isSelected()) {
                selectedCards.add(card);
            }
        }

        return selectedCards;
    }

    public Card setSelected(String appName) {
        Card selected = getByName(appName);
        for (Card card : list) {
            if (!card.equals(selected)) {
                card.setSelected(false);
            }
        }
        selected.setSelected(true);
        return selected;
    }

//    public static List<Card> getCards() {
//        return list;
//    }

    public Card get(int i) {
         return list.get(i);
    }

    public int size() {
        return list.size();
    }

    public static void clear() {
        Log.i("CardList", "CLEAR " + list.size());

        list.clear();
    }

//    public static CardList readCardsFromXML(IconMap iconMap) {
//        CardList cardList = new CardList();
//        String line;
//        String appName = "";
//        Drawable appIcon = null;
//        String mainColor;
//        int mainColorInt = 0;
//        try {
//            Scanner sc = new Scanner(appFile);
//            sc.next();
//            do {
//                line = sc.nextLine();
//                if (line.contains("<name>")) {
//                    appName = getValue(line);
//                    appIcon = iconMap.getValue(appName);
//                } else if (line.contains("<color")) {
//                    mainColor = getValue(line);
//                    mainColorInt = Integer.parseInt(mainColor);
//                } else if (line.contains("</app-card>")) {
//                    cardList.addCard(new
//                            AppCard(appName,
//                            appIcon,
//                            mainColorInt));
//                }
//            } while(!line.contains("</cards>"));
//
//            Log.i("XmlParser", cardList.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            cardList.writeToFile();
//        }
//        return cardList;
//    }
//
//    static CardList readBlacklist() {
//        CardList list = new CardList();
//        String line;
//        String appName = "";
//        try {
//            Scanner sc = new Scanner(appFile);
//            sc.next();
//            do {
//                line = sc.nextLine();
//                Log.i("LIST", line);
//
//                if (line.contains("<blacklist>")) {
//                    do {
//                        line = sc.nextLine();
//                        Log.i("LIST", line);
//                        if (line.contains("<name>")) {
//                            Log.i("CARDS " , getValue(line));
////                            list.addCard(new AppCard(getValue(line)));
//                            blacklist.add(getValue(line));
//                        }
//                    } while(!line.contains("</blacklist>"));
//                }
//
//            } while(!line.contains("</resources>"));
//
////            Log.i("XmlParser", list.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            list.writeToFile();
//        }
//        return list;
//    }

    static String getValue(String line) {
        int begin = line.indexOf(">");
        int end = line.indexOf("<", begin);
        String res = "";
        for (int i = begin + 1; i < end; i++) {
            res += line.charAt(i);
        }
        return res;
    }

    @Override
    public String toString() {
        String string = "";
        for (Card card : list) {
            string += card.getAppName() + " " + card.getAppIcon() + " " + " \n";
        }
        return string;
    }
}
