package com.emanuel.shotgun;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.emanuel.shotgun.utils.*;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Daniel on 3/23/2016.
 */
public class DBHelper {

    private static final String TYPE_TEXT = "TEXT";
    private static final String TYPE_INT = "INTEGER";
    private static final String PK = "PRIMARY KEY";
    private static final String COMMA = ", ";

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;

    private final Context context;

    //**********************        CONTRACT        *******************************

    private static abstract class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "UserTable";

        public static final String COLUMN_USERNAME = "Username";
        public static final String COLUMN_PASSWORD = "Password";
        public static final String COLUMN_FIRST_NAME = "FirstName";
        public static final String COLUMN_LAST_NAME = "LastName";
        public static final String COLUMN_CAR_OCCUPANCY = "CarOccupancy";
        public static final String COLUMN_CAR_MPG = "CarMPG";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " " + TYPE_INT + " " + PK + COMMA +
                        COLUMN_USERNAME + " " + TYPE_TEXT + COMMA +
                        COLUMN_PASSWORD + " " + TYPE_TEXT + COMMA +
                        COLUMN_FIRST_NAME + " " + TYPE_TEXT + COMMA +
                        COLUMN_LAST_NAME + " " + TYPE_TEXT + COMMA +
                        COLUMN_CAR_OCCUPANCY + " " + TYPE_INT + COMMA +
                        COLUMN_CAR_MPG + " " + TYPE_INT + ")";

        public static final String DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    private static abstract class TripTable implements BaseColumns {
        public static final String TABLE_NAME = "Trip";

        public static final String COLUMN_CREATOR_ID = "CreatorID";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_DRIVER_ID = "DriverId";
        public static final String COLUMN_LOCATION = "Location";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_DEPART_TIME = "DepartTime";
        public static final String COLUMN_RETURN_TIME = "ReturnTime";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " " + TYPE_INT + " " + PK + COMMA +
                        COLUMN_CREATOR_ID + " " + TYPE_INT + COMMA +
                        COLUMN_NAME + " " + TYPE_TEXT + COMMA +
                        COLUMN_DRIVER_ID + " " + TYPE_INT + COMMA +
                        COLUMN_LOCATION + " " + TYPE_TEXT + COMMA +
                        COLUMN_DESCRIPTION + " " + TYPE_TEXT + COMMA +
                        COLUMN_DEPART_TIME + " " + TYPE_INT + COMMA +
                        COLUMN_RETURN_TIME + " " + TYPE_INT + ")";

        public static final String DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    private static abstract class TripUserTable implements BaseColumns {
        public static final String TABLE_NAME = "TripUser";

        public static final String COLUMN_TRIP_ID = "TripId";
        public static final String COLUMN_USER_ID = "UserId";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " " + TYPE_INT + " " + PK + COMMA +
                        COLUMN_TRIP_ID + " " + TYPE_INT + COMMA +
                        COLUMN_USER_ID + " " + TYPE_INT + ")";

        public static final String DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //**********************        HELPER        *******************************

    private static class DatabaseHelper extends SQLiteOpenHelper {

        final static int DB_VERSION = 1;
        final static String DB_NAME = "shotgun.db";

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(UserTable.CREATE_TABLE);
            db.execSQL(TripTable.CREATE_TABLE);
            db.execSQL(TripUserTable.CREATE_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(UserTable.DROP_TABLE);
            db.execSQL(TripTable.DROP_TABLE);
            db.execSQL(TripUserTable.DROP_TABLE);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    //**********************        BASIC METHODS        *******************************

    public DBHelper(Context context){
        this.context = context;
        mDBHelper = new DatabaseHelper(context);
    }

    public DBHelper open() throws SQLException {
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDBHelper.close();
    }

    //*******************************        ACCESSORS        *******************************

    public ArrayList<User> getUsers(String username){
        ArrayList<User> users = new ArrayList<>();
        String whereClause = UserTable.COLUMN_USERNAME + " <> '" + username + "'";
        Cursor c = mDB.query(UserTable.TABLE_NAME, null, whereClause, null, null, null, null);
        while(c.moveToNext()){
            User user = new User();
            user.id = c.getInt(c.getColumnIndex(UserTable._ID));
            user.username = c.getString(c.getColumnIndex(UserTable.COLUMN_USERNAME));
            user.firstName = c.getString(c.getColumnIndex(UserTable.COLUMN_FIRST_NAME));
            user.lastName = c.getString(c.getColumnIndex(UserTable.COLUMN_LAST_NAME));
            user.carMPG = c.getInt(c.getColumnIndex(UserTable.COLUMN_CAR_MPG));
            user.carOccupancy = c.getInt(c.getColumnIndex(UserTable.COLUMN_CAR_OCCUPANCY));
            users.add(user);
        }
        return users;
    }

    public User getUser(String username){
        String whereClause = UserTable.COLUMN_USERNAME + " = '" + username + "'";
        Cursor c = mDB.query(UserTable.TABLE_NAME, null, whereClause, null, null, null, null);

        User user = new User();
        while(c.moveToNext()) {
            user.id = c.getInt(c.getColumnIndex(UserTable._ID));
            user.username = c.getString(c.getColumnIndex(UserTable.COLUMN_USERNAME));
            user.firstName = c.getString(c.getColumnIndex(UserTable.COLUMN_FIRST_NAME));
            user.lastName = c.getString(c.getColumnIndex(UserTable.COLUMN_LAST_NAME));
            user.carMPG = c.getInt(c.getColumnIndex(UserTable.COLUMN_CAR_MPG));
            user.carOccupancy = c.getInt(c.getColumnIndex(UserTable.COLUMN_CAR_OCCUPANCY));
        }
        return user;
    }

    public ArrayList<String> getUsernames(){
        ArrayList<String> usernames = new ArrayList<>();
        String[] columns = new String[] {UserTable.COLUMN_USERNAME};
        Cursor c = mDB.query(UserTable.TABLE_NAME, columns, null,null,null,null,null);
        while(c.moveToNext()){
            usernames.add(c.getString(c.getColumnIndex(UserTable.COLUMN_USERNAME)));
        }
        c.close();
        return usernames;
    }

    public ArrayList<Trip> getTrips(){
        ArrayList<Trip> trips = new ArrayList<>();
        Cursor c = mDB.query(TripTable.TABLE_NAME, null, null, null, null, null, TripTable.COLUMN_DEPART_TIME);
        while(c.moveToNext()){
            Trip trip = new Trip();
            trip.id = c.getInt(c.getColumnIndex(TripTable._ID));
            trip.creatorId = c.getInt(c.getColumnIndex(TripTable.COLUMN_CREATOR_ID));
            trip.name = c.getString(c.getColumnIndex(TripTable.COLUMN_NAME));
            trip.driverId = c.getInt(c.getColumnIndex(TripTable.COLUMN_DRIVER_ID));
            trip.description = c.getString(c.getColumnIndex(TripTable.COLUMN_DESCRIPTION));
            trip.location = c.getString(c.getColumnIndex(TripTable.COLUMN_LOCATION));
            trip.setDepartDateTime(c.getLong(c.getColumnIndex(TripTable.COLUMN_DEPART_TIME)));
            trip.setReturnDateTime(c.getLong(c.getColumnIndex(TripTable.COLUMN_RETURN_TIME)));
            trips.add(trip);
        }
        return trips;
    }

    public ArrayList<User> getUsersForTrip(Trip trip){
        // TODO: TEST
        ArrayList<User> users = new ArrayList<>();
        Cursor c = mDB.rawQuery("SELECT * FROM TripUser " +
                "INNER JOIN UserTable ON " +
                "TripUser.UserId = UserTable._Id " +
                "WHERE TripUser.TripId = " + trip.id, null);
        while(c.moveToNext()){
            User user = new User();
            user.id = c.getInt(c.getColumnIndex(UserTable._ID));
            user.username = c.getString(c.getColumnIndex(UserTable.COLUMN_USERNAME));
            user.firstName = c.getString(c.getColumnIndex(UserTable.COLUMN_FIRST_NAME));
            user.lastName = c.getString(c.getColumnIndex(UserTable.COLUMN_LAST_NAME));
            user.carMPG = c.getInt(c.getColumnIndex(UserTable.COLUMN_CAR_MPG));
            user.carOccupancy = c.getInt(c.getColumnIndex(UserTable.COLUMN_CAR_OCCUPANCY));
            users.add(user);
        }
        return users;
    }

    public ArrayList<Trip> getMyTrips(String username){
        int creatorId = getUser(username).id;
        String whereClause = TripTable.COLUMN_CREATOR_ID + " = " + creatorId;
        ArrayList<Trip> trips = new ArrayList<>();
        Cursor c = mDB.query(TripTable.TABLE_NAME, null, whereClause, null, null, null, TripTable.COLUMN_DEPART_TIME);
        while(c.moveToNext()){
            Trip trip = new Trip();
            trip.id = c.getInt(c.getColumnIndex(TripTable._ID));
            trip.creatorId = c.getInt(c.getColumnIndex(TripTable.COLUMN_CREATOR_ID));
            trip.name = c.getString(c.getColumnIndex(TripTable.COLUMN_NAME));
            trip.driverId = c.getInt(c.getColumnIndex(TripTable.COLUMN_DRIVER_ID));
            trip.description = c.getString(c.getColumnIndex(TripTable.COLUMN_DESCRIPTION));
            trip.location = c.getString(c.getColumnIndex(TripTable.COLUMN_LOCATION));
            trip.setDepartDateTime(c.getLong(c.getColumnIndex(TripTable.COLUMN_DEPART_TIME)));
            trip.setReturnDateTime(c.getLong(c.getColumnIndex(TripTable.COLUMN_RETURN_TIME)));
            trips.add(trip);
        }
        return trips;
    }

    //*******************************        MUTATORS        *******************************

    public void addUser(User user, String password){
        if(userExists(user.username))
            return;

        ContentValues cv = new ContentValues();
        cv.put(UserTable.COLUMN_USERNAME,user.username);
        cv.put(UserTable.COLUMN_FIRST_NAME,user.firstName);
        cv.put(UserTable.COLUMN_LAST_NAME,user.lastName);
        cv.put(UserTable.COLUMN_PASSWORD, password);
        if(user.isDriver()){
            cv.put(UserTable.COLUMN_CAR_OCCUPANCY,user.carOccupancy);
            cv.put(UserTable.COLUMN_CAR_MPG, user.carMPG);
        }
        mDB.insert(UserTable.TABLE_NAME, null,cv);
    }

    public void addTrip(Trip trip){
        ContentValues cv = new ContentValues();
        cv.put(TripTable.COLUMN_CREATOR_ID, trip.creatorId);
        cv.put(TripTable.COLUMN_NAME, trip.name);
        cv.put(TripTable.COLUMN_DRIVER_ID, trip.driverId);
        cv.put(TripTable.COLUMN_DESCRIPTION, trip.description);
        cv.put(TripTable.COLUMN_LOCATION, trip.location);
        cv.put(TripTable.COLUMN_DEPART_TIME, trip.getDepartDateTime());
        cv.put(TripTable.COLUMN_RETURN_TIME, trip.getReturnDateTime());
        mDB.insert(TripTable.TABLE_NAME, null, cv);
    }

    public void joinTrip(int tripId, int userId){
        ContentValues cv = new ContentValues();
        cv.put(TripUserTable.COLUMN_TRIP_ID, tripId);
        cv.put(TripUserTable.COLUMN_USER_ID, userId);
        mDB.insert(TripUserTable.TABLE_NAME, null, cv);
    }

    public void cancelTrip(Trip trip, User user){
        // if user.id == trip.creatorId, allow delete of trip
        // be sure to also delete all rows where for this trip.id in TripUserTable
        // TODO: IMPLEMENT
    }

    //*******************************        USEFUL METHODS        *******************************

    public boolean userExists(String username){
        ArrayList<String> users = this.getUsernames();
        return users.contains(username);
    }

    public boolean isValidPassword(String username, String password){
        String[] columns = new String[] {UserTable.COLUMN_USERNAME, UserTable.COLUMN_PASSWORD};
        String whereClause = UserTable.COLUMN_USERNAME + " = '" + username + "'";
        Cursor c = mDB.query(UserTable.TABLE_NAME, columns, whereClause, null, null, null, null);
        c.moveToFirst();
        return password.equals(c.getString(c.getColumnIndex(UserTable.COLUMN_PASSWORD)));
    }
}
