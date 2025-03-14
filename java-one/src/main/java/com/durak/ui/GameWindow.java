package com.durak.ui;

import com.durak.game.DurakGame;
import com.durak.model.Card;
import com.durak.model.Player;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GameWindow extends JFrame {
    private final DurakGame game;
    private final JPanel humanPanel;
    private final JPanel tablePanel;
    private final JPanel aiPanel;
    private final JLabel deckInfo;
    private final JLabel statusLabel;
    private final JButton endTurnButton;
    private final JButton takeCardsButton;
    private Card selectedCard;
    private final Color ATTACK_COLOR = new Color(255, 200, 200);
    private final Color DEFEND_COLOR = new Color(200, 255, 200);
    private final Color INACTIVE_COLOR = new Color(220, 220, 220);

    public GameWindow() {
        game = new DurakGame();

        setTitle("Карточная игра Дурак");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(1200, 900));

        // Верхняя информационная панель
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Информация о колоде и козыре
        JPanel deckPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        deckPanel.setBackground(new Color(238, 238, 238));
        deckPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Добавляем изображение колоды
        JLabel deckImage = new JLabel("🎴");
        deckImage.setFont(new Font("Arial", Font.PLAIN, 48));
        deckPanel.add(deckImage);

        deckInfo = new JLabel();
        deckInfo.setFont(new Font("Arial", Font.BOLD, 16));
        deckPanel.add(deckInfo);
        topPanel.add(deckPanel, BorderLayout.WEST);

        // Статус игры
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        statusLabel.setBackground(new Color(238, 238, 238));
        statusLabel.setOpaque(true);
        topPanel.add(statusLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Панель компьютера
        aiPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        aiPanel.setBackground(new Color(220, 237, 200));
        TitledBorder aiTitle = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Карты компьютера");
        aiTitle.setTitleFont(new Font("Arial", Font.BOLD, 16));
        aiPanel.setBorder(BorderFactory.createCompoundBorder(
                aiTitle,
                BorderFactory.createEmptyBorder(15, 10, 15, 10)));
        add(aiPanel, BorderLayout.NORTH);

        // Центральная панель с игровым столом
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(76, 153, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Игровой стол
        tablePanel = new JPanel();
        tablePanel.setBackground(new Color(76, 153, 0));
        TitledBorder tableTitle = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Игровой стол");
        tableTitle.setTitleFont(new Font("Arial", Font.BOLD, 16));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                tableTitle,
                BorderFactory.createEmptyBorder(30, 20, 30, 20)));
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Панель игрока
        humanPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        humanPanel.setBackground(new Color(255, 223, 186));
        TitledBorder humanTitle = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Ваши карты");
        humanTitle.setTitleFont(new Font("Arial", Font.BOLD, 16));
        humanPanel.setBorder(BorderFactory.createCompoundBorder(
                humanTitle,
                BorderFactory.createEmptyBorder(15, 10, 15, 10)));
        add(humanPanel, BorderLayout.SOUTH);

        // Панель управления
        JPanel controlPanel = new JPanel(new GridLayout(6, 1, 5, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.setBackground(new Color(238, 238, 238));

        // Кнопки управления
        endTurnButton = new JButton("Бито");
        takeCardsButton = new JButton("Взять карты");
        JButton helpButton = new JButton("Правила игры");
        JButton newGameButton = new JButton("Новая игра");

        styleButton(endTurnButton);
        styleButton(takeCardsButton);
        styleButton(helpButton);
        styleButton(newGameButton);

        endTurnButton.addActionListener(e -> {
            game.endTurn();
            if (!game.isGameOver() && game.getAttacker() == game.getAI()) {
                game.makeAIMove();
            }
            updateUI();
        });

        takeCardsButton.addActionListener(e -> {
            game.takeCards();
            if (!game.isGameOver() && game.getAttacker() == game.getAI()) {
                game.makeAIMove();
            }
            updateUI();
        });

        helpButton.addActionListener(e -> showRules());

        newGameButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Вы уверены, что хотите начать новую игру?",
                    "Новая игра",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                dispose();
                new GameWindow().setVisible(true);
            }
        });

        controlPanel.add(endTurnButton);
        controlPanel.add(takeCardsButton);
        controlPanel.add(new JSeparator());
        controlPanel.add(helpButton);
        controlPanel.add(new JSeparator());
        controlPanel.add(newGameButton);

        add(controlPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        updateUI();
        showStartupHelp();
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(47, 79, 79), 2),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void showStartupHelp() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    "Добро пожаловать в игру Дурак!\n\n" +
                            "Как играть:\n" +
                            "• Атакующий игрок подсвечивается розовым\n" +
                            "• Защищающийся игрок подсвечивается зеленым\n" +
                            "• Нажмите на карту в своей руке для хода\n" +
                            "• Кнопка 'Бито' завершает текущий ход\n" +
                            "• Кнопка 'Взять карты' - если не можете отбиться\n" +
                            "• Козырная масть отмечена в верхнем левом углу\n",
                    "Как играть",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void showRules() {
        JOptionPane.showMessageDialog(this,
                "Правила игры 'Дурак':\n\n" +
                        "1. Цель игры: избавиться от всех карт\n" +
                        "2. Козырная масть бьёт все остальные масти\n" +
                        "3. Карту можно бить только старшей картой той же масти или козырем\n" +
                        "4. При атаке можно подкидывать карты того же достоинства\n" +
                        "5. Если не можете отбиться - берите все карты со стола\n" +
                        "6. После каждого хода игроки добирают карты до 6\n\n" +
                        "Управление:\n" +
                        "• Нажмите на карту для хода или отбоя\n" +
                        "• 'Бито' - завершить текущий ход\n" +
                        "• 'Взять карты' - забрать все карты со стола",
                "Правила игры",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateUI() {
        // Обновляем информацию о колоде
        deckInfo.setText(String.format(" Козырь: %s | В колоде: %d карт",
                game.getDeck().getTrump().getSymbol(),
                game.getDeck().getSize()));

        // Обновляем статус игры и цвета панелей
        String status;
        if (game.isGameOver()) {
            status = "🏆 Игра окончена! Победитель: " + game.getWinner();
            statusLabel.setBackground(new Color(255, 215, 0));
            aiPanel.setBackground(INACTIVE_COLOR);
            humanPanel.setBackground(INACTIVE_COLOR);
        } else {
            if (game.getAttacker() == game.getHuman()) {
                status = "🎯 Ваш ход: Атакуйте!";
                statusLabel.setBackground(new Color(144, 238, 144));
                humanPanel.setBackground(ATTACK_COLOR);
                aiPanel.setBackground(DEFEND_COLOR);
            } else {
                status = "🛡️ Ваш ход: Защищайтесь!";
                statusLabel.setBackground(new Color(255, 182, 193));
                humanPanel.setBackground(DEFEND_COLOR);
                aiPanel.setBackground(ATTACK_COLOR);
            }
        }
        statusLabel.setText(status);

        // Обновляем доступность кнопок
        endTurnButton.setEnabled(!game.getTable().isEmpty());
        takeCardsButton.setEnabled(game.getDefender() == game.getHuman() && !game.getTable().isEmpty());

        // Обновляем карты игроков
        updatePlayerCards(humanPanel, game.getHuman().getHand(), true);
        updatePlayerCards(aiPanel, game.getAI().getHand(), false);
        updateTableCards();

        revalidate();
        repaint();
    }

    private void updatePlayerCards(JPanel panel, List<Card> cards, boolean isHuman) {
        panel.removeAll();
        for (Card card : cards) {
            JButton cardButton = createCardButton(card, isHuman);
            if (isHuman) {
                cardButton.addActionListener(e -> {
                    selectedCard = card;
                    if (game.getAttacker() == game.getHuman()) {
                        if (game.attack(card)) {
                            if (!game.isGameOver()) {
                                game.makeAIMove();
                            }
                            updateUI();
                        } else {
                            showInvalidMoveMessage("Этой картой нельзя ходить!");
                        }
                    } else if (game.getDefender() == game.getHuman()) {
                        if (game.defend(card)) {
                            if (!game.isGameOver()) {
                                game.makeAIMove();
                            }
                            updateUI();
                        } else {
                            showInvalidMoveMessage("Этой картой нельзя отбиться!");
                        }
                    }
                });
            }
            panel.add(cardButton);
        }
    }

    private JButton createCardButton(Card card, boolean isHuman) {
        JButton cardButton = new JButton();
        cardButton.setPreferredSize(new Dimension(100, 150));
        cardButton.setFont(new Font("Arial", Font.BOLD, 20));

        if (isHuman) {
            String suit = card.getSuit().getSymbol();
            Color textColor = (suit.equals("♥") || suit.equals("♦")) ? Color.RED : Color.BLACK;
            cardButton.setForeground(textColor);

            // Добавляем информацию о козыре
            boolean isTrump = card.getSuit() == game.getDeck().getTrump();
            String trumpMark = isTrump ? "⭐" : "";

            cardButton.setText(String.format("<html><center>%s%s<br><font size='+2'>%s</font></center></html>",
                    trumpMark, card.getRank(), suit));
        } else {
            cardButton.setText("🂠");
            cardButton.setFont(new Font("Arial", Font.PLAIN, 48));
        }

        cardButton.setBackground(Color.WHITE);
        cardButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        cardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return cardButton;
    }

    private void updateTableCards() {
        tablePanel.removeAll();
        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        List<Card> tableCards = game.getTable();
        for (int i = 0; i < tableCards.size(); i += 2) {
            JPanel pairPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            pairPanel.setBackground(new Color(76, 153, 0));

            // Атакующая карта
            pairPanel.add(createCardLabel(tableCards.get(i), true));

            // Если есть отбивающая карта
            if (i + 1 < tableCards.size()) {
                pairPanel.add(createCardLabel(tableCards.get(i + 1), false));
            }

            tablePanel.add(pairPanel);
        }
    }

    private JLabel createCardLabel(Card card, boolean isAttacking) {
        JLabel cardLabel = new JLabel();
        cardLabel.setPreferredSize(new Dimension(100, 150));
        cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardLabel.setVerticalAlignment(SwingConstants.CENTER);

        String suit = card.getSuit().getSymbol();
        Color textColor = (suit.equals("♥") || suit.equals("♦")) ? Color.RED : Color.BLACK;
        cardLabel.setForeground(textColor);

        boolean isTrump = card.getSuit() == game.getDeck().getTrump();
        String trumpMark = isTrump ? "⭐" : "";

        cardLabel.setText(String.format("<html><center>%s%s<br><font size='+2'>%s</font></center></html>",
                trumpMark, card.getRank(), suit));

        cardLabel.setFont(new Font("Arial", Font.BOLD, 20));
        cardLabel.setBackground(Color.WHITE);
        cardLabel.setOpaque(true);
        cardLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        return cardLabel;
    }

    private void showInvalidMoveMessage(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Недопустимый ход",
                JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new GameWindow().setVisible(true);
        });
    }
}