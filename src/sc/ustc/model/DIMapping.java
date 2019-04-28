package sc.ustc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DIMapping {
	private String name;
	private String classPath;
	private List<HashMap<String, String>> bean_ref;
	public DIMapping( ) {
		this.bean_ref=new ArrayList<>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	public List<HashMap<String, String>> getBean_ref() {
		return bean_ref;
	}
	public void setBean_ref(List<HashMap<String, String>> bean_ref) {
		this.bean_ref = bean_ref;
	}
	
	
	
}
