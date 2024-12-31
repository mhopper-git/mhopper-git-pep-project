package DAO;
import Model.*;
import Util.*;
import java.util.*;
import java.sql.*;

public class AccountDAO {

    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();

        List<Account> accounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                                              rs.getString("username"),
                                              rs.getString("password"));
                accounts.add(account);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
