package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.ParserException;

import java.io.File;
import java.io.IOException;

/**
 * This class is the main class for Project 2 for 410J.  It takes arguments from the command line, verifies their
 * validity and completeness, and uses them to create objects of the {@link Flight} and {@link Airline} classes.  This
 * class can also take in the name of a text file and write the data passed in to the file if both the name of the
 * airline passed in and the name of the airline in the file match.
 */
public class Project2 {
  private static final int OPTIONS = 4;

  public static void main(String[] args) {
    int argOffset = 0;                    //Variable to account for options when parsing arguments
    int flightNumber;                     //Flight number in integer form (after conversion from string)
    String depart;                        //Combined date and time of departure
    String arrive;                        //Combined date and time of arrival
    String fileName = null;               //Name of file passed in from command line
    boolean toPrint = false;              //Flag to tell the program to print flight data
    Airline airlineFromTextFile = null;   //Airline read in from from text file specified in command line

    //If one of the first 4 (counting -textFile as 2) args is -README, print readme
    for (int i = 0; i < OPTIONS; ++i) {
      if (((args.length >= i + 1) && args[i].equals("-README"))) {
        readme();
      }
    }

    //Checks for minimum number of arguments necessary to run the program
    if (args.length < 8) {
      System.err.println("Missing command line arguments.  " +
              "Make sure there are exactly 8 (in addition to options) when running the program");
      for (String arg : args) {
        System.out.println(arg);
      }
      System.exit(1);
    }

    //Check for option to print flight data
    for (int i = 0; i < OPTIONS; ++i) {
      if (args[i].equals("-print")) {
        toPrint = true;
        argOffset += 1;
      }
    }

    //Check for option to store flight in text file
    for (int i = 0; i < OPTIONS; ++i) {
      if (args[i].equals("-textFile")) {
        if (args[i + 1].contains(".txt") || args[i + 1].contains(".xml")) {
          fileName = args[i + 1];
          argOffset += 2;
        } else {
          System.err.println("File extension not valid for a text file");
          System.exit(7);
        }
      }
    }

    //Checks for maximum number of possible arguments
    if (args.length - argOffset > 8) {
      System.err.println("Too many command line arguments.  " +
              "Make sure there are exactly 8 (in addition to options) when running the program");
      for (String arg : args) {
        System.out.println(arg);
      }
      System.exit(1);
    }

    flightNumber = verifyFlightNumberIsInteger(args[1 + argOffset]);

    isDateLengthLegal(args[3 + argOffset]);
    isDateLengthLegal(args[6 + argOffset]);

    if (!verifyDateFormat(args[3 + argOffset]) || !verifyDateFormat(args[6 + argOffset])) {
      System.err.println("Date not in the correct MM/DD/YYYY format");
      System.exit(4);
    }

    isTimeLengthLegal(args[4 + argOffset]);
    isTimeLengthLegal(args[7 + argOffset]);

    if (!verifyTimeFormat(args[4 + argOffset]) || !verifyTimeFormat(args[7 + argOffset])) {
      System.err.println("Time not in the correct HH:MM format");
      System.exit(5);
    }

    isAirportCodeLegal(args[2 + argOffset]);
    isAirportCodeLegal(args[5 + argOffset]);

    //Combines date and time to create single strings
    depart = args[3 + argOffset] + " " + args[4 + argOffset];
    arrive = args[6 + argOffset] + " " + args[7 + argOffset];

    //Creates new instance of Airline and Flight classes and sets their data to args
    Airline airline = new Airline(args[argOffset]);
    Flight flight = new Flight(flightNumber, args[2 + argOffset], depart, args[5 + argOffset], arrive);
    airline.addFlight(flight);

    //If file given in command line is valid, call the parse method and catch an exception if the file is not
    //properly formatted.  Save the airline info in a variable.
    if (fileName != null) {
      File file = new File(fileName);

      if (file.exists()) {
        TextParser parser = new TextParser(fileName);
        try {
          airlineFromTextFile = (Airline) parser.parse();
        } catch (ParserException e) {
          System.err.println("File not correctly formatted");
          System.exit(8);
        }
      }
    }

    //If the airline info was stored from the file, compare name to name of airline passed in from command line.
    //If equal, add flight from command line to airline and dump back to file.
    if (airlineFromTextFile != null) {
      if (airlineFromTextFile.getName().compareTo(airline.getName()) == 0) {
        airlineFromTextFile.addFlight(flight);
        TextDumper dumper = new TextDumper(fileName);
        try {
          dumper.dump(airlineFromTextFile);
        } catch (IOException e) {
          System.err.println("Can't write to file");
        }
      } else {
        System.err.println("Name of airline from command line does not match name of airline from text file");
        System.exit(9);
      }
    }

    //Prints flight data if option was flagged
    if (toPrint) {
      System.out.println(flight.toString());
      System.exit(0);
    }
  }

