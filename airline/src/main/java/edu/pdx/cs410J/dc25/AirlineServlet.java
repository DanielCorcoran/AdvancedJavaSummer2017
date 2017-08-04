package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.AirportNames;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet provides a REST API for working with an <code>Airline</code>.  It can post a {@link Flight}, return all
 * flights in the airline, or return flights that match specified search criteria.
 */
public class AirlineServlet extends HttpServlet {
  private Airline airline = null;

  /**
   * Handles an HTTP GET request from a client by returning all flights on the server if {@link Airline} matches name
   * passed in, or returns an {@link Airline} containing flights that match the search parameters if search is
   * specified.
   */
  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
    response.setContentType( "text/plain" );

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
   * Handles an HTTP POST request by storing the {@link Flight} specified by the flight request parameters.
   * It checks the parameters for validity and writes the flight to the HTTP response.
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
    response.setContentType( "text/plain" );

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
   * Handles an HTTP DELETE request by removing the airline.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/plain");

    this.airline = null;
    response.setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Creates a new airline and stores flights that meet the search criteria in that airline
   * @param source Airport the flight is departing from
   * @param destination Airport the flight is arriving at
   * @return Returns the {@link Airline} with all flights that match the search criteria
   */
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
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
          throws IOException
  {
    String message = "The required parameter " + parameterName + " is missing.  There should be 6 total parameters " +
            "if adding a flight, or 3 if performing a search.";
    response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

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

  /**
   * Parses date and time string and checks validity.
   * @param dateAndTime String to check
   * @return Returns true if date and time are valid, false otherwise
   */
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
