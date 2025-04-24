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
    private DurakGame game;
    private JPanel humanPanel;
    private JPanel tablePanel;
    private JPanel aiPanel;
    private final JLabel deckInfo = new JLabel();
    private final JLabel statusLabel = new JLabel();
    private final JButton endTurnButton = createStyledButton("Бито");
    private final JButton takeCardsButton = createStyledButton("Взять карты");
    private Card selectedCard;
    private final Color ATTACK_COLOR = new Color(255, 182, 193); // Светло-розовый
    private final Color DEFEND_COLOR = new Color(144, 238, 144); // Светло-зеленый
    private final Color INACTIVE_COLOR = new Color(220, 220, 220);
    private final Color TABLE_COLOR = new Color(53, 101, 77); // Темно-зеленый
    private final Color BUTTON_COLOR = new Color(70, 130, 180);
    private final Font CARD_FONT = new Font("Arial", Font.BOLD, 20);
    private final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 16);
    private final Font STATUS_FONT = new Font("Arial", Font.BOLD, 20);

    public GameWindow() {
        showDifficultyDialog();

        setTitle("Карточная игра Дурак");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(1200, 900));
        getContentPane().setBackground(new Color(240, 240, 240));

        // Верхняя информационная панель
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Панель компьютера
        aiPanel = createPlayerPanel("Карты компьютера", new Color(220, 237, 200));
        add(aiPanel, BorderLayout.NORTH);

        // Центральная панель с игровым столом
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Панель игрока
        humanPanel = createPlayerPanel("Ваши карты", new Color(255, 223, 186));
        add(humanPanel, BorderLayout.SOUTH);

        // Панель управления
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        updateUI();
        showStartupHelp();
    }

    private void showDifficultyDialog() {
        String[] difficulties = { "Новичок (1)", "Легкий (2)", "Средний (3)", "Сложный (4)", "Эксперт (5)" };
        String selected = (String) JOptionPane.showInputDialog(
                null,
                "Выберите уровень сложности:",
                "Уровень сложности",
                JOptionPane.QUESTION_MESSAGE,
                null,
                difficulties,
                difficulties[2]);

        int difficulty = 3; // По умолчанию средний уровень
        if (selected != null) {
            difficulty = Integer.parseInt(selected.replaceAll("\\D+", ""));
        }

        game = new DurakGame(difficulty);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setOpaque(false);

        // Информация о колоде и козыре
        JPanel deckPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        deckPanel.setBackground(new Color(238, 238, 238));
        deckPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        deckPanel.setPreferredSize(new Dimension(200, 60));

        JLabel deckImage = new JLabel("🎴");
        deckImage.setFont(new Font("Arial", Font.PLAIN, 48));
        deckPanel.add(deckImage);

        deckInfo.setFont(new Font("Arial", Font.BOLD, 16));
        deckPanel.add(deckInfo);
        topPanel.add(deckPanel, BorderLayout.WEST);

        // Статус игры
        statusLabel.setFont(STATUS_FONT);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        statusLabel.setBackground(new Color(238, 238, 238));
        statusLabel.setOpaque(true);
        topPanel.add(statusLabel, BorderLayout.CENTER);

        // Уровень сложности
        JLabel difficultyLabel = new JLabel(String.format("Уровень: %d/5", game.getDifficulty()));
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        difficultyLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        difficultyLabel.setBackground(new Color(238, 238, 238));
        difficultyLabel.setOpaque(true);
        topPanel.add(difficultyLabel, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createPlayerPanel(String title, Color backgroundColor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(backgroundColor);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2, true),
                title);
        titledBorder.setTitleFont(BUTTON_FONT);
        panel.setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(15, 10, 15, 10)));
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(TABLE_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tablePanel = new JPanel();
        tablePanel.setBackground(TABLE_COLOR);
        TitledBorder tableTitle = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2, true),
                "Игровой стол");
        tableTitle.setTitleFont(BUTTON_FONT);
        tableTitle.setTitleColor(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                tableTitle,
                BorderFactory.createEmptyBorder(30, 20, 30, 20)));
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(6, 1, 5, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.setBackground(new Color(238, 238, 238));

        JButton helpButton = createStyledButton("Правила игры");
        JButton newGameButton = createStyledButton("Новая игра");
        JButton changeDifficultyButton = createStyledButton("Сменить сложность");

        setupButtonActions(helpButton, newGameButton, changeDifficultyButton);

        controlPanel.add(endTurnButton);
        controlPanel.add(takeCardsButton);
        controlPanel.add(new JSeparator());
        controlPanel.add(helpButton);
        controlPanel.add(new JSeparator());
        controlPanel.add(newGameButton);
        controlPanel.add(changeDifficultyButton);

        return controlPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(47, 79, 79), 2, true),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Добавляем эффекты при наведении
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }

    private void setupButtonActions(JButton helpButton, JButton newGameButton, JButton changeDifficultyButton) {
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

        changeDifficultyButton.addActionListener(e -> {
            showDifficultyDialog();
            updateUI();
        });
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
            showGameOverDialog();
            status = "🏆 Игра окончена! " + game.getWinner();
            statusLabel.setBackground(new Color(255, 215, 0));
            aiPanel.setBackground(INACTIVE_COLOR);
            humanPanel.setBackground(INACTIVE_COLOR);
            endTurnButton.setEnabled(false);
            takeCardsButton.setEnabled(false);
        } else {
            if (game.getAttacker() == game.getHuman()) {
                status = "🎯 Ваш ход: Атакуйте!";
                statusLabel.setBackground(new Color(144, 238, 144));
                humanPanel.setBackground(ATTACK_COLOR);
                aiPanel.setBackground(DEFEND_COLOR);
            } else {
                if (game.getTable().isEmpty()) {
                    status = "⏳ Ход компьютера...";
                } else {
                    status = "🛡️ Ваш ход: Защищайтесь!";
                }
                statusLabel.setBackground(new Color(255, 182, 193));
                humanPanel.setBackground(DEFEND_COLOR);
                aiPanel.setBackground(ATTACK_COLOR);
            }
        }
        statusLabel.setText(status);

        // Обновляем доступность кнопок
        boolean isDefending = game.getDefender() == game.getHuman() && !game.getTable().isEmpty();
        endTurnButton.setEnabled(!game.getTable().isEmpty() && !isDefending);
        takeCardsButton.setEnabled(isDefending);

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
                    boolean isValidMove = false;

                    if (game.getAttacker() == game.getHuman()) {
                        if (game.isValidAttack(card)) {
                            isValidMove = game.attack(card);
                            if (!isValidMove) {
                                showInvalidMoveMessage("Этой картой нельзя ходить!");
                            }
                        } else {
                            showInvalidMoveMessage("Сейчас нельзя атаковать этой картой!");
                        }
                    } else if (game.getDefender() == game.getHuman()) {
                        if (game.isValidDefense(card)) {
                            isValidMove = game.defend(card);
                            if (!isValidMove) {
                                showInvalidMoveMessage("Этой картой нельзя отбиться!");
                            }
                        } else {
                            showInvalidMoveMessage("Сейчас нельзя защищаться этой картой!");
                        }
                    }

                    if (isValidMove) {
                        updateUI();
                    }
                });
            }
            panel.add(cardButton);
        }
    }

    private JButton createCardButton(Card card, boolean isHuman) {
        JButton cardButton = new JButton();
        cardButton.setPreferredSize(new Dimension(100, 150));
        cardButton.setFont(CARD_FONT);

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

        cardLabel.setFont(CARD_FONT);
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

    private void showGameOverDialog() {
        String winner = game.getWinner();
        String stats = game.getGameStats();

        // Создаем панель с красивым оформлением
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Добавляем иконку победителя
        JLabel iconLabel = new JLabel(winner.contains("выиграли") ? "🏆" : "😢");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        // Добавляем текст победителя
        JLabel winnerLabel = new JLabel(winner);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(winnerLabel);

        // Добавляем статистику
        JTextArea statsArea = new JTextArea(stats);
        statsArea.setFont(new Font("Arial", Font.PLAIN, 16));
        statsArea.setEditable(false);
        statsArea.setBackground(panel.getBackground());
        statsArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(statsArea);

        // Создаем кнопки
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton newGameButton = createStyledButton("Играть снова");
        JButton exitButton = createStyledButton("Выход");

        newGameButton.addActionListener(e -> {
            dispose();
            new GameWindow().setVisible(true);
        });

        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(newGameButton);
        buttonPanel.add(exitButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(buttonPanel);

        // Показываем диалог
        JOptionPane.showOptionDialog(
                this,
                panel,
                "Игра окончена",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[] {},
                null);
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