package edu.pdx.cs410J.dc25;

import com.google.common.annotations.VisibleForTesting;

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
      response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "There is no airline on the server");
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

    Flight flight = new Flight(flightNumber, source, departTime, destination, arriveTime);
    this.airline.addFlight(flight);

    response.setStatus( HttpServletResponse.SC_OK);
  }

  /**
   * Handles an HTTP DELETE request by removing all key/value pairs.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
   */
  /*
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/plain");

    this.data.clear();

    PrintWriter pw = response.getWriter();
    pw.println(Messages.allMappingsDeleted());
    pw.flush();

    response.setStatus(HttpServletResponse.SC_OK);

  }
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
   *
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
          throws IOException
  {
    String message = Messages.missingRequiredParameter(parameterName);
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
}
