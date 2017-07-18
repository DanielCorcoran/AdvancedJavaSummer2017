package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * An integration test for the {@link Project2} main class.
 */
public class Project2IT extends InvokeMainTestCase {

  /**
   * Invokes the main method of {@link Project2} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain( Project2.class, args );
  }

  private static File airlineFile;

  @BeforeClass
  public static void createTempDirectoryForAirlineFile() throws IOException {
    File tmpDirectory = new File(System.getProperty("java.io.tmpdir"));
    airlineFile = new File(tmpDirectory, "airline.txt");
  }

  @AfterClass
  public static void deleteTempDirectoryForAirlineFile() {
    if (airlineFile.exists()) {
      assertTrue(airlineFile.delete());
    }
  }

  private String readFile(File file) throws FileNotFoundException {
    BufferedReader br = new BufferedReader(new FileReader(file));
    StringBuilder sb = new StringBuilder();
    Stream<String> lines = br.lines();
    lines.forEach(line -> sb.append(line).append("\n"));

    return sb.toString();
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
    MainMethodResult result =
            invokeMain("-print", "-otheroption", "arg1", "arg2", "arg3", "arg4",
                    "arg5", "arg6", "1/1/1111", "00:00", "arg too many!");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments."));
  }

  @Test
  public void tooManyArgumentsInFlightInfo() {
    MainMethodResult result =
            invokeMain("arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "1/1/1111", "00:00", "arg too many!");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments."));
  }

  @Test
  public void flightNumberIsNotAnInteger() {
    MainMethodResult result =
            invokeMain("arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(2));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Flight number must contain only numbers"));
  }

  @Test
  public void tooManyNumbersInMonthOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "1111//1111", "arg5", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void noNumbersInMonthOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "/11/1111", "arg5", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void noNumbersInDayOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "10//1111", "arg5", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void tooManyNumbersInDayOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/111/111", "arg5", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void notEnoughNumbersInYearOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/111", "arg5", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void tooManyNumbersInYearOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/11111", "arg5", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(3));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void nonNumberInMonth() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "1t/11/1111", "", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void nonNumberInDay() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/t1/1111", "", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void nonNumberInYear() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/t111", "", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void checkToSeeIfThereAreAtLeast4CharsInDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "1/1", "arg5", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(3));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void dateHappyPath() {
    MainMethodResult result =
            invokeMain("arg1", "0", "AAA", "11/11/1111", "00:00", "AAA", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(null));
  }

  @Test
  public void timeStringIsEmpty() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1111", "", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void timeStringHasTooManyChars() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1111", "111111", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void hourIsLessThanOneDigitLong() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1111", ":0000", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void hourIsGreaterThan2DigitsLong() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1111", "000:0", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void minutesAreGreaterThan2DigitsLong() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg", "11/11/1111", "0:000", "arg", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void invalidArrivalDateFormat() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1111", "arg5", "arg6", "1//11111", "00:00");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void invalidArrivalTimeFormat() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1111", "00:00", "arg6", "1/1/1111", "0000000");
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void airportCodeStringEmpty() {
    MainMethodResult result =
            invokeMain("arg1", "0", "", "11/11/1111", "00:00", "arg6", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(6));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Airport code must be 3 letters"));
  }

  @Test
  public void airportCodeStringNotAllLetters() {
    MainMethodResult result =
            invokeMain("arg1", "0", "3al", "11/11/1111", "00:00", "arg", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(6));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Airport code must be 3 letters"));
  }

  @Test
  public void readmeIsFirstArgument() {
    MainMethodResult result =
            invokeMain("-README", "arg1", "0", "3al", "11/11/1111", "00:00", "arg", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  public void readmeIsSecondArgument() {
    MainMethodResult result =
            invokeMain("-print", "-README", "arg1", "0", "3al", "11/11/1111", "00:00", "arg", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  public void printIsFirstArgument() {
    MainMethodResult result =
            invokeMain("-print", "arg1", "0", "aal", "11/11/1111", "00:00", "arg", "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  public void readmeIsThirdArgument() {
    MainMethodResult result =
            invokeMain("-print", "-arg", "-README", "arg1", "0", "3al", "11/11/1111", "00:00", "arg",
                    "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  public void textFileNotTextFileFormatExtension() {
    MainMethodResult result =
            invokeMain("-print", "-arg", "-textFile", "file.q", "arg1", "0", "aal", "11/11/1111", "00:00", "arg",
                    "1/1/1111", "00:00");
    assertThat(result.getExitCode(), equalTo(7));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("File extension not valid for a text file"));
  }

  @Test
  public void test1CreateNewAirlineFileWhenFileDoesNotExist() throws FileNotFoundException {
    assertThat(airlineFile.exists(), equalTo(false));

    invokeMain("-textFile", airlineFile.getAbsolutePath(), "MyAirline",
            "123", "PDX", "7/16/2017 15:00", "LAX", "7/16/2017", "18:00");

    String fileContents = readFile(airlineFile);
    assertThat(fileContents, containsString("123"));
  }

  @Test
  public void test2AddFlightToExistingAirlineFile() throws FileNotFoundException {
    assertThat(airlineFile.exists(), equalTo(true));

    invokeMain("-textFile", airlineFile.getAbsolutePath(), "MyAirline",
            "234", "PDX", "7/17/2017 15:00", "LAX", "7/17/2017", "18:00");

    String fileContents = readFile(airlineFile);
    assertThat(fileContents, containsString("123"));
    assertThat(fileContents, containsString("234"));
  }
}