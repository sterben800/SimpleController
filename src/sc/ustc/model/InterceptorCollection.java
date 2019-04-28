package sc.ustc.model;

import com.sun.jmx.snmp.SnmpStringFixed;

public class InterceptorCollection {
	private String interceptorName;
	private String interceptorClass;
	private String interceptorPredo;
	private String interceptorAfterdo;
	
	public InterceptorCollection() {
		
	}
	public InterceptorCollection(String interceptorName, String interceptorClass, String interceptorPredo,
			String interceptorAfterdo) {
		super();
		this.interceptorName = interceptorName;
		this.interceptorClass = interceptorClass;
		this.interceptorPredo = interceptorPredo;
		this.interceptorAfterdo = interceptorAfterdo;
	}
	public String getInterceptorName() {
		return interceptorName;
	}
	public void setInterceptorName(String interceptorName) {
		this.interceptorName = interceptorName;
	}
	public String getInterceptorClass() {
		return interceptorClass;
	}
	public void setInterceptorClass(String interceptorClass) {
		this.interceptorClass = interceptorClass;
	}
	public String getInterceptorPredo() {
		return interceptorPredo;
	}
	public void setInterceptorPredo(String interceptorPredo) {
		this.interceptorPredo = interceptorPredo;
	}
	public String getInterceptorAfterdo() {
		return interceptorAfterdo;
	}
	public void setInterceptorAfterdo(String interceptorAfterdo) {
		this.interceptorAfterdo = interceptorAfterdo;
	}
	
}
