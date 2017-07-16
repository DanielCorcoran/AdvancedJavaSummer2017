package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <code>TextParser</code> class takes a file specified from the command line, verifies that the file can be read,
 * then reads the file and creates an instance of an {@link Airline} class and an instance of the {@link Flight} class
 * for each flight in the file.  The flights are added to the {@link Airline} class.
 */
public class TextParser implements AirlineParser{
  private String fileName;

  TextParser(String fileNameIn) {
    this.fileName = fileNameIn;
  }

  /**
   * This method takes the file name (established in the class constructor), checks if it is valid, and throws an
   * exception if it isn't.  The method then reads the file line by line to create an {@link Airline} and each
   * {@link Flight} associated with it.
   *
   * @return Returns the {@link Airline} object
   * @throws ParserException
   *         Exception thrown if file name is not valid or if the file is incorrectly formatted
   */
  @Override
  public AbstractAirline parse() throws ParserException {

    try (Scanner read_in = new Scanner(new File(this.fileName))) {
      read_in.useDelimiter("\\|");                  //Uses the pipe as a delimiter

      String line = read_in.nextLine();             //Reads a line and splits it at each pipe,
      String [] part = line.split("\\|");     //then creates a variable for each split

      Airline airline = new Airline(part[0]);
      int flightNumber = Integer.valueOf(part[1]);
      String sourceAirport = part[2];
      String departTime = part[3];
      String destinationAirport = part[4];
      String arriveTime = part[5];

      //Creates a flight with the data read in and adds it to the airline
      Flight flight = new Flight(flightNumber, sourceAirport, departTime, destinationAirport, arriveTime);
      airline.addFlight(flight);

      while (read_in.hasNext()) {          //Continues to create flights as long as not at EOF
        line = read_in.nextLine();
        part = line.split("\\|");

        flightNumber = Integer.valueOf(part[0]);
        sourceAirport = part[1];
        departTime = part[2];
        destinationAirport = part[3];
        arriveTime = part[4];

        flight = new Flight(flightNumber, sourceAirport, departTime, destinationAirport, arriveTime);
        airline.addFlight(flight);
      }
      read_in.close();
      return airline;
    } catch (FileNotFoundException e) {
      throw new ParserException("Cannot find file to read from");
    } catch (Exception e) {
      throw new ParserException("File not correctly formatted");
    }
  }
}
