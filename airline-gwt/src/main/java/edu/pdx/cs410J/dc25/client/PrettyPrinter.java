package edu.pdx.cs410J.dc25.client;

import com.google.gwt.i18n.shared.DateTimeFormat;
import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirportNames;

import java.text.DateFormat;
import java.util.Arrays;

/**
 * The <code>PrettyPrinter</code> class takes the {@link Airline} and all of its associated {@link Flight} in the
 * program and prints the data to standard out in an easy to read format.  The list of {@link Flight} will be sorted by
 * departing airport then departing time and flight duration will also be printed.  There is also a method to print only
 * the flights that are departing from and arriving at specific airports given as parameters.
 */
class PrettyPrinter {

  /**
   * This method takes in an ArrayList of {@link Flight} and prints all flights in the list to standard out
   *
   * @param abstractAirline
   *        Airline and all its associated flight data to be written to file.
   *        Can also be airline with filtered flights.
   * @param source
   *        Code of source airport
   * @param destination
   *        Code of destination airport
   * @return Returns pretty printed flights for given airline
   */
  String httpDump(AbstractAirline abstractAirline, String source, String destination) {
    if (abstractAirline.getFlights() == null) {
      return "There are no flights that match the search criteria.";
    } else {
      Flight[] flightArray =
              (Flight[]) abstractAirline.getFlights().toArray(new Flight[abstractAirline.getFlights().size()]);
      Arrays.sort(flightArray);

      String toWrite = "Flights for airline " + abstractAirline.getName();
      if (source != null && destination != null) {
        toWrite += " between " + source + " and " + destination + ":\n";
      } else {
        toWrite += ":\n";
      }

      for (Flight aFlightArray : flightArray) {
        toWrite += "\n\n" + createFlightInfoString(aFlightArray);
      }
      return toWrite;
    }
  }

  /**
   * This method concatenates the members of the {@link Flight} class separated by the delimiter ("|") to create a
   * string of data for that flight that will be saved to the text file.
   *
   * @param flight
   *        Object of the {@link Flight} class to be turned into a string
   * @return Returns flight data as a string
   */
  private String createFlightInfoString(Flight flight) {
    DateTimeFormat df = DateTimeFormat.getFormat("MM/dd/yyyy");
    DateTimeFormat tf = DateTimeFormat.getFormat("hh:mm a");

    return "Flight number " + String.valueOf(flight.getNumber()) + ":\n" + "Departs from " +
            AirportNames.getNamesMap().get(flight.getSource()) + " (" + flight.getSource() + ")" + " on " +
            df.format(flight.getDeparture()) + " at " + tf.format(flight.getDeparture()) + ".\n" +
            "Arrives at " + AirportNames.getNamesMap().get(flight.getDestination()) + " (" +
            flight.getDestination() + ")" + " on " + df.format(flight.getArrival()) + " at " +
            tf.format(flight.getArrival()) + ".\nThe duration of this flight is " +
            ((flight.getArrival().getTime() - flight.getDeparture().getTime()) / 60000) + " minutes.";
  }
}
