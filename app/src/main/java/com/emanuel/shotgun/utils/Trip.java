package com.emanuel.shotgun.utils;

/**
 * Created by Daniel on 2/19/2016.
 */
public class Trip {
    public String name;
    public String location;
    public String description;
    private long departDateTime;
    private long returnDateTime;

    public long getDepartDateTime() { return departDateTime; }
    public long getReturnDateTime() { return returnDateTime; }

    public void setDepartDateTime(long departDT) { departDateTime = departDT; }
    public void setReturnDateTime(long returnDT) { returnDateTime = returnDT; }
}
