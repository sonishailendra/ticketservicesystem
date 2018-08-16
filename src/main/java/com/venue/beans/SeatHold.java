package com.venue.beans;


import com.venue.services.AllocationService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SeatHold {
    private Integer seatHoldId;
    private int requestNumOfSeat;
    private List<Seat> holdSeatsList;
    private String customerEmail;
    private long timeToStart;
    private Timer timer;

    public SeatHold(){
        this.timeToStart = System.currentTimeMillis ();
        //Each Timer creates a thread and this is a very expensive object but it is okay in small program. We can have Monitor Thread to
        // reduce this overhead
        this.timer = new Timer ();
        timer.schedule (new TimerTask () {
            @Override
            public void run () {
                holdSeatsList.stream ().filter (seat -> SeatStatus.HOLD.equals (seat.getSeatStatus ())).
                        forEach (seat -> AllocationService.getVenueSeats ()[seat.getRow ()][seat.getColumn ()].setSeatStatus (SeatStatus.AVAILABLE));
                cancelTimer();
            }
        }, TimeUnit.MINUTES.toMillis (1));

    }

    public String getCustomerEmail () {
        return customerEmail;
    }

    public void setCustomerEmail ( String customerEmail ) {
        this.customerEmail = customerEmail;
    }

    public Integer getSeatHoldId () {
        return seatHoldId;
    }

    public void setSeatHoldId ( Integer seatHoldId ) {
        this.seatHoldId = seatHoldId;
    }

    public int getRequestNumOfSeat () {
        return requestNumOfSeat;
    }

    public void setRequestNumOfSeat ( int requestNumOfSeat ) {
        this.requestNumOfSeat = requestNumOfSeat;
    }

    public List<Seat> getHoldSeatsList () {
        return holdSeatsList;
    }

    public void setHoldSeatsList ( List<Seat> holdSeatsList ) {
        this.holdSeatsList = holdSeatsList;
    }

    public void cancelTimer(){
        this.timer.cancel ();
        this.timer.purge ();
    }
}
