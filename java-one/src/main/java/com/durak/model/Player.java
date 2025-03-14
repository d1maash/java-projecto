package com.durak.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Card> hand;
    private final boolean isAI;
    private final String name;

    public Player(String name, boolean isAI) {
        this.name = name;
        this.isAI = isAI;
        this.hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        if (card != null) {
            hand.add(card);
        }
    }

    public List<Card> getHand() {
        return hand;
    }

    public boolean isAI() {
        return isAI;
    }

    public String getName() {
        return name;
    }

    public Card findLowestCard(Card.Suit trump) {
        Card lowest = null;
        for (Card card : hand) {
            if (lowest == null ||
                    (card.getSuit() != trump && (lowest.getSuit() == trump ||
                            card.getRank().getValue() < lowest.getRank().getValue()))) {
                lowest = card;
            }
        }
        return lowest;
    }

    public Card findCardToBeat(Card attackingCard, Card.Suit trump) {
        Card bestCard = null;
        for (Card card : hand) {
            if (card.canBeat(attackingCard, trump)) {
                if (bestCard == null ||
                        card.getRank().getValue() < bestCard.getRank().getValue()) {
                    bestCard = card;
                }
            }
        }
        return bestCard;
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public int getHandSize() {
        return hand.size();
    }
}