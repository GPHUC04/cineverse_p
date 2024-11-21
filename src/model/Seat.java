package model;

import java.util.Objects;


public class Seat {
    private int seat_id;  // Mã ghế (ví dụ: "A1", "B2", ...)
    private String type;     // Loại ghế (ví dụ: "VIP", "Regular", ...)
    private String status; 
    private int seat_row;
    private int seat_column;
    // Constructor mặc định
    public Seat() {
        seat_id = 0;
        type = "";
        status = "Available"; // Mặc định là ghế có sẵn
    }

    // Constructor với các tham số
    public Seat(int seat_id, String type, String status) {
        this.seat_id = seat_id;
        this.type = type;
        this.status = status;
    }

    // Getter và Setter
    public int getSeat_id() {
        return seat_id;
    }

    public void setSeat_id(int seat_id) {
        this.seat_id = seat_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Phương thức hashCode để hỗ trợ so sánh đối tượng
    @Override
    public int hashCode() {
        return Objects.hash(seat_id, type, status);
    }

    // Phương thức equals để so sánh hai ghế
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Seat other = (Seat) obj;
        return Objects.equals(seat_id, other.seat_id) &&
               Objects.equals(type, other.type) &&
               Objects.equals(status, other.status);
    }

    // Phương thức toString để hiển thị thông tin ghế
    @Override
    public String toString() {
        return "Seat{" + "seat_id='" + seat_id + "', type='" + type + "', status='" + status + "'}";
    }

    public int getSeat_row() {
        return seat_row;
    }

    public void setSeat_row(int seat_row) {
        this.seat_row = seat_row;
    }

    public int getSeat_column() {
        return seat_column;
    }

    public void setSeat_column(int seat_column) {
        this.seat_column = seat_column;
    }
    
    
    
    
    
}
