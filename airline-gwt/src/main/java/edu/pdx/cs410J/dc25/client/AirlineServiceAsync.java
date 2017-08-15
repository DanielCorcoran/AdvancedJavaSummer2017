package edu.pdx.cs410J.dc25.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client-side interface to the airline service
 */
public interface AirlineServiceAsync {

  /**
   * Return an airline created on the server
   */
  void getAirline(AsyncCallback<Airline> async);

  /**
   * Adds an <code>Airline</code> to the server if one isn't present
   */
  void addAirlineToServer(String airlineName, AsyncCallback<Void> async);

  /**
   * Adds a <code>Flight</code> to the server
   * @param flightNumber
   *        Number of the flight to be added
   * @param source
   *        Source airport code of the flight to be added
   * @param departDateTime
   *        Date and time of departure of flight to be added
   * @param dest
   *        Destination airport code of the flight to be added
   * @param arriveDateTime
   *        Date and time of arrival of flight to be added
   * @throws Exception
   *        Throws an exception if the flight cannot be added
   */
  void addFlightToServer(int flightNumber, String source, String departDateTime, String dest, String arriveDateTime,
                         AsyncCallback<Void> async);

  /**
   * Searches the server for any airlines between the two given airports
   * @param source
   *        Source airport code to search for
   * @param destination
   *        Destination airport code to search for
   * @return
   *        Returns an <code>Airline</code> with all flights that match the search criteria
   * @throws Exception
   *        Throws an exception if no flights are found
   */
  void searchServerForFlights(String source, String destination, AsyncCallback<Airline> async);
}
