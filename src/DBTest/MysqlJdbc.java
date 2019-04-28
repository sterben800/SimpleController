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
	            System.out.println("成功加载sql驱动");
	        } catch (ClassNotFoundException e) {
	            // TODO Auto-generated catch block
	            System.out.println("找不到sql驱动");
	            e.printStackTrace();
	        }
	        String url="jdbc:mysql://localhost:3306/shop";   
	        Connection conn;
	        try {
	            conn = (Connection) DriverManager.getConnection(url,"root","password");
	            //创建一个Statement对象
	            Statement stmt = (Statement) conn.createStatement(); //创建Statement对象
	            System.out.print("成功连接到数据库！");
	            ResultSet rs = stmt.executeQuery("select * from user");
	            //user 为你表的名称
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

