import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class BookingManager {
    
    List<Flight> ava_f;
    List<Booking> bookings;
    FlightManager flightManager;
    //AircraftManager aircraftManager = new AircraftManager();
    PassengerManager passengerManager;
    Scanner reader = new Scanner(System.in);

    Connection connection;
	public BookingManager(Connection connection){
		this.connection = connection;
	}

    public List<Booking> getAll() {
        bookings = new ArrayList<Booking>();
	    try{
			// create a statement
			Statement statement = connection.createStatement();

			//Excecute a sttement
			ResultSet resultSet = statement.executeQuery("select id, number, flightNo, date_Time, takeOff_Point, destination, seat_No from bookings");
            
		    // Iterate through the result 
			while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String number = resultSet.getString(2);
                String flightNo = resultSet.getString(3);
                Date date_Time = resultSet.getDate(4);
                String takeOff_Point = resultSet.getString(5);
                String destination = resultSet.getString(6);
                int seat_No = resultSet.getInt(7);
                Booking booking = new Booking(id, number, flightNo, date_Time, takeOff_Point, destination, seat_No);
                bookings.add(booking);
		    }
	    }
     	    catch(SQLException ex){
        	ex.printStackTrace();
        }
        return bookings;
    }

    public BookingManager(FlightManager flightManager) {
        this.flightManager = flightManager;
    }
    
    public void show(Booking b){
        System.out.println(b.id + " " + b.number + " " + b.flightNo + " " + b.date_Time + " " + b.takeOff_Point + " " + b.destination + " " + b.seat_No);
    }
    
    public void list() {
        List<Booking> bookings = getAll();
        for(Booking b: bookings){
            show(b);
        }
    }

    public boolean createBooking(String number, String flightNo, Date date_Time, String takeOff_point, String destination,  int seat_No) {
        try{
			// Create a statement
			Statement statement = connection.createStatement();

			// Execute a statement
			int count= statement.executeUpdate("insert into bookings (number, flightNo, date_Time, takeOff_point, destination, seat_No) values ('"+number+"','"+flightNo+"','"+date_Time+"','"+takeOff_point+"','"+destination+"', '"+seat_No+"')");
			if(count >0) {
				return true;
			}
		}
			catch(SQLException ex){
        	ex.printStackTrace();
     	}
		return false;
    }

    public void bookFlight(String takeOff_Point, String destination) {
        
        flightManager = new FlightManager(connection);
        passengerManager = new PassengerManager(connection);
        ava_f = flightManager.findAvailableFlight(takeOff_Point, destination);
        int check;
        String flightNo;
        do {
            System.out.print("Enter the Fligth number of your choosed Flight: ");
            flightNo = reader.nextLine();
            check = flightManager.checkAvaFlight(ava_f, flightNo);
        }while(check == 0);
        Flight f = flightManager.find(flightNo);
        System.out.printf("Cost of the flight is %f.\n", f.price);
        System.out.println("Do you want to proceed to book the flight? ");
        System.out.println("Please enter (1) to proceed or (0) to cancel: ");
        int yes_No = reader.nextInt(); reader.nextLine();
        if(yes_No == 0) {
            return;
        }
        System.out.print("Enter your Name:");
        String name = reader.nextLine();
        System.out.print("Enter your Address: ");
        String address = reader.nextLine();
        System.out.print("Enter your Email: ");
        String email = reader.nextLine();
        System.out.print("Enter your Phone No: ");
        String phone_No = reader.nextLine();
        System.out.print("Enter number of passenger: ");
        int No_passenger = reader.nextInt(); reader.nextLine();
        
        // Make payment here............

        f.availableSeats -= No_passenger;
        flightManager.updateAvaSeat(flightNo, f.availableSeats);
        int seat_No = f.availableSeats + No_passenger;

        List<String>  b_numbers = getAllBookingNo();
        String number = generateUniqueId(b_numbers);

        System.out.println("Thank you for choosing to fly with us...");

        createBooking(number, flightNo, f.takeOff_Time, takeOff_Point, destination, seat_No);
        passengerManager.create(number, name, address, email, phone_No);
    }

    private List<String> getAllBookingNo() {
        List<String> b_numbers = new ArrayList<String>();
        try{
			// create a statement
			Statement statement = connection.createStatement();

			//Excecute a sttement
			ResultSet resultSet = statement.executeQuery("select number from bookings");
            
		    // Iterate through the result 
			while (resultSet.next()) {
                String number = resultSet.getString(1);
                b_numbers.add(number);
		    }
	    }
     	    catch(SQLException ex){
        	ex.printStackTrace();
        }
        return b_numbers;
    }

    public String generateUniqueId(List<String>  b_numbers) {
        Random rand = new Random(); //instance of random class
        //generate random values from 0-1000000
        int num = 0, count = 0; String uniqueId;
        do{
            num = rand.nextInt(1000000);
            uniqueId = ( "00" + Integer.toString(num) );
            for(String bn : b_numbers) {
                if(bn.equals(uniqueId)) {
                    count++;
                }
            }
        }while(count != 0);
        
        return uniqueId;
    }

    public Booking find(String number){
        Booking b = null;
		try{
			// create a statement
			Statement statement = connection.createStatement();

			//Excecute a sttement
			ResultSet resultSet = statement.executeQuery("select id, number, flightNo, date_Time, takeOff_Point, destination, seat_No from bookings where number = '"+number+"'");

		// Iterate through the result 
			while (resultSet.next()) {

            int id = resultSet.getInt(1);
            String flightNo = resultSet.getString(3);
            Date date_Time = resultSet.getDate(4);
            String takeOff_Point = resultSet.getString(5);
            String destination = resultSet.getString(6);
            int seat_No = resultSet.getInt(7);
            b = new Booking(id, number, flightNo, date_Time, takeOff_Point, destination, seat_No);
		}
	    }
     	    catch(SQLException ex){
        	ex.printStackTrace();
        }
     	return b;
    }

    public void findBook(String number){
        Booking b = find(number);
        if(b == null){
            System.out.printf("There is no Booking with number %s in the Bookings...\n",number); 
            return;
        }
        System.out.println(b.id + " " + number + " " + b.flightNo + " " + b.date_Time + " " + b.takeOff_Point + " " + b.destination + " " + b.seat_No);
    }

    public boolean update(String number, String flightNo, Date date_Time, String takeOff_Point, String destination, int seat_No) {
        try{
			// Create a statement
			Statement statement = connection.createStatement();

			// Execute a statement
			int count = statement.executeUpdate("update bookings set flightNo ='"+flightNo+"',date_Time='"+date_Time+"',takeOff_Point='"+takeOff_Point+"',destination='"+destination+"',seat_No='"+seat_No+"' where number='"+number+"'");
			if(count >0) {
				return true;
			}
		}
			catch(SQLException ex){
        	ex.printStackTrace();
     	}
		return false;
    }

    public boolean removeBook(String number){
        try{
			// Create a statement
			Statement statement = connection.createStatement();

			// Execute a statement
			int count = statement.executeUpdate("delete from bookings where number='"+number+"'");
			if(count >0) {
				return true;
			}
		}
			catch(SQLException ex){
        	ex.printStackTrace();
     	}
		return false;
    }
    
    public void showManageBookingsMenu(){
		System.out.println("Enter 0 to return to Main Menu");
		System.out.println("Enter 1 to Book a Flight");
		System.out.println("Enter 2 to List Bookings");
		System.out.println("Enter 3 to Remove Booking");
		System.out.println("Enter 4 to Find a Booking");
		System.out.println("Enter 5 to Update a Booking");
	}

	public void handleManageBookingsAction(String action){
		try{
			if(action.equals("0")){
                return;
				//showMainMenu();
			}else if(action.equals("2")){
				list();
			}
			else if(action.equals("3")){
				System.out.print("Enter the number of Booking to remove: ");
				String Booking_No = reader.nextLine();
				removeBook(Booking_No);
			}
			else if(action.equals("4")){
				System.out.print("Enter the number of Booking to find: ");
				String number = reader.nextLine();
				findBook(number);
			}
			else if(action.equals("5")){
				System.out.print("Enter the number of Booking to update: ");
				String number = reader.nextLine();

				System.out.print("Enter the flightNo No: ");
				String flightNo = reader.nextLine();
				System.out.print("Enter the takeOff_Point: ");
                String takeOff_Point = reader.nextLine();
                System.out.print("Enter the destination: ");
                String destination = reader.nextLine();
                
				System.out.print("Enter Date and Time (yyyy-mm-dd hh:mm:ss): ");
				String pDate = reader.nextLine();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                java.util.Date utildate = formatter.parse(pDate);
				Date date_Time = new Date(utildate.getTime());
				System.out.print("Enter the Seat_No: ");
				int seat_No = reader.nextInt(); reader.nextLine();
				update(number, flightNo, date_Time, takeOff_Point, destination, seat_No);
			}
			else if (action.equals("1")){
				System.out.print("Enter your take off point: ");
				String takeOff_Point = reader.nextLine();
				System.out.print("Enter your destination: ");
				String destination = reader.nextLine();
				bookFlight(takeOff_Point, destination);
            }
		}
        catch(Exception ex){
            System.out.println(ex.getMessage());      
		}
	}
}