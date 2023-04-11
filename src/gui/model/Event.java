package gui.model;

import java.util.Calendar;
import java.util.Date;


/**
 * Represents the game's event.
 */
public class Event {
    private static final int HASH_CONSTANT = 13;
    private Date dateLogged;
    private String description;

    /**
     * Creates an event with the given description
     * and the current date/time stamp.
     *
     * @param description a description of the event
     */
    public Event(String description) {
        this.dateLogged = Calendar.getInstance().getTime();
        this.description = description;
    }

    /**
     * Gets the date of this event (includes time).
     *
     * @return the date of the event
     */
    public Date getDate() {
        return this.dateLogged;
    }

    /**
     * Gets the description of this event.
     *
     * @return the description of the event
     */
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other.getClass() != this.getClass()) {
            return false;
        }

        Event otherEvent = (Event) other;

        return (this.dateLogged.equals(otherEvent.dateLogged)
                && this.description.equals(otherEvent.description));
    }

    @Override
    public int hashCode() {
        return (HASH_CONSTANT * this.dateLogged.hashCode() + this.description.hashCode());
    }

    @Override
    public String toString() {
        return this.dateLogged.toString() + "\n" + this.description;
    }
}
