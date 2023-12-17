package calendar.notes;

import calendar.isdayoff.IsDayOffIntegration;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Window;

import java.util.*;

/**
 * Диалог информации о записях для конкретного дня.
 */
public class NotesDialog extends Dialog<String> {
    /**
     * Основной конструктор диалога.
     * @param year год
     * @param month месяц
     * @param day день
     * @param notes список записей
     * @param notesAmount надпись, на которой отображается количество записей
     */
    public NotesDialog(int year, int month, int day, List<Note> notes, Text notesAmount) {
        this.setTitle("Список заметок");
        this.setHeaderText("Дата: " + year + "/" + month + "/" + day + (IsDayOffIntegration.isHoliday(year, month, day) ? "\nВыходной день" : "\nРабочий день"));

        Window window = getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(request -> window.hide());

        rebuildButtons(year, month, day, notes, notesAmount);
    }

    /**
     * Обновляет диалог для нужных данных.
     * @param year год
     * @param month месяц
     * @param day день
     * @param notes список записей
     * @param notesAmount надпись, на которой отображается количество записей
     */
    private void rebuildButtons(int year, int month, int day, List<Note> notes, Text notesAmount) {
        VBox vBox = new VBox(8);
        vBox.setAlignment(Pos.BOTTOM_CENTER);

        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);

            Button noteButton = new Button((i + 1) + ") " + note.getText());
            noteButton.setPrefSize(240f, 24f);
            noteButton.setOnMouseClicked(event -> {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Информация о заметке");
                dialog.setHeaderText("Заметка:");

                Window window = dialog.getDialogPane().getScene().getWindow();
                window.setOnCloseRequest(request -> window.hide());

                VBox vBox1 = new VBox(16f);
                HBox hBox1 = new HBox(8f);

                Button deleteButton = new Button("Удалить");
                deleteButton.setPrefSize(120f, 24f);
                deleteButton.setOnMouseClicked(ignored -> {
                    NotesDatabase.removeNote(year, month, day, note);

                    notesAmount.setText(notes.isEmpty() ? "" : "Заметок: " + notes.size());
                    rebuildButtons(year, month, day, notes, notesAmount);

                    dialog.getDialogPane().getScene().getWindow().hide();
                });

                Button closeButton = new Button("Закрыть");
                closeButton.setPrefSize(120f, 24f);
                closeButton.setOnMouseClicked(ignored -> dialog.getDialogPane().getScene().getWindow().hide());

                hBox1.getChildren().add(deleteButton);
                hBox1.getChildren().add(closeButton);

                vBox1.getChildren().add(new Text("ID: " + (note.getId() + 1) + "\nДата: " + year + "/" + month + "/" + day + "\nНомер: " + (notes.indexOf(note) + 1) + "\nТекст: " + note.getText()));
                vBox1.getChildren().add(hBox1);

                dialog.getDialogPane().setContent(vBox1);
                dialog.showAndWait();
            });

            vBox.getChildren().add(noteButton);
        }

        HBox hBox = new HBox(8);

        Button addButton = new Button("Добавить...");
        addButton.setPrefSize(116f, 24f);
        addButton.setOnMouseClicked(event -> {
            TextInputDialog input = new TextInputDialog();
            input.setTitle("Новая заметка...");
            input.setHeaderText("Введите текст для заметки");

            Optional<String> noteText = input.showAndWait();
            noteText.ifPresent(text -> {
                Note note = new Note(text);
                NotesDatabase.addNote(year, month, day, note);

                notesAmount.setText(notes.isEmpty() ? "" : "Заметок: " + notes.size());
                rebuildButtons(year, month, day, notes, notesAmount);
            });
        });

        Button clearAllButton = new Button("Очистить все");
        clearAllButton.setPrefSize(116f, 24f);
        clearAllButton.setOnMouseClicked(event -> {
            NotesDatabase.removeNotes(year, month, day);

            notesAmount.setText(notes.isEmpty() ? "" : "Заметок: " + notes.size());
            rebuildButtons(year, month, day, notes, notesAmount);
        });

        hBox.getChildren().add(addButton);
        hBox.getChildren().add(clearAllButton);
        vBox.getChildren().add(hBox);

        getDialogPane().setContent(vBox);
        setHeight(notes.size() * 34 + 160);
    }
}
