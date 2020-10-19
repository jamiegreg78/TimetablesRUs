import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.time.LocalDateTime;

/**
 * Represents a timetable. Keeps track of timetable meetings and possible venues
 *
 * @author Chris Loftus
 * @version 1 (27th February 2020)
 */
public class Timetable {

    private ArrayList<Event> events;
    private ArrayList<Venue> venues;

    /**
     * Initialises the timetable meetings and venues
     */
    public Timetable() {
        events = new ArrayList<>();
        venues = new ArrayList<>();
    }

    /**
     * Add a timetabled meeting.
     *
     * @param event A non-null, unique meeting object.
     * @return true if meeting added else false if the meeting already exists
     * @throws IllegalArgumentException is thrown if meeting is null
     */
    public boolean add(Event event) throws IllegalArgumentException {
        boolean success = false;
        if (event == null) {
            throw new IllegalArgumentException("The meeting must not be null");
        }
        if (!events.contains(event)) {
            events.add(event);
            success = true;
        }
        return success;
    }

    /**
     * Add a venue to the timetable system.
     * @param venue A non-null, unique venue object.
     * @return true if venue added else false if the venue already exists
     * @throws IllegalArgumentException is thrown if venue is null
     */
    public boolean add(Venue venue) {
        boolean success = false;
        if (venue == null) {
            throw new IllegalArgumentException("The venue must not be null");
        }
        if (!venues.contains(venue)) {
            venues.add(venue);
            success = true;
        }
        return success;
    }

    /**
     * Removes the meeting from the timetable
     * @param eventId The ID of timetabled meeting to be removed
     * @return true if removed else false if not found
     */
    public boolean removeEvent(int eventId) {
        // Search for the Event by name
        Event which = null;
        for (Event e : events) {
            if (e.eventId == eventId) {
                which = e;
                break;
            }
        }
        if (which != null) {
            events.remove(which);
            return true;
        } else {
            return false;
        }
    }


    /**
     * sorts the events by starting time, and if needed, by venue name
     */
    public void sortEvents(){
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                LocalDateTime e1Date = ((Event)o1).getStartTime();
                LocalDateTime e2Date = ((Event)o2).getStartTime();
                int dComparison = e1Date.compareTo(e2Date);

                if (dComparison != 0) {
                    return dComparison;
                }

                String e1Venue = ((Event)o1).getVenue().getName();
                String e2Venue = ((Event)o2).getVenue().getName();
                return e1Venue.compareTo(e2Venue);
            }
        });
    }

    /**
     * Obtains a copy of the timetabled meetings
     * @return A copy of the timetabled meetings
     */
    public Event[] obtainAllEvents() {
        Event[] result = new Meeting[events.size()];
        result = events.toArray(result);
        return result;
    }

    /**
     * Sorts the venues by their name, using the .equals() method
     */
    public void sortVenues(){
        Collections.sort(venues);
    }

    /**
     * Obtains a copy of the timetable system venues
     * @return A copy of the timetable system venues
     */
    public Venue[] obtainAllVenues() {
        Venue[] result = new Venue[venues.size()];
        result = venues.toArray(result);
        return result;
    }

    /**
     * Searches for a given timetabled meeting
     * @param eventId the meeting to search for
     * @return The found meeting or else null if not found
     */
    public Event searchForEvent(int eventId) {
        Event result = null;
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).eventId == eventId) {
                result = events.get(i);
            }
        }
        return result;
    }


    /**
     * Searches for the given venue
     * @param name The name of the venue. Must not be null.
     * @return The venue if found else null
     */
    public Venue searchForVenue(String name) {
        Venue result = null;
        for (int i = 0; i < venues.size(); i++) {
            if (venues.get(i).getName().equals(name)) {
                result = venues.get(i);
                return result;
            }
        }
        return result;
    }

    /**
     * Generates a Stringbuilder object with the information from the venues and events. Returns the toString() of the object
     * @return Stringbuilder string
     */
    @Override
    public String toString() {

        StringBuilder output = new StringBuilder("Venues in timetable system are: ");
        output.append("\n");
        for (Venue venue : venues) {
            output.append(venue);
            output.append("\n");
        }
        output.append("\n");

        output.append("events in timetable are: ");
        output.append("\n");
        for (Event event : events) {
            output.append(event);
            output.append("\n");
        }
        output.append("\n");

        return output.toString();

    }

    /**
     * Loads the timetable data from the given file
     * @param filename The text file. Must exist.
     * @throws FileNotFoundException thrown if the file does not exist
     * @throws IOException thrown if some other kind of IO error occurs
     */
    public void load(String filename) throws FileNotFoundException, IOException {
        // Using try-with-resource (see my slides from workshop 15)
        try (FileReader fr = new FileReader(filename);
             BufferedReader br = new BufferedReader(fr);
             Scanner infile = new Scanner(br)) {

            events.clear();
            venues.clear();

            infile.useDelimiter("\r?\n|\r"); // End of line characters on Unix or DOS or others OSs

            // Read in the venues first
            Meeting meeting = null;
            Lecture lecture = null;
            Venue venue = null;
            int numVenues = infile.nextInt();

            for (int i = 0; i < numVenues; i++) {
                String venueName = infile.next();
                boolean hasDataProjector = infile.nextBoolean();
                boolean hasAdjustibleSeating = infile.nextBoolean();

                venue = new Venue(venueName);
                venue.setHasDataProjector(hasDataProjector);
                venue.setHasAdjustibleSeating(hasAdjustibleSeating);
                venues.add(venue);
            }

            while (infile.hasNext()) {
                String type = infile.next();
                if (type.equals("lecture")){
                    lecture = new Lecture();

                    lecture.load(infile);
                } else if (type.equals("meeting")){
                    meeting = new Meeting();

                    meeting.load(infile);
                } else {
                    System.out.println(type);
                }

                // Read the venue data
                String venueName = infile.next();
                Venue theVenue = searchForVenue(venueName);
                if (type.equals("lecture")){
                    lecture.setVenue(theVenue);
                    events.add(lecture);
                } else if (type.equals("meeting")) {
                    meeting.setVenue(theVenue);
                    events.add(meeting);
                }




            }
        }
    }


    /**
     * Saves the timetabled data to a text file
     * @param outfileName The file. Will create a new file if it does not exist. Will overwrite an
     *                    existing file.
     * @throws IOException Thrown if some IO problem occurs.
     */

    public void save(String outfileName) throws IOException {
        try (FileWriter fw = new FileWriter(outfileName);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter outfile = new PrintWriter(bw);) {

            // Start by saving the list of allowable venues.
            // We save the number of venues first so that later on
            // we know how many to read in
            outfile.println(venues.size());
            Venue venue;
            for (int i = 0; i < venues.size(); i++) {
                venue = venues.get(i);
                outfile.println(venue.getName());
                outfile.println(venue.hasDataProjector());
                outfile.println(venue.HasAdjustableSeating());
            }

            Event event;
            for (int pos = 0; pos < events.size(); pos++) {
                event = events.get(pos);
                event.save(outfile);

                venue = event.getVenue();
                outfile.print(venue.getName());

                // Only print a newline if we're not at the end of the list. Don't want a trailing blank line
                if (pos < events.size() - 1) {
                    outfile.println();
                }
            }
        }
    }


}
