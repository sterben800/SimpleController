package sc.ustc.tool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import sc.ustc.model.ActionCollection;
import sc.ustc.model.InterceptorCollection;

public class CglibProxy implements MethodInterceptor{
	private Enhancer enhancer=new Enhancer();
	private ActionCollection actionCollection;
	
	public CglibProxy(ActionCollection action) {
		// TODO Auto-generated constructor stub
		this.actionCollection=action;	
	}
	
	public Object getProxy(Class<?> clazz) {
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		return enhancer.create();
	}
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("开始代理");
		System.out.println(method);
		for (Object object : args) {
			System.out.print(object+" ");
		}
		String result;
		result=preDo();
		//执行前置拦截方法
		if(result.equals("failure")) 
			return "failure";
		//执行action方法
		result=(String) proxy.invokeSuper(obj, args);//执行action方法
		//执行后置拦截方法
		if(result!=null) {
			afterDo(result);
		if(result.equals("failure")) 
			return "failure";
		System.out.println("代理完成");
		}
		return "success";
	}
	public String preDo() {
		ArrayList<InterceptorCollection> interceptorlist=actionCollection.getInterceptorCollection();		
		for (InterceptorCollection interceptor : interceptorlist) {
			String className=interceptor.getInterceptorClass();
			String predo=interceptor.getInterceptorPredo();	
			String result;
			System.out.println(predo);
			try {
				Class<?> clazz=Class.forName(className);
				Method preMethod=clazz.getDeclaredMethod(predo,String.class);
				System.out.println(preMethod);
				result=(String) preMethod.invoke(clazz.newInstance(),actionCollection.getActionName());
				System.out.println(result);
				if(result.equals("failure")) {
					return "failure";
				}
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "success";
	}
	public String afterDo(String outcome) {
		ArrayList<InterceptorCollection> interceptorlist=actionCollection.getInterceptorCollection();
		for (InterceptorCollection interceptor : interceptorlist) {
			String className=interceptor.getInterceptorClass();
			String afterDo=interceptor.getInterceptorAfterdo();	
			String result;
			System.out.println(afterDo);
			try {
				Class<?> clazz=Class.forName(className);
				Method afterMethod=clazz.getDeclaredMethod(afterDo,String.class);
				System.out.println(afterMethod);
				result=(String) afterMethod.invoke(clazz.newInstance(),outcome);
				System.out.println(result);
				if(result.equals("failure")) {
					return "failure";
					}
				} catch (NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return "success";
	}

}
