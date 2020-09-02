public class Passenger {
    int id;
    String bookingNo;
    String name;
    String address;
    String email;
    String phone_No;

    Booking b = new Booking();

    public Passenger() {
        
    }

    public Passenger(String name) {
        this.name = name;
    }

    public Passenger(int id, String bookingNo, String name, String address, String email, String phone_No) {
        this.id = id;
        this.bookingNo = bookingNo;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone_No = phone_No;
    }
}
