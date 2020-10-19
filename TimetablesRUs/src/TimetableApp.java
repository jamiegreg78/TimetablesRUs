

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * The timetable system
 *
 * @author Chris Loftus
 * @version 1 (27th February 2020)
 */
public class TimetableApp {

    private String filename;
    private Scanner scan;
    private Timetable timetable;

    private TimetableApp() {
        scan = new Scanner(System.in);
        System.out.print("Please enter the filename of timetable information: ");
        filename = scan.next();

        timetable = new Timetable();
    }

    /*
     * initialise() method runs from the main and reads from a file
     */
    private void initialise() {
        System.out.println("Using file " + filename);

        try {
            timetable.load(filename);
        } catch (FileNotFoundException e) {
            System.err.println("The file: " + " does not exist. Assuming first use and an empty file." +
                    " If this is not the first use then have you accidentally deleted the file?");
        } catch (IOException e) {
            System.err.println("An unexpected error occurred when trying to open the file " + filename);
            System.err.println(e.getMessage());
        }
    }

    /*
     * runMenu() method runs from the main and allows entry of data etc
     */
    private void runMenu() {
        String response;
        do {
            printMenu();
            System.out.println("What would you like to do:");
            scan = new Scanner(System.in);
            response = scan.nextLine().toUpperCase();
            switch (response) {
                case "1":
                    addEvent();
                    break;
                case "2":
                    searchForEvent();
                    break;
                case "3":
                    removeEvent();
                    break;
                case "4":
                    addVenue();
                    break;
                case "5":
                    printAll();
                    break;
                case "Q":
                    break;
                default:
                    System.out.println("Try again");
            }
        } while (!(response.equals("Q")));
    }

    private void printMenu() {
        System.out.println("1 -  add a new event");
        System.out.println("2 -  search for a booked event");
        System.out.println("3 -  remove a timetable event");
        System.out.println("4 -  add a venue");
        System.out.println("5 -  display everything");
        System.out.println("q -  Quit");
    }

    private void addEvent() {
        String choice;
        System.out.println("Meeting or Lecture?(M/L)");
        choice = scan.nextLine().toUpperCase();
        if (choice.equals("M")) {
            addMeeting();
        } else if (choice.equals("L")) {
            addLecture();
        }
    }

    private void addMeeting() {
        System.out.println("Enter meeting name: ");
        String meetingName = scan.nextLine();

        System.out.println("Enter organiser name: ");
        String organiser = scan.nextLine();

        MeetingType meetingType = getMeetingType();

        Meeting meeting = new Meeting();

        meeting.setMeetingName(meetingName);
        meeting.setOrganiser(organiser);
        meeting.setMeetingType(meetingType);

        populateAndAddMeetingToTimetable(meeting);
    }

    private void searchForEvent() {
        try {
            System.out.println("Enter unique meeting ID?");
            int meetingId = scan.nextInt();


            scan.nextLine();

            Event event = timetable.searchForEvent(meetingId);
            if (event != null) {
                System.out.println(event);
            } else {
                System.out.println("Could not find booked timetable meeting: " + meetingId);
            }

        } catch (InputMismatchException exception) {
            System.out.println("This is not an integer");
        }

    }

    private void addLecture() {
        System.out.println("Enter module code: ");
        String moduleCode = scan.nextLine();

        System.out.println("Enter lecturer name: ");
        String lecturerName = scan.nextLine();

        System.out.println("Set lecturer number");
        int lecturerNumber = scan.nextInt();
        Lecture lecture = new Lecture();

        lecture.setModuleCode(moduleCode);
        lecture.setLecturerName(lecturerName);
        lecture.setLecturerNumber(lecturerNumber);

        populateAndAddLectureToTimetable(lecture);
    }

    private void removeEvent() {
        try {
            System.out.println("Which booked event do you want to remove? Enter its ID: ");
            int eventId = scan.nextInt();
            scan.nextLine();
            boolean removed = timetable.removeEvent(eventId);
            if (removed) {
                System.out.println("Removed event " + eventId);
            } else {
                System.out.println("Unable to find event " + eventId);
            }
        } catch (InputMismatchException exception) {
            System.out.println("This is not an integer");
        }
    }

    private void addVenue() {
        Venue venue;
        String venueName;

        while (true) {
            System.out.println("Enter the venue name");
            venueName = scan.nextLine();
            venue = timetable.searchForVenue(venueName);
            if (venue != null) {
                System.out.println("This venue already exists. Give it a different name");
            } else {
                break;
            }
        }

        System.out.println("Does it have a data projector?(Y/N)");
        String answer = scan.nextLine().toUpperCase();
        boolean hasDataProjector = answer.equals("Y");

        System.out.println("Does it have adjustable seating?(Y/N)");
        String seatingAnswer = scan.nextLine().toUpperCase();
        boolean hasAdjustibleSeating = answer.equals("Y");

        venue = new Venue(venueName);
        venue.setHasDataProjector(hasDataProjector);
        venue.setHasAdjustibleSeating(hasAdjustibleSeating);

        timetable.add(venue);
    }

    private void printAll() {
        timetable.sortVenues();

        timetable.sortEvents();

        System.out.println(timetable.toString());
    }

