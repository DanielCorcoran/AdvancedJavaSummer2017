package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * An integration test for the {@link Project1} main class.
 */
public class Project1IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project1} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project1.class, args );
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
            invokeMain("arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "arg7", "arg8", "arg too many!");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments."));
  }

  @Test
  public void flightNumberIsNotAnInteger() {
    MainMethodResult result =
            invokeMain("arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(2));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Flight number (second argument) must contain only numbers"));
  }

  @Test
  public void tooManyNumbersInMonthOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "1111", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void noNumbersInMonthOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "/11/1111", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void noNumbersInDayOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "10//1111", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void tooManyNumbersInDayOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/111", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void notEnoughNumbersInYearOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void tooManyNumbersInYearOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/11111", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(3));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void nonNumberInMonth() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "1t/11/1111", "", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void nonNumberInDay() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/t1/1111", "", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void nonNumberInYear() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/t111", "", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(4));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void checkToSeeIfThereAreAtLeast4CharsInDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "1/1", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(3));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void dateHappyPath() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1111", "00:00", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(null));
  }

  @Test
  public void timeStringIsEmpty() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1111", "", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void timeStringHasTooManyChars() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1111", "111111", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void hourIsLessThanOneDigitLong() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1111", ":0000", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void hourIsGreaterThan2DigitsLong() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1111", "000:0", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }

  @Test
  public void minutesAreGreaterThan2DigitsLong() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1111", "0:000", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(5));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Time not in the correct HH:MM format"));
  }
}