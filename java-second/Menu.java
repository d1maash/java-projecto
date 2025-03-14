import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Menu extends JPanel {
    private static final int MENU_WIDTH = 800;
    private static final int MENU_HEIGHT = 600;
    private Game game;
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainContainer;

    public Menu(JFrame frame) {
        this.frame = frame;
        this.cardLayout = new CardLayout();
        this.mainContainer = new JPanel(cardLayout);

        setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(40, 40, 40));

        // Заголовок
        JLabel titleLabel = new JLabel("2D Racing Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(100));
        add(titleLabel);
        add(Box.createVerticalStrut(50));

        // Кнопки
        addButton("Новая игра", e -> startNewGame());
        addButton("Лучшее время", e -> showBestTimes());
        addButton("Настройки", e -> showSettings());
        addButton("Выход", e -> System.exit(0));

        mainContainer.add(this, "menu");
    }

    private void addButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 50));
        button.addActionListener(action);
        add(Box.createVerticalStrut(20));
        add(button);
    }

    private void startNewGame() {
        game = new Game(frame, mainContainer, cardLayout);
        mainContainer.add(game, "game");
        cardLayout.show(mainContainer, "game");
        game.requestFocus();
        game.startGame();
    }

    private void showBestTimes() {
        // Показать таблицу лучших времен
        JPanel bestTimesPanel = new JPanel();
        bestTimesPanel.setBackground(new Color(40, 40, 40));
        JLabel label = new JLabel("Лучшее время: " + ScoreManager.getBestTime() + " сек");
        label.setForeground(Color.WHITE);
        bestTimesPanel.add(label);
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> cardLayout.show(mainContainer, "menu"));
        bestTimesPanel.add(backButton);
        mainContainer.add(bestTimesPanel, "bestTimes");
        cardLayout.show(mainContainer, "bestTimes");
    }

    private void showSettings() {
        // Показать настройки
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(new Color(40, 40, 40));
        JLabel label = new JLabel("Настройки");
        label.setForeground(Color.WHITE);
        settingsPanel.add(label);
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> cardLayout.show(mainContainer, "menu"));
        settingsPanel.add(backButton);
        mainContainer.add(settingsPanel, "settings");
        cardLayout.show(mainContainer, "settings");
    }

    public JPanel getMainContainer() {
        return mainContainer;
    }
}