import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Represents events {Meetings and Lectures}
 * @author Jamie Gregory
 * @version 1
 */

public class Event implements Comparable<Event>{
    int eventId;
    Venue venue;
    LocalDateTime startTime;
    LocalDateTime endTime;
    boolean projectorNeeded;
    boolean adjustableSeatingRequired;


    Event() {
    }

    /**
     * Creates an event with an ID
     * @param id
     */
    Event(int id) {
        eventId = id;
    }

    /**
     * Sets the ID of the event
     * @param id the desired ID
     */
    public void setEventId(int id) {
        eventId = id;
    }

    /**
     * Returns the ID
     * @return eventid
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Sets the venue of the event
     * @param ven desired venue
     */
    public void setVenue(Venue ven) {
        // Only allow this if the venue spec matches the
        // the meeting requirement
        if (projectorNeeded && !ven.hasDataProjector() || adjustableSeatingRequired && !ven.HasAdjustableSeating()) {
            System.err.println("Meeting requires a data projector or adjustible seating. " +
                    "Venue " + ven.getName() + " isn't compatible");
        } else {
            this.venue = ven;
        }
    }

    /**
     * Returns the current venue
     * @return venue
     */
    public Venue getVenue() {
        return venue;
    }

    /**
     * returns whether a data projector is required
     * @return projectorneeded
     */
    public boolean isDataProjectorRequired(){
        return projectorNeeded;
    }

    /**
     * sets the dataProjectorRequired variable, as long as the current venue fits the requirements
     * @param dataProjectorRequired is a projector needed?
     */
    public void setDataProjectorRequired(boolean dataProjectorRequired) {
        if (venue != null && (dataProjectorRequired && !venue.hasDataProjector())){
            System.err.println("Event currently has a venue " +
                    venue.getName() + " that does not have a data projector. Change the venue first");
        } else {
            this.projectorNeeded = dataProjectorRequired;
        }
    }

    /**
     *
     * @param startTime the starting time of the event
     * @param endTime the ending time of the event
     * @throws IllegalArgumentException thrown if time is out of bounds
     */
    public void setStartAndEndTime(LocalDateTime startTime, LocalDateTime endTime) throws IllegalArgumentException {
        if (startTime.compareTo(endTime) >= 0){
            throw new IllegalArgumentException("start time: " + startTime + " must be before end time: " + endTime);
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * returns the starting time of an event
     * @return starting time
     */
    public LocalDateTime getStartTime(){
        return startTime;
    }

    /**
     * returns the ending time of an event
     * @return ending time
     */
    public LocalDateTime getEndTime(){
        return endTime;
    }

    /**
     * reads and handles the dateTime from a text file
     * @param infile the input file
     * @return the information parsed from the file
     */
    LocalDateTime readDateTime(Scanner infile){
        String dateTime = infile.next();
        return LocalDateTime.parse(dateTime);
    }

    /**
     * prints the dateTime information to text file
     * @param outfile output file
     * @param dateTime a dateTime object
     */
    void writeDateTime(PrintWriter outfile, LocalDateTime dateTime){
        outfile.println(dateTime);
    }

    /**
     * Empty save method - overridden in meeting and lecture
     * @param outfile the output file
     */
    public void save(PrintWriter outfile){
    }

    /**
     * compares two events by starting time
     * @param other the event to be compared with
     * @return the result of the comparison
     */
    public int compareTo(Event other) {
        return startTime.compareTo(other.startTime);
    }


    /**
     * determines if an object is the same via the event id
     * @param o an input object
     * @return true if the event id is the same
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return eventId == event.eventId;
    }




}