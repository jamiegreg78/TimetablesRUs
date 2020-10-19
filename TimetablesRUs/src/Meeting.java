import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents a meeting
 * @author Chris Loftus
 * @version 1 (27th February 2020)
 */
public class Meeting extends Event {
    private String meetingName;
    private String organiser;
    private MeetingType meetingType;

    /**
     * Creates a blank meeting
     */
    public Meeting(){
        meetingName = "none";
    }


    /**
     * Build a meeting with given values.
     * @param id Unique ID for the meeting
     * @param meetingName The name of the meeting
     * @param organiser Who organised the meeting
     * @param meetingType The kind of meeting
     */
    public Meeting(int id, String meetingName, String organiser, MeetingType meetingType){
        super(id);
        this.meetingName = meetingName;
        this.meetingType = meetingType;
        this.organiser = organiser;
    }

    /**
     * sets the meeting name
     * @param meetingName desired name
     */
    public void setMeetingName(String meetingName){
        this.meetingName = meetingName;
    }

    /**
     * sets the organiser of the event
     * @param organiser desired organiser
     */
    public void setOrganiser(String organiser){
        this.organiser = organiser;
    }

    /**
     * sets the meeting type
     * @param meetingType one of four types - {STAFF, LTC, SUBJECT_PANEL, OTHER}
     */
    public void setMeetingType(MeetingType meetingType){
        this.meetingType = meetingType;
    }

    /**
     * gets the name of the current meeting
     * @return meeting name
     */
    public String getMeetingName(){
        return meetingName;
    }

    /**
     * gets the organiser of the meeting
     * @return the meeting organiser
     */
    public String getOrganiser(){
        return organiser;
    }


    /**
     * returns a hashcode for venue, starttime and endtime
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(venue, startTime, endTime);
    }

    /**
     * Loads meeting data from the given text file
     * @param infile An open scanner on the text file
     * @exception IllegalArgumentException thrown if infile is null
     */
    public void load(Scanner infile) throws IllegalArgumentException{
        if (infile == null) {
            throw new IllegalArgumentException("infile must not be null");
        }
        super.eventId = infile.nextInt();
        startTime = readDateTime(infile);
        endTime = readDateTime(infile);

        super.projectorNeeded = infile.nextBoolean();

        meetingName = infile.next();
        organiser = infile.next();
        String meetingTypeStr = infile.next();
        meetingType = MeetingType.valueOf(meetingTypeStr);
    }

    /**
     * Saves meeting data to the given text file
     * @param outfile An open scanner on the text file
     * @exception IllegalArgumentException thrown if outfile is null
     */
    @Override
    public void save(PrintWriter outfile) {
        if (outfile == null) {
            throw new IllegalArgumentException("outfile must not be null");
        }
        outfile.println("meeting");
        outfile.println(super.eventId);
        writeDateTime(outfile, startTime);
        writeDateTime(outfile, endTime);

        outfile.println(super.projectorNeeded);
        outfile.println(meetingName);
        outfile.println(organiser);
        outfile.println(meetingType);
    }

    /**
     * gives the meeting information
     * @return a string containing all important meeting information
     */
    @Override
    public String toString() {
        return "Meeting{" +
                "meetingName='" + meetingName + '\'' +
                ", organiser='" + organiser + '\'' +
                ", meetingType=" + meetingType +
                ", meetingId=" + super.eventId +
                ", requiresDataProjector=" + super.projectorNeeded +
                ", venue=" + venue +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }


}
