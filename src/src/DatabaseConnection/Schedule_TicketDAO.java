/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DatabaseConnection;

import com.mysql.cj.xdevapi.Result;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Schedule_model;
import model.Seat;
import model.Seatstatus;
import view.Schedulepanel;

/**
 *
 * @author kenlee
 */
public class Schedule_TicketDAO {
    private List<Schedule_model> schedules ;
    private static DatabaseConnection databaseconection;
   
    private Connection connection;


    public Schedule_TicketDAO(Connection connection) {
        this.connection = connection;
    }
    
    public  Schedule_TicketDAO(){
    this.schedules = new ArrayList<Schedule_model>();
        setScheduleData();
   
    
}

    public List<Schedule_model> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule_model> schedules) {
        this.schedules = schedules;
    }
    
     public static ArrayList<String> loaddatacomboboxMovie(){
        ArrayList<String> list = new ArrayList<>(); 
        try {
            Connection con = databaseconection.getConnection();
            
            String sql = "select mid from Movie; ";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet result =  ps.executeQuery();
              while(result.next()){  
                  list.add(result.getString("mid" ));
              }
            
        } catch (Exception e) {
        }
        return list;
        
    }



     
     
public String getMovieDuration(String movieName) {
        String duration = "";
        String query = "SELECT duration FROM Movie WHERE title = ?";
        try (   Connection con = databaseconection.getConnection();
                PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, movieName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                duration = rs.getString("duration");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duration;
    }
         public static ArrayList<String> loaddatacomboboxroom(){
        ArrayList<String> list = new ArrayList<>(); 
        try {
            Connection con = databaseconection.getConnection();
            
            String sql = "select rid from ScreenRoom; ";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet result =  ps.executeQuery();
              while(result.next()){  
                  list.add(result.getString("rid" ));
              }
            
        } catch (Exception e) {
        }
        return list;
        
    }
        public static ArrayList<String> loaddatacomboboxMovieName(){
        ArrayList<String> list = new ArrayList<>(); 
        try {
            Connection con = databaseconection.getConnection();
            
            String sql = "select title from Movie;";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet result =  ps.executeQuery();
              while(result.next()){  
                  list.add(result.getString("title" ));
              }
            
        } catch (Exception e) {
        }
        return list;
        
    }
        
        
    
    

      public static ArrayList<String> loaddatacomboboxShowTime(){
        ArrayList<String> list = new ArrayList<>(); 
        try {
            Connection con = databaseconection.getConnection();
            
            String sql = "select stime from Schedule; ";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet result =  ps.executeQuery();
              while(result.next()){  
                  list.add(result.getString("stime" ));
              }
            
        } catch (Exception e) {
        }
        return list;
        
    }
public boolean isExistschedule(String scid) {
    String sql = "SELECT COUNT(*) FROM Schedule WHERE scid = ?";
    try (Connection conn = databaseconection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, scid);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
     public void deleteschedule(Schedule_model m) {
    try {
        
        Connection conn = databaseconection.getConnection();
        
       
        CallableStatement c = conn.prepareCall("{call sp_deleteSchedule(?)}");
        
        
        c.setString(1, m.getScid());  
       
        c.execute(); 

      
        schedules.removeIf(schedule -> schedule.getScid().equals(m.getScid()));
        
    } catch (SQLException e) {
        
        System.err.println("Database error: " + e.getMessage());
    } catch (Exception e) {
       
        e.printStackTrace();
    }
}
     
    public boolean isCustomerExist(String name, String phone) {
    String query = "SELECT COUNT(*) FROM Customer WHERE  phone = ?";
    
    try (Connection con = databaseconection.getConnection();
         PreparedStatement stmt = con.prepareStatement(query)) {
        
        
        stmt.setInt(1, Integer.parseInt(phone));
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0; 
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // Khách hàng chưa tồn tại
}
        
        
public void setScheduleData() {
    String sql = "SELECT * FROM schedule;";
    try (Connection con = databaseconection.getConnection();
         Statement statement = con.createStatement()) {

        ResultSet result = statement.executeQuery(sql);
        
  
        while (result.next()) {
            String scid = result.getString("scid");
            String mid = result.getString("mid");
            String rid = result.getString("rid");
            
       
            LocalDate sdateLocal = result.getDate("sdate").toLocalDate(); 
            LocalTime stimeLocal = result.getTime("stime").toLocalTime(); 
            float price = result.getFloat("price");
            
          
            schedules.add(new Schedule_model(scid, mid, rid, sdateLocal, stimeLocal, price));
        }
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}

  
   public void addSchedule(Schedule_model s) {
    if (isExistschedule(s.getScid())) {
        System.out.println("Schedule already exists!");
        return;
    }
    
    
    try (Connection conn = databaseconection.getConnection()) {
        CallableStatement c = conn.prepareCall("{call sp_addSchedule(?, ?, ?, ?, ?, ?)}");

       
        java.sql.Date sqlDate = java.sql.Date.valueOf(s.getSdate()); 
        Time sqlTime = Time.valueOf(s.getStime()); 
        
      
        c.setString(1, s.getScid());
        c.setString(2, s.getMid());
        c.setString(3, s.getRid());
        c.setDate(4, sqlDate);  
        c.setTime(5, sqlTime);  
        c.setDouble(6, s.getPrice()); 
       
        c.executeUpdate();

        
        schedules.add(s);
    } catch (SQLException e) {
        
        System.err.println("Database error: " + e.getMessage());
        e.printStackTrace();
    }
}

public void editSchedule(Schedule_model s) {
    Connection conn = null;
    CallableStatement c = null;
    try {
       
        conn = databaseconection.getConnection();
        conn.setAutoCommit(false);  

     
        c = conn.prepareCall("{call sp_editSchedule(?, ?, ?, ?, ?, ?)}");

        
        java.sql.Date sqlDate = java.sql.Date.valueOf(s.getSdate());
        java.sql.Time sqlTime = java.sql.Time.valueOf(s.getStime());  

        // 
        c.setInt(1, Integer.parseInt(s.getScid())); 
        c.setString(2, s.getMid());
        c.setString(3, s.getRid());
        c.setDate(4, sqlDate);
        c.setTime(5, sqlTime);
        c.setDouble(6, s.getPrice());

       
        c.executeUpdate(); 

        conn.commit();  

       
        schedules.removeIf(schedule -> schedule.getScid().equals(s.getScid()));
        schedules.add(s);

    } catch (SQLException e) {
        if (conn != null) {
            try {
                conn.rollback();  
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
        }
        System.err.println("Database error: " + e.getMessage());
        e.printStackTrace();
    } catch (IllegalArgumentException e) {
        System.err.println("Invalid schedule data: " + e.getMessage());
    } finally {
        try {
            if (c != null) c.close();
            if (conn != null) conn.setAutoCommit(true);  // Reset auto-commit
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}

public ArrayList<String> getShowTimesByMovieNameAndRoomType(String movieName, String roomType) {
    ArrayList<String> showTimes = new ArrayList<>();
    String sql = "SELECT DISTINCT s.stime FROM Schedule s " +
                 "JOIN Movie m ON s.mid = m.mid " +
                 "JOIN ScreenRoom sr ON s.rid = sr.rid " +
                 "WHERE m.title = ? AND sr.type = ?;";
    try (Connection conn = databaseconection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, movieName);
        stmt.setString(2, roomType); 
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            showTimes.add(rs.getString("stime"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return showTimes;
}

    
public ArrayList<String> loadMovieNames() {
    ArrayList<String> movieNames = new ArrayList<>();

    
    String sql = "SELECT title FROM Movie";

    try (   Connection conn = databaseconection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
           
            movieNames.add(rs.getString("title"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return movieNames;
}




public ArrayList<String> loaddatacomboboxTypeRoom(String title) {
    ArrayList<String> roomTypes = new ArrayList<>();
    String query = "SELECT DISTINCT type FROM ScreenRoom sr " +
                   "JOIN Schedule s ON sr.rid = s.rid " +
                   "WHERE s.mid = (SELECT mid FROM Movie WHERE title = ?)";
    try (   Connection conn = databaseconection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, title); // Thiết lập title vào câu truy vấn
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            roomTypes.add(rs.getString("type")); 
        }
    } catch (SQLException e) {
        e.printStackTrace(); 
    }
    return roomTypes;
}


public static ArrayList<String> loaddatacomboboxSeat(String movieTitle, String roomType) {
    ArrayList<String> seats = new ArrayList<>();
    
   
    String query = "SELECT seid FROM Seat s " +
                   "JOIN Schedule sc ON s.rid = sc.rid " +
                   "JOIN Movie m ON sc.mid = m.mid " +
                   "JOIN ScreenRoom sr ON s.rid = sr.rid " +
                   "WHERE m.title = ? AND sr.type = ? AND s.status = 'Available'";
    
    try (Connection conn = databaseconection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, movieTitle);  
        stmt.setString(2, roomType);   
       
        ResultSet rs = stmt.executeQuery();
      
        while (rs.next()) {
            seats.add(rs.getString("seid"));  
        }
    } catch (SQLException e) {
        e.printStackTrace();  
    }
    
    return seats; 
}

   
    
public ArrayList<String> getAvailableSeats(String selectedMovie, String selectedRoomType, String selectedShowtime) {
    ArrayList<String> availableSeats = new ArrayList<>();
    
    
    String query = "SELECT ss.seid FROM SeatStatus ss " +
                   "JOIN Schedule sc ON ss.scid = sc.scid " +
                   "JOIN Movie m ON sc.mid = m.mid " +
                   "JOIN ScreenRoom sr ON sc.rid = sr.rid " +
                   "WHERE m.title = ? AND sr.type = ? AND sc.stime = ? AND ss.status = 'Available'";

    try (Connection conn = databaseconection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, selectedMovie);
        stmt.setString(2, selectedRoomType);
        stmt.setString(3, selectedShowtime);

       
        ResultSet rs = stmt.executeQuery();

       
        while (rs.next()) {
            availableSeats.add(rs.getString("seid"));
        }
    } catch (SQLException e) {
        e.printStackTrace();  
    }

    return availableSeats;
}

public void updateSeatBookingTable(String selectedMovie, String selectedRoom, String selectedShowtime, JTable seatTable) {
    ArrayList<String> availableSeats = getAvailableSeats(selectedMovie, selectedRoom, selectedShowtime);
    
    DefaultTableModel model = (DefaultTableModel) seatTable.getModel();
    model.setRowCount(0); 

    for (String seat : availableSeats) {
        model.addRow(new Object[]{seat, "Available"}); 
    }
    
    seatTable.setModel(model);
}

public void addCustomer(String name, String phone) {
    String query = "INSERT INTO Customer (cname, phone) VALUES (?, ?)";
    
    try (Connection con = databaseconection.getConnection();
         PreparedStatement stmt = con.prepareStatement(query)) {
        
        stmt.setString(1, name); 
        stmt.setInt(2, Integer.parseInt(phone)); 
        
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public int getScheduleId(String movieName, String roomName, String showtime) {
    int scheduleId = -1; 
    String query = """
        SELECT s.scid
        FROM Schedule s
        JOIN Movie m ON s.mid = m.mid
        JOIN ScreenRoom r ON s.rid = r.rid
        WHERE m.title = ? AND r.type = ? AND s.stime = ?
    """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
    
        stmt.setString(1, movieName);
        stmt.setString(2, roomName);
        stmt.setString(3, showtime);

    
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                scheduleId = rs.getInt("scid");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); 
    }

    return scheduleId;
}


public Map<Integer, String> getSeatStatus(int scheduleId) {
    Map<Integer, String> seatStatusMap = new HashMap<>();
    String query = "SELECT seid, status FROM SeatStatus WHERE scid = ?";

    try (Connection con = databaseconection.getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setInt(1, scheduleId); 
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int seatId = rs.getInt("seid");
            String status = rs.getString("status");
            seatStatusMap.put(seatId, status); 
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return seatStatusMap; 
}

public List<Seatstatus> getSeatStatusBySchedule(int scid, int rid) {
        List<Seatstatus> seatStatuses = new ArrayList<>();
        String sql = "SELECT s.seat_row, s.seat_column, ss.status "
                   + "FROM Seat s "
                   + "JOIN SeatStatus ss ON s.seid = ss.seid "
                   + "WHERE ss.scid = ? AND s.rid = ?";

        try (Connection con = databaseconection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, scid);
            stmt.setInt(2, rid);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String seatRow = rs.getString("seat_row");
                    String seatColumn = rs.getString("seat_column");
                    String status = rs.getString("status");

                    seatStatuses.add(new Seatstatus(seatRow, seatColumn, status));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seatStatuses;
}
public double getTicketPrice(String movieName, String roomType, String showTime, String showDate) {
    double ticketPrice = 0.0;

    String query = "{ ? = CALL getTicketPriceFunction(?, ?, ?, ?) }";

    try (CallableStatement stmt = connection.prepareCall(query)) {
       
        stmt.registerOutParameter(1, java.sql.Types.DECIMAL);

      
        stmt.setString(2, movieName);
        stmt.setString(3, roomType);
        stmt.setString(4, showTime);
        stmt.setString(5, showDate);

      
        stmt.execute();
        ticketPrice = stmt.getDouble(1);
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return ticketPrice;
}

public String getShowDateByMovieAndShowTime(String movieName, String showTime) {
    String showDate = "";

    String query = "SELECT s.sdate " +
                   "FROM Schedule s " +
                   "JOIN Movie m ON s.mid = m.mid " +
                   "WHERE m.title = ? AND s.stime = ?";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, movieName); 
        stmt.setString(2, showTime); 

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            showDate = rs.getString("sdate");
        }
    } catch (SQLException e) {
        e.printStackTrace(); 
    }

    return showDate;
}
public String getRoomNameByMovieAndShowTime(String movieName, String showTime) {
    String roomName = "";

    String query = "SELECT r.rname " +
                   "FROM Schedule s " +
                   "JOIN Movie m ON s.mid = m.mid " +
                   "JOIN ScreenRoom r ON s.rid = r.rid " +
                   "WHERE m.title = ? AND s.stime = ?";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, movieName); 
        stmt.setString(2, showTime); 

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            roomName = rs.getString("rname");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return roomName;
}

public List<String> getShowDatesByMovieAndShowTime(String movieName, String showTime) {
    List<String> showDates = new ArrayList<>();
    
    String query = "SELECT s.sdate " +
                   "FROM Schedule s " +
                   "JOIN Movie m ON s.mid = m.mid " +
                   "WHERE m.title = ? AND s.stime = ?";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, movieName); 
        stmt.setString(2, showTime);  

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            showDates.add(rs.getString("sdate")); 
        }
    } catch (SQLException e) {
        e.printStackTrace(); 
    }

    return showDates;
}


public void updateSeatStatus(int scheduleId, String seat, String status) throws SQLException {
    String query = "UPDATE SeatStatus SET status = ? WHERE scid = ? AND seid = ?";
    try (
            PreparedStatement stmt = connection.prepareStatement(query)) {
       
        stmt.setString(1, status);     
        stmt.setInt(2, scheduleId);     
        stmt.setString(3, seat);         

        // Thực thi truy vấn
        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Updated chair status successfully.");
        } else {
            System.out.println("No seats found to update.");
        }
    } catch (SQLException e) {
        // Xử lý ngoại lệ
        System.err.println("Error updating seat status: " + e.getMessage());
        throw e;
    }
}


public int getCustomerIdByPhone(String phoneNumber) {
    int customerId = -1;

   
    String query = "SELECT cid FROM Customer WHERE phone = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
       
        stmt.setString(1, phoneNumber);

        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                customerId = rs.getInt("cid"); 
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); 
    }

    return customerId;
}

