package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.AirportNames;

import java.io.IOException;
import java.io.PrintStream;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project4 {

  static final String MISSING_ARGS = "Missing command line arguments";

  public static void main(String... args) {
    int argOffset = 0;                    //Variable to account for options when parsing arguments
    int hostPortArgsOnCommandLine = 0;    //Count for host/port args (2 if both are given)
    int flightNumber;                     //Flight number in integer form (after conversion from string)
    String depart;                        //Combined date and time of departure
    String arrive;                        //Combined date and time of arrival
    boolean toPrint = false;              //Flag to tell the program to print flight data

    String hostName = null;
    String portString = null;
    String key = null;
    String value = null;

    //If one of the args is -README, print readme
    for (int i = 0; i < args.length; ++i) {
      if (((args.length >= i + 1) && args[i].equals("-README"))) {
        readme();
      }
    }

    /*
    //Checks for minimum number of arguments necessary to run the program
    if (args.length < 10) {
      System.err.println("Missing command line arguments.  " +
              "Make sure there are exactly 10 (in addition to options) when running the program");
      for (String arg : args) {
        System.out.println(arg);
      }
      System.exit(1);
    }
    */

    //Check for option to print flight data
    for (String arg : args) {
      if (arg.equals("-print")) {
        toPrint = true;
        argOffset += 1;
      }
    }

    //Get host name and port
    for (int i = 0; i < args.length; ++i) {
      if (args[i].equals("-host")) {
        for (int j = 0; j < args.length; ++j) {
          if (args[j].equals("-port")) {
            portString = args[j + 1];
            hostPortArgsOnCommandLine += 1;
          }
        }
        hostName = args[i + 1];
        hostPortArgsOnCommandLine += 1;
      }
    }

    //If only host name or only port, display error message and exit
    if (hostPortArgsOnCommandLine == 1) {
      System.err.println("Both host name and port must be specified");
      System.exit(12);
    } else if (hostPortArgsOnCommandLine == 2) {
      argOffset += 4;
    }

    int port = 0;
    if (portString != null) {
      try {
        port = Integer.parseInt(portString);

      } catch (NumberFormatException ex) {
        System.err.println("Port \"" + portString + "\" must be an integer");
        System.exit(13);
      }
    }

    //If search option is selected, searches for flights
    if (args.length == 8 && port != 0) {
      for (int i = 0; i < args.length; ++i) {
        if (args[i].equals("-search")) {
          String airlineName = args[i + 1];
          String departAirport = args[i + 2].toUpperCase();
          String arriveAirport = args[i + 3].toUpperCase();

          isAirportCodeLegal(departAirport);
          isAirportCodeLegal(arriveAirport);

          AirlineRestClient client = new AirlineRestClient(hostName, port);
          try {
            System.out.println(client.searchForFlights(airlineName, departAirport, arriveAirport));
          } catch (IOException e) {
            error("While contacting server: " + e);
            return;
          }
          System.exit(0);
        }
      }
    }

    if (args.length - argOffset == 1) {
      if (port != 0) {
        AirlineRestClient client = new AirlineRestClient(hostName, port);
        try {
          System.out.println(client.getAllFlights(args[argOffset]));
        } catch (IOException e) {
          error("While contacting server: " + e);
          return;
        }
        System.exit(0);
      }
    } else if (args.length - argOffset == 10) {
      String flightNumberAsString = args[1 + argOffset];
      String departAirport = args[2 + argOffset].toUpperCase();
      String departDate = args[3 + argOffset];
      String departTime = args[4 + argOffset];
      String departAP = args[5 + argOffset];
      String arriveAirport = args[6 + argOffset].toUpperCase();
      String arriveDate = args[7 + argOffset];
      String arriveTime = args[8 + argOffset];
      String arriveAP = args[9 + argOffset];

      flightNumber = verifyFlightNumberIsInteger(flightNumberAsString);

      isDateLengthLegal(departDate);
      isDateLengthLegal(arriveDate);

      if (!verifyDateFormat(departDate) || !verifyDateFormat(arriveDate)) {
        System.err.println("Date not in the correct MM/DD/YYYY format");
        System.exit(4);
      }

      isTimeLengthLegal(departTime);
      isTimeLengthLegal(arriveTime);

      if (!verifyTimeFormat(departTime) || !verifyTimeFormat(arriveTime)) {
        System.err.println("Time not in the correct HH:MM format");
        System.exit(5);
      }

      departAP = getAP(departAP);
      arriveAP = getAP(arriveAP);

      isAirportCodeLegal(departAirport);
      isAirportCodeLegal(arriveAirport);

      //Combines date and time to create single strings
      depart = departDate + " " + departTime + " " + departAP;
      arrive = arriveDate + " " + arriveTime + " " + arriveAP;

      //Creates new instance of Airline and Flight classes and sets their data to args
      Airline airline = new Airline(args[argOffset]);
      Flight flight = new Flight(flightNumber, departAirport, depart, arriveAirport, arrive);
      airline.addFlight(flight);

      if (port != 0) {
        AirlineRestClient client = new AirlineRestClient(hostName, port);
        try {
          client.addFlightToServer(args[argOffset], flight);
        } catch (IOException e) {
          error("While contacting server: " + e);
          return;
        }
        System.out.println("Added flight to server");
      }

      //Prints flight data if option was flagged
      if (toPrint) {
        System.out.println(flight.toString());
        System.exit(0);
      }
    } else {
      System.err.println("Wrong number of command line arguments.  " +
              "Make sure there are exactly 10 (in addition to options) when running the program");
      for (String arg : args) {
        System.out.println(arg);
      }
      System.exit(1);
    }
  }

    /*
    for (String arg : args) {
      if (hostName == null) {
        hostName = arg;

      } else if ( portString == null) {
        portString = arg;

      } else if (key == null) {
        key = arg;

      } else if (value == null) {
        value = arg;

      } else {
        usage("Extraneous command line argument: " + arg);
      }
    }

    if (hostName == null) {
      usage( MISSING_ARGS );

    } else if ( portString == null) {
      usage( "Missing port" );
    }

    int port;
    try {
      port = Integer.parseInt( portString );

    } catch (NumberFormatException ex) {
      usage("Port \"" + portString + "\" must be an integer");
      return;
    }
    */

    /*
    AirlineRestClient client = new AirlineRestClient(hostName, port);

    String message;
    try {
      if (key == null) {
        // Print all key/value pairs
        Map<String, String> keysAndValues = client.getAllKeysAndValues();
        StringWriter sw = new StringWriter();
        Messages.formatKeyValueMap(new PrintWriter(sw, true), keysAndValues);
        message = sw.toString();

      } else if (value == null) {
        // Print all values of key
        message = Messages.formatKeyValuePair(key, client.getValue(key));

      } else {
        // Post the key/value pair
        client.addKeyValuePair(key, value);
        message = Messages.mappedKeyValue(key, value);
      }

    } catch ( IOException ex ) {
      error("While contacting server: " + ex);
      return;
    }

    System.out.println(message);

    System.exit(0);
  }
  */

  private static void error( String message )
  {
    PrintStream err = System.err;
    err.println("** " + message);

    System.exit(1);
  }

  /*
  /**
   * Prints usage information for this program and exits
   * @param message An error message to print
   */
  /*
  private static void usage( String message )
  {
    PrintStream err = System.err;
    err.println("** " + message);
    err.println();
    err.println("usage: java Project4 host port [key] [value]");
    err.println("  host    Host of web server");
    err.println("  port    Port of web server");
    err.println("  key     Key to query");
    err.println("  value   Value to add to server");
    err.println();
    err.println("This simple program posts key/value pairs to the server");
    err.println("If no value is specified, then all values are printed");
    err.println("If no key is specified, all key/value pairs are printed");
    err.println();

    System.exit(1);
  }
  */

  /**
   * Checks the command line argument for am or pm.  Gives an error and exits the program gracefully if neither am or pm
   * are found.
   *
   * @param apIn
   *        am/pm argument from command line
   * @return
   *        Returns the string "am" if am, "pm" if pm.  Otherwise prints an error and exits.
   */
  private static String getAP(String apIn) {
    switch (apIn) {
      case "am":
        return "am";
      case "pm":
        return "pm";
      default:
        System.err.println("am/pm must be lower case and contain only the characters am or pm");
        System.exit(10);
    }
    return null;
  }

  /**
   * Checks if an airport code is 3 chars, all chars are letters, and code is a real airport
   *
   * @param airportCode
   *        Argument to be tested
   */
  private static void isAirportCodeLegal(String airportCode) {
    if (!(airportCode.length() == 3 && (Character.isLetter(airportCode.charAt(0)) &&
            Character.isLetter(airportCode.charAt(1)) && Character.isLetter(airportCode.charAt(2))) &&
            AirportNames.getNamesMap().containsKey(airportCode))) {
      System.err.println("Airport code must be 3 letters and match an existing airport");
      System.exit(6);
    }
  }

  /**
   * Checks if time format has sufficient digits to be valid
   *
   * @param time
   *        Argument to be tested
   */
  private static void isTimeLengthLegal(String time) {
    if (time.length() < 4 || time.length() > 5) {
      System.err.println("Time not in the correct HH:MM format");
      System.exit(5);
    }
  }

  /**
   * Checks if date format has sufficient digits to be valid
   *
   * @param date
   *        Argument to be tested
   */
  private static void isDateLengthLegal(String date) {
    if (date.length() < 6 || date.length() > 10) {
      System.err.println("Date not in the correct MM/DD/YYYY format");
      System.exit(3);
    }
  }

  /**
   * Calculates the number of digits in the month for date format.
   * Necessary to calculate the rest of the date accurately.
   *
   * @param month
   *        Argument to be tested
   * @return
   *        Returns number of digits in month of date.  Returns 0 if number of digits in month is not valid.
   */
  private static int numberOfDigitsInMonth(String month) {
    if (Character.isDigit(month.charAt(0)) && month.charAt(1) == '/') {
      return 1;
    } else if (Character.isDigit(month.charAt(0)) && Character.isDigit(month.charAt(1)) &&
            month.charAt(2) == '/') {
      return 2;
    } else {
      return 0;
    }
  }

  /**
   * Calculates the number of digits in the day for date format.
   * Necessary to calculate the rest of the date accurately.
   *
   * @param day
   *        Argument to be tested
   * @param digitsInMonth
   *        Number of digits in the month.  Used as offset to calculate number of days in format
   * @return
   *        Returns number of digits in day of date.  Returns 0 if number of digits in day is not valid.
   */
  private static int numberOfDigitsInDay(String day, int digitsInMonth) {
    if (Character.isDigit(day.charAt(digitsInMonth + 1)) &&
            day.charAt(digitsInMonth + 2) == '/') {
      return 1;
    } else if (Character.isDigit(day.charAt(digitsInMonth + 1)) &&
            Character.isDigit(day.charAt(digitsInMonth + 2)) &&
            day.charAt(digitsInMonth + 3) == '/') {
      return 2;
    } else {
      return 0;
    }
  }

  /**
   * Checks if the year in the date format is 4 chars long (or 2 if read from file) and all chars are numbers.
   *
   * @param year
   *        Argument to be tested
   * @param yearStartingPosition
   *        Index of the first number of the year in the date
   * @return
   *        Returns true if year is exactly 4 chars long (or 2 if read from file) and all chars are numbers
   */
  private static boolean checkYearFormat(String year, int yearStartingPosition) {
    boolean yearIsAllNumbers = true;

    for(int i = yearStartingPosition; i < year.length(); ++i) {
      if (!Character.isDigit(year.charAt(i))) {
        yearIsAllNumbers = false;
      }
    }

    return (((year.length() == yearStartingPosition + 4) || (year.length() == yearStartingPosition + 2)) &&
            yearIsAllNumbers);
  }

  /**
   * Calls the <code>numberOfDigitsInMonth</code>, <code>numberOfDigitsInDay</code>, and <code>checkYearFormat</code>
   * methods to determine if the date format is valid
   *
   * @param date
   *        Argument to be tested
   * @return
   *        Returns true is date format is valid.  Returns false otherwise.
   */
  private static boolean verifyDateFormat(String date) {
    int digitsInMonth;
    int digitsInDay;
    int yearStartingPosition;

    digitsInMonth = numberOfDigitsInMonth(date);
    if (digitsInMonth == 0) {
      return false;
    }

    digitsInDay = numberOfDigitsInDay(date, digitsInMonth);
    if (digitsInDay == 0) {
      return false;
    }

    yearStartingPosition = digitsInMonth + digitsInDay + 2;
    return checkYearFormat(date, yearStartingPosition);
  }

  /**
   * Calculates the number of digits in the hour of the time
   *
   * @param time
   *        Argument to be tested
   * @return
   *        Returns number of digits in the hour of the time.  Returns 0 if number of digits in hours is not valid.
   */
  private static int numberOfDigitsInHour(String time) {
    if (Character.isDigit(time.charAt(0)) && time.charAt(1) == ':') {
      return 1;
    } else if (Character.isDigit(time.charAt(0)) && Character.isDigit(time.charAt(1)) && time.charAt(2) == ':') {
      return 2;
    } else {
      return 0;
    }
  }

  /**
   * Checks if the minutes of the time is 2 digits long
   *
   * @param time
   *        Argument to be tested
   * @param digitsInHour
   *        Number of digits in the hour of the time
   * @return
   *        Returns true if there are 2 digits in the minutes place.  Returns false otherwise.
   */
  private static boolean verifyNumberOfDigitsInMinutes(String time, int digitsInHour) {
    return time.substring(digitsInHour + 1).length() == 2 && Character.isDigit(time.charAt(digitsInHour + 1)) &&
            Character.isDigit(time.charAt(digitsInHour + 2));
  }

  /**
   * Checks if the time is in the correct format
   *
   * @param time
   *        Argument to be tested
   * @return
   *        Returns true if time is in correct format.  Returns false otherwise.
   */
  private static boolean verifyTimeFormat(String time) {
    int digitsInHour;

    digitsInHour = numberOfDigitsInHour(time);
    return digitsInHour != 0 && verifyNumberOfDigitsInMinutes(time, digitsInHour);
  }

  /**
   * Checks if the flight number passed in is an integer
   *
   * @param flightNumberIn
   *        Command line argument to be tested
   * @return
   *        Returns flight number as an integer if valid.  Otherwise exits the program with an error message.
   */
  private static int verifyFlightNumberIsInteger(String flightNumberIn) {
    int flightNumber = 0;

    try {
      flightNumber = Integer.parseInt(flightNumberIn);
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
    System.out.println("This program interacts with a server that contains airline flight data via HTTP.");
    System.out.println("A flight can be added that has already happened or will happen in the future.  " +
            "This can be done via the command line or URL");
    System.out.println("The flight data will remain as long as the server is online.");
    System.out.println("Flights can be searched for using the -search option.  This will return the flights that " +
            "use the specified airports.");
    System.out.println("The search option cannot be used simultaneously with the -print option or when adding a " +
            "flight");
    System.out.println("If the airline names do not match when adding a flight, it will not be added to the airline.");
    System.out.println("All data must be entered correctly in the correct position of the command line " +
            "or the program will not work.");
    System.out.println("The user will be given an error message if data has been incorrectly entered.");
    System.out.println();
    System.out.println("The command line interface must be as follows (options without [] and arguments without <>):");
    System.out.println("java edu.pdx.cs410J.dc25.Project4 [options] <args>");
    System.out.println("Arguments are (in this order):");
    System.out.println("name            Name of the airline (if more than one word, must be surrounded by quotes)");
    System.out.println("flightNumber    The flight number");
    System.out.println("src             Three letter code of departure airport");
    System.out.println("departDate      Departure date of flight (in MM/DD/YYYY)");
    System.out.println("departTime      Departure time of flight (in HH:MM)");
    System.out.println("departAP        Flight departs in the am or pm (case sensitive)");
    System.out.println("dest            Three letter code of arrival airport");
    System.out.println("arriveDate      Arrival date of the flight (in MM/DD/YYYY");
    System.out.println("arriveTime      Arrival time of the flight (in HH:MM)");
    System.out.println("arriveAP        Flight arrives in the am or pm (case sensitive)");
    System.out.println("The options are (and may appear in any order)");
    System.out.println("-host hostname  Host computer on which the server runs");
    System.out.println("-port port      Port on which the server is listening");
    System.out.println("-search         Search for flights");
    System.out.println("-print          Prints the new flight data back to the user");
    System.out.println("-README         Prints a README for the project and exits the program");
    System.out.println("OPTIONS ARE CASE SENSITIVE");
    System.out.println();
    System.out.println("If -search is used, the arguments should be as follows: -search name src dest");
    System.out.println("If specifying -host or -port, the other must be specified for the program to run.");
    System.out.println();
    System.out.println("Dan Corcoran  Project 4");
    System.exit(0);
  }
}