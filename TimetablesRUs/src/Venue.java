import java.util.Objects;
/**
 * Represents a venue with a name and possible data projector facilities
 * @author Chris Loftus
 * @version 1 (27th February 2020)
 */
public class Venue implements Comparable<Venue>{
    private String name;
    private boolean hasDataProjector;
    private boolean hasAdjustibleSeating;

    /**
     * Creates an empty venue
     */
    public Venue(){}

    /**
     * Creates a venue with the desired name
     * @param name desired name
     */
    public Venue(String name){
        this.name = name;
    }

    /**
     * Gets the venue name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * sets the venue name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns whether it has a projector
     * @return hasDataProjector
     */
    public boolean hasDataProjector() {
        return hasDataProjector;
    }

    /**
     * sets whether it has a projector
     * @param hasDataProjector true or false
     */
    public void setHasDataProjector(boolean hasDataProjector) {
        this.hasDataProjector = hasDataProjector;
    }

    /**
     * returns whether it has adjustible seating
     * @return true or false
     */
    public boolean HasAdjustableSeating() {
        return hasAdjustibleSeating;
    }

    /**
     * set whether it has adjustible seating
     * @param adjustibleSeating true or false
     */
    public void setHasAdjustibleSeating(boolean adjustibleSeating) {
        hasAdjustibleSeating = adjustibleSeating;
    }

    /**
     * custom equals method using the venue name
     * @param o another object (venue)
     * @return true if object names match
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return name.equals(venue.name);
    }

    /**
     * returns a hashcode
     * @return a hashcode of name
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Builds a string of the venue information
     * @return contains all important venue information
     */
    @Override
    public String toString() {
        return "Venue{" +
                "name='" + name + '\'' +
                ", hasDataProjector=" + hasDataProjector +
                ", hasAdjustibleSeating=" + hasAdjustibleSeating +
                '}';
    }

    /**
     * compareTo method used when comparing venues
     * @param other
     * @return
     */
    @Override
    public int compareTo(Venue other) {
        return this.name.compareTo(other.name);
    }
}
