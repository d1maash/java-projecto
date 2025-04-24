package com.durak.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private final List<Card> hand;
    private final boolean isAI;
    private final String name;
    private int difficulty; // 1-5, где 5 - самый сложный
    private final Random random;

    public Player(String name, boolean isAI, int difficulty) {
        this.name = name;
        this.isAI = isAI;
        this.hand = new ArrayList<>();
        this.difficulty = isAI ? difficulty : 0;
        this.random = new Random();
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

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        if (difficulty >= 1 && difficulty <= 5) {
            this.difficulty = difficulty;
        }
    }

    public Card findLowestCard(Card.Suit trump) {
        if (!isAI || hand.isEmpty())
            return null;

        List<Card> possibleCards = new ArrayList<>(hand);

        // На более высоких уровнях сложности ИИ старается сохранить козыри
        if (difficulty >= 4 && random.nextInt(100) < 80) {
            List<Card> nonTrumpCards = new ArrayList<>(possibleCards);
            nonTrumpCards.removeIf(card -> card.getSuit() == trump);
            if (!nonTrumpCards.isEmpty()) {
                possibleCards = nonTrumpCards;
            }
        }

        // На низких уровнях сложности иногда выбираем случайную карту
        if (difficulty <= 2 && random.nextInt(100) < 40) {
            return possibleCards.get(random.nextInt(possibleCards.size()));
        }

        // Находим карту с наименьшим значением
        Card lowest = possibleCards.get(0);
        for (Card card : possibleCards) {
            if (card.getSuit() != trump && lowest.getSuit() == trump) {
                lowest = card;
            } else if ((card.getSuit() == trump) == (lowest.getSuit() == trump) &&
                    card.getRank().getValue() < lowest.getRank().getValue()) {
                lowest = card;
            }
        }
        return lowest;
    }

    public Card findCardToBeat(Card attackingCard, Card.Suit trump) {
        if (!isAI)
            return null;

        List<Card> validCards = new ArrayList<>();
        for (Card card : hand) {
            if (card.canBeat(attackingCard, trump)) {
                validCards.add(card);
            }
        }

        if (validCards.isEmpty())
            return null;

        // На низких уровнях сложности иногда принимаем случайные решения
        if (difficulty <= 2 && random.nextInt(100) < 30) {
            return validCards.get(random.nextInt(validCards.size()));
        }

        // На высоких уровнях сложности стараемся сохранить козыри
        if (difficulty >= 4) {
            List<Card> nonTrumpCards = new ArrayList<>();
            for (Card card : validCards) {
                if (card.getSuit() != trump) {
                    nonTrumpCards.add(card);
                }
            }
            if (!nonTrumpCards.isEmpty()) {
                validCards = nonTrumpCards;
            }
        }

        // Выбираем карту с минимальным преимуществом над атакующей
        Card bestCard = validCards.get(0);
        int minDiff = Integer.MAX_VALUE;

        for (Card card : validCards) {
            int diff = card.getRank().getValue() - attackingCard.getRank().getValue();
            if (diff < minDiff) {
                minDiff = diff;
                bestCard = card;
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