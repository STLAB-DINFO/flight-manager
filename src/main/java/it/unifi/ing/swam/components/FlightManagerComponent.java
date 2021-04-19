package it.unifi.ing.swam.components;


import it.unifi.ing.swam.dao.FlightDao;
import it.unifi.ing.swam.model.Flight;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Named
@ConversationScoped
public class FlightManagerComponent implements Serializable {

    static final long serialVersionUID = 120948301L;
    private static final int maxMinutesConversation = 15;

    @Inject
    private FlightDao flightDao;

    @Inject
    private Conversation conversation;
    private List<Flight> flightsOut;
    private List<Flight> flightsBack;

/*
    @PostConstruct
    private void init(){
        System.out.println("[CREATED] - " + this.getClass() + " has just been constructed");
    }
*/

    public boolean existsResult(){
        if (flightsOut==null) return false;
        return !flightsOut.isEmpty();
    }

    public void start() {
        if(conversation.isTransient()){
            conversation.begin();
            conversation.setTimeout(1000 * 60 * maxMinutesConversation);
            System.out.println("started new conversation with cid: " + conversation.getId());
        }
    }


    //si suppone sia un fetch di tutti i dati del volo
    public Flight fetchFlight(Long flightID){
        return flightDao.getFlightData(flightID);
    }

    public void stop() {
        if(!conversation.isTransient())
            System.out.println("Conversation with ID " + conversation.getId() + " is being closed");
            conversation.end();
    }

    //si suppone siano dei fetch parziali
    public void searchFlightsOut(Date departureDate, String source, String destination) {
        if(flightsOut == null || flightsOut.isEmpty()) {
            flightsOut = flightDao.getFlights(source, destination, departureDate);
        }
    }

    //si suppone siano dei fetch parziali
    public void searchFlightsBack(Date backDate, String source, String destination) {
        if(flightsBack == null || flightsBack.isEmpty()) {
            flightsBack = flightDao.getFlights(destination, source, backDate);
        }
    }


    public List<Flight> getFlightsOut() {
        return flightsOut;
    }

    public List<Flight> getFlightsBack() {
        return flightsBack;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
