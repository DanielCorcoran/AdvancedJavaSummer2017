package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.AbstractFlight;

public class Flight extends AbstractFlight {
  private int flightNumber;
  private String source;
  private String destination;
  private String depart;
  private String arrival;

  Flight(int flightNumberIn, String sourceIn, String departIn, String destinationIn, String arrivalIn) {
    this.flightNumber = flightNumberIn;
    this.source = sourceIn;
    this.depart = departIn;
    this.destination = destinationIn;
    this.arrival = arrivalIn;
  }

  @Override
  public int getNumber() {
    return this.flightNumber;
  }

  @Override
  public String getSource() {
    return this.source;
  }

  @Override
  public String getDepartureString() {
    return this.depart;
  }

  @Override
  public String getDestination() {
    return this.destination;
  }

  @Override
  public String getArrivalString() {
    return this.arrival;
  }
}
