import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Запускаем игру в потоке EDT
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Racing Game");
            Menu menu = new Menu(frame);
            frame.add(menu.getMainContainer());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);

            // Загружаем звуки
            SoundManager.loadSounds();
        });
    }
}