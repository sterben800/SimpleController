package sc.ustc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sun.java2d.pipe.AATextRenderer;

public class ActionCollection {
	private String actionName;
	private String actionClass;
	private String actionMethod;
	private HashMap<String,Result> actionrResult;
	private ArrayList<InterceptorCollection> interceptorCollection;
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getActionClass() {
		return actionClass;
	}
	public void setActionClass(String actionClass) {
		this.actionClass = actionClass;
	}
	public String getActionMethod() {
		return actionMethod;
	}
	public void setActionMethod(String actionMethod) {
		this.actionMethod = actionMethod;
	}
	public HashMap<String, Result> getActionrResult() {
		return actionrResult;
	}
	public void setActionrResult(HashMap<String, Result> actionrResult) {
		this.actionrResult = actionrResult;
	}
	
	public ArrayList<InterceptorCollection> getInterceptorCollection() {
		return interceptorCollection;
	}
	public void setInterceptorCollection(ArrayList<InterceptorCollection> interceptorCollection) {
		this.interceptorCollection = interceptorCollection;
	}
	public ActionCollection() {

	}
	
}
