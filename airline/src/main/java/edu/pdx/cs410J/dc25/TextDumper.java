package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;

/**
 *
 */
public class TextDumper implements AirlineDumper {
  private String fileName;

  public TextDumper(String fileNameIn) {
    this.fileName = fileNameIn;
  }

  @Override
  public void dump(AbstractAirline abstractAirline) throws IOException {

  }
}
