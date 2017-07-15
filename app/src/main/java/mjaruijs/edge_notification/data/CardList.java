package mjaruijs.edge_notification.data;

import java.util.ArrayList;
import java.util.List;

public class CardList {

    private List<AppCard> cards;

    public CardList() {
        cards = new ArrayList<>();
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
}
