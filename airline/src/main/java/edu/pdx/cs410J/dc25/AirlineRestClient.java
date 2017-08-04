package edu.pdx.cs410J.dc25;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send key/value pairs.
 */
public class AirlineRestClient extends HttpRequestHelper
{
  private static final String WEB_APP = "airline";
  private static final String SERVLET = "flights";

  private final String url;


  /**
   * Creates a client to the airline REST service running on the given host and port
   * @param hostName The name of the host
   * @param port The port
   */
  AirlineRestClient(String hostName, int port)
  {
    this.url = String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET);
  }

  /**
   * Returns all flights on the server if the airline names match.  Returns an error otherwise
   * @param airlineName Name of the airline to check against airline on the server
   * @return Pretty prints all flights on the server if the airline names match, otherwise returns error
   * @throws IOException Throws exception if <code>Airline</code> does not match
   */
  String getAllFlights(String airlineName) throws IOException {
    Response response = get(this.url, "name", airlineName);
    return response.getContent();
  }

  /**
   * Searches for flights between two airports specified on the given airline.  All parameters must match to return
   * a result.
   * @param airlineName Name of the airline to check against airline on the server
   * @param source Code of the airport flight departs from
   * @param destination Code of the airport flight arrives at
   * @return Pretty prints all flights that match search criteria, otherwise states no flights match
   * @throws IOException Throws exception if input not correctly formatted
   */
  String searchForFlights(String airlineName, String source, String destination) throws IOException {
    Response response = get(this.url, "name", airlineName, "src", source, "dest", destination);
    throwExceptionIfNotOkayHttpStatus(response);
    return response.getContent();
  }

  /**
   * Posta a flight to the server
   * @param airlineName Name of the airline to check against airline on the server
   * @param flight Flight to add to the server
   * @throws IOException Throws exception if flight data is not correctly formatted
   */
  void addFlightToServer(String airlineName, Flight flight) throws IOException {
    Response response = postToMyURL("name", airlineName,
            "number", String.valueOf(flight.getNumber()), "src", flight.getSource(),
            "departTime", flight.getDepartDataMember(), "dest", flight.getDestination(),
            "arriveTime", flight.getArrivalDataMember());
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   * Removes an airline from the server
   * @throws IOException Throws exception if <code>Airline</code> cannot be deleted
   */
  void removeAirline() throws IOException {
    Response response = delete(this.url);
    throwExceptionIfNotOkayHttpStatus(response);
  }

  @VisibleForTesting
  private Response postToMyURL(String... keysAndValues) throws IOException {
    return post(this.url, keysAndValues);
  }

  /**
   * Throws an exeption if an abnormal situation occurs
   * @param response Response from the server
   * @return Returns response if status is normal
   */
  private Response throwExceptionIfNotOkayHttpStatus(Response response) {
    int code = response.getCode();
    String message = response.getContent();
    if (code != HTTP_OK) {
      throw new AppointmentBookRestException(code, message);
    }
    return response;
  }

  /**
   * Exception class
   */
  private class AppointmentBookRestException extends RuntimeException {
    AppointmentBookRestException(int httpStatusCode, String message) {
      super("Got an HTTP Status Code of " + httpStatusCode + ": " + message);
    }
  }
}
