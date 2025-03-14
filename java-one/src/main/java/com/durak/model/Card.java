package com.durak.model;

public class Card {
    public enum Suit {
        HEARTS("♥"),
        DIAMONDS("♦"),
        CLUBS("♣"),
        SPADES("♠");

        private final String symbol;

        Suit(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public enum Rank {
        SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(11), QUEEN(12), KING(13), ACE(14);

        private final int value;

        Rank(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean canBeat(Card other, Suit trump) {
        if (suit == other.suit) {
            return rank.getValue() > other.rank.getValue();
        }
        return suit == trump && other.suit != trump;
    }

    @Override
    public String toString() {
        return rank + " " + suit.getSymbol();
    }
}