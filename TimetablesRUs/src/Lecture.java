import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Represents Lectures
 * @author Jamie Gregory
 * @version 1
 */


public class Lecture extends Event{

    private String moduleCode;
    private String lecturerName;
    private int lecturerNumber;

    public Lecture() {
    }

    /**
     * Builds a lecture object from input information
     * @param id Unique id for the lecture
     * @param lecturer Lecturer name
     * @param number Lecturer number
     * @param adjustibleNeeded Is adjustible seating required
     */
    public Lecture(int id, String module,  String lecturer, int number, boolean adjustibleNeeded) {
        super(id);
        this.moduleCode = module;
        this.lecturerName = lecturer;
        this.lecturerNumber = number;
        adjustableSeatingRequired = adjustibleNeeded;
    }

    /**
     * sets the module code
     * @param module desired module code
     */
    public void setModuleCode(String module) {
        this.moduleCode = module;
    }

    /**
     * sets the name of the lecturer
     * @param name desired name
     */
    public void setLecturerName(String name) {
        this.lecturerName = name;
    }

    /**
     * sets the lecturer number
     * @param number desired number
     */
    public void setLecturerNumber(int number) {
        this.lecturerNumber = number;
    }

    /**
     * sets whether adjustible seating is needed
     * @param required is adjustible seating needed?
     */
    public void setAdjustibleSeating(boolean required) {
        adjustableSeatingRequired = required;
    }

    /**
     * Saves the lecture information to the given text file
     * @param outfile the output file
     */
    @Override
    public void save(PrintWriter outfile) {
        if (outfile == null) {
            throw new IllegalArgumentException("outfile must not be null");
        }
        outfile.println("lecture");
        outfile.println(super.eventId);
        writeDateTime(outfile, startTime);
        writeDateTime(outfile, endTime);

        outfile.println(super.projectorNeeded);
        outfile.println(moduleCode);
        outfile.println(lecturerName);
        outfile.println(lecturerNumber);
        outfile.println(adjustableSeatingRequired);
    }

    /**
     * loads the lecture information from a given text file
     * @param infile input file
     * @throws IllegalArgumentException thrown if the infile is null
     */
    public void load(Scanner infile) throws IllegalArgumentException{
        if (infile == null) {
            throw new IllegalArgumentException("infile must not be null");
        }
        super.eventId = infile.nextInt();
        startTime = readDateTime(infile);
        endTime = readDateTime(infile);

        super.projectorNeeded = infile.nextBoolean();

        moduleCode = infile.next();
        lecturerName = infile.next();
        lecturerNumber = infile.nextInt();
        adjustableSeatingRequired = infile.nextBoolean();
    }

    /**
     * return the lecture information
     * @return A string containing all important lecture information
     */
    @Override
    public String toString() {
        return "Lecture{" +
                "moduleCode='" + moduleCode + '\'' +
                ", Lecturer='" + lecturerName + '\'' +
                ", meetingId=" + super.eventId +
                ", requiresAdjustibleSeating=" + adjustableSeatingRequired +
                ", venue=" + venue +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }


}
