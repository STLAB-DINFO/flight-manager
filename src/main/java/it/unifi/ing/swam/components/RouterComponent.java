package it.unifi.ing.swam.components;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class RouterComponent {

    public String navigate(String loginType) {
        if (loginType.equals("user-login")) return "login?faces-redirect=true";
        if (loginType.equals("booking-login")) return "booking-login?faces-redirect=true";
        if (loginType.equals("booking-list")) return "booking-list?faces-redirect=true";
        return "";
    }

}
