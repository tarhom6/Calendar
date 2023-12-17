package calendar;

import calendar.isdayoff.IsDayOffIntegration;
import calendar.notes.*;
import com.groupstp.isdayoff.IsDayOffDateType;
import com.groupstp.isdayoff.enums.DayType;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Dialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;

import java.net.URL;
import java.time.*;
import java.util.*;

import static calendar.MainApplication.*;

/**
 * Основной контроллер приложения. Управляет интерфейсом и кнопками.
 */
public class CalendarController implements Initializable {

    /**
     * Текущая выбранная дата
     */
    ZonedDateTime dateFocus;

    /**
     * Сегодняшняя дата
     */
    ZonedDateTime today;

    /**
     * Надпись, на которой обозначается выбранный год
     */
    @FXML
    private Text year;

    /**
     * Надпись, на которой обозначается выбранный месяц
     */
    @FXML
    private Text month;

    /**
     * Надпись, на которой обозначается весь календарь
     */
    @FXML
    private FlowPane calendar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();

        drawCalendar();
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    /**
     * Основной метод приложения.
     * Отрисовывает календарь, создавая ячейки с номерами дней, создает все кнопки, надписи и другие элементы UI
     */
    public void drawCalendar() {
        // Устанавливаем нужный год и месяц на экране
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        // Получаем количество дней в текущем месяце
        int monthMaxDate = dateFocus.getMonth().maxLength();

        // Проверяем год на високосность
        if (!IsDayOffIntegration.isLeap(dateFocus.getYear()) && dateFocus.getMonth() == Month.FEBRUARY) {
            monthMaxDate = 28;
        }

        // Получаем список выходных
        List<IsDayOffDateType> holidays = IsDayOffIntegration.getHolidays(dateFocus.getYear(), dateFocus.getMonthValue());

        // Высчитываем отступ даты
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(1f);

                double rectangleWidth = (calendarWidth / 7) - 1f - spacingH;
                double rectangleHeight = (calendarHeight / 6) - 1f - spacingV;

                rectangle.setWidth(rectangleWidth);
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 2) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        date.setFont(new Font(16f));
                        date.setTranslateY(-(rectangleHeight / 2) * 0.75);

                        stackPane.getChildren().add(date);

                        List<Note> calendarActivities = NotesDatabase.getNotes(dateFocus.getYear(), dateFocus.getMonthValue(), currentDate);
                        createCalendarActivity(dateFocus.getYear(), dateFocus.getMonthValue(), currentDate, calendarActivities, rectangleHeight, rectangleWidth, stackPane);

                        // Выделяем текущий день
                        if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                            rectangle.setFill(Color.CYAN);
                        }

                        // Выделяем выходной день
                        else if (holidays.get(currentDate - 1).getDayType() == DayType.NOT_WORKING_DAY) {
                            rectangle.setFill(new Color(1f, 0.5f, 0.5f, 1f));
                        }
                    }
                }

                calendar.getChildren().add(stackPane);
            }
        }

        logger.info("Rendered the month: {} {}", dateFocus.getMonth(), dateFocus.getYear());
    }

    /**
     * Создает ячейку для конкретной даты.
     * @param year год
     * @param month месяц
     * @param day день
     * @param notes список записей для конкретной даты
     * @param rectangleHeight высота ячейки
     * @param rectangleWidth ширина ячейки
     * @param stackPane панель, на которую надо добавить ячейку
     */
    private void createCalendarActivity(int year, int month, int day, List<Note> notes, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        Text notesAmount = new Text(notes.isEmpty() ? "" : "Заметок: " + notes.size());
        notesAmount.setTextAlignment(TextAlignment.CENTER);

        VBox calendarActivityBox = new VBox();
        calendarActivityBox.getChildren().add(notesAmount);

        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);

        calendarActivityBox.setOnMouseClicked(event -> {
            Dialog<String> dialog = new NotesDialog(year, month, day, notes, notesAmount);
            dialog.showAndWait();
        });

        stackPane.getChildren().add(calendarActivityBox);
    }
}