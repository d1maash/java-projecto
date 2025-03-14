import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreManager {
    private static final String SCORES_FILE = "best_times.txt";
    private static List<Double> bestTimes = new ArrayList<>();
    private static final int MAX_SCORES = 5;

    static {
        loadBestTimes();
    }

    public static void addTime(double time) {
        bestTimes.add(time);
        Collections.sort(bestTimes);
        if (bestTimes.size() > MAX_SCORES) {
            bestTimes = bestTimes.subList(0, MAX_SCORES);
        }
        saveBestTimes();
    }

    public static double getBestTime() {
        if (bestTimes.isEmpty()) {
            return 0.0;
        }
        return bestTimes.get(0);
    }

    public static List<Double> getAllBestTimes() {
        return new ArrayList<>(bestTimes);
    }

    private static void loadBestTimes() {
        try {
            File file = new File(SCORES_FILE);
            if (!file.exists()) {
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                bestTimes.add(Double.parseDouble(line));
            }
            reader.close();
            Collections.sort(bestTimes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveBestTimes() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(SCORES_FILE));
            for (Double time : bestTimes) {
                writer.write(String.valueOf(time));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}