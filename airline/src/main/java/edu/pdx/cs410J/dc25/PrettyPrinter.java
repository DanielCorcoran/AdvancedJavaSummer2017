package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;

/**
 * The <code>PrettyPrinter</code> class takes the {@link Airline} and all of its associated {@link Flight} in the
 * program and writes the data to a file specified on the command line in an easy to read format.  If no file is found,
 * then <code>PrettyPrinter</code> will create one with the name passed in and write the current airline and flight data
 * to it.  The list of {@link Flight} will be sorted by departing airport then departing time and flight duration will
 * also be written to the file.
 */
public class PrettyPrinter implements AirlineDumper{
  private String fileName;

  PrettyPrinter(String fileNameIn) {
    this.fileName = fileNameIn;
  }

  /**
   * This method takes the file name argument (established in the class constructor, which can also be "-"), creates a
   * new file if necessary, and writes the {@link Airline} and {@link Flight} data into it, overwriting any data that
   * was present in the original file.
   *
   * @param abstractAirline
   *        Airline and all its associated flight data to be written to file
   * @throws IOException
   *        Exception thrown if file can't be written to (which should never occur)
   */
  @Override
  public void dump(AbstractAirline abstractAirline) throws IOException {
    //Turns collection of flights in airline object to an array of flights
    Flight[] flightArray =
            (Flight[]) abstractAirline.getFlights().toArray(new Flight[abstractAirline.getFlights().size()]);
    Arrays.sort(flightArray);

    String toWrite = "Flights for airline " + abstractAirline.getName() + ":\n";

    for (Flight aFlightArray : flightArray) {
      toWrite += "\n\n" + createFlightInfoString(aFlightArray);
    }

    if (this.fileName.equals("-")) {
      System.out.println(toWrite);
    } else {
      try (FileWriter fw = new FileWriter(this.fileName); BufferedWriter bw = new BufferedWriter(fw)) {
        bw.write(toWrite);
      } catch (IOException e) {
        System.err.println("Can't write to file");
      }
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
    DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
    DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);

    return "Flight number " + String.valueOf(flight.getNumber()) + ":\n" + "Departs from " +
            AirportNames.getNamesMap().get(flight.getSource()) + " (" + flight.getSource() + ")" + " on " +
            df.format(flight.getDeparture()) + " at " + tf.format(flight.getDeparture()) + ".\n" +
            "Arrives at " + AirportNames.getNamesMap().get(flight.getDestination()) + " (" +
            flight.getDestination() + ")" + " on " + df.format(flight.getArrival()) + " at " +
            tf.format(flight.getArrival()) + ".\nThe duration of this flight is " +
            ((flight.getArrival().getTime() - flight.getDeparture().getTime()) / 60000) + " minutes.";
  }
}
