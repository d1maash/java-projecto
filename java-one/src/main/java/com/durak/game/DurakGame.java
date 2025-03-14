package com.durak.game;

import com.durak.model.*;
import java.util.*;

public class DurakGame {
    private final Deck deck;
    private final Player human;
    private final Player ai;
    private Player attacker;
    private Player defender;
    private final List<Card> table;
    private final List<Card> discardPile;
    private boolean gameOver;

    public DurakGame() {
        deck = new Deck();
        human = new Player("Игрок", false);
        ai = new Player("Компьютер", true);
        table = new ArrayList<>();
        discardPile = new ArrayList<>();
        gameOver = false;

        // Раздача начальных карт
        for (int i = 0; i < 6; i++) {
            human.addCard(deck.drawCard());
            ai.addCard(deck.drawCard());
        }

        // Определение первого хода
        Card humanLowest = human.findLowestCard(deck.getTrump());
        Card aiLowest = ai.findLowestCard(deck.getTrump());

        if (humanLowest != null && (aiLowest == null ||
                humanLowest.getRank().getValue() < aiLowest.getRank().getValue())) {
            attacker = human;
            defender = ai;
        } else {
            attacker = ai;
            defender = human;
        }
    }

    public void makeAIMove() {
        if (ai == attacker) {
            Card cardToPlay = ai.findLowestCard(deck.getTrump());
            if (cardToPlay != null) {
                attack(cardToPlay);
            } else {
                endTurn();
            }
        } else {
            Card attackingCard = table.get(table.size() - 1);
            Card cardToPlay = ai.findCardToBeat(attackingCard, deck.getTrump());
            if (cardToPlay != null) {
                defend(cardToPlay);
            } else {
                takeCards();
            }
        }
    }

    public boolean attack(Card card) {
        if (isValidAttack(card)) {
            table.add(card);
            attacker.removeCard(card);
            return true;
        }
        return false;
    }

    public boolean defend(Card card) {
        if (isValidDefense(card)) {
            table.add(card);
            defender.removeCard(card);
            return true;
        }
        return false;
    }

    private boolean isValidAttack(Card card) {
        if (table.isEmpty()) {
            return true;
        }
        return table.stream().anyMatch(c -> c.getRank() == card.getRank());
    }

    private boolean isValidDefense(Card card) {
        if (table.isEmpty()) {
            return false;
        }
        Card attackingCard = table.get(table.size() - 1);
        return card.canBeat(attackingCard, deck.getTrump());
    }

    public void takeCards() {
        defender.getHand().addAll(table);
        table.clear();
        endTurn();
    }

    public void endTurn() {
        // Сбрасываем карты со стола
        discardPile.addAll(table);
        table.clear();

        // Добираем карты
        while (attacker.getHandSize() < 6 && !deck.isEmpty()) {
            attacker.addCard(deck.drawCard());
        }
        while (defender.getHandSize() < 6 && !deck.isEmpty()) {
            defender.addCard(deck.drawCard());
        }

        // Проверяем условия окончания игры
        if (deck.isEmpty() && (attacker.getHandSize() == 0 || defender.getHandSize() == 0)) {
            gameOver = true;
            return;
        }

        // Меняем роли
        Player temp = attacker;
        attacker = defender;
        defender = temp;
    }

    public Player getHuman() {
        return human;
    }

    public Player getAI() {
        return ai;
    }

    public Player getAttacker() {
        return attacker;
    }

    public Player getDefender() {
        return defender;
    }

    public List<Card> getTable() {
        return table;
    }

    public Deck getDeck() {
        return deck;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        if (!gameOver) {
            return null;
        }
        if (human.getHandSize() == 0) {
            return "Игрок";
        } else if (ai.getHandSize() == 0) {
            return "Компьютер";
        }
        return "Ничья";
    }
}