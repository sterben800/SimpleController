package sc.ustc.model;

public class UserBean {
	private String name;
	private String password;
	private int uno;
	public UserBean() {
		// TODO Auto-generated constructor stub
	}
	
	public UserBean(int uno) {
		super();
		this.uno = uno;
	}

	public UserBean(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public int getUno() {
		return uno;
	}

	public void setUno(int uno) {
		this.uno = uno;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
