package sc.ustc.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.dom4j.DocumentException;

import sc.ustc.model.ActionCollection;
import sc.ustc.model.DIMapping;
import sc.ustc.model.IntrospectManager;
import sc.ustc.model.Result;
import sc.ustc.tool.CglibProxy;
import sc.ustc.tool.Tool;

/**
 * Servlet implementation class SimpleController
 */
@WebServlet("/SimpleController")
public class SimpleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SimpleController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.sendRedirect("/SimpleController/index.jsp");
		//request.getRequestDispatcher("/index.jsp").forward(request, response);
		//response.setContentType("text/html");
		//response.setCharacterEncoding("UTF-8");
		//request.getRequestDispatcher("/index.jsp").include(request, response);
		//response.setCharacterEncoding("UTF-8");
		//System.out.println(this.getServletContext().getRequestDispatcher("/index.jsp").toString());
		//this.getServletContext().getRequestDispatcher("/page/welcome.jsp").forward(request, response); 		
		//response.sendRedirect("/UseSC/welcome.html");
		response.setContentType("text/html,charset=utf-8");
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        //得到action的名称
        String url=request.getRequestURI();
        System.out.println(" uri:"+url);
        String[] urlParse=url.split("/");
        String actionName=urlParse[urlParse.length-1];
        actionName=actionName.substring(0, actionName.indexOf("."));
        System.out.println("actionName:"+actionName);
        //解析controller.xml文件,判断是否存在对应的action
        Tool tool=new Tool();
        ActionCollection actionCollection=new ActionCollection();
        DIMapping diMapping=new DIMapping();
        //获得xml文件的路径
        String path=this.getServletContext().getRealPath("WEB-INF/classes/controller.xml");
        String dipath=this.getServletContext().getRealPath("WEB-INF/classes/di.xml");
        actionCollection=tool.getAction(actionName,path);
        if(actionCollection.getActionName()!=null) {        	
        	String className=actionCollection.getActionClass();
        	String classMethod=actionCollection.getActionMethod();
        	System.out.println(className+" "+classMethod);
        	Object obj=null;
			try {
				Class clazz = Class.forName(actionCollection.getActionClass());
				obj=clazz.newInstance();
				//查找有参方法
				Method method=clazz.getDeclaredMethod(actionCollection.getActionMethod(),HttpServletRequest.class,HttpServletResponse.class);
				System.out.println(method);
				String returnResult = null;
				if(actionCollection.getInterceptorCollection().size()>0) {
				  //开始代理
				  CglibProxy proxy=new CglibProxy(actionCollection);
				  Object ob=proxy.getProxy(clazz);
					ArrayList<DIMapping> diMappings=tool.readDiXML(dipath);
					//查找对应action有无bean节点
					diMapping=tool.getBeanFromDi(actionName,diMappings);
					if(diMapping!=null) {
						List<String> bean_dependecy=new ArrayList<>();
						System.out.println("查找到："+diMapping.getName()+" ");
						List<HashMap<String, String>> bean_ref=diMapping.getBean_ref();
						//查找对应action有无依赖
						if(bean_ref!=null) {
							Iterator iterator=bean_ref.iterator();
							while(iterator.hasNext()) {
								HashMap<String, String> hashMap=(HashMap<String, String>) iterator.next();
								Set map=hashMap.entrySet();
								Iterator iterator2=map.iterator();
								while(iterator2.hasNext()) {
									Map.Entry entry=(Map.Entry) iterator2.next();
									String classid=(String) entry.getKey();
									String classFieldName=(String) entry.getValue();
									System.out.println(classid+" "+classFieldName);
									//通过id找到对应的依赖类的实际类名
									DIMapping diMapping2=tool.getBeanFromDi(classid, diMappings);
									String classname=diMapping2.getClassPath();
									System.out.println(classname);
									//实例化依赖类
									Class cls=Class.forName(classname);
									Object beanObject=cls.newInstance();
									Field[] fields=cls.getDeclaredFields();
									//为依赖类赋值
									for (Field f : fields) {
										System.out.println("field:"+request.getParameter(f.getName()));
										//if(request.getParameter(f.getName())!=null) {
										IntrospectManager.setValueFromRequest(beanObject, f.getName(), request.getParameter(f.getName()));
										System.out.println("赋值为："+IntrospectManager.getValueFromBean(beanObject,f.getName()));
										//}
									}
									//为被依赖类中的依赖成员赋值
									IntrospectManager.setValueFromRequest(ob, classFieldName, beanObject);
								}						
							}
						}
					}
				  returnResult=(String) method.invoke(ob, request,response);
				}
				else {
				   returnResult=(String) method.invoke(obj, request,response);		
				}
				System.out.println(returnResult);
				if(returnResult.equals("success")) {
					HashMap<String, Result> resultAction=actionCollection.getActionrResult();
					Result result=new Result();
					result=resultAction.get("success");
					System.out.println(result.getResultValue()+" "+result.getResultType());
					if(result.getResultType().equals("forward")) {
						int i=result.getResultValue().indexOf("_view.xml");
						//生成XML的推送html文件
							if(i>0) {
								String projectPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
								System.out.println(projectPath);
								//拼接路径
								projectPath = projectPath.substring(0, projectPath.lastIndexOf("/"));  
						        projectPath = projectPath.substring(0, projectPath.lastIndexOf("/"));
						        //WebRoot根路径：
						        projectPath = projectPath.substring(0, projectPath.lastIndexOf("/") + 1); 
						        System.out.println(projectPath);
						        String xmlPath=projectPath+result.getResultValue();
						        String xslPath=projectPath+result.getResultValue().replaceAll("xml", "xsl");
						        String htmlPath=projectPath+result.getResultValue().replaceAll("xml", "html");
						        System.out.println(htmlPath);
						        tool.convertXMLToHtml(xmlPath, htmlPath, xslPath);
						        String[] newpath=htmlPath.split("/");
						        String despath=newpath[newpath.length-2]+"/"+newpath[newpath.length-1];
						        System.out.println(despath);
						        request.getRequestDispatcher(despath).forward(request, response);
						       // this.getServletContext().getRequestDispatcher("/"+result.getResultValue()).forward(request, response);
							}
							else {
								String path1="/"+result.getResultValue();
								System.out.println(path1);
								this.getServletContext().getRequestDispatcher("/"+result.getResultValue()).forward(request, response);
							}
						}
				}else {
					HashMap<String, Result> resultAction=actionCollection.getActionrResult();
					Result result=new Result();
					result=resultAction.get("failure");
					System.out.println(result.getResultValue()+" "+result.getResultType());
					if(result.getResultType().equals("redirect")) {
						System.out.println(request.getContextPath()+"/"+result.getResultValue());
						response.sendRedirect(request.getContextPath()+"/"+result.getResultValue());
					}
				}
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    		
        }else {
        	response.sendRedirect("/UseSC/login.jsp");
        }
        
        
	}

}
