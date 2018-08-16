package com.venue.services;




import com.venue.beans.SeatHold;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

public class TestTicketingServiceImpl {

    private TicketingService ticketingService;

    @Before
    public void setup(){
        ticketingService = new TicketingServiceImpl ();
    }


    @Test
    public void testAvalialeSeat(){
        int noOfSeatAvl = ticketingService.numSeatsAvailable ();
        Assert.assertTrue (noOfSeatAvl > 0);
    }


    @Test
    public void testInvalidInput(){
        SeatHold seatHold = ticketingService.findAndHoldSeats (0, "test.com");

        Assert.assertNull (seatHold);
    }

    @Test
    public void testHoldSeats(){
        SeatHold seatHold = ticketingService.findAndHoldSeats (50, "test.com");

        Assert.assertEquals (50,seatHold.getHoldSeatsList ().size ());
    }

    @Test
    public void testReservedSeat(){
        SeatHold seatHold = ticketingService.findAndHoldSeats (10, "test.com");
        Assert.assertNotNull (seatHold);

        String reserNo = ticketingService.reserveSeats (seatHold.getSeatHoldId (),"test.com");
        Assert.assertNotNull (reserNo);
    }

    @Test
    public void testReservedSeatPassWrongId(){
        String reserNo = ticketingService.reserveSeats (1111,"test.com");
        Assert.assertNull (reserNo);
    }



}
