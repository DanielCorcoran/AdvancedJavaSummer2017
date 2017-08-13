package edu.pdx.cs410J.dc25.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;
import edu.pdx.cs410J.AirportNames;

import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A basic GWT class that makes sure that we can send an airline back from the server
 */
public class AirlineGwt implements EntryPoint {

  private final Alerter alerter;
  private final AirlineServiceAsync airlineService;
  private final Logger logger;

  @VisibleForTesting
  Button showAirlineButton;

  public AirlineGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
  }

  @VisibleForTesting
  AirlineGwt(Alerter alerter) {
    this.alerter = alerter;
    this.airlineService = GWT.create(AirlineService.class);
    this.logger = Logger.getLogger("airline");
    Logger.getLogger("").setLevel(Level.INFO);  // Quiet down the default logging
  }

  private void alertOnException(Throwable throwable) {
    Throwable unwrapped = unwrapUmbrellaException(throwable);
    StringBuilder sb = new StringBuilder();
    sb.append(unwrapped.toString());
    sb.append('\n');

    for (StackTraceElement element : unwrapped.getStackTrace()) {
      sb.append("  at ");
      sb.append(element.toString());
      sb.append('\n');
    }

    this.alerter.alert(sb.toString());
  }

  private Throwable unwrapUmbrellaException(Throwable throwable) {
    if (throwable instanceof UmbrellaException) {
      UmbrellaException umbrella = (UmbrellaException) throwable;
      if (umbrella.getCauses().size() == 1) {
        return unwrapUmbrellaException(umbrella.getCauses().iterator().next());
      }

    }

    return throwable;
  }

  private void addWidgets(VerticalPanel panel) {
    final TextBox airlineNameBox = makeTextBox("Airline name");
    final TextBox flightNumberBox = makeTextBox("Flight number");
    final TextBox sourceBox = makeTextBox("Departing airport code");
    final TextBox destBox = makeTextBox("Arrival airport code");
    final TextBox searchSourceBox = makeTextBox("Departing airport code to search");
    final TextBox searchDestBox = makeTextBox("Arrival airport code to search");

    final DatePicker departDatePicker = new DatePicker();
    departDatePicker.setValue(new Date());
    final DatePicker arriveDatePicker = new DatePicker();
    arriveDatePicker.setValue(new Date());

    final ListBox departHourBox = makeHourBox();
    final ListBox departMinuteBox = makeMinuteBox();
    final ListBox departAmPmBox = makeAmPmBox();
    final ListBox arriveHourBox = makeHourBox();
    final ListBox arriveMinuteBox = makeMinuteBox();
    final ListBox arriveAmPmBox = makeAmPmBox();

    Button addAirlineToServerButton = new Button("Add new airline");
    addAirlineToServerButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        addAirline(airlineNameBox.getText());
      }
    });

    showAirlineButton = new Button("Show Airline");
    showAirlineButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        showAirline();
      }
    });

    Button addFlightButton = new Button("Add new flight");
    addFlightButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        if (isAirportCodeLegal(sourceBox.getText().toUpperCase()) &&
                isAirportCodeLegal(destBox.getText().toUpperCase())) {
          int flightNumber = verifyFlightNumberIsInteger(flightNumberBox.getValue());
          if (flightNumber != -1) {
            DateTimeFormat format = DateTimeFormat.getFormat("MM/dd/yyyy");
            String depart = format.format(departDatePicker.getValue()) + " " +
                    departHourBox.getSelectedItemText() + ":" +
                    departMinuteBox.getSelectedItemText() + " " +
                    departAmPmBox.getSelectedItemText();
            String arrive = format.format(departDatePicker.getValue()) + " " +
                    departHourBox.getSelectedItemText() + ":" +
                    departMinuteBox.getSelectedItemText() + " " +
                    departAmPmBox.getSelectedItemText();

            addFlight(flightNumber, sourceBox.getText().toUpperCase(), depart, destBox.getText().toUpperCase(), arrive);
          }
        } else {
          alerter.alert("Airport codes for departure and arrival airports must both be valid");
        }
      }
    });

    Button searchForFlightsButton = new Button("Search for flights");
    searchForFlightsButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        searchForFlights(searchSourceBox.getText(), searchDestBox.getText());
      }
    });

    panel.add(airlineNameBox);
    panel.add(addAirlineToServerButton);
    panel.add(showAirlineButton);
    panel.add(flightNumberBox);
    panel.add(sourceBox);
    panel.add(departDatePicker);
    panel.add(departHourBox);
    panel.add(departMinuteBox);
    panel.add(departAmPmBox);
    panel.add(destBox);
    panel.add(arriveDatePicker);
    panel.add(arriveHourBox);
    panel.add(arriveMinuteBox);
    panel.add(arriveAmPmBox);
    panel.add(addFlightButton);
    panel.add(searchSourceBox);
    panel.add(searchDestBox);
    panel.add(searchForFlightsButton);
  }

  private TextBox makeTextBox(String textToSet) {
    final TextBox airlineNameBox = new TextBox();
    airlineNameBox.setText(textToSet);
    airlineNameBox.addFocusHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent focusEvent) {
        airlineNameBox.setText("");
      }
    });
    return airlineNameBox;
  }

  private ListBox makeHourBox() {
    ListBox hourBox = new ListBox();
    for (int i = 1; i <= 12; ++i) {
      hourBox.addItem(String.valueOf(i));
    }
    hourBox.setVisibleItemCount(1);
    return hourBox;
  }

  private ListBox makeAmPmBox() {
    ListBox amPmBox = new ListBox();
    amPmBox.addItem("am");
    amPmBox.addItem("pm");
    amPmBox.setVisibleItemCount(1);
    return amPmBox;
  }

  private ListBox makeMinuteBox() {
    ListBox minuteBox = new ListBox();
    for (int i = 0; i <= 59; ++i) {
      if (i < 10) {
        minuteBox.addItem("0" + String.valueOf(i));
      } else {
        minuteBox.addItem(String.valueOf(i));
      }
    }
    minuteBox.setVisibleItemCount(1);
    return minuteBox;
  }

  private void showAirline() {
    logger.info("Calling getAirline");
    airlineService.getAirline(new AsyncCallback<Airline>() {

      @Override
      public void onFailure(Throwable ex) {
        alerter.alert(ex.getMessage());
      }

      @Override
      public void onSuccess(Airline airline) {
        StringBuilder sb = new StringBuilder(airline.toString());
        Collection<Flight> flights = airline.getFlights();
        for (Flight flight : flights) {
          sb.append(flight);
          sb.append("\n");
        }
        alerter.alert(sb.toString());
      }
    });
  }

  private void addAirline(String airlineName) {
    logger.info("Adding airline");
    airlineService.addAirlineToServer(airlineName, new AsyncCallback<Void>() {

      @Override
      public void onFailure(Throwable ex) {
        alerter.alert(ex.getMessage());
      }

      @Override
      public void onSuccess(Void aVoid) {
        alerter.alert("Added airline to server");
      }
    });
  }

  private void addFlight(int flightNumber, String source, String departDateTime, String dest, String arriveDateTime) {
    logger.info("Adding flight");
    airlineService.addFlightToServer(
            flightNumber, source, departDateTime, dest, arriveDateTime, new AsyncCallback<Void>() {
              @Override
              public void onFailure(Throwable ex) {
                alerter.alert(ex.getMessage());
              }

              @Override
              public void onSuccess(Void aVoid) {
                alerter.alert("Added flight to airline");
              }
            });
  }

  private void searchForFlights(String source, String destination) {
    logger.info("Searching for flights");
    airlineService.searchServerForFlights(source, destination, new AsyncCallback<Airline>() {
      @Override
      public void onFailure(Throwable ex) {
        alerter.alert(ex.getMessage());
      }

      @Override
      public void onSuccess(Airline airline) {
        StringBuilder sb = new StringBuilder(airline.toString());
        Collection<Flight> flights = airline.getFlights();
        for (Flight flight : flights) {
          sb.append(flight);
          sb.append("\n");
        }
        alerter.alert(sb.toString());
      }
    });
  }

  /**
   * Checks if an airport code is 3 chars, all chars are letters, and code is a real airport
   *
   * @param airportCode
   *        Argument to be tested
   * @return true if airport code is in correct format and a valid airport
   */
  private boolean isAirportCodeLegal(String airportCode) {
    return (airportCode.length() == 3 && (Character.isLetter(airportCode.charAt(0)) &&
            Character.isLetter(airportCode.charAt(1)) && Character.isLetter(airportCode.charAt(2))) &&
            AirportNames.getNamesMap().containsKey(airportCode));
  }

  /**
   * Checks if the flight number passed in is an integer
   *
   * @param flightNumberIn
   *        Command line argument to be tested
   * @return
   *        Returns flight number as an integer if valid.  Otherwise exits the program with an error message.
   */
  private int verifyFlightNumberIsInteger(String flightNumberIn) {
    int flightNumber = -1;

    try {
      flightNumber = Integer.parseInt(flightNumberIn);
    } catch (NumberFormatException e) {
      alerter.alert("Flight number must contain only numbers");
    }

    return flightNumber;
  }

  @Override
  public void onModuleLoad() {
    setUpUncaughtExceptionHandler();

    // The UncaughtExceptionHandler won't catch exceptions during module load
    // So, you have to set up the UI after module load...
    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        setupUI();
      }
    });

  }

  private void setupUI() {
    RootPanel rootPanel = RootPanel.get();
    VerticalPanel panel = new VerticalPanel();
    rootPanel.add(panel);

    addWidgets(panel);
  }

  private void setUpUncaughtExceptionHandler() {
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      @Override
      public void onUncaughtException(Throwable throwable) {
        alertOnException(throwable);
      }
    });
  }

  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }
}
