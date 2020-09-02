import java.sql.Date;

public class Booking {
    int id;
    String number;
    String flightNo;
    Date date_Time;
    String takeOff_Point;
    String destination;
    int seat_No;

    public Booking(){

    }

    public Booking(int id, String number, String flightNo, Date date_Time, String takeOff_Point, String destination,  int seat_No){
        this.id = id;
        this.number = number;
        this.flightNo = flightNo;
        this.date_Time = date_Time;
        this.takeOff_Point = takeOff_Point;
        this.destination = destination;
        this.seat_No = seat_No;
    }
}

