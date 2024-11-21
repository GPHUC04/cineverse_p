/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author kenlee
 */
public class Seatstatus {
    private String seatRow;
    private String seatColumn;
    private String status;
    
    
     public Seatstatus(String seatRow, String seatColumn, String status) {
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.status = status;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public String getSeatColumn() {
        return seatColumn;
    }

    public String getStatus() {
        return status;
    }

    
}
