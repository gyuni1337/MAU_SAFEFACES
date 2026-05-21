package com.safefaces.safefaces.Core.Model;

import java.sql.Time;

/**
 * Represents a reminder with a title, description, and a time interval.
 * Used to store information about scheduled events or tasks.
 *
 * @author Gyundyuz Sadulov
 */
public class Reminder {

    /** The title of the reminder. */
    public String title;

    /** A detailed description of the reminder. */
    public String description;

    /** The start time of the reminder. */
    public Time startTime;

    /** The end time of the reminder. */
    public Time endTime;
}
