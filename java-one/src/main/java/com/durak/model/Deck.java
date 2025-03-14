package com.durak.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;
    private final Card.Suit trump;

    public Deck() {
        cards = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
        trump = cards.get(0).getSuit();
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }

    public Card.Suit getTrump() {
        return trump;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int getSize() {
        return cards.size();
    }
}