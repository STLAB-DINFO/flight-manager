package it.unifi.ing.swam.components;

import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.model.ModelFactory;
import it.unifi.ing.swam.model.temp.TemporaryReservationSeats;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Dependent
public class TemporaryReservationComponent implements Serializable {

    private static final long serialVersionUID = 27620988221L;

    private boolean graciouslyCleaned = false;

    @Inject
    private TemporaryReservationRepository temporaryReservations;

    private ArrayList<Long> temporaryReservationSeatsList;

    @PostConstruct
    public void init(){
        System.out.println("[OPEN] - Componente* "+ this.toString() +" avviato");
    }

    public void reserveTemporarySeats(List<Flight> flightsList, int nPassengers) {
        if(temporaryReservationSeatsList == null || temporaryReservationSeatsList.isEmpty()) {
            temporaryReservationSeatsList = new ArrayList<Long>();
        }
        TemporaryReservationSeats temporaryReservationSeats;
        System.out.println("Temporary booking for " + nPassengers + " passengers");
        for(int i = 0; i < flightsList.size(); i++) {
            temporaryReservationSeats = ModelFactory.temporaryReservationSeats();
            temporaryReservationSeats.setDate( new Date() );
            temporaryReservationSeats.setFlight(flightsList.get(i));
            temporaryReservationSeats.setnPassengers(nPassengers);
            temporaryReservationSeats = temporaryReservations.addTemporarySeats(temporaryReservationSeats);
            if(temporaryReservationSeats != null ){
                temporaryReservationSeatsList.add(temporaryReservationSeats.getId());
            }

        }
    }


    public int getTemporaryReservedSeats(Flight flight){
        return temporaryReservations.getTemporaryReservedSeatsOfFlight(flight);
    }

    @PreDestroy
    public void cleanTemporarySeats(){
        if(!graciouslyCleaned){
            System.out.println("Context of user is closing, cleaning temporary seats");
            System.out.println("[CLOSE] - Componente "+ this.toString() +" distrutto");
            removeTemporarySeats();
        }
        else System.out.println("Context of user is closing - Componente "+ this.toString() +" distrutto");
    }


    public void removeTemporarySeats(){
        System.out.println("Cleaning temporary seats .. ");
        for(int i = 0; i < temporaryReservationSeatsList.size(); i++){
            System.out.println("Removing booking " + (i+1) + " of " + temporaryReservationSeatsList.size() );
            temporaryReservations.removeTemporaryReservation(temporaryReservationSeatsList.get(i));
        }
        temporaryReservationSeatsList.clear();
        graciouslyCleaned = true;
        System.out.println("Temporary seats graciously cleaned");
    }

}
