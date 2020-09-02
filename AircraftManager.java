import java.util.*;
import java.sql.*;

public class AircraftManager {

	List<Aircraft> aircrafts;
	static Scanner reader = new Scanner(System.in);

    Connection connection;
	public AircraftManager(Connection connection){
		this.connection = connection;
	}

    public List<Aircraft> getAll() {
        aircrafts = new ArrayList<Aircraft>();
	    try{
			// create a statement
			Statement statement = connection.createStatement();

			//Excecute a sttement
			ResultSet resultSet = statement.executeQuery("select id, reg_No, type, name, capacity from aircrafts");

		    // Iterate through the result 
			while (resultSet.next()) {

                int id = resultSet.getInt(1);
                String reg_No = resultSet.getString(2);
                String type = resultSet.getString(3);
                String name = resultSet.getString(4);
                int capacity = resultSet.getInt(5);
                Aircraft aircraft = new Aircraft(id, reg_No, type, name, capacity);
                aircrafts.add(aircraft);
		    }
	    }
     	    catch(SQLException ex){
        	ex.printStackTrace();
        }
        return aircrafts;
    }

    public void show(Aircraft a){
		//System.out.printf("%d \t %s \t %s \t %s \t %d \n", a.id, a.reg_No, a.type, a.name, a.capacity);
		System.out.println(a.id +" "+ a.reg_No +" "+ a.type +" "+ a.name +" "+ a.capacity);
    }

    public void list() {
        List<Aircraft> aircrafts = getAll();
        for(Aircraft a: aircrafts) {
			show(a);
        }
    }

    public boolean create(String reg_No, String type, String name, int capacity) throws Exception {
        try{
			// Create a statement
			Statement statement = connection.createStatement();

			// Execute a statement
			int count= statement.executeUpdate("insert into aircrafts (reg_No,type,name,capacity) values ('"+reg_No+"','"+type+"','"+name+"','"+capacity+"')");
			if(count >0) {
				return true;
			}
		}
			catch(SQLException ex){
        	ex.printStackTrace();
     	}
		return false;
    }

    public Aircraft find(String reg_No){
		Aircraft a = null;
		try{
			// create a statement
			Statement statement = connection.createStatement();

			//Excecute a sttement
			ResultSet resultSet = statement.executeQuery("select id, reg_No, type, name, capacity from aircrafts where reg_No = '"+reg_No+"'");

		// Iterate through the result 
			while (resultSet.next()) {

			int id = resultSet.getInt(1);
			String type = resultSet.getString(3);
			String name = resultSet.getString(4);
			int capacity = resultSet.getInt(5);
			a = new Aircraft(id,reg_No,type,name,capacity);
		}
	    }
     	    catch(SQLException ex){
        	ex.printStackTrace();
        }
     	return a;
	}

    public void findAndShow(String reg_No){
        Aircraft a = find(reg_No);
        if(a == null){
            System.out.printf("There is no Aircraft %s  amaong the Aircrafts...\n",reg_No); 
            return;
        }
        System.out.println("Aircraft found!");
		System.out.printf("%d \t %s \t %s \t %s \t %d \n", a.id, a.reg_No, a.type, a.name, a.capacity);
		//System.out.println(aircr.toString());
    }

    public boolean updateAir(String reg_No, String type, String name, int capacity){
		try{
			// Create a statement
			Statement statement = connection.createStatement();

			// Execute a statement
			int count = statement.executeUpdate("update aircrafts set type ='"+type+"',name='"+name+"',capacity='"+capacity+"' where reg_No='"+reg_No+"'");
			if(count >0) {
				return true;
			}
		}
			catch(SQLException ex){
        	ex.printStackTrace();
     	}
		return false;
	}

	public boolean removeAir(String reg_No){
		try{
			// Create a statement
			Statement statement = connection.createStatement();

			// Execute a statement
			int count = statement.executeUpdate("delete from aircrafts where reg_No='"+reg_No+"'");
			if(count >0) {
				return true;
			}
		}
			catch(SQLException ex){
        	ex.printStackTrace();
     	}
		return false;
	}

    public void showManageAirCraftsMenu(){
		System.out.println("Enter 0 to return to Main Menu");
		System.out.println("Enter 1 to Create Aircraft");
		System.out.println("Enter 2 to List Aircrafts");
		System.out.println("Enter 3 to Remove Aircraft");
		System.out.println("Enter 4 to Find an Aircraft");
		System.out.println("Enter 5 to Update an Aircraft");
	}

	public void handleManageAirCraftsAction(String action){
        if(action.equals("0")){
            return;
            //showMainMenu();
        }else if(action.equals("2")){
            list();
        }
        else if(action.equals("3")){
            System.out.println("Enter the Reg_No of Aircraft to remove: ");
            String reg_No = reader.nextLine();
            removeAir(reg_No);
        }
        else if(action.equals("4")){
            System.out.println("Enter the Reg_No of Aircraft to find: ");
            String reg_No = reader.nextLine();
            findAndShow(reg_No);
        }
        else if(action.equals("5")){
            System.out.println("Enter the Reg_No of Aircraft to update: ");
            String reg_No = reader.nextLine();

            System.out.println("Enter the Aircraft Name: ");
            String name = reader.nextLine();
            System.out.println("Enter the Aircraft Type: ");
            String type = reader.nextLine();
            System.out.println("Enter the Aircraft Capacity: ");
            int capacity = reader.nextInt();
            reader.nextLine();
            updateAir(reg_No, type, name, capacity);
        }
        else if (action.equals("1")){
		try{
				System.out.println("Enter the Aircraft No: ");
				String aircraftNo = reader.nextLine();
				System.out.println("Enter the Aircraft Name: ");
				String aircraftName = reader.nextLine();
				System.out.println("Enter the Aircraft Type: ");
				String aircraftType = reader.nextLine();
				System.out.println("Enter the Aircraft Capacity: ");
				int aircraftCapacity = reader.nextInt();
				reader.nextLine();
				create(aircraftNo, aircraftType, aircraftName, aircraftCapacity);
			}
				catch(Exception ex){
        		ex.printStackTrace();
     		}
        }
	}
}