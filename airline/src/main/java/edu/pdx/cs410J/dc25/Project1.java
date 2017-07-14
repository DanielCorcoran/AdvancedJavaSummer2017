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
    int digitsInMonth;
    int digitsInDay;
    int yearStartingPosition;

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

    if (args[3].length() < 4) {
      System.err.println("Date not in the correct MM/DD/YYYY format");
      System.exit(1);
    }

    digitsInMonth = numberOfDigitsInMonth(args, 3);
    if (digitsInMonth != 1 && digitsInMonth != 2) {
      System.err.println("Date not in the correct MM/DD/YYYY format");
      System.exit(1);
    }

    digitsInDay = numberOfDigitsInDay(args, 3, digitsInMonth);
    if (digitsInDay != 1 && digitsInDay != 2) {
      System.err.println("Date not in the correct MM/DD/YYYY format");
      System.exit(1);
    }

    yearStartingPosition = digitsInMonth + digitsInDay + 2;
    if (!checkCorrectDateFormat(args, 3, yearStartingPosition)) {
      System.err.println("Date not in the correct MM/DD/YYYY format");
      System.exit(1);
    }



    airlineName = args[0];
    source = args[2];
    departDate = args[3];
    departTime = args[4];
    destination = args[5];
    arriveDate = args[6];
    arriveTime = args[7];

  }

  private static boolean checkIfCharAtPositionIsANumber(String arg, int position) {
    return arg.charAt(position) >= '0' && arg.charAt(position) <= '9';
  }

  private static int numberOfDigitsInMonth(String[] args, int argNumber) {
    int digitsInMonth = 0;

    if ((checkIfCharAtPositionIsANumber(args[argNumber], 0)) &&
            args[argNumber].charAt(1) == '/') {
      digitsInMonth = 1;
    }

    if (checkIfCharAtPositionIsANumber(args[argNumber],0) &&
            checkIfCharAtPositionIsANumber(args[argNumber], 1)&&
            args[argNumber].charAt(2) == '/') {
      digitsInMonth = 2;
    }

    return digitsInMonth;
  }

  private static int numberOfDigitsInDay(String[] args, int argNumber, int digitsInMonth) {
    int digitsInDay = 0;

    if ((checkIfCharAtPositionIsANumber(args[argNumber], digitsInMonth + 1)) &&
            args[argNumber].charAt(digitsInMonth + 2) == '/') {
      digitsInDay = 1;
    }

    if (checkIfCharAtPositionIsANumber(args[argNumber],digitsInMonth + 1) &&
            checkIfCharAtPositionIsANumber(args[argNumber], digitsInMonth + 2)&&
            args[argNumber].charAt(digitsInMonth + 3) == '/') {
      digitsInDay = 2;
    }

    return digitsInDay;
  }

  private static boolean checkCorrectDateFormat(String[] args, int argNumber, int yearStartingPosition) {
    return args[argNumber].length() == yearStartingPosition + 3 &&
            checkIfCharAtPositionIsANumber(args[argNumber], yearStartingPosition) &&
            checkIfCharAtPositionIsANumber(args[argNumber], yearStartingPosition + 1) &&
            checkIfCharAtPositionIsANumber(args[argNumber], yearStartingPosition + 2) &&
            checkIfCharAtPositionIsANumber(args[argNumber], yearStartingPosition + 3);
  }

  private static int verifyFlightNumberIsInteger(String[] args) {
    int flightNumber = 0;
    try {
      flightNumber = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.err.println("Flight number (second argument) must contain only numbers");
      System.exit(1);
    }

    return flightNumber;
  }

}