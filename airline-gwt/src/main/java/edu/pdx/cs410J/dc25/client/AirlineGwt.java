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

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class sets up the UI for the website and handles the client side interaction (button events, etc)
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

  /**
   * This method adds widgets to the main panel and creates the UI.  Buttons are created and their events are handled
   * in this method.
   * @param panel
   *        Main panel to populate with UI widgets
   */
  private void addWidgets(VerticalPanel panel) {
    String helpString = "This website is a way to maintain flight information for a specific airline.\n" +
            "To begin, click on the Add/Display Airline tab.\n" +
            "You must enter an airline before you can use the other features.\n" +
            "Once an airline is entered, you may enter flights for that airline.\n\n" +
            "To enter a flight into the system, enter the departure data (on the left), " +
            "the arrival data (on the right), and click the Add flight button.\n" +
            "If the flight is unique, it will be added to the system.\n\n" +
            "To search for flights, enter the airport code of the departing and arriving airports you wish to search.\n" +
            "If any matches are found, they will be displayed in the box below.";

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

    final TextArea airlineInfo = new TextArea();
    airlineInfo.setCharacterWidth(80);
    airlineInfo.setVisibleLines(10);
    final TextArea searchInfo = new TextArea();
    searchInfo.setCharacterWidth(80);
    searchInfo.setVisibleLines(10);
    final TextArea helpInfo = new TextArea();
    helpInfo.setCharacterWidth(80);
    helpInfo.setVisibleLines(12);
    helpInfo.setText(helpString);

    TabPanel tabPanel = new TabPanel();
    VerticalPanel airlinePanel = new VerticalPanel();
    VerticalPanel flightPanel = new VerticalPanel();
    VerticalPanel searchPanel = new VerticalPanel();
    VerticalPanel helpPanel = new VerticalPanel();

    HorizontalPanel airlineButtonsPanel = new HorizontalPanel();
    HorizontalPanel airportCodePanel = new HorizontalPanel();
    HorizontalPanel datePickerPanel = new HorizontalPanel();
    HorizontalPanel timePanel = new HorizontalPanel();

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
        showAirline(airlineInfo);
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
                    arriveHourBox.getSelectedItemText() + ":" +
                    arriveMinuteBox.getSelectedItemText() + " " +
                    arriveAmPmBox.getSelectedItemText();

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
        searchForFlights(searchInfo, searchSourceBox.getText().toUpperCase(), searchDestBox.getText().toUpperCase());
      }
    });

    airlinePanel.add(airlineNameBox);
    airlinePanel.add(airlineButtonsPanel);
    airlineButtonsPanel.add(addAirlineToServerButton);
    airlineButtonsPanel.add(showAirlineButton);
    airlinePanel.add(airlineInfo);
    flightPanel.add(flightNumberBox);
    flightPanel.add(airportCodePanel);
    airportCodePanel.add(sourceBox);
    flightPanel.add(datePickerPanel);
    datePickerPanel.add(departDatePicker);
    flightPanel.add(timePanel);
    timePanel.add(departHourBox);
    timePanel.add(departMinuteBox);
    timePanel.add(departAmPmBox);
    airportCodePanel.add(destBox);
    datePickerPanel.add(arriveDatePicker);
    timePanel.add(arriveHourBox);
    timePanel.add(arriveMinuteBox);
    timePanel.add(arriveAmPmBox);
    flightPanel.add(addFlightButton);
    searchPanel.add(searchSourceBox);
    searchPanel.add(searchDestBox);
    searchPanel.add(searchForFlightsButton);
    searchPanel.add(searchInfo);
    helpPanel.add(helpInfo);
    tabPanel.add(airlinePanel, "Add/Display Airline");
    tabPanel.add(flightPanel, "Add Flight");
    tabPanel.add(searchPanel, "Search for Flights");
    tabPanel.add(helpPanel, "Help");
    panel.add(tabPanel);
  }

  /**
   * Creates a text box and sets its text
   * @param textToSet
   *        Text to set as default for the text box
   * @return
   *        Returns the new text box
   */
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

  /**
   * Creates a list box for the hours of the flight time
   * @return
   *        Returns the new list box
   */
  private ListBox makeHourBox() {
    ListBox hourBox = new ListBox();
    for (int i = 1; i <= 12; ++i) {
      hourBox.addItem(String.valueOf(i));
    }
    hourBox.setVisibleItemCount(1);
    return hourBox;
  }

  /**
   * Creates a list box for AM/PM distinction of the flight time
   * @return
   *        Returns the new list box
   */
  private ListBox makeAmPmBox() {
    ListBox amPmBox = new ListBox();
    amPmBox.addItem("am");
    amPmBox.addItem("pm");
    amPmBox.setVisibleItemCount(1);
    return amPmBox;
  }

  /**
   * Creates a list box for minutes of the flight time
   * @return
   *        Returns the new list box
   */
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

  /**
   * Retrieves <code>Flight</code> data for the airline and calls the <code>PrettyPrinter</code> to display it
   * in the appropriate text area.
   * @param airlineInfo
   *        Text area in which to display the airline info
   */
  private void showAirline(final TextArea airlineInfo) {
    logger.info("Calling getAirline");
    airlineService.getAirline(new AsyncCallback<Airline>() {

      @Override
      public void onFailure(Throwable ex) {
        alerter.alert(ex.getMessage());
      }

      @Override
      public void onSuccess(Airline airline) {
        PrettyPrinter pretty = new PrettyPrinter();
        airlineInfo.setText(pretty.httpDump(airline, null, null));
      }
    });
  }

  /**
   * Adds an <code>Airline</code> to the server if one does not already exist.  Alerts the user with an error otherwise.
   * @param airlineName
   *        Name of the airline to add to the server
   */
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

  /**
   * Adds a <code>Flight</code> to the server.  Does not add the <code>Flight</code> if an identical one exists
   * or if there is no airline in the server.
   * @param flightNumber
   *        Number of new flight
   * @param source
   *        Source airport code of new flight
   * @param departDateTime
   *        Departure date and time of new flight
   * @param dest
   *        Destination airport code of new flight
   * @param arriveDateTime
   *        Arrival date and time of new flight
   */
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

  /**
   * Searches for flights from specified airports and reports results to the appropriate text area
   * @param searchInfo
   *        Text area to report results of the search
   * @param sourceIn
   *        Airport code of the source of the flight
   * @param destinationIn
   *        Airport code of the destination of the flight
   */
  private void searchForFlights(final TextArea searchInfo, String sourceIn, String destinationIn) {
    final String source = sourceIn;
    final String destination = destinationIn;

    logger.info("Searching for flights");
    airlineService.searchServerForFlights(sourceIn, destinationIn, new AsyncCallback<Airline>() {
      @Override
      public void onFailure(Throwable ex) {
        alerter.alert(ex.getMessage());
      }

      @Override
      public void onSuccess(Airline airline) {
        PrettyPrinter pretty = new PrettyPrinter();
        searchInfo.setText(pretty.httpDump(airline, source, destination));
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
