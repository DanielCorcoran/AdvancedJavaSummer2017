package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * An integration test for the {@link Project3} main class.
 */
public class Project3IT extends InvokeMainTestCase {
  private String airline = "airline";
  private String flightNumber = "555";
  private String source = "PDX";
  private String departDate = "7/25/2017";
  private String departTime = "12:00";
  private String departAP = "am";
  private String destination = "DEN";
  private String arriveDate = "7/25/2017";
  private String arriveTime = "12:01";
  private String arriveAP = "am";

  /**
   * Invokes the main method of {@link Project3} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain(Project3.class, args );
  }

  private MainMethodResult createFlight(String airline, String flightNumber, String source, String departDate,
                                        String departTime, String departAP, String destination, String arriveDate,
                                        String arriveTime, String arriveAP) {
    return invokeMain(airline, flightNumber, source, departDate, departTime, departAP, destination, arriveDate, arriveTime, arriveAP);
  }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  public void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }

  @Test
  public void tooFewCommandLineArguments() {
    MainMethodResult result = invokeMain("Baller Air");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }

  @Test
  public void tooManyCommandLineArguments() {
    MainMethodResult result = invokeMain("-print", "-otheroption", airline, flightNumber, source, departDate,
            departTime, departAP, destination, arriveDate, arriveTime, arriveAP, "arg too many!");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments."));
  }

  @Test
  public void tooManyArgumentsInFlightInfo() {
    MainMethodResult result = invokeMain(airline, flightNumber, source, departDate, departTime, departAP,
            destination, arriveDate, arriveTime, arriveAP, "arg too many!");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments."));
  }

  @Test
  public void flightNumberIsNotAnInteger() {
    MainMethodResult result = createFlight(airline, "not a number", source, departDate, departTime,
            departAP, destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(2));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Flight number must contain only numbers"));
  }

  @Test
  public void tooManyNumbersInMonthOfDate() {
    MainMethodResult result = createFlight(airline, flightNumber, source, "111/1/2017", departTime, departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void noNumbersInMonthOfDate() {
    MainMethodResult result = createFlight(airline, flightNumber, source, "/11/2017", departTime, departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void noNumbersInDayOfDate() {
    MainMethodResult result = createFlight(airline, flightNumber, source, "11//2017", departTime, departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void tooManyNumbersInDayOfDate() {
    MainMethodResult result = createFlight(airline, flightNumber, source, "1/111/2017", departTime, departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void notEnoughNumbersInYearOfDate() {
    MainMethodResult result = createFlight(airline, flightNumber, source, "11/11/201", departTime, departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void tooManyNumbersInYearOfDate() {
    MainMethodResult result = createFlight(airline, flightNumber, source, "1/1/20177", departTime, departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void nonNumberInMonth() {
    MainMethodResult result = createFlight(airline, flightNumber, source, "T/1/2017", departTime, departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void nonNumberInDay() {
    MainMethodResult result = createFlight(airline, flightNumber, source, "1/T/2017", departTime, departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void nonNumberInYear() {
    MainMethodResult result = createFlight(airline, flightNumber, source, "1/1/TTTT", departTime, departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void checkToSeeIfThereAreAtLeast4CharsInDate() {
    MainMethodResult result = createFlight(airline, flightNumber, source, "1/1", departTime, departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(3));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void dateHappyPath() {
    MainMethodResult result = createFlight(airline, flightNumber, source, departDate, departTime, departAP, destination,
            arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(null));
  }

  @Test
  public void timeStringIsEmpty() {
    MainMethodResult result = createFlight(airline, flightNumber, source, departDate, "", departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void timeStringHasTooManyChars() {
    MainMethodResult result = createFlight(airline, flightNumber, source, departDate, "121212", departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void hourIsLessThanOneDigitLong() {
    MainMethodResult result = createFlight(airline, flightNumber, source, departDate, ":00", departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void hourIsGreaterThan2DigitsLong() {
    MainMethodResult result = createFlight(airline, flightNumber, source, departDate, "122:0", departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void minutesAreGreaterThan2DigitsLong() {
    MainMethodResult result = createFlight(airline, flightNumber, source, departDate, "1:000", departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void invalidArrivalDateFormat() {
    MainMethodResult result = createFlight(airline, flightNumber, source, departDate, departTime, departAP, destination,
            "1//20177", arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void invalidArrivalTimeFormat() {
    MainMethodResult result = createFlight(airline, flightNumber, source, departDate, departTime, departAP, destination,
            arriveDate, "121212", arriveAP);
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void airportCodeStringEmpty() {
    MainMethodResult result = createFlight(airline, flightNumber, "", departDate, departTime, departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(6));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Airport code must be 3 letters"));
  }

  @Test
  public void airportCodeStringNotAllLetters() {
    MainMethodResult result = createFlight(airline, flightNumber, "3DX", departDate, departTime, departAP,
            destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(6));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Airport code must be 3 letters"));
  }

  @Test
  public void readmeIsFirstArgument() {
    MainMethodResult result =
            invokeMain("-README", airline, flightNumber, source, departDate, departTime, departAP, destination,
                    arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  public void readmeIsSecondArgument() {
    MainMethodResult result =
            invokeMain("-print", "-README", airline, flightNumber, source, departDate, departTime, departAP,
                    destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  public void printIsFirstArgument() {
    MainMethodResult result =
            invokeMain("-print", airline, flightNumber, source, departDate, departTime, departAP, destination,
                    arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  public void readmeIsThirdArgument() {
    MainMethodResult result =
            invokeMain("-print", "-arg", "-README", airline, flightNumber, source, departDate, departTime,
                    departAP, destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  public void airlineNotTheSameAsTextFile() {
    MainMethodResult result =
            invokeMain("-print", "-textFile", "test.txt", "wrong", flightNumber, source, departDate, departTime,
                    departAP, destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(9));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Name of airline from command line"));
  }

  @Test
  public void fileIncorrectlyFormatted() {
    MainMethodResult result =
            invokeMain("-print", "-textFile", "badfile.txt", airline, flightNumber, source, departDate,
                    departTime, departAP, destination, arriveDate, arriveTime, arriveAP);
    assertThat(result.getExitCode(), equalTo(8));
    assertThat(result.getTextWrittenToStandardError(), containsString("File not correctly formatted"));
  }
}