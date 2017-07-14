package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.AbstractAirline;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

  public static void main(String[] args) {
    //Class c = AbstractAirline.class;  // Refer to one of Dave's classes so that we can be sure it is on the classpath

    String airlineName;
    int flightNumber;
    String source;
    String departDate;
    String departTime;
    String destination;
    String arriveDate;
    String arriveTime;

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

    if (!verifyDateFormat(args, 3)) {
      System.err.println("Date not in the correct MM/DD/YYYY format");
      System.exit(4);
    }

    isTimeLengthLegal(args[4]);

    if (!verifyTimeFormat(args, 4)) {
      System.err.println("Time not in the correct HH:MM format");
      System.exit(5);
    }

    airlineName = args[0];
    source = args[2];
    departDate = args[3];
    departTime = args[4];
    destination = args[5];
    arriveDate = args[6];
    arriveTime = args[7];

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