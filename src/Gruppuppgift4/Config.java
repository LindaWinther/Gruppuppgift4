package Gruppuppgift4;

import javax.imageio.IIOException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private Properties prop = new Properties();
    public Config() {
        try {
            prop.load(new FileInputStream("src/Gruppuppgift4/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Kunde inte läsa config.properties");
        }
    }

    public int getQuestionsinRound() {
        return Integer.parseInt(prop.getProperty("questionsInRound"));
    }

    public int getRoundsInGame() {
        return Integer.parseInt(prop.getProperty("roundsInGame"));
    }
}

