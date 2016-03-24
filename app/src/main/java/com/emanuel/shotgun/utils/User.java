package com.emanuel.shotgun.utils;

import java.util.Date;

/**
 * Created by Daniel on 3/23/2016.
 */
public class User {
    public String username;
    public String firstName;
    public String lastName;
    public int carOccupancy;
    public int carMPG;

    public boolean isDriver(){
        return carOccupancy != 0;
    }
}
