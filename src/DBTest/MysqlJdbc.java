package DBTest;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;


public class MysqlJdbc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 try {
	            Class.forName("com.mysql.jdbc.Driver");
	            System.out.println("�ɹ�����sql����");
	        } catch (ClassNotFoundException e) {
	            // TODO Auto-generated catch block
	            System.out.println("�Ҳ���sql����");
	            e.printStackTrace();
	        }
	        String url="jdbc:mysql://localhost:3306/shop";   
	        Connection conn;
	        try {
	            conn = (Connection) DriverManager.getConnection(url,"root","password");
	            //����һ��Statement����
	            Statement stmt = (Statement) conn.createStatement(); //����Statement����
	            System.out.print("�ɹ����ӵ����ݿ⣡");
	            ResultSet rs = stmt.executeQuery("select * from user");
	            //user Ϊ��������
	            System.out.println(rs.toString());
	            while (rs.next()) {
	                System.out.println(rs.getString("name"));
	            }

	            stmt.close();
	            conn.close();
	        } catch (SQLException e){
	            System.out.println("fail to connect the database!");
	            e.printStackTrace();
	        }
	}
}

