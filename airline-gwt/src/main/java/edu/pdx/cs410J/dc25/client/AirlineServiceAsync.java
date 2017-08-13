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

  void addFlightToServer(int flightNumber, String source, String departDateTime, String dest, String arriveDateTime,
                         AsyncCallback<Void> async);

  void searchServerForFlights(String source, String destination, AsyncCallback<Airline> async);
}
