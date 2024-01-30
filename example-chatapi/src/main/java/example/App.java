package example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("Starting application.");

        logger.warn("Trying to do something interesting...");
        try {
            System.out.println(divide(42, 0));
        } catch (Exception e) {
            logger.error("Didn't work", e);
        }

        String question = "Ultimate Question of Life, the Universe, and Everything";
        int answer = 42;
        logger.info("The answer to the {} is {}.", question, answer);
    }

    public static int divide(int x, int y) {
        logger.info("Let's divide {} by {}.", x, y);
        return x / y;
    }
}
