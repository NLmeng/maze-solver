package gui_test.model;

import gui.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


/**
 * Unit tests for the Event class
 */
public class EventTest {
	private Event e;
	private Date d;

	
	@BeforeEach
	public void runBefore() {
		this.e = new Event("Game");
		this.d = Calendar.getInstance().getTime();
	}
	
	@Test
	public void testEvent() {
		assertEquals("Game", this.e.getDescription());
		assertEquals(this.d, this.e.getDate());
	}

    @Test
    public void testEquals() {
        assertNotEquals(this.e, null);
        assertNotEquals(this.e, 42);
        assertNotEquals(0, this.e.hashCode());
    }

	@Test
	public void testToString() {
		assertEquals(this.d.toString() + "\n" + "Game", this.e.toString());
	}
}
