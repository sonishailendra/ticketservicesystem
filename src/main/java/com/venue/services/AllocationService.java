package com.venue.services;

import com.venue.beans.Seat;
import com.venue.beans.SeatStatus;

/**
 * This Service is building the Seat arrangement, assume 10 X 10 Seats
 * @author Shailendra Soni
 */
public class AllocationService {

    //should have one object per JVM
    private static Seat venueSeats[][] = null ;

    static{
        //assume we have 10 rows and 10 Seats of each rows
        venueSeats = new Seat[10][10];
        int seatNo = 0;
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                venueSeats[i][j] = new Seat ( ++seatNo,i,j);
            }
        }
    }

    //as venueSeats is updated by main thread and Timer hence making Synchronized
    public synchronized static Seat[][] getVenueSeats(){
        return venueSeats;
    }

    public static void  printVenue(){
        for(int i=0;i<venueSeats.length;i++){
            for(int j=0;j<venueSeats[i].length;j++){
                System.out.print (venueSeats[i][j].getSeatNo () + "-"+venueSeats[i][j].getSeatStatus ().name ().substring (0,1) + " " );
            }
            System.out.println ();
        }
    }
}