  /**
   * Checks if an airport code is 3 chars and all chars are letters
   *
   * @param arg
   *        Argument to be tested
   */
  private static void isAirportCodeLegal(String arg) {
    if (!(arg.length() == 3 && (Character.isLetter(arg.charAt(0)) &&
            Character.isLetter(arg.charAt(1)) && Character.isLetter(arg.charAt(2))))) {
      System.err.println("Airport code must be 3 letters");
      System.exit(6);
    }
  }

  /**
   * Checks if time format has sufficient digits to be valid
   *
   * @param arg
   *        Argument to be tested
   */
  private static void isTimeLengthLegal(String arg) {
    if (arg.length() < 4 || arg.length() > 5) {
      System.err.println("Time not in the correct HH:MM format");
      System.exit(5);
    }
  }

  /**
   * Checks if date format has sufficient digits to be valid
   *
   * @param arg
   *        Argument to be tested
   */
  private static void isDateLengthLegal(String arg) {
    if (arg.length() < 8 || arg.length() > 10) {
      System.err.println("Date not in the correct MM/DD/YYYY format");
      System.exit(3);
    }
  }

  /**
   * Calculates the number of digits in the month for date format.
   * Necessary to calculate the rest of the date accurately.
   *
   * @param arg
   *        Argument to be tested
   * @return
   *        Returns number of digits in month of date.  Returns 0 if number of digits in month is not valid.
   */
  private static int numberOfDigitsInMonth(String arg) {
    if (Character.isDigit(arg.charAt(0)) && arg.charAt(1) == '/') {
      return 1;
    } else if (Character.isDigit(arg.charAt(0)) && Character.isDigit(arg.charAt(1)) &&
            arg.charAt(2) == '/') {
      return 2;
    } else {
      return 0;
    }
  }

  /**
   * Calculates the number of digits in the day for date format.
   * Necessary to calculate the rest of the date accurately.
   *
   * @param arg
   *        Argument to be tested
   * @param digitsInMonth
   *        Number of digits in the month.  Used as offset to calculate number of days in format
   * @return
   *        Returns number of digits in day of date.  Returns 0 if number of digits in day is not valid.
   */
  private static int numberOfDigitsInDay(String arg, int digitsInMonth) {
    if (Character.isDigit(arg.charAt(digitsInMonth + 1)) &&
            arg.charAt(digitsInMonth + 2) == '/') {
      return 1;
    } else if (Character.isDigit(arg.charAt(digitsInMonth + 1)) &&
            Character.isDigit(arg.charAt(digitsInMonth + 2)) &&
            arg.charAt(digitsInMonth + 3) == '/') {
      return 2;
    } else {
      return 0;
    }
  }

  /**
   * Checks if the year in the date format is 4 chars long and all chars are numbers.
   *
   * @param arg
   *        Argument to be tested
   * @param yearStartingPosition
   *        Index of the first number of the year in the date
   * @return
   *        Returns true if year is exactly 4 chars long and all chars are numbers
   */
  private static boolean checkYearFormat(String arg, int yearStartingPosition) {
    boolean yearIsAllNumbers = true;

    for(int i = yearStartingPosition; i < arg.length(); ++i) {
      if (!Character.isDigit(arg.charAt(i))) {
        yearIsAllNumbers = false;
      }
    }

    return ((arg.length() == yearStartingPosition + 4) && yearIsAllNumbers);
  }

  /**
   * Calls the <code>numberOfDigitsInMonth</code>, <code>numberOfDigitsInDay</code>, and <code>checkYearFormat</code>
   * methods to determine if the date format is valid
   *
   * @param arg
   *        Argument to be tested
   * @return
   *        Returns true is date format is valid.  Returns false otherwise.
   */
  private static boolean verifyDateFormat(String arg) {
    int digitsInMonth;
    int digitsInDay;
    int yearStartingPosition;

    digitsInMonth = numberOfDigitsInMonth(arg);
    if (digitsInMonth == 0) {
      return false;
    }

    digitsInDay = numberOfDigitsInDay(arg, digitsInMonth);
    if (digitsInDay == 0) {
      return false;
    }

    yearStartingPosition = digitsInMonth + digitsInDay + 2;
    return checkYearFormat(arg, yearStartingPosition);
  }

