package edu.pdx.cs410J.dc25;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.SimpleTimeZone;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send key/value pairs.
 */
public class AirlineRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "airline";
    private static final String SERVLET = "flights";

    private final String url;


    /**
     * Creates a client to the airline REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    AirlineRestClient(String hostName, int port)
    {
        this.url = String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET);
    }

    String getAllFlights(String airlineName) throws IOException {
      Response response = get(this.url, "name", airlineName);
      return response.getContent();
    }

    String searchForFlights(String airlineName, String source, String destination) throws IOException {
      Response response = get(this.url, "name", airlineName, "src", source, "dest", destination);
      throwExceptionIfNotOkayHttpStatus(response);
      return response.getContent();
    }

    void addFlightToServer(String airlineName, Flight flight) throws IOException {
      Response response = postToMyURL("name", airlineName,
              "number", String.valueOf(flight.getNumber()), "src", flight.getSource(),
              "departTime", flight.getDepartDataMember(), "dest", flight.getDestination(),
              "arriveTime", flight.getArrivalDataMember());
      throwExceptionIfNotOkayHttpStatus(response);
    }

    /**
     * Returns all keys and values from the server
     */
    Map<String, String> getAllKeysAndValues() throws IOException {
      Response response = get(this.url);
      return Messages.parseKeyValueMap(response.getContent());
    }

    /**
     * Returns the value for the given key
     */
    String getValue(String key) throws IOException {
      Response response = get(this.url, "key", key);
      throwExceptionIfNotOkayHttpStatus(response);
      String content = response.getContent();
      return Messages.parseKeyValuePair(content).getValue();
    }

    void addKeyValuePair(String key, String value) throws IOException {
      Response response = postToMyURL("key", key, "value", value);
      throwExceptionIfNotOkayHttpStatus(response);
    }

    @VisibleForTesting
    Response postToMyURL(String... keysAndValues) throws IOException {
      return post(this.url, keysAndValues);
    }

    void removeAllMappings() throws IOException {
      Response response = delete(this.url);
      throwExceptionIfNotOkayHttpStatus(response);
    }

    private Response throwExceptionIfNotOkayHttpStatus(Response response) {
      int code = response.getCode();
      if (code != HTTP_OK) {
        throw new AppointmentBookRestException(code);
      }
      return response;
    }

    private class AppointmentBookRestException extends RuntimeException {
      AppointmentBookRestException(int httpStatusCode) {
        super("Got an HTTP Status Code of " + httpStatusCode);
      }
    }
}
