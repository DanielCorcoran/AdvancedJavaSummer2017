package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirlineDumper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * The <code>TextDumper</code> class takes the {@link Airline} and all of its associated {@link Flight} in the program
 * and writes the data to a file specified on the command line in a format congruent with how the {@link TextParser}
 * class reads the file.  If no file is found, then <code>TextDumper</code> will create one with the name passed in and
 * write the current airline and flight data to it.
 */
public class TextDumper implements AirlineDumper {
  private String fileName;

  TextDumper(String fileNameIn) {
    this.fileName = fileNameIn;
  }

  /**
   * This method takes the file name (established in the class constructor), creates a new file if one with that name
   * isn't present, and writes the {@link Airline} and {@link Flight} data into it, overwriting any data that was
   * present in the original file.
   *
   * @param abstractAirline
   *        Airline and all its associated flight data to be written to file
   * @throws IOException
   *        Exception thrown if file can't be written to (which should never occur)
   */
  @Override
  public void dump(AbstractAirline abstractAirline) throws IOException {
    try (FileWriter fw = new FileWriter(this.fileName); BufferedWriter bw = new BufferedWriter(fw)) {
      //Turns collection of flights in airline object to an array of flights
      Flight[] flightArray =
              (Flight[]) abstractAirline.getFlights().toArray(new Flight[abstractAirline.getFlights().size()]);
      String toWrite = abstractAirline.getName() + "|" + createFlightInfoString(flightArray[0]);

      for (int i = 1; i < flightArray.length; ++i) {
        toWrite += "\n" + createFlightInfoString(flightArray[i]);
      }

      bw.write(toWrite);
    } catch (IOException e) {
      System.err.println("Can't write to file");
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
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    return String.valueOf(flight.getNumber()) + "|" + flight.getSource() + "|" + df.format(flight.getDeparture()) +
            "|" + flight.getDestination() + "|" + df.format(flight.getArrival()) + "|";
  }
}
