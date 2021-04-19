package it.unifi.ing.swam.controller.booking;

import it.unifi.ing.swam.components.LoggedUserComponent;
import it.unifi.ing.swam.components.TemporaryReservationComponent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;

@SessionScoped
@Named
public class RegisteredBookingController extends BookingController implements Serializable {

    static final long serialVersionUID = 4891678203L;

    @Inject
    protected LoggedUserComponent loggedUserComponent;

    @PostConstruct
    private void init(){
        System.out.println("[CREATED] - this component: " + this.getClass() + " has just been constructed");
    }

    @Transactional
    public String save() {
        booking.setRegisteredUserOwner(loggedUserComponent.getUser());
        return super.save();
    }


    public String cancel(){ //non ci sono conversazioni da chiudere
        return "index?faces-redirect=true";
    }



}
