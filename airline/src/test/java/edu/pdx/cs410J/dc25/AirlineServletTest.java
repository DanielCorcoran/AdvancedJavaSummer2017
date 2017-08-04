package edu.pdx.cs410J.dc25;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AirlineServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class AirlineServletTest {

  @Test
  public void addOneFlight() throws ServletException, IOException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "My Airline";
    String departTime = "1/1/2017 12:00 am";
    String source = "PDX";
    String arriveTime = "1/1/2017 12:30 am";
    String destination = "LAX";
    int number = 123;
    String numberAsString = String.valueOf(number);

    HttpServletResponse response = makeRequestOfServlet(
            HttpVerb.POST, servlet, airlineName, departTime, source, arriveTime, destination, numberAsString);
    verify(response).setStatus(HttpServletResponse.SC_OK);

    Airline airline = servlet.getAirline();
    assertThat(airline.getName(), equalTo(airlineName));

    List<Flight> flights = airline.getFlights();
    assertThat(flights.size(), equalTo(1));
    Flight flight = flights.get(0);
    assertThat(flight.getSource(), equalTo(source));
    assertThat(flight.getDestination(), equalTo(destination));
    assertThat(flight.getNumber(), equalTo(number));
  }

  private HttpServletResponse makeRequestOfServlet(HttpVerb verb, AirlineServlet servlet, String airlineName, String departTime, String source, String arriveTime, String destination, String numberAsString) throws IOException, ServletException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("name")).thenReturn(airlineName);
    when(request.getParameter("departTime")).thenReturn(departTime);
    when(request.getParameter("src")).thenReturn(source);
    when(request.getParameter("arriveTime")).thenReturn(arriveTime);
    when(request.getParameter("dest")).thenReturn(destination);
    when(request.getParameter("number")).thenReturn(numberAsString);

    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    switch (verb) {
      case GET:
        servlet.doGet(request, response);
        break;
      case POST:
        servlet.doPost(request, response);
        break;
      default:
        throw new UnsupportedOperationException("Don't know how to" + verb);
    }
    return response;
  }

  @Test
  public void prettyPrintFlightsBetweenAirports() throws IOException, ServletException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "My airline";
    String pdx = "PDX";
    String lax = "LAX";
    String las = "LAS";
    String departTime = "1/1/2017 12:00 am";
    String arriveTime = "1/1/2017 12:30 am";

    HttpServletResponse response;
    response = makeRequestOfServlet(HttpVerb.POST, servlet, airlineName, "1/1/2017 12:00 am", pdx, arriveTime, lax, "123");
    verify(response).setStatus(HttpServletResponse.SC_OK);
    response = makeRequestOfServlet(HttpVerb.POST, servlet, airlineName, "1/1/2017 12:01 am", pdx, arriveTime, las, "234");
    verify(response).setStatus(HttpServletResponse.SC_OK);
    response = makeRequestOfServlet(HttpVerb.POST, servlet, airlineName, "1/1/2017 12:02 am", pdx, arriveTime, lax, "345");
    verify(response).setStatus(HttpServletResponse.SC_OK);

    response = makeRequestOfServlet(HttpVerb.GET, servlet, airlineName, departTime, pdx, arriveTime, lax, null);
    verify(response).setStatus(HttpServletResponse.SC_OK);

    PrintWriter pw = response.getWriter();
    ArgumentCaptor<String> pretty = ArgumentCaptor.forClass(String.class);
    verify(pw).println(pretty.capture());

    assertThat(pretty.getValue(), containsString("123"));
    assertThat(pretty.getValue(), containsString("345"));
    assertThat(pretty.getValue(), not(containsString("LAS")));

  }

  private enum HttpVerb {
    POST, GET
  }
}
