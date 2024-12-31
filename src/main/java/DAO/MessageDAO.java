package DAO;
import Model.*;
import Util.*;
import java.util.*;
import java.sql.*;

public class MessageDAO {

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();

        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                                              rs.getInt("posted_by"),
                                              rs.getString("message_text"),
                                              rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageByID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                                              rs.getInt("posted_by"),
                                              rs.getString("message_text"),
                                              rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            
            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id,
                                   message.getPosted_by(),
                                   message.getMessage_text(),
                                   message.getTime_posted_epoch());
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageByID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        Message message = this.getMessageByID(message_id);

        if (message != null) {
            try {
                String sql = "DELETE FROM Message WHERE message_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
                preparedStatement.setInt(1, message_id);
                
                preparedStatement.executeUpdate();
    
                return message;

            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public Message updateMessage_textbyID(int message_id, String message_text) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, message_text);
            preparedStatement.setInt(2, message_id);
            
            preparedStatement.executeUpdate();

            return this.getMessageByID(message_id);

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