  /**
   * Calculates the number of digits in the hour of the time
   *
   * @param arg
   *        Argument to be tested
   * @return
   *        Returns number of digits in the hour of the time.  Returns 0 if number of digits in hours is not valid.
   */
  private static int numberOfDigitsInHour(String arg) {
    if (Character.isDigit(arg.charAt(0)) && arg.charAt(1) == ':') {
      return 1;
    } else if (Character.isDigit(arg.charAt(0)) && Character.isDigit(arg.charAt(1)) && arg.charAt(2) == ':') {
      return 2;
    } else {
      return 0;
    }
  }

  /**
   * Checks if the minutes of the time is 2 digits long
   *
   * @param arg
   *        Argument to be tested
   * @param digitsInHour
   *        Number of digits in the hour of the time
   * @return
   *        Returns true if there are 2 digits in the minutes place.  Returns false otherwise.
   */
  private static boolean verifyNumberOfDigitsInMinutes(String arg, int digitsInHour) {
    return Character.isDigit(arg.charAt(digitsInHour + 1)) &&
            Character.isDigit(arg.charAt(digitsInHour + 2)) &&
            arg.substring(digitsInHour + 1).length() == 2;
  }

  /**
   * Checks if the time is in the correct format
   *
   * @param arg
   *        Argument to be tested
   * @return
   *        Returns true if time is in correct format.  Returns false otherwise.
   */
  private static boolean verifyTimeFormat(String arg) {
    int digitsInHour;

    digitsInHour = numberOfDigitsInHour(arg);
    return digitsInHour != 0 && verifyNumberOfDigitsInMinutes(arg, digitsInHour);
  }

  /**
   * Checks if the flight number passed in is an integer
   *
   * @param arg
   *        Command line argument to be tested
   * @return
   *        Returns flight number as an integer if valid.  Otherwise exits the program with an error message.
   */
  private static int verifyFlightNumberIsInteger(String arg) {
    int flightNumber = 0;

    try {
      flightNumber = Integer.parseInt(arg);
    } catch (NumberFormatException e) {
      System.err.println("Flight number must contain only numbers");
      System.exit(2);
    }

    return flightNumber;
  }

  /**
   * Readme file for the program.  Printed when -README option is used when starting the program from the command line.
   */
  private static void readme() {
    System.out.println("This program creates an airline flight via data passed in from the user on the command line.");
    System.out.println("This can be a flight that has already happened or will happen in the future.");
    System.out.println("The flight data will not be stored after the program terminates, unless a file is specified.");
    System.out.println("If a file is given, but no file exists with that name, one will be created and the flight " +
            "data will be stored there");
    System.out.println("Otherwise, the flight data will be added to the file if the airline names match");
    System.out.println("If they do not match, the flight will not be added to the file and the data will be lost " +
            "when the program terminates.");
    System.out.println("All data must be entered correctly in the correct position of the command line " +
            "or the program will not work.");
    System.out.println("The user will be given an error message if data has been incorrectly entered.");
    System.out.println();
    System.out.println("The command line interface must be as follows (options without [] and arguments without <>):");
    System.out.println("java edu.pdx.cs410J.dc25.Project2 [options] <args>");
    System.out.println("Arguments are (in this order):");
    System.out.println("name            Name of the airline (if more than one word, must be surrounded by quotes)");
    System.out.println("flightNumber    The flight number");
    System.out.println("src             Three letter code of departure airport");
    System.out.println("departDate      Departure date of flight (in MM/DD/YYYY)");
    System.out.println("departTime      Departure time of flight (in HH:MM, 24 hour time)");
    System.out.println("dest            Three letter code of arrival airport");
    System.out.println("arriveDate      Arrival date of the flight (in MM/DD/YYYY");
    System.out.println("arriveTime      Arrival time of the flight (in HH:MM, 24 hour time)");
    System.out.println("The options are (and may appear in any order)");
    System.out.println("-textFile file  Where to read/write the airline info (file must be a text document)");
    System.out.println("-print          Prints the new flight data back to the user");
    System.out.println("-README         Prints a README for the project and exits the program");
    System.out.println("OPTIONS ARE CASE SENSITIVE");
    System.out.println();
    System.out.println("Dan Corcoran  Project 1");
    System.exit(0);
  }
}