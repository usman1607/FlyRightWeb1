import java.sql.Date;

public class Flight {
    int id;
    String number;
    String aircraftNo;
    double price;
    String takeOff_Point;
    Date takeOff_Time;
    String destination;
    int availableSeats;

    Aircraft aircraft = new Aircraft();

    public Flight() {

    }

    public Flight(double price) {
        this.price = price;
    }

    public Flight(int id, String number, String aircraftNo, double price, String takeOff_Point, Date takeOff_Time, String destination, int availableSeats){
        this.id = id;
        this.number = number;
        this.aircraftNo = aircraftNo;
        this.price = price;
        this.takeOff_Point = takeOff_Point;
        this.takeOff_Time = takeOff_Time;
        this.destination = destination;
        this.availableSeats = availableSeats;
    }
}
