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
    private int difficulty;

    public DurakGame(int difficulty) {
        this.difficulty = Math.min(Math.max(difficulty, 1), 5);
        deck = new Deck();
        human = new Player("Игрок", false, 0);
        ai = new Player("Компьютер", true, this.difficulty);
        table = new ArrayList<>();
        discardPile = new ArrayList<>();
        gameOver = false;

        // Раздача начальных карт
        for (int i = 0; i < 6; i++) {
            human.addCard(deck.drawCard());
            ai.addCard(deck.drawCard());
        }

        // Определение первого хода
        Card lowestTrumpHuman = findLowestTrump(human.getHand(), deck.getTrump());
        Card lowestTrumpAI = findLowestTrump(ai.getHand(), deck.getTrump());

        if (lowestTrumpHuman != null && (lowestTrumpAI == null ||
                lowestTrumpHuman.getRank().getValue() < lowestTrumpAI.getRank().getValue())) {
            attacker = human;
            defender = ai;
        } else if (lowestTrumpAI != null) {
            attacker = ai;
            defender = human;
            // Сразу делаем ход ИИ, если он атакующий
            makeAIMove();
        } else {
            // Если нет козырей, ищем наименьшую карту
            Card lowestHuman = findLowestCard(human.getHand());
            Card lowestAI = findLowestCard(ai.getHand());

            if (lowestHuman != null && (lowestAI == null ||
                    lowestHuman.getRank().getValue() < lowestAI.getRank().getValue())) {
                attacker = human;
                defender = ai;
            } else {
                attacker = ai;
                defender = human;
                // Сразу делаем ход ИИ, если он атакующий
                makeAIMove();
            }
        }
    }

    private Card findLowestTrump(List<Card> hand, Card.Suit trump) {
        Card lowest = null;
        for (Card card : hand) {
            if (card.getSuit() == trump) {
                if (lowest == null || card.getRank().getValue() < lowest.getRank().getValue()) {
                    lowest = card;
                }
            }
        }
        return lowest;
    }

    private Card findLowestCard(List<Card> hand) {
        Card lowest = null;
        for (Card card : hand) {
            if (lowest == null || card.getRank().getValue() < lowest.getRank().getValue()) {
                lowest = card;
            }
        }
        return lowest;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = Math.min(Math.max(difficulty, 1), 5);
        ai.setDifficulty(this.difficulty);
    }

    public void makeAIMove() {
        if (ai == attacker) {
            // Если стол пустой, находим подходящую карту для атаки
            if (table.isEmpty()) {
                Card cardToPlay = ai.findLowestCard(deck.getTrump());
                if (cardToPlay != null) {
                    attack(cardToPlay);
                } else {
                    endTurn();
                }
            } else if (table.size() % 2 == 0) { // Если все карты отбиты, можно подкидывать
                // Ищем карты того же достоинства, что уже есть на столе
                List<Card> validCards = new ArrayList<>();
                for (Card tableCard : table) {
                    for (Card aiCard : ai.getHand()) {
                        if (aiCard.getRank() == tableCard.getRank() && isValidAttack(aiCard)) {
                            validCards.add(aiCard);
                        }
                    }
                }

                if (!validCards.isEmpty()) {
                    // Выбираем случайную карту из подходящих
                    Card cardToPlay = validCards.get(new Random().nextInt(validCards.size()));
                    attack(cardToPlay);
                } else {
                    endTurn();
                }
            }
        } else if (ai == defender && !table.isEmpty()) {
            Card attackingCard = table.get(table.size() - 1);
            Card cardToPlay = ai.findCardToBeat(attackingCard, deck.getTrump());
            if (cardToPlay != null) {
                defend(cardToPlay);
            } else {
                takeCards();
            }
        }
    }

    public boolean isValidAttack(Card card) {
        if (!attacker.getHand().contains(card)) {
            return false;
        }

        // Если стол пустой, можно ходить любой картой
        if (table.isEmpty()) {
            return true;
        }

        // Проверяем, что на столе четное количество карт (все атаки отбиты)
        if (table.size() % 2 != 0) {
            return false;
        }

        // Можно подкидывать только карты того же достоинства, что уже есть на столе
        for (Card tableCard : table) {
            if (tableCard.getRank() == card.getRank()) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidDefense(Card card) {
        if (!defender.getHand().contains(card)) {
            return false;
        }

        // Если стол пустой или количество карт четное, защищаться нельзя
        if (table.isEmpty() || table.size() % 2 == 0) {
            return false;
        }

        // Получаем последнюю атакующую карту
        Card attackingCard = table.get(table.size() - 1);

        // Проверяем, может ли карта побить атакующую
        return card.canBeat(attackingCard, deck.getTrump());
    }

    public boolean attack(Card card) {
        if (isValidAttack(card)) {
            table.add(card);
            attacker.removeCard(card);

            if (defender == ai) {
                makeAIMove();
            }
            return true;
        }
        return false;
    }

    public boolean defend(Card card) {
        if (isValidDefense(card)) {
            table.add(card);
            defender.removeCard(card);

            if (attacker == ai) {
                makeAIMove();
            }
            return true;
        }
        return false;
    }

    public void takeCards() {
        defender.getHand().addAll(table);
        table.clear();

        if (attacker == ai) {
            makeAIMove();
        }
    }

    public void endTurn() {
        discardPile.addAll(table);
        table.clear();

        while (attacker.getHandSize() < 6 && !deck.isEmpty()) {
            attacker.addCard(deck.drawCard());
        }
        while (defender.getHandSize() < 6 && !deck.isEmpty()) {
            defender.addCard(deck.drawCard());
        }

        if (deck.isEmpty() && (attacker.getHandSize() == 0 || defender.getHandSize() == 0)) {
            gameOver = true;
            return;
        }

        if (table.isEmpty()) {
            Player temp = attacker;
            attacker = defender;
            defender = temp;

            if (attacker == ai) {
                makeAIMove();
            }
        }
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
            return "Вы выиграли!";
        } else if (ai.getHandSize() == 0) {
            return "Компьютер выиграл!";
        }
        return "Ничья";
    }

    public String getGameStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("Итоговый счет:\n");
        stats.append("Ваши карты: ").append(human.getHandSize()).append("\n");
        stats.append("Карты компьютера: ").append(ai.getHandSize()).append("\n");
        stats.append("Осталось в колоде: ").append(deck.getSize()).append("\n");
        return stats.toString();
    }
}