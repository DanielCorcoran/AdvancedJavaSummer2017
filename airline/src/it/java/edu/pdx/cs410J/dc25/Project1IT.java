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
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Flight number (second argument) must contain only numbers"));
  }

  @Test
  public void tooManyNumbersInMonthOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "111/1/1111", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void noNumbersInMonthOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "/1/1111", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void noNumbersInDayOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "10//1111", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void tooManyNumbersInDayOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/111/1111", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void notEnoughNumbersInYearOfDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "11/11/1", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

  @Test
  public void checkToSeeIfThereAreAtLeast4CharsInDate() {
    MainMethodResult result =
            invokeMain("arg1", "0", "arg3", "1/1", "arg5", "arg6", "arg7", "arg8");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(),
            containsString("Date not in the correct MM/DD/YYYY format"));
  }

}