import java.util.*;
import java.sql.*;

public class PassengerManager {
    
    List<Passenger> passengers;
    BookingManager bookingManager;
    static Scanner reader = new Scanner(System.in);

    Connection connection;
	public PassengerManager(Connection connection){
		this.connection = connection;
	}

    public List<Passenger> getAll() {
        passengers = new ArrayList<Passenger>();
        try{
			// create a statement
			Statement statement = connection.createStatement();

			//Excecute a sttement
			ResultSet resultSet = statement.executeQuery("select id, bookingNo, name, address, email, phone_No from passengers");

		    // Iterate through the result 
			while (resultSet.next()) {

                int id = resultSet.getInt(1);
                String bookingNo = resultSet.getString(2);
                String name = resultSet.getString(3);
                String address = resultSet.getString(4);
                String email = resultSet.getString(5);
                String phone_No = resultSet.getString(6);
                Passenger passenger = new Passenger(id, bookingNo, name, address, email, phone_No);
                passengers.add(passenger);
		    }
	    }
     	    catch(SQLException ex){
        	ex.printStackTrace();
        }
        return passengers;
    }

    public PassengerManager(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }

    public void show(Passenger p){
        System.out.println(p.id + " " + p.bookingNo + " " + p.name + " " + p.address + " " + p.email + " " + p.phone_No);
    }
    
    public void list() {
        List<Passenger> passengers = getAll();
        for(Passenger p: passengers){
            show(p);
        }
    }

    public boolean create(String bookingNo, String name, String address, String email, String phone_No) {
        try{
			// Create a statement
			Statement statement = connection.createStatement();

			// Execute a statement
			int count= statement.executeUpdate("insert into passengers (bookingNo,name,address,email,phone_No) values ('"+bookingNo+"','"+name+"','"+address+"','"+email+"','"+phone_No+"')");
			if(count >0) {
				return true;
			}
		}
			catch(SQLException ex){
        	ex.printStackTrace();
     	}
		return false;
    }

    public Passenger find(String bookingNo){
        Passenger p = null;
		try{
			// create a statement
			Statement statement = connection.createStatement();

			//Excecute a sttement
			ResultSet resultSet = statement.executeQuery("select id, bookingNo, name, address, email, phone_No from passengers where bookingNo = '"+bookingNo+"'");

		// Iterate through the result 
			while (resultSet.next()) {

            int id = resultSet.getInt(1);
            String name = resultSet.getString(3);
            String address = resultSet.getString(4);
            String email = resultSet.getString(5);
            String phone_No = resultSet.getString(6);
			p = new Passenger(id, bookingNo, name, address, email, phone_No);
		}
	    }
     	    catch(SQLException ex){
        	ex.printStackTrace();
        }
     	return p;
    }

    public void findPers(String bookingNo){
        Passenger pers = find(bookingNo);
        if(pers == null){
            System.out.printf("There is no Passenger with %s booking in the Passengers...\n",bookingNo); 
            return;
        }
        System.out.println(pers.id + " " + bookingNo + " " + pers.name + " " + pers.address + " " + pers.email + " " + pers.phone_No);
    }

    public boolean update(String bookingNo, String name, String address, String email, String phone_No) {
        try{
			// Create a statement
			Statement statement = connection.createStatement();

			// Execute a statement
			int count = statement.executeUpdate("update passengers set name ='"+name+"',address='"+address+"',email='"+email+"',phone_No='"+phone_No+"' where bookingNo='"+bookingNo+"'");
			if(count >0) {
				return true;
			}
		}
			catch(SQLException ex){
        	ex.printStackTrace();
     	}
		return false;
    }

    public boolean removePers(String bookingNo){
        try{
			// Create a statement
			Statement statement = connection.createStatement();

			// Execute a statement
			int count = statement.executeUpdate("delete from passengers where bookingNo='"+bookingNo+"'");
			if(count >0) {
				return true;
			}
		}
			catch(SQLException ex){
        	ex.printStackTrace();
     	}
		return false;
    }
    
    public void showManagePassengersMenu(){
		System.out.println("Enter 0 to return to Main Menu");
		System.out.println("Enter 1 to List Passengers");
		System.out.println("Enter 2 to Remove Passenger");
		System.out.println("Enter 3 to Find a Passenger");
        System.out.println("Enter 4 to Update a Passenger");
        //System.out.println("Enter 5 to Create Passenger");
	}

	public void handleManagePassengersAction(String action){
		if(action.equals("0")){
            return;
			//showMainMenu();
		}else if(action.equals("1")){
			list();
		}
		else if(action.equals("2")){
			System.out.print("Enter bookingNo of Passenger to remove: ");
			String bookingNo = reader.nextLine();
			removePers(bookingNo);
		}
		else if(action.equals("3")){
			System.out.print("Enter the bookingNo of Passenger to find: ");
			String bookingNo = reader.nextLine();
			findPers(bookingNo);
		}
		else if(action.equals("4")){
			System.out.print("Enter the bookingNo of Passenger to update: ");
            String bookingNo = reader.nextLine();
            
			System.out.print("Enter the Passenger Name: ");
			String name = reader.nextLine();
			System.out.print("Enter the Passenger Address: ");
			String address = reader.nextLine();
			System.out.print("Enter the Passenger Email: ");
			String email = reader.nextLine();
			System.out.print("Enter the Passenger Phone No: ");
			String phone_No = reader.nextLine();
			update(bookingNo, name, address, email, phone_No);
		}
	}
}