package sc.ustc.model;

import java.util.List;

import com.sun.tracing.dtrace.ProviderAttributes;

public class ClassInfo {
	private String name;
	private String table;
	private String id_name;
	private List<DbTableInfo> dbTableInfos;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getId_name() {
		return id_name;
	}
	public void setId_name(String id_name) {
		this.id_name = id_name;
	}
	public List<DbTableInfo> getDbTableInfos() {
		return dbTableInfos;
	}
	public void setDbTableInfos(List<DbTableInfo> dbTableInfos) {
		this.dbTableInfos = dbTableInfos;
	}
	
}
