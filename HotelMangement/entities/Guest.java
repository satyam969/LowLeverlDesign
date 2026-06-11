package entities;

import java.util.ArrayList;
import java.util.List;

public class Guest {
    private String id;
    private String name;
    private String mail;
    private String phone;
    private List<Reservation> reservations = new ArrayList<>();
    public Guest(String id,String name,String mail,String phone){
        this.id=id;
        this.name=name;
        this.mail=mail;
        this.phone=phone;
    }
    public void addReservation(Reservation r){
        reservations.add(r);
    }
    public List<Reservation> getReservations(){
        return reservations;
    }
}
