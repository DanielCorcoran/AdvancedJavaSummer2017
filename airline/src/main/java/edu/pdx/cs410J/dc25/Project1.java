package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.AbstractAirline;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

  public static void main(String[] args) {
    //Class c = AbstractAirline.class;  // Refer to one of Dave's classes so that we can be sure it is on the classpath

    int flightNumber;
    String depart;
    String arrive;

    if (args.length < 8) {
      System.err.println("Missing command line arguments.  " +
              "Make sure there are exactly 8 (in addition to options) when running the program");
      for (String arg : args) {
        System.out.println(arg);
      }
      System.exit(1);
    }

    if (args.length > 10) {
      System.err.println("Too many command line arguments.  " +
              "Make sure there are exactly 8 (in addition to options) when running the program");
      for (String arg : args) {
        System.out.println(arg);
      }
      System.exit(1);
    }

    if (args[0].equals("-README") || (args[1].equals("-README"))) {
      readme();
    }

    flightNumber = verifyFlightNumberIsInteger(args);

    isDateLengthLegal(args[3]);
    isDateLengthLegal(args[6]);

    if (!verifyDateFormat(args, 3) || !verifyDateFormat(args, 6)) {
      System.err.println("Date not in the correct MM/DD/YYYY format");
      System.exit(4);
    }

    isTimeLengthLegal(args[4]);
    isTimeLengthLegal(args[7]);

    if (!verifyTimeFormat(args, 4) || !verifyTimeFormat(args, 7)) {
      System.err.println("Time not in the correct HH:MM format");
      System.exit(5);
    }

    isAirportCodeLegal(args, 2);
    isAirportCodeLegal(args, 5);

    depart = args[3] + " " + args[4];
    arrive = args[6] + " " + args[7];

    Airline airline = new Airline(args[0]);
    Flight flight = new Flight(flightNumber, args[2], depart, args[5], arrive);
    airline.addFlight(flight);
  }

  private static void isAirportCodeLegal(String[] args, int argNumber) {
    if (!(args[argNumber].length() == 3 && (Character.isLetter(args[2].charAt(0)) &&
            Character.isLetter(args[2].charAt(1)) && Character.isLetter(args[2].charAt(2))))) {
      System.err.println("Airport code must be 3 letters");
      System.exit(6);
    }
  }

  private static void isTimeLengthLegal(String arg) {
    if (arg.length() < 4 || arg.length() > 5) {
      System.err.println("Time not in the correct HH:MM format");
      System.exit(5);
    }
  }

  private static void isDateLengthLegal(String arg) {
    if (arg.length() < 4 || arg.length() > 10) {
      System.err.println("Date not in the correct MM/DD/YYYY format");
      System.exit(3);
    }
  }

  private static int numberOfDigitsInMonth(String[] args, int argNumber) {
    if (Character.isDigit(args[argNumber].charAt(0)) && args[argNumber].charAt(1) == '/') {
      return 1;
    } else if (Character.isDigit(args[argNumber].charAt(0)) && Character.isDigit(args[argNumber].charAt(1)) &&
            args[argNumber].charAt(2) == '/') {
      return 2;
    } else {
      return 0;
    }
  }

  private static int numberOfDigitsInDay(String[] args, int argNumber, int digitsInMonth) {
    if (Character.isDigit(args[argNumber].charAt(digitsInMonth + 1)) &&
            args[argNumber].charAt(digitsInMonth + 2) == '/') {
      return 1;
    } else if (Character.isDigit(args[argNumber].charAt(digitsInMonth + 1)) &&
            Character.isDigit(args[argNumber].charAt(digitsInMonth + 2)) &&
            args[argNumber].charAt(digitsInMonth + 3) == '/') {
      return 2;
    } else {
      return 0;
    }
  }

  private static boolean checkYearFormat(String[] args, int argNumber, int yearStartingPosition) {
    boolean yearIsAllNumbers = true;

    for(int i = yearStartingPosition; i < args[argNumber].length(); ++i) {
      if (!Character.isDigit(args[argNumber].charAt(i))) {
        yearIsAllNumbers = false;
      }
    }

    return ((args[argNumber].length() == yearStartingPosition + 4) && yearIsAllNumbers);
  }

  private static boolean verifyDateFormat(String[] args, int argNumber) {
    int digitsInMonth;
    int digitsInDay;
    int yearStartingPosition;

    digitsInMonth = numberOfDigitsInMonth(args, argNumber);
    if (digitsInMonth == 0) {
      return false;
    }

    digitsInDay = numberOfDigitsInDay(args, argNumber, digitsInMonth);
    if (digitsInDay == 0) {
      return false;
    }

    yearStartingPosition = digitsInMonth + digitsInDay + 2;
    return checkYearFormat(args, argNumber, yearStartingPosition);
  }

  private static int numberOfDigitsInHour(String[] args, int argNumber) {
    if (Character.isDigit(args[argNumber].charAt(0)) && args[argNumber].charAt(1) == '/') {
      return 1;
    } else if (Character.isDigit(args[argNumber].charAt(0)) && Character.isDigit(args[argNumber].charAt(1)) &&
            args[argNumber].charAt(2) == ':') {
      return 2;
    } else {
      return 0;
    }
  }

  private static boolean verifyNumberOfDigitsInMinutes(String[] args, int argNumber, int digitsInHour) {
    return Character.isDigit(args[argNumber].charAt(digitsInHour + 1)) &&
            Character.isDigit(args[argNumber].charAt(digitsInHour + 2));
  }

  private static boolean verifyTimeFormat(String[] args, int argNumber) {
    int digitsInHour;

    digitsInHour = numberOfDigitsInHour(args, argNumber);
    return digitsInHour != 0 && verifyNumberOfDigitsInMinutes(args, argNumber, digitsInHour);
  }

  private static int verifyFlightNumberIsInteger(String[] args) {
    int flightNumber = 0;

    try {
      flightNumber = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.err.println("Flight number (second argument) must contain only numbers");
      System.exit(2);
    }

    return flightNumber;
  }

  private static void readme() {
    System.out.println("This program creates an airline flight via data passed in from the user on the command line.");
    System.out.println("This can be a flight that has already happened or will happen in the future.");
    System.out.println("The flight data will not be stored after the program terminates.");
    System.out.println("All data must be entered correctly in the correct position of the command line " +
            "or the program will not work.");
    System.out.println("The user will be given an error message if data has been incorrectly entered.");
    System.out.println();
    System.out.println("The command line interface must be as follows (options without [] and arguments without <>):");
    System.out.println("java edu.pdx.cs410J.dc25.Project1 [options] <args>");
    System.out.println("Arguments are (in this order):");
    System.out.println("name            Name of the airline");
    System.out.println("flightNumber    The flight number");
    System.out.println("src             Three letter code of departure airport");
    System.out.println("departDate      Departure date of flight (in MM/DD/YYYY)");
    System.out.println("departTime      Departure time of flight (in HH:MM, 24 hour time)");
    System.out.println("dest            Three letter code of arrival airport");
    System.out.println("arriveDate      Arrival date of the flight (in MM/DD/YYYY");
    System.out.println("arriveTime      Arrival time of the flight (in HH:MM, 24 hour time)");
    System.out.println("The options are (and may appear in any order)");
    System.out.println("-print          Prints the new flight data back to the user");
    System.out.println("-README         Prints a README for the project and exits the program");
    System.out.println("OPTIONS ARE CASE SENSITIVE");
    System.out.println();
    System.out.println("Dan Corcoran  Project 1");
    System.exit(0);
  }
}