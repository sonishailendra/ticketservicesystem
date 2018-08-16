package com.venue.services;

import com.venue.beans.Seat;
import com.venue.beans.SeatHold;
import com.venue.beans.SeatStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hold and Reserved the ticket of the Venue.
 * @author Shailendra Soni
 */
public class TicketingServiceImpl implements TicketingService {

    private Map<Integer, SeatHold> seatHoldMap = new HashMap<> ();

    //default starting number
    private static int seatIdCount = 1000;

    /**
     * Return No. of Seat Available.
     * @return
     */
    public int numSeatsAvailable () {
        long avaList = Arrays.stream (AllocationService.getVenueSeats ()).
                flatMap (Stream::of).filter (seat -> SeatStatus.AVAILABLE.equals (seat.getSeatStatus ())).count ();
        return (int) avaList;
    }

    /**
     * Find Available Seat and if found then Hold the Seat. Try to search in a same row, if not then it is assign sequencelly
     * availability of Seats.
     * @param numSeats the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return SeatHold
     */
    public SeatHold findAndHoldSeats ( int numSeats, String customerEmail ) {

        if(numSeats <= 0){
            System.out.println ("Invalid Input of numSeats");
            return null;
        }

        //find available seats count of each row...
        SeatHold seatHold = new SeatHold ();
        seatHold.setSeatHoldId (++seatIdCount);
        seatHold.setCustomerEmail (customerEmail);
        seatHold.setRequestNumOfSeat (numSeats);

        seatHoldMap.put (seatHold.getSeatHoldId (), seatHold);

        //give to customer best seats
        for (int i = 0; i < AllocationService.getVenueSeats ().length; i++) {
            int avaliableSeatsByRow = (int) Arrays.stream (AllocationService.getVenueSeats ()[i])
                    .filter (seat -> SeatStatus.AVAILABLE.equals (seat.getSeatStatus ())).count ();
            //
            if (avaliableSeatsByRow >= numSeats) {
                List<Seat> availableSeatsList = Arrays.stream (AllocationService.getVenueSeats ()[i])
                        .filter (seat -> SeatStatus.AVAILABLE.equals (seat.getSeatStatus ()))
                        .collect (Collectors.toList ());

                //change the status
                availableSeatsList.stream ().limit (numSeats).forEach (seat -> seat.setSeatStatus (SeatStatus.HOLD));
                seatHold.setHoldSeatsList (availableSeatsList);
                return seatHold;
            }
        }

        //assign random seats but in sequence of rows
        List<Seat> availableSeatsList = Arrays.stream (AllocationService.getVenueSeats ()).
                flatMap (Stream::of)
                .filter (seat -> SeatStatus.AVAILABLE.equals (seat.getSeatStatus ()))
                .limit (numSeats).collect (Collectors.toList ());

        if (availableSeatsList.size () >= numSeats) {
            availableSeatsList.forEach (seat -> seat.setSeatStatus (SeatStatus.HOLD));
            seatHold.setHoldSeatsList (availableSeatsList);
            return seatHold;
        } else {
            System.out.println ("Total No. of Available Seats :- " + availableSeatsList.size () + " which is less than " + numSeats);
            return null;
        }


    }

    /**
     * Reserved Seats for customer
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the
    seat hold is assigned
     * @return Reservation ID
     */
    public String reserveSeats ( int seatHoldId, String customerEmail ) {
        if (seatHoldMap.containsKey (seatHoldId)) {

            long count =
                    seatHoldMap.get (seatHoldId).getHoldSeatsList ().stream ()
                            .filter (seat -> AllocationService.getVenueSeats ()[seat.getRow ()][seat.getColumn ()].getSeatStatus ()
                                    .equals (SeatStatus.HOLD)).count ();
            //We should check for Count of the Actual Holding and existing Hold Seats, if not match then Timer update the status
            if(count != seatHoldMap.get (seatHoldId).getHoldSeatsList ().size ()){
                System.out.println ("Reservation Time Out... Please try again ");
                return null;
            }else{
                seatHoldMap.get (seatHoldId).getHoldSeatsList ().stream ().
                        forEach (seat -> AllocationService.getVenueSeats ()[seat.getRow ()][seat.getColumn ()].setSeatStatus (SeatStatus.RESERVED));
            }
            //cancel the timer as it is seat are reserved.
            seatHoldMap.get (seatHoldId).cancelTimer ();
            //remove once reserved
            seatHoldMap.remove (seatHoldId);

            System.out.println ("Reservation Is Done For Id :- " + seatHoldId);
            return String.valueOf (System.currentTimeMillis ());
        } else {
            System.out.println ("No Reservation Found... Please try again.");
            return null;
        }

    }

    public static void main ( String... args ) {

        AllocationService.printVenue ();
        TicketingServiceImpl ticketingService = new TicketingServiceImpl ();
        System.out.println ("Available Seats :- " + ticketingService.numSeatsAvailable ());

        System.out.println ("----------------------------");
        System.out.println ("No of Seats :- " + 5);
        SeatHold seatHold50 = ticketingService.findAndHoldSeats (5, "test");
        AllocationService.printVenue ();

        System.out.println ("----------------------------");
        System.out.println ("No of Seats :- " + 5);
        SeatHold seatHold51 = ticketingService.findAndHoldSeats (5,"test");
        AllocationService.printVenue ();


        System.out.println ("----------------------------");
        System.out.println ("No of Seats :- " + 7);
        SeatHold seatHold7 = ticketingService.findAndHoldSeats (7,"test");
        AllocationService.printVenue ();

        System.out.println ("----------------------------");
        System.out.println ("No of Seats :- " + 3);

        SeatHold seatHold3 = ticketingService.findAndHoldSeats (3,"test");
        AllocationService.printVenue ();

        System.out.println ("----------------------------");
        System.out.println ("No of Seats :- " + 14);

        SeatHold seatHold14 = ticketingService.findAndHoldSeats (14,"test");
        AllocationService.printVenue ();
        ticketingService.reserveSeats (seatHold14.getSeatHoldId (),seatHold14.getCustomerEmail ());


        System.out.println ("----------------------------");
        AllocationService.printVenue ();
    }


}
