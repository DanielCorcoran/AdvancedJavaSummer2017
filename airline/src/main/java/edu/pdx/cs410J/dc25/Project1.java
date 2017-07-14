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
      System.err.println("Missing command line arguments.  Make sure there are exactly 8 when running the program");
      for (String arg : args) {
        System.out.println(arg);
      }
      System.exit(1);
    }

    if (args.length > 8) {
      System.err.println("Too many command line arguments.  Make sure there are exactly 8 when running the program");
      for (String arg : args) {
        System.out.println(arg);
      }
      System.exit(1);
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
}