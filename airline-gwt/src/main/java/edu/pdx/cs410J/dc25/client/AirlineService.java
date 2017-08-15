package edu.pdx.cs410J.dc25.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A GWT remote service that returns a dummy airline
 */
@RemoteServiceRelativePath("airline")
public interface AirlineService extends RemoteService {

  /**
   * Returns the current date and time on the server
   */
  Airline getAirline() throws Exception;

  /**
   * Adds an <code>Airline</code> to the server
   * @param airlineName
   *        Name of the <code>Airline</code> to create
   * @throws Exception
   *         If airline already exists on the server
   */
  void addAirlineToServer(String airlineName) throws Exception;

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
  void addFlightToServer(int flightNumber, String source, String departDateTime, String dest, String arriveDateTime)
          throws Exception;

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
  Airline searchServerForFlights(String source, String destination) throws Exception;
}
