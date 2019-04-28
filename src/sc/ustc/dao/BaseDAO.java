package sc.ustc.dao;

import java.sql.DriverManager;
import com.mysql.jdbc.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.management.Query;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.mysql.jdbc.Connection;

public  class BaseDAO {
	protected String driver;
	protected String url;
	protected String userName;
	protected String userPassword;
	
	
	public BaseDAO(String driver, String url, String userName, String userPassword) {
		super();
		this.driver = driver;
		this.url = url;
		this.userName = userName;
		this.userPassword = userPassword;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public Connection openDBConnection() {
		Connection connection=null;
		try {
			Class.forName(driver);
			System.out.println("成功加载sql驱动");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("找不到sql驱动");
			e.printStackTrace();
		}
		try {
			 connection = (Connection) DriverManager.getConnection(url, userName, userPassword);
			System.out.print("成功连接到数据库！");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.print("连接到数据库失败！");
			e.printStackTrace();
		}
		return connection;
	}
	public Boolean closeDBConnection(Connection conn,PreparedStatement stmt) {
			try {
				if(stmt!=null) {
				stmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		return false;
	}
	public Boolean closeDBConnection(Connection conn,PreparedStatement stmt,ResultSet rs) {
		try {
			if(rs!=null) {
				rs.close();
			}
			if(stmt!=null) {
				stmt.close();
			}
			if(conn!=null) {
				conn.close();
			}
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	return false;
}
	/*public abstract Object query(String sql);
	public abstract Boolean insert(String sql);
	public abstract Boolean update(String sql);
	public abstract Boolean delete(String sql);*/
}
