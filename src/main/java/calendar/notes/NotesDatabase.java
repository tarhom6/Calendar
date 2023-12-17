package calendar.notes;

import java.io.*;
import java.util.*;

import static calendar.MainApplication.*;

/**
 * Класс для сохранения и загрузки записей.
 */
public class NotesDatabase {
    /**
     * Файл, в котором хранятся все записи
     */
    private static final File notesFile = new File("notes.bin");

    /**
     * Загруженный кэш всех записей
     */
    private static HashMap<String, List<Note>> notes = new HashMap<>();

    /**
     * @param year год
     * @param month месяц
     * @param day день
     * @return список всех записей для конкретного дня конкретного месяца конкретного года
     */
    public static List<Note> getNotes(int year, int month, int day) {
        notes.putIfAbsent(dateToString(year, month, day), new ArrayList<>());
        return notes.get(dateToString(year, month, day));
    }

    /**
     * Добавляет запись для выбранной даты
     * @param year год
     * @param month месяц
     * @param day день
     * @param note запись, которую надо добавить
     */
    public static void addNote(int year, int month, int day, Note note) {
        try {
            notes.putIfAbsent(dateToString(year, month, day), new ArrayList<>());
            notes.get(dateToString(year, month, day)).add(note);

            logger.info("Added a note, Date: {}/{}/{}, ID: {}, text: {}", year, month, day, note.getId(), note.getText());
        } finally {
            save();
        }
    }

    /**
     * Удаляет запись для выбранной даты
     * @param year год
     * @param month месяц
     * @param day день
     * @param note запись, которую надо удалить
     */
    public static void removeNote(int year, int month, int day, Note note) {
        try {
            notes.getOrDefault(dateToString(year, month, day), new ArrayList<>()).remove(note);
            logger.info("Removed a note, Date: {}/{}/{}, ID: {}, text: {}", year, month, day, note.getId(), note.getText());
        } finally {
            save();
        }
    }

    /**
     * Удаляет все записи для выбранной даты
     * @param year год
     * @param month месяц
     * @param day день
     */
    public static void removeNotes(int year, int month, int day) {
        try {
            notes.getOrDefault(dateToString(year, month, day), new ArrayList<>()).clear();
            logger.info("Removed all notes, Date: {}/{}/{}", year, month, day);
        } finally {
            save();
        }
    }

    /**
     * Загружает список записей из файла
     */
    @SuppressWarnings("unchecked")
    public static void load() {
        if (!notesFile.exists()) {
            logger.warn("Notes file not found. Notes will NOT be loaded!");
            return;
        }

        try (FileInputStream fileInput = new FileInputStream(notesFile); ObjectInputStream objectInput = new ObjectInputStream(fileInput)) {
            notes = (HashMap<String, List<Note>>) objectInput.readObject();
            Note.lastNoteID = objectInput.readInt();

            logger.info("Successfully loaded the notes for {} days!", notes.size());
        } catch (Exception e) {
            logger.error("Failed to load the notes. Error: {}", e.getMessage());
        }
    }

    /**
     * Сохраняет список записей в файл
     */
    public static void save() {
        try (FileOutputStream fileOutput = new FileOutputStream(notesFile); ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput)) {
            objectOutput.writeObject(notes);
            objectOutput.writeInt(Note.lastNoteID);

            logger.info("Successfully saved the notes for {} days!", notes.size());
        } catch (Exception e) {
            logger.error("Failed to save the notes. Error: {}", e.getMessage());
        }
    }

    /**
     * @param year год
     * @param month месяц
     * @param day день
     * @return строковое представление переданной даты
     */
    private static String dateToString(int year, int month, int day) {
        return year + "/" + month + "/" + day;
    }
}
