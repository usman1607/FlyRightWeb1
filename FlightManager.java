import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.text.*;

public class FlightManager {
    
    List<Flight> ava_f;
    List<Flight> flights;
    AircraftManager aircraftManager; 
    static Scanner reader = new Scanner(System.in);

    Connection connection;
	public FlightManager(Connection connection){
		this.connection = connection;
	}
    
    public List<Flight> getAll() {
        flights = new ArrayList<Flight>();
	    try{
			// create a statement
			Statement statement = connection.createStatement();

			//Excecute a sttement
			ResultSet resultSet = statement.executeQuery("select id, number, aircraftNo, price, takeOff_Point, takeOff_Time, destination, availableSeats from flights");

		    // Iterate through the result 
			while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String number = resultSet.getString(2);
                String aircraftNo = resultSet.getString(3);
                double price = resultSet.getDouble(4);
                String takeOff_Point = resultSet.getString(5);
                Date takeOff_Time = resultSet.getDate(6);
                String destination = resultSet.getString(7);
                int availableSeats = resultSet.getInt(8);
                Flight flight = new Flight(id, number, aircraftNo, price, takeOff_Point, takeOff_Time, destination, availableSeats);
                flights.add(flight);
		    }
	    }
     	    catch(SQLException ex){
        	ex.printStackTrace();
        }
        return flights;
    }

    public FlightManager(AircraftManager aircraftManager) {
        this.aircraftManager = aircraftManager; 
    }

    public void show(Flight f){
        //System.out.printf("%d %s %s %f %s %tc %s %d \n",f.id, f.number, f.aircraftNo, f.price, f.takeOff_Point, f.takeOff_Time, f.destination, f.availableSeats);
        System.out.println(f.id + " " + f.number + " " + f.aircraftNo + " " + f.price + " " + f.takeOff_Point + " " + f.takeOff_Time + " " + f.destination + " " + f.availableSeats);
    }

    public void list() {
        List<Flight> flights = getAll();
        for(Flight f: flights){
            show(f);
        }
    }

   // Aircraft airc = new Aircraft();
    public boolean create(String number, String aircraftNo, double price, String takeOff_Point, Date takeOff_Time, String destination, int availableSeats){
        aircraftManager = new AircraftManager(connection);
        Aircraft aircraft = aircraftManager.find(aircraftNo);
        if(aircraft == null){
            System.out.printf("Aircfratf %s cannot be found \n",aircraftNo); 
            return false;
        }
        try{
			// Create a statement
			Statement statement = connection.createStatement();

			// Execute a statement
			int count= statement.executeUpdate("insert into flights (number, aircraftNo, price, takeOff_Point, takeOff_Time, destination, availableSeats) values ('"+number+"','"+aircraftNo+"','"+price+"','"+takeOff_Point+"','"+takeOff_Time+"','"+destination+"','"+availableSeats+"')");
			if(count >0) {
				return true;
			}
		}
			catch(SQLException ex){
        	ex.printStackTrace();
     	}
		return false;
    }

    public Flight find(String number){
		Flight f = null;
		try{
			// create a statement
			Statement statement = connection.createStatement();

			//Excecute a sttement
			ResultSet resultSet = statement.executeQuery("select id, number, aircraftNo, price, takeOff_Point, takeOff_Time, destination, availableSeats from flights where number = '"+number+"'");

		// Iterate through the result 
			while (resultSet.next()) {

            int id = resultSet.getInt(1);
            String aircraftNo = resultSet.getString(3);
            double price = resultSet.getDouble(4);
            String takeOff_Point = resultSet.getString(5);
            Date takeOff_Time = resultSet.getDate(6);
            String destination = resultSet.getString(7);
            int availableSeats = resultSet.getInt(8);
            f = new Flight(id, number, aircraftNo, price, takeOff_Point, takeOff_Time, destination, availableSeats);
            
		}
	    }
     	    catch(SQLException ex){
        	ex.printStackTrace();
        }
     	return f;
	}

    public void findAndShow(String number){
        Flight f = find(number);
        if(f == null){
            System.out.printf("There is no Flight %s  amaong the Flights...\n",number); 
            return;
        }
        System.out.println("Flight found!");
        System.out.println(f.id + " " + f.number + " " + f.aircraftNo + " " + f.price + " " + f.takeOff_Point + " " + f.takeOff_Time + " " + f.destination + " " + f.availableSeats);
    }

    public List<Flight> findAvailableFlight(String takeOff_Point, String destination) {
        int count = 0;
        ava_f = new ArrayList<Flight>();
        List<Flight> flights = getAll();
        for(Flight f: flights){
            if((f.takeOff_Point.equals(takeOff_Point)) && (f.destination.equals(destination)) && (f.availableSeats > 0)){
                ava_f.add(f);
                count++;
            }
        }
        if(count == 0) {
            System.out.printf("There is no Flight going from %s to %s available now.\n", takeOff_Point, destination);
            return null;
        }else{
            System.out.printf("There are/is %d Flight going from %s to %s available, they are:\n", count, takeOff_Point, destination);
            System.out.println("Id" + "   " + "Flight No." + " " + "Take Off Point" + " " + "takeOff_time" + " " + "Destination"  + " " + "Price");
            System.out.println("============================================================================");
            for(Flight af : ava_f) {
                System.out.println(af.id + " " + af.number + " " + af.takeOff_Point + " " + af.takeOff_Time + " " + af.destination + " " + af.price);
            }
        }
        return ava_f;
    }

    public int checkAvaFlight(List<Flight> ava_fl, String flightNo) {
        int num = 0;
        for(Flight f : ava_fl) {
            if(f.number.equals(flightNo)) {
                num++;
            }
        }
        return num;
    }

    public boolean updateFli(String number, String aircraftNo, double price, String takeOff_Point, Date takeOff_Time, String destination, int availableSeats){
		try{
			// Create a statement
			Statement statement = connection.createStatement();

			// Execute a statement
			int count = statement.executeUpdate("update flights set aircraftNo ='"+aircraftNo+"',price='"+price+"',takeOff_Point='"+takeOff_Point+"', takeOff_Time='"+takeOff_Time+"', destination='"+destination+"', availableSeats='"+availableSeats+"' where number='"+number+"'");
			if(count >0) {
				return true;
			}
		}
			catch(SQLException ex){
        	ex.printStackTrace();
     	}
		return false;
    }
    
    public boolean updateAvaSeat(String number, int availableSeats){
		try{
			// Create a statement
			Statement statement = connection.createStatement();

			// Execute a statement
			int count = statement.executeUpdate("update flights set availableSeats='"+availableSeats+"' where number='"+number+"'");
			if(count >0) {
				return true;
			}
		}
			catch(SQLException ex){
        	ex.printStackTrace();
     	}
		return false;
	}

    public boolean removeFli(String number){
		try{
			// Create a statement
			Statement statement = connection.createStatement();

			// Execute a statement
			int count = statement.executeUpdate("delete from flights where number='"+number+"'");
			if(count >0) {
				return true;
			}
		}
			catch(SQLException ex){
        	ex.printStackTrace();
     	}
		return false;
	}

    public void showManageFlightsMenu(){

		System.out.println("Enter 0 to return to Main Menu");
		System.out.println("Enter 1 to Create Flight");
		System.out.println("Enter 2 to List Flights");
		System.out.println("Enter 3 to Remove Flight");
		System.out.println("Enter 4 to Find a Flight");
		System.out.println("Enter 5 to Update a Flight");
	}

	public void handleManageFlightsAction(String action){
		try{
			if(action.equals("0")){
                return;
				//showMainMenu();
			}else if(action.equals("2")){
				list();
			}
			else if(action.equals("3")){
				System.out.println("Enter the Flight_No of Flight to remove?");
				String flight_No = reader.nextLine();
				removeFli(flight_No);
			}
			else if(action.equals("4")){
				System.out.println("Enter the Number of Flight to find?");
				String number = reader.nextLine();
				findAndShow(number);
			}
			else if(action.equals("5")){
				System.out.println("Enter the number of Flight to update: ");
				String number = reader.nextLine();

				System.out.println("Enter the aircraft No:");
				String aircraftNo = reader.nextLine();
				System.out.println("Enter the Flight price: ");
				double price = reader.nextDouble();
				reader.nextLine();
				System.out.println("Enter the Take_Off Point: ");
				String takeOff_Point = reader.nextLine();
				System.out.println("Enter Date and Time (yyyy-mm-dd hh:mm:ss): ");
				String pDate = reader.nextLine();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                java.util.Date utildate = formatter.parse(pDate);
				Date takeOff_Time = new Date(utildate.getTime());
				System.out.println("Enter the Destination: ");
                String destination = reader.nextLine();
                System.out.println("Enter the availableSeats: ");
				int availableSeats = reader.nextInt(); reader.nextLine();
				updateFli(number, aircraftNo, price, takeOff_Point, takeOff_Time, destination, availableSeats);
			}
			else if (action.equals("1")) {
				System.out.println("Enter the flight No:");
				String flight_No = reader.nextLine();
				System.out.println("Enter the aircraft No:");
				String aircraftno = reader.nextLine();
				System.out.println("Enter the Flight price: ");
				double price = reader.nextDouble(); reader.nextLine();
				System.out.println("Enter the Take_Off Point: ");
				String takeOff_Point = reader.nextLine();
				System.out.println("Enter Date and Time (yyyy-mm-dd hh:mm:ss): ");
				String pDate = reader.nextLine();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                java.util.Date utildate = formatter.parse(pDate);
				Date takeOff_Time = new Date(utildate.getTime());
				System.out.println("Enter the Destination: ");
				String destination = reader.nextLine();
				System.out.println("Enter the availableSeats: ");
				int availableSeats = reader.nextInt(); reader.nextLine();
				create(flight_No, aircraftno, price, takeOff_Point, takeOff_Time, destination, availableSeats);
			}
		}
        catch(Exception ex){
            System.out.println(ex.getMessage());      
		}
	}
    
}