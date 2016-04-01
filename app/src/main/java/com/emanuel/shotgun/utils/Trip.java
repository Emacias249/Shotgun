package com.emanuel.shotgun.utils;

import java.util.ArrayList;

/**
 * Created by Daniel on 2/19/2016.
 */
public class Trip {

    public int creatorId;
    public int driverId;
    public String name;
    public String location;
    public String description;
    private long departDateTime;
    private long returnDateTime;
    public ArrayList<Integer> riderIds;

    public long getDepartDateTime() { return departDateTime; }
    public long getReturnDateTime() { return returnDateTime; }

    public void setDepartDateTime(long departDT) { departDateTime = departDT; }
    public void setReturnDateTime(long returnDT) { returnDateTime = returnDT; }
}
