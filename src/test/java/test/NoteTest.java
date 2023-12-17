package test;

import calendar.notes.Note;
import org.junit.*;

import static org.junit.Assert.*;

public class NoteTest {
    private final String text = "Абсолютно уникальный текст записи!";
    private Note note;

    @Before
    public void setUp() {
        note = new Note(text);
    }

    @Test
    public void testGetText() {
        assertEquals(text, note.getText());
    }
}