    private void populateAndAddMeetingToTimetable(Meeting meeting) {
        try {
            System.out.println("Enter the unique timetable meeting identifier: (unique number)");
            int meetingId = scan.nextInt();
            scan.nextLine();
            inputStartEndTime(meeting);

            System.out.println("Is a data projector required?(Y/N)");
            String answer = scan.nextLine().toUpperCase();
            boolean projectorRequired = true;
            if (answer.equals("N")) {
                projectorRequired = false;
            }

            Venue venue = null;
            while (true) {
                System.out.println("Enter venue name");
                String venueName = scan.nextLine();
                venue = timetable.searchForVenue(venueName);

                if (venue != null) {
                    if (projectorRequired && !venue.hasDataProjector()) {
                        System.out.println("Selected venue does not have a data projector. Choose a different venue");
                    } else {
                        meeting.setEventId(meetingId);
                        meeting.setDataProjectorRequired(projectorRequired);

                        meeting.setVenue(venue);

                        timetable.add(meeting);
                        break; // out of the loop
                    }
                } else {
                    System.out.println("Venue " + venue + " does not exist. Try a different venue? (Y/N)");
                    answer = scan.nextLine().toUpperCase();
                    if (!answer.equals("Y")) break; // if not Y then break out of the loop
                }
            }
        } catch (InputMismatchException exception) {
            System.out.println("Incorrect data type");
        }

    }

    private void populateAndAddLectureToTimetable(Lecture lecture) {
        try {
            System.out.println("Enter the unique timetable meeting identifier: (unique number)");
            int meetingId = scan.nextInt();
            scan.nextLine();
            inputStartEndTime(lecture);

            System.out.println("Is adjustable seating required?(Y/N)");
            String answer = scan.nextLine().toUpperCase();
            boolean adjustibleSeatingRequired = true;
            if (answer.equals("N")) {
                adjustibleSeatingRequired = false;
            }

            Venue venue = null;
            while (true) {
                System.out.println("Enter venue name");
                String venueName = scan.nextLine();
                venue = timetable.searchForVenue(venueName);

                if (venue != null) {
                    if (adjustibleSeatingRequired && !venue.HasAdjustableSeating()) {
                        System.out.println("Selected venue does not have adjustable seating. Choose a different venue");
                    } else {
                        lecture.setEventId(meetingId);
                        lecture.setAdjustibleSeating(adjustibleSeatingRequired);

                        lecture.setVenue(venue);

                        timetable.add(lecture);
                        break; // out of the loop
                    }
                } else {
                    System.out.println("Venue " + venue + " does not exist. Try a different venue? (Y/N)");
                    answer = scan.nextLine().toUpperCase();
                    if (!answer.equals("Y")) break; // if not Y then break out of the loop
                }
            }
        } catch (InputMismatchException exception) {
            System.out.println("Incorrect data type");
        }

    }

    private void inputStartEndTime(Event event) {
        while (true) {
            System.out.println("Enter start time for timetable event");
            LocalDateTime startTime = getDateTime();
            System.out.println("Enter end time for timetable event");
            LocalDateTime endTime = getDateTime();
            if (startTime.compareTo(endTime) < 0) {
                event.setStartAndEndTime(startTime, endTime);
                break;
            } else {
                System.out.println("Start time: " + startTime + " must be before end time: " + endTime);
            }
        }
    }

    private LocalDateTime getDateTime() {
        LocalDateTime result = null;
        while (true) {
            try {
                System.out.println("On one line (numbers): year month day hour minutes");

                // Note that an InputMismatchException is thrown if an
                // illegal value is entered. For simplicity, we will pretend that won't happen.
                // STUDENTS MUST HANDLE THIS SITUATION.
                int year = scan.nextInt();
                int month = scan.nextInt();
                int day = scan.nextInt();
                int hour = scan.nextInt();
                int minutes = scan.nextInt();
                scan.nextLine(); // Clear the end of line character

                result = LocalDateTime.of(year, month, day, hour, minutes);
                break; // break out of the loop
            } catch (DateTimeException dte) {
                System.out.println("Try again. " + dte.getMessage());
            } catch (InputMismatchException ime) {
                System.out.println("Use integers");
                scan.next();
            }
        }

        System.out.println("The date/time you entered was: " + result);
        return result;
    }

    private MeetingType getMeetingType() {
        MeetingType meetingType = MeetingType.OTHER; // Make the default
        System.out.println("Meeting type, enter the number (1 - staff meeting \n 2 - learning and teaching meeting \n 3 - subject panel meeting \n 4 - other kind of meeting");
        String answer = scan.nextLine();
        switch (answer) {
            case "1":
                meetingType = MeetingType.STAFF;
                break;
            case "2":
                meetingType = MeetingType.LTC;
                break;
            case "3":
                meetingType = MeetingType.SUBJECT_PANEL;
                break;
        }

        System.out.println("Meeting type selected: " + meetingType);
        return meetingType;
    }

    private void save() {
        try {
            timetable.save(filename);
        } catch (IOException e) {
            System.err.println("Problem when trying to write to file: " + filename);
        }
    }


    // /////////////////////////////////////////////////
    public static void main(String[] args) {
        System.out.println("**********HELLO***********");

        TimetableApp app = new TimetableApp();
        app.initialise();
        app.runMenu();
        app.printAll();

        app.save();

        System.out.println("***********GOODBYE**********");
    }
}
