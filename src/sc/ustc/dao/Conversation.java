package sc.ustc.dao;

import java.awt.image.DataBufferByte;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.mysql.jdbc.PreparedStatement;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.mysql.jdbc.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;



import sc.ustc.model.ClassInfo;
import sc.ustc.model.DbTableInfo;
import sc.ustc.model.Jdbc;
import sc.ustc.model.ORMapping;
import sc.ustc.model.UserBean;

public class Conversation {
	public static Conversation conversation;
	private ORMapping orMapping;
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	//构造一个单例的Conversation,重复使用。
	public static synchronized Conversation getInstance() {
		if (conversation == null) {
			conversation = new Conversation();	
		}
		return conversation;
	}
	public ORMapping getOrMapping() {
		return orMapping;
	}
	public void setOrMapping(ORMapping orMapping) {
		this.orMapping = orMapping;
	}
	public Boolean delete(Object obj) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		String sql="delete from ";
		Class<? extends Object> clazz=obj.getClass();
		String tableName=clazz.getSimpleName();
		List<ClassInfo> classInfos=orMapping.getClassInfos();
		for (ClassInfo classInfo : classInfos) {
			if(tableName.equals(classInfo.getName())) {
				sql=sql+classInfo.getTable()+" where ";
				String name=classInfo.getId_name();
				sql=sql+name+"=";
				//System.out.println(name);
				String method="get"+name.substring(0, 1).toUpperCase()+name.substring(1);
				//System.out.println(method);
				//给对象赋值
				Method m=clazz.getDeclaredMethod(method);
				int value=(int) m.invoke(obj);	
				sql=sql+value;
			}
		}
		System.out.println(sql);
		BaseDAO baseDAO=new BaseDAO(orMapping.getJdbc().getDriver_class(),orMapping.getJdbc().getUrl_path(),orMapping.getJdbc().getDb_username(),orMapping.getJdbc().getDb_userpassword());
		conn=baseDAO.openDBConnection();
		ps=(PreparedStatement) conn.prepareStatement(sql);
		int a=ps.executeUpdate();
		if(a==1)
			return true;
		else
			return false;
	}
	public Boolean update(Object obj) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException, SQLException {
		String sql="update ";
		Class<? extends Object> clazz=obj.getClass();
		String tableName=clazz.getSimpleName();
		List<ClassInfo> classInfos=orMapping.getClassInfos();
		ClassInfo classInfo=new ClassInfo();
		for (ClassInfo Info : classInfos) {
			classInfo=Info;
			if(tableName.equals(Info.getName())) {
				sql=sql+Info.getTable()+" set ";
				Field[] fields=clazz.getDeclaredFields();
				for (Field f : fields) {
					f.setAccessible(true);
					for(int i=0;i<Info.getDbTableInfos().size();i++) {
						DbTableInfo dbTableInfo=Info.getDbTableInfos().get(i);
						if(dbTableInfo.getName().equals(f.getName())) {
							System.out.println(f.getName()+" "+f.get(obj));
							sql=sql+dbTableInfo.getColumn()+"='"+f.get(obj);
							if(i!=Info.getDbTableInfos().size()-1) 
								sql=sql+"',";
							else
								sql=sql+"' where ";
						}
					}
				}
				String name=Info.getId_name();
				sql=sql+name+"=";
				//System.out.println(name);
				String method="get"+name.substring(0, 1).toUpperCase()+name.substring(1);
				//System.out.println(method);
				//给对象赋值
				Method m=clazz.getDeclaredMethod(method);
				int value=(int) m.invoke(obj);	
				sql=sql+value;
			}
		}
		System.out.println(sql);
		BaseDAO baseDAO=new BaseDAO(orMapping.getJdbc().getDriver_class(),orMapping.getJdbc().getUrl_path(),orMapping.getJdbc().getDb_username(),orMapping.getJdbc().getDb_userpassword());
		conn=baseDAO.openDBConnection();
		ps=(PreparedStatement) conn.prepareStatement(sql);
		int a=ps.executeUpdate();
		if(a==1)
			return true;
		else
			return false;
	}
	public Boolean insert(Object obj) throws IllegalArgumentException, IllegalAccessException, SQLException {
		String sql="insert into ";
		Class<? extends Object> clazz=obj.getClass();
		String tableName=clazz.getSimpleName();
		List<ClassInfo> classInfos=orMapping.getClassInfos();
		ClassInfo classInfo=new ClassInfo();
		Object[] value=new Object[10];
		for (ClassInfo Info : classInfos) {
			classInfo=Info;
			if(tableName.equals(Info.getName())) {
				sql=sql+Info.getTable()+"(";
				//获取对象的所有属性
				Field[] fields=clazz.getDeclaredFields();
				for (Field f : fields) {
					f.setAccessible(true);
					for(int i=0;i<Info.getDbTableInfos().size();i++) {
						DbTableInfo dbTableInfo=Info.getDbTableInfos().get(i);
						if(dbTableInfo.getName().equals(f.getName())) {
							System.out.println(f.getName()+" "+f.get(obj));
							value[i]=f.get(obj);
							sql=sql+dbTableInfo.getColumn();
							if(i!=Info.getDbTableInfos().size()-1) 
								sql=sql+",";
							else
								sql=sql+") values('";
						}
					}
				}
				for (int i=0;i<Info.getDbTableInfos().size();i++) {
					if(i!=Info.getDbTableInfos().size()-1) 
						sql=sql+value[i]+"','";
					else
						sql=sql+value[i]+"')";
				}
			}
		}
		System.out.println(sql);
		BaseDAO baseDAO=new BaseDAO(orMapping.getJdbc().getDriver_class(),orMapping.getJdbc().getUrl_path(),orMapping.getJdbc().getDb_username(),orMapping.getJdbc().getDb_userpassword());
		conn=baseDAO.openDBConnection();
		ps=(PreparedStatement) conn.prepareStatement(sql);
		int a=ps.executeUpdate();
		if(a==1)
			return true;
		else
			return false;
	}
	public <T> List<T> query(Object obj) throws IllegalArgumentException, IllegalAccessException, SQLException, InstantiationException, NoSuchMethodException, SecurityException, InvocationTargetException {
		String sql="select * from ";
		Class<? extends Object> clazz=obj.getClass();
		String tableName=clazz.getSimpleName();
		List<ClassInfo> classInfos=orMapping.getClassInfos();
		ClassInfo classInfo=new ClassInfo();
		for (ClassInfo Info : classInfos) {
			classInfo=Info;
			if(tableName.equals(Info.getName())) {
				sql=sql+Info.getTable()+" where ";
				//获取对象的所有属性
				Field[] fields=clazz.getDeclaredFields();
				for (Field f : fields) {
					f.setAccessible(true);
					for(int i=0;i<Info.getDbTableInfos().size();i++) {
						DbTableInfo dbTableInfo=Info.getDbTableInfos().get(i);
						//查找到表中对应的名称和该名称的实际值
						if(dbTableInfo.getName().equals(f.getName())) {
							//System.out.println(f.getName()+" "+f.get(obj));
							sql=sql+dbTableInfo.getColumn()+"='"+f.get(obj)+"'";
							if(i!=Info.getDbTableInfos().size()-1) {
								sql=sql+" and ";
							}
						}
					}
				}
			}
		}
		System.out.println(sql);
		//初始化参数，建立数据库连接
		BaseDAO baseDAO=new BaseDAO(orMapping.getJdbc().getDriver_class(),orMapping.getJdbc().getUrl_path(),orMapping.getJdbc().getDb_username(),orMapping.getJdbc().getDb_userpassword());
		conn=baseDAO.openDBConnection();
		ps=(PreparedStatement) conn.prepareStatement(sql);
		rs=ps.executeQuery();
		List<T> list=new ArrayList<>();
		int columnCount=rs.getMetaData().getColumnCount();//获得列的个数
		while(rs.next()) {
			T t=(T) clazz.newInstance();
			for(int i=0;i<columnCount;i++) {
				//得到每一列的名字和值
				String columnName=rs.getMetaData().getColumnName(i+1);
				//System.out.println(columnName);
				Object value=rs.getObject(columnName);
				//查找classInfo中的数据表，得到column对应的name值,合成方法
				for(int j=0;j<classInfo.getDbTableInfos().size();j++) {
					DbTableInfo dbTableInfo=classInfo.getDbTableInfos().get(j);
					if(dbTableInfo.getColumn().equals(columnName)) {
						String name=dbTableInfo.getName();
						//System.out.println(name);
						String method="set"+name.substring(0, 1).toUpperCase()+name.substring(1);
						//System.out.println(method);
						//给对象赋值
						if(value instanceof String) {
						Method m=clazz.getDeclaredMethod(method,String.class);
						m.invoke(t, value);
						}						
					}
				}
			}
			list.add(t);
		}
		baseDAO.closeDBConnection(conn, ps, rs);
		return list;
	}
	
	/*
	public static void main(String[] args) throws DocumentException, IllegalArgumentException, IllegalAccessException, InstantiationException, SQLException, NoSuchMethodException, SecurityException, InvocationTargetException {
		Configuration configuration=new Configuration("E:\\java\\UseSC\\src\\OR-Mappings.xml");
		Conversation conversation=configuration.createConversation();
		//UserBean userBean=new UserBean("devie","123456");
		//userBean.setUno(12);
		Boolean flag=conversation.delete(new UserBean(11));
		//Boolean flag=conversation.insert(userBean);
		System.out.println(flag);
		List<UserBean> userBeans= conversation.query(userBean);
		for (UserBean userBean2 : userBeans) {
			System.out.println(userBean2.getName()+" "+userBean2.getPassword());
		}
	}*/
	
}
