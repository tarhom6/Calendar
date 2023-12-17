package calendar.notes;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс записи. Содержит текст и уникальный ID.
 */
public class Note implements Serializable {
    /**
     * ID последней созданной записи. Автоматически увеличивается.
     */
    public static int lastNoteID = 0;

    /**
     * ID этой записи
     */
    private final int id;

    /**
     * Текст этой записи
     */
    private final String text;

    /**
     * Конструктор записи. Автоматически выбирает ID.
     * @param text текст записи
     */
    public Note(String text) {
        this.id = lastNoteID++;
        this.text = text;
    }

    /**
     * @return ID этой записи
     */
    public int getId() {
        return id;
    }

    /**
     * @return текст этой записи
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "CalendarNote{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof Note note && (id == note.id && Objects.equals(text, note.text));
    }
}