package test;

import calendar.notes.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NotesDatabaseTest {
    @Test
    public void testSaveLoad() {
        Note note1 = new Note("Текст 1");
        Note note2 = new Note("Текст 2");

        NotesDatabase.addNote(2023, 11, 11, note1);
        NotesDatabase.addNote(2023, 12, 31, note2);

        NotesDatabase.save();
        NotesDatabase.load();

        List<Note> notes1 = NotesDatabase.getNotes(2023, 11, 11);
        List<Note> notes2 = NotesDatabase.getNotes(2023, 12, 31);

        assertFalse(notes1.isEmpty());
        assertEquals(note1, notes1.get(0));

        assertFalse(notes2.isEmpty());
        assertEquals(note2, notes2.get(0));
    }
}
