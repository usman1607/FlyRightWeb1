import java.util.*;
//import java.text.*;
import java.sql.*;

public class AirlineManagementSystem {

	Booking booking = new Booking();
	Aircraft aircraft = new Aircraft();
	Passenger passenger = new Passenger();

	static FlightManager flightManager;
	static BookingManager bookingManager;
	static AircraftManager aircraftManager;	
	static PassengerManager passengerManager;
	static Scanner reader = new Scanner(System.in);

	public static void main(String [] args) throws ClassNotFoundException, SQLException{
       
	   // Load the JDBC driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("Driver loaded");

		// Connect to a database
		Connection connection = DriverManager.getConnection
		("jdbc:mysql://localhost/airlinedb?userTimeZone=UTC&serverTimeZone=UTC" , "root", "Oluwatobiloba007");
		System.out.println("Database connected");

		flightManager = new FlightManager(connection);
		bookingManager = new BookingManager(connection);
		aircraftManager = new AircraftManager(connection);
		passengerManager = new PassengerManager(connection);

		boolean flag = true;
		while(flag) {
			showMainMenu();
			String option = reader.nextLine();
			if(option.equals("0")) {
				flag=false;
			}else {
               showSubMenu(option);
			}
		}
	}

	public static void showMainMenu(){
		System.out.println("Enter 0 to exit");
		System.out.println("Enter 1 to Manage Aircrafts");
		System.out.println("Enter 2 to Manage Flights");
		System.out.println("Enter 3 to Manage Bookings");
		System.out.println("Enter 4 to Manage Passengers");
	}

	public static void showSubMenu(String option){

		if(option.equals("1")){
			aircraftManager.showManageAirCraftsMenu();
			String subOption = reader.nextLine();
			aircraftManager.handleManageAirCraftsAction(subOption);
		}
		else if(option.equals("2")) {
			flightManager.showManageFlightsMenu();
            String subOption = reader.nextLine();
            flightManager.handleManageFlightsAction(subOption);
		}
		else if(option.equals("3")) {
			bookingManager.showManageBookingsMenu();
            String subOption = reader.nextLine();
            bookingManager.handleManageBookingsAction(subOption);
		}
		else if(option.equals("4")) {
			passengerManager.showManagePassengersMenu();
            String subOption = reader.nextLine();
            passengerManager.handleManagePassengersAction(subOption);
		}
		//showSubMenu(option);
	}
}