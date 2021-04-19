package it.unifi.ing.swam.components;

import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.model.temp.TemporaryReservationSeats;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class TemporaryReservationRepository { // TemporaryReservationRepository

    private Long tosID;
    private Map<Long, TemporaryReservationSeats> temporaryReservations;


    @PostConstruct
    private void init(){
        tosID = 0L;
        temporaryReservations = new HashMap<>();

    }
    // forse diventerà addTemporarySeats
    public TemporaryReservationSeats addTemporarySeats(TemporaryReservationSeats temporaryReservationSeats){
        Long id = temporaryReservationSeats.getId();
        if(id != null){ // si aggiorna una booking già presente
            temporaryReservations.replace(id, temporaryReservationSeats);
            return temporaryReservations.get(id);
        } else {
            temporaryReservationSeats.setId(tosID);
            temporaryReservations.put(tosID, temporaryReservationSeats);
            tosID ++;
            return temporaryReservationSeats;
        }

    }

    public int getTemporaryReservedSeatsOfFlight(Flight flight){
        int counter=0;
        for (TemporaryReservationSeats tempSeat: temporaryReservations.values()) {
            if(tempSeat.getFlight().getId().longValue() == flight.getId().longValue()){
                counter += tempSeat.getnPassengers();
            }
        }
        return counter;
    }

    public void removeTemporaryReservation(Long reservationID){
        temporaryReservations.remove(reservationID);
    }

    @PreDestroy
    private void free(){
        tosID = null;
        temporaryReservations = null;
    }

}