public int getSeatId(int scheduleId, int seatRow, int seatColumn) {
    int seatId = -1;
    try {Connection con = databaseconection.getConnection();
       
        String query = "SELECT seid FROM Seat " +
                       "WHERE rid = (SELECT rid FROM Schedule WHERE scid = ?) " +
                       "AND seat_row = ? AND seat_column = ?";
       
        PreparedStatement stmt = con.prepareStatement(query);
        
        
        stmt.setInt(1, scheduleId); 
        stmt.setInt(2, seatRow); 
        stmt.setInt(3, seatColumn); 
        
    
        ResultSet rs = stmt.executeQuery();
     
        if (rs.next()) {
            seatId = rs.getInt("seid"); 
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return seatId;
}

public void executePayProcedure(int customerId, int scheduleId, Date tidate, Time titime, String payMethod, int seatId, double totalAmount) {
    try (Connection con = databaseconection.getConnection();
         CallableStatement stmt = con.prepareCall("{CALL ProcessPayment(?, ?, ?, ?, ?, ?,?)}")) {
        
        stmt.setInt(1, customerId);
        stmt.setInt(2, scheduleId);
        stmt.setDate(3, tidate);
        stmt.setTime(4, titime);
        stmt.setString(5, payMethod);
        stmt.setInt(6, seatId);
        stmt.setDouble(7, totalAmount);

        stmt.executeUpdate();
        
        JOptionPane.showMessageDialog(null, "Payment successful!");
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "There was an error during payment: " + e.getMessage());
    }
}



public String getRoomIdByScheduleId(int scheduleId) {
    String roomId = null;
    String query = "SELECT rid FROM Schedule WHERE scid = ?";
    
    try (Connection con = databaseconection.getConnection();
         PreparedStatement pstmt = con.prepareStatement(query)) {
        
        pstmt.setInt(1, scheduleId);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                roomId = rs.getString("rid");
            }
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return roomId;
}
public String getRoomIdByScheduleAndRoomType(int scheduleId, String roomType) {
    String query = "SELECT r.rid FROM Schedule s JOIN screenroom r ON s.rid = r.rid WHERE r.type = ? AND s.scid = ?";
    try (Connection con = databaseconection.getConnection();
         PreparedStatement stmt = con.prepareStatement(query)) {
        stmt.setString(1, roomType);  
        stmt.setInt(2, scheduleId);   

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("rid");  
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; 
}

public void cancelPayment(int ticketId, int scheduleId, List<Integer> selectedSeatIds) {
    try (Connection conn = databaseconection.getConnection()) {
        
        String sql = "{CALL CancelPayment(?, ?, ?)}";  
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            for (int seatId : selectedSeatIds) {
                if (seatId != -1) {
                    // Đảm bảo rằng tất cả các tham số đã được set đúng trước khi gọi thủ tục
                    stmt.setInt(1, ticketId);          
                    stmt.setInt(2, scheduleId);        
                    stmt.setInt(3, seatId);                

                    // Thực thi thủ tục
                    stmt.executeUpdate();
                    System.out.println("Successfully cancelled payment for seat ID: " + seatId);
                } else {
                    System.out.println("Invalid seat ID: " + seatId);
                }
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Có lỗi khi hủy giao dịch: " + e.getMessage(), e);
    }
}


  public int getTicketIdByCustomerAndSchedule(int customerId, int scheduleId , int seatID) {
        String query = "SELECT tid FROM Ticket WHERE cid = ? AND scid = ? and seid = ?;";
        try (   Connection con = databaseconection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, customerId);  
            ps.setInt(2, scheduleId);  
            ps.setInt(3, seatID);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("tid");  
            } else {
                return -1;  
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }
  

    public List<Seat> getSeats(int scheduleId) {
        String query = "select rid from schedule where scid = '" + scheduleId + "'";
        String rid = new String();
        List <Seat> seats =  new ArrayList<>();
        try {
            Connection con = databaseconection.getConnection();
           Statement stm = con.createStatement();
            
            ResultSet rs = stm.executeQuery(query);
            if (rs.next()) {
                rid = rs.getString(1);
                String query1 = "select * from seat where rid = '" + rid + "'";
                Statement stm1 = con.createStatement();
            
                ResultSet rs1 = stm1.executeQuery(query1);
                
                while (rs1.next()){
                    Seat s = new Seat();
                    s.setSeat_id(rs1.getInt(1));
                    s.setSeat_row(rs1.getInt(3));
                    s.setSeat_column(rs1.getInt(4));
                    s.setType(rs1.getString(5));
                    s.setStatus(rs1.getString(6));
                    seats.add(s);
                    
                }
            }
            
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
       return seats;
    }

public int getSeatIdFromDatabase(int seatRow, int seatColumn, String roomId) {
    String query = "SELECT seid FROM Seat WHERE seat_row = ? AND seat_column = ? AND rid = ?";
    int seatId = -1;

    try (Connection con = databaseconection.getConnection();
         PreparedStatement statement = con.prepareStatement(query)) {
        
        // Set tham số cho câu truy vấn
        statement.setInt(1, seatRow);
        statement.setInt(2, seatColumn);
        statement.setString(3, roomId); // Thêm roomId vào câu truy vấn

        ResultSet resultSet = statement.executeQuery();

        // Nếu có kết quả, lấy seatId
        if (resultSet.next()) {
            seatId = resultSet.getInt("seid");
        }
    } catch (SQLException e) {
        System.out.println("Error while fetching seat ID: " + e.getMessage());
        e.printStackTrace();
    }

    return seatId; 
}
public int getSeid(int scheduleId, int row, int column) {
    int seatId = -1; 
    String sql = "SELECT s.seid " +
                 "FROM Seat s " +
                 "JOIN ScreenRoom r ON s.rid = r.rid " +
                 "JOIN Schedule sc ON sc.rid = r.rid " +
                 "WHERE sc.scid = ? AND s.seat_row = ? AND s.seat_column = ?";

    try (Connection conn = databaseconection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

      
        stmt.setInt(1, scheduleId);
        stmt.setInt(2, row);
        stmt.setInt(3, column);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                seatId = rs.getInt("seid");
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Có lỗi khi lấy Seat ID: " + e.getMessage(), e);
    }

    return seatId;
}


    public String getCustomerNameByPhone(String phone) {
        String query = "SELECT cname FROM Customer WHERE phone = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("cname");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

 public boolean isCustomerExist(String phone) {
        String query = "SELECT COUNT(*) FROM Customer WHERE phone = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



}