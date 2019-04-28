package sc.ustc.model;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jdk.internal.dynalink.beans.StaticClass;

public class IntrospectManager {
	public static void setValueFromRequest(Object bean,String name,Object value) {
		try {
			PropertyDescriptor pDescriptor=new PropertyDescriptor(name, bean.getClass());
			Method method=pDescriptor.getWriteMethod();
			if(value!=null) {
			method.invoke(bean,value);
			System.out.println("赋值成功");
			}else {
				method.invoke(bean,0);
			}
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			System.out.println("该类中没有该域");
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
		}
	}
	public static Object getValueFromBean(Object bean,String name) {
		 Object propertyValue = null ;
		try {
			PropertyDescriptor pDescriptor=new PropertyDescriptor(name, bean.getClass());
			Method method=pDescriptor.getReadMethod();
			propertyValue=method.invoke(bean);
		} catch (IntrospectionException e) {
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
		}
		return propertyValue;
	}
	/*public static void main(String[] args) {
		UserBean userBean=new UserBean(1);
		IntrospectManager.setValueFromRequest(userBean, "name", "lisa");
		System.out.println(userBean.getName());
	}*/
}
