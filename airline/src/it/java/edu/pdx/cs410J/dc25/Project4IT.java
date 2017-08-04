package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * An integration test for {@link Project4} that invokes its main method with
 * various arguments
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Project4IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    @Test
    public void test0RemoveAirline() throws IOException {
        AirlineRestClient client = new AirlineRestClient(HOSTNAME, Integer.parseInt(PORT));
        client.removeAirline();
    }

    @Test
    public void test1AddOneFlight() {
        MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, "My Airline", "123", "PDX", "1/1/2017", "12:00", "am", "LAX", "1/1/2017", "12:30", "am");
        assertThat(result.getTextWrittenToStandardError(), result.getExitCode(), equalTo(null));
    }

    @Test
    public void test2AddMoreFlights() {
        MainMethodResult result;
        result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, "My Airline", "234", "PDX", "1/1/2017", "12:01", "am", "LAS", "1/1/2017", "12:30", "am");
        assertThat(result.getTextWrittenToStandardError(), result.getExitCode(), equalTo(null));
        result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, "My Airline", "345", "PDX", "1/1/2017", "12:02", "am", "LAX", "1/1/2017", "12:30", "am");
        assertThat(result.getTextWrittenToStandardError(), result.getExitCode(), equalTo(null));

        result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, "-search", "My Airline", "PDX", "LAX");
        assertThat(result.getTextWrittenToStandardError(), result.getExitCode(), equalTo(0));

        String pretty = result.getTextWrittenToStandardOut();

        assertThat(pretty, containsString("123"));
        assertThat(pretty, containsString("345"));
        assertThat(pretty, not(containsString("LAS")));
    }
}