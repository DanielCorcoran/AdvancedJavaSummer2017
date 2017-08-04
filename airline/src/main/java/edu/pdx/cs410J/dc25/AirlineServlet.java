package edu.pdx.cs410J.dc25;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.AirportNames;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>Airline</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class AirlineServlet extends HttpServlet {
  //private final Map<String, String> data = new HashMap<>();
  private Airline airline = null;

  /**
   * Handles an HTTP GET request from a client by writing the value of the key
   * specified in the "key" HTTP parameter to the HTTP response.  If the "key"
   * parameter is not specified, all of the key/value pairs are written to the
   * HTTP response.
   */
  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
    response.setContentType( "text/plain" );

      /*
      String key = getParameter( "key", request );
      if (key != null) {
          writeValue(key, response);

      } else {
          writeAllMappings(response);
      }
      */

    String airlineName = getParameter("name", request);
    String source = getParameter("src", request);
    String destination = getParameter("dest", request);

    if (airlineName == null) {
      missingRequiredParameter(response, "name");
      return;
    } else if (this.airline == null) {
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "There is no airline on the server");
    } else if (!this.airline.getName().equals(airlineName)) {
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Airline names do not match");
      return;
    }

    PrettyPrinter pretty = new PrettyPrinter();
    if (source == null && destination == null) {
      response.getWriter().println(pretty.httpDump(this.airline, null, null));
      response.setStatus(HttpServletResponse.SC_OK);
    } else if (source == null) {
      missingRequiredParameter(response, "src");
    } else if (destination == null) {
      missingRequiredParameter(response, "dest");
    } else {
      Airline searchResult = searchForFlights(source, destination);
      response.getWriter().println(pretty.httpDump(searchResult, source, destination));
      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  /**
   * Handles an HTTP POST request by storing the key/value pair specified by the
   * "key" and "value" request parameters.  It writes the key/value pair to the
   * HTTP response.
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
    response.setContentType( "text/plain" );

    /*
    String key = getParameter( "key", request );
    if (key == null) {
      missingRequiredParameter(response, "key");
      return;
    }

    String value = getParameter( "value", request );
    if ( value == null) {
      missingRequiredParameter( response, "value" );
      return;
    }

    this.data.put(key, value);

    PrintWriter pw = response.getWriter();
    pw.println(Messages.mappedKeyValue(key, value));
    pw.flush();
    */

    String airlineName = getParameter("name", request);
    if (airlineName == null) {
      missingRequiredParameter(response, "name");
      return;
    }

    String flightNumberAsString = getParameter("number", request);
    if (flightNumberAsString == null) {
      missingRequiredParameter(response, "number");
      return;
    }

    String source = getParameter("src", request);
    if (source == null) {
      missingRequiredParameter(response, "src");
      return;
    }

    String departTime = getParameter("departTime", request);
    if (departTime == null) {
      missingRequiredParameter(response, "departTime");
      return;
    }

    String destination = getParameter("dest", request);
    if (destination == null) {
      missingRequiredParameter(response, "dest");
      return;
    }

    String arriveTime = getParameter("arriveTime", request);
    if (arriveTime == null) {
      missingRequiredParameter(response, "arriveTime");
      return;
    }

    int flightNumber = 0;
    try {
      flightNumber = Integer.parseInt(flightNumberAsString);
    } catch (NumberFormatException e) {
      response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "Invalid flight number");
    }

    if (this.airline == null) {
      this.airline = new Airline(airlineName);
    } else if (!this.airline.getName().equals(airlineName)) {
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Airline names do not match");
      return;
    }

    if (!isAirportCodeLegal(source) || !isAirportCodeLegal(destination)) {
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Airport code is not valid");
      return;
    }

    if (!parseAndVerifyDateAndTime(departTime) || !parseAndVerifyDateAndTime(arriveTime)) {
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Date and time format incorrect");
      return;
    }

    Flight flight = new Flight(flightNumber, source, departTime, destination, arriveTime);
    this.airline.addFlight(flight);

    response.setStatus( HttpServletResponse.SC_OK);
  }

  /**
   * Handles an HTTP DELETE request by removing all key/value pairs.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/plain");

    this.airline = null;
    /*
    this.data.clear();

    PrintWriter pw = response.getWriter();
    pw.println(Messages.allMappingsDeleted());
    pw.flush();
    */

    response.setStatus(HttpServletResponse.SC_OK);

  }

  private Airline searchForFlights(String source, String destination) {
    Airline searchResult = new Airline(this.airline.getName());
    Flight[] storedFlights = this.airline.getFlights().toArray(new Flight[this.airline.getFlights().size()]);

    for (Flight storedFlight : storedFlights) {
      if (storedFlight.getSource().equals(source) && storedFlight.getDestination().equals(destination)) {
        searchResult.addFlight(storedFlight);
      }
    }
    return searchResult;
  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   *
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
          throws IOException
  {
    String message = "The required parameter " + parameterName + " is missing.  There should be 6 total parameters " +
            "if adding a flight, or 3 if performing a search.";
    response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /**
   * Writes the value of the given key to the HTTP response.
   *
   * The text of the message is formatted with
   * {@link Messages#formatKeyValuePair(String, String)}
   */
  /*
  private void writeValue( String key, HttpServletResponse response ) throws IOException {
    String value = this.data.get(key);

    PrintWriter pw = response.getWriter();
    pw.println(Messages.formatKeyValuePair(key, value));

    pw.flush();

    response.setStatus( HttpServletResponse.SC_OK );
  }
  */

  /**
   * Writes all of the key/value pairs to the HTTP response.
   *
   * The text of the message is formatted with
   * {@link Messages#formatKeyValuePair(String, String)}
   */
  /*
  private void writeAllMappings( HttpServletResponse response ) throws IOException {
    PrintWriter pw = response.getWriter();
    Messages.formatKeyValueMap(pw, data);

    pw.flush();

    response.setStatus( HttpServletResponse.SC_OK );
  }
  */

  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }

  public Airline getAirline() {
    return this.airline;
  }

  /*
  @VisibleForTesting
  void setValueForKey(String key, String value) {
    this.data.put(key, value);
  }

  @VisibleForTesting
  String getValueForKey(String key) {
    return this.data.get(key);
  }
  */

  /**
   * Checks the command line argument for am or pm.  Gives an error and exits the program gracefully if neither am or pm
   * are found.
   *
   * @param apIn
   *        am/pm argument from command line
   * @return
   *        Returns true if the string is "am" or "pm", false otherwise
   */
  private static boolean getAP(String apIn) {
    return (apIn.equals("am") || apIn.equals("pm"));
  }

  /**
   * Checks if an airport code is 3 chars, all chars are letters, and code is a real airport
   *
   * @param airportCode
   *        Argument to be tested
   * @return true if airport code is in correct format and a valid airport
   */
  private static boolean isAirportCodeLegal(String airportCode) {
    return (airportCode.length() == 3 && (Character.isLetter(airportCode.charAt(0)) &&
            Character.isLetter(airportCode.charAt(1)) && Character.isLetter(airportCode.charAt(2))) &&
            AirportNames.getNamesMap().containsKey(airportCode));
  }

  /**
   * Checks if time format has sufficient digits to be valid
   *
   * @param time
   *        Argument to be tested
   * @return Returns true if time length has valid number of characters, false otherwise
   */
  private static boolean isTimeLengthLegal(String time) {
    return (time.length() < 4 || time.length() > 5);
  }

  /**
   * Checks if date format has sufficient digits to be valid
   *
   * @param date
   *        Argument to be tested
   * @return Returns true if date length has valid number of characters, false otherwise
   */
  private static boolean isDateLengthLegal(String date) {
    return (date.length() < 6 || date.length() > 10);
  }

  /**
   * Calculates the number of digits in the month for date format.
   * Necessary to calculate the rest of the date accurately.
   *
   * @param date
   *        Argument to be tested
   * @return
   *        Returns number of digits in month of date.  Returns 0 if number of digits in month is not valid.
   */
  private static int numberOfDigitsInMonth(String date) {
    if (Character.isDigit(date.charAt(0)) && date.charAt(1) == '/') {
      return 1;
    } else if (Character.isDigit(date.charAt(0)) && Character.isDigit(date.charAt(1)) &&
            date.charAt(2) == '/') {
      return 2;
    } else {
      return 0;
    }
  }

  /**
   * Calculates the number of digits in the day for date format.
   * Necessary to calculate the rest of the date accurately.
   *
   * @param date
   *        Argument to be tested
   * @param digitsInMonth
   *        Number of digits in the month.  Used as offset to calculate number of days in format
   * @return
   *        Returns number of digits in day of date.  Returns 0 if number of digits in day is not valid.
   */
  private static int numberOfDigitsInDay(String date, int digitsInMonth) {
    if (Character.isDigit(date.charAt(digitsInMonth + 1)) &&
            date.charAt(digitsInMonth + 2) == '/') {
      return 1;
    } else if (Character.isDigit(date.charAt(digitsInMonth + 1)) &&
            Character.isDigit(date.charAt(digitsInMonth + 2)) &&
            date.charAt(digitsInMonth + 3) == '/') {
      return 2;
    } else {
      return 0;
    }
  }

  /**
   * Checks if the year in the date format is 4 chars long (or 2 if read from file) and all chars are numbers.
   *
   * @param date
   *        Argument to be tested
   * @param yearStartingPosition
   *        Index of the first number of the year in the date
   * @return
   *        Returns true if year is exactly 4 chars long (or 2 if read from file) and all chars are numbers
   */
  private static boolean checkYearFormat(String date, int yearStartingPosition) {
    boolean yearIsAllNumbers = true;

    for(int i = yearStartingPosition; i < date.length(); ++i) {
      if (!Character.isDigit(date.charAt(i))) {
        yearIsAllNumbers = false;
      }
    }

    return (((date.length() == yearStartingPosition + 4) || (date.length() == yearStartingPosition + 2)) &&
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

  private static boolean parseAndVerifyDateAndTime(String dateAndTime) {
    String parts[] = dateAndTime.split(" ");
    String date = parts[0];
    String time = parts[1];
    String amPm = parts[2];

    if (isDateLengthLegal(date)) {
      if (!verifyDateFormat(date)) {
        return false;
      }
    }

    if (isTimeLengthLegal(time)) {
      if (!verifyTimeFormat(time)) {
        return false;
      }
    }

    return getAP(amPm);
  }
}
