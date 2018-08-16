package com.venue.beans;

public class Seat {

    private int seatNo;
    private SeatStatus seatStatus;
    //To identify best seat for Customer
    int row;
    int column;


    public Seat( int seatNo, int row, int column){
        this.seatNo = seatNo;
        this.seatStatus = SeatStatus.AVAILABLE;
        this.row = row;
        this.column = column;
    }

    public int getSeatNo () {
        return seatNo;
    }

    public SeatStatus getSeatStatus () {
        return seatStatus;
    }

    public void setSeatStatus ( SeatStatus seatStatus ) {
        this.seatStatus = seatStatus;
    }

    public int getRow () {
        return row;
    }

    public int getColumn () {
        return column;
    }


}
