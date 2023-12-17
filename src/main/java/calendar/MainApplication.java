package calendar;

import calendar.isdayoff.IsDayOffIntegration;
import calendar.notes.NotesDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Основной класс приложения.
 */
public class MainApplication extends Application {
    /**
     * Логгер приложения, который записывает всю информацию в консоль и в файл.
     */
    public static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Calendar.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.show();

        logger.info("Calendar application started!");
    }

    /**
     * Метод запуска приложения.
     * @param args аргументы консоли
     */
    public static void main(String... args) {
        NotesDatabase.load();
        IsDayOffIntegration.load();

        launch();
    }
}