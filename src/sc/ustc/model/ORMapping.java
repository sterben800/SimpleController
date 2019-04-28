package sc.ustc.model;

import java.util.List;

public class ORMapping {
	private List<ClassInfo> classInfos;
	private Jdbc jdbc;
	public List<ClassInfo> getClassInfos() {
		return classInfos;
	}
	public void setClassInfos(List<ClassInfo> classInfos) {
		this.classInfos = classInfos;
	}
	public Jdbc getJdbc() {
		return jdbc;
	}
	public void setJdbc(Jdbc jdbc) {
		this.jdbc = jdbc;
	}
	
}
