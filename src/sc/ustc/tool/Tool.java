package sc.ustc.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import sc.ustc.model.ActionCollection;
import sc.ustc.model.DIMapping;
import sc.ustc.model.InterceptorCollection;
import sc.ustc.model.Result;

public class Tool {
	/**
	 * ��ȡcontroller.xml�ļ�
	 * **/
	public static ArrayList<ActionCollection> readXML(String path) throws DocumentException {
		ArrayList<ActionCollection> actionlist=new ArrayList<>();//���е�action�����б�
		SAXReader reader=new SAXReader();
		Document document=reader.read(new File(path));
		String xpath="//sc-configuration//controller//action";
		//�������action�ڵ�
		List<String> list=document.selectNodes(xpath);
		//�������е�action�ڵ�
		Iterator iterator=list.iterator();
		while(iterator.hasNext()) {
			//System.out.println((Element)iterator.next());
			ActionCollection actionsingle=new ActionCollection();
			HashMap<String, Result> resultlist=new HashMap<>();
			Element element=(Element)iterator.next();
			//���action��Ԫ�ص�����
			//System.out.println(element.attribute(0).getValue()+" "+element.attribute(1).getValue()+""+element.attribute(2).getValue());
			actionsingle.setActionName(element.attribute(0).getValue());
			actionsingle.setActionClass(element.attribute(1).getValue());
			actionsingle.setActionMethod(element.attribute(2).getValue());
			//��ȡÿһ��action��ǩ�����result��ǩ
			xpath="//sc-configuration//controller//action[@name='"+element.attribute(0).getValue()+"']//reslut";
			List<String> list2=document.selectNodes(xpath);
			Iterator iterator2=list2.iterator();
			while(iterator2.hasNext()) {
				Element element2=(Element)iterator2.next();
				Result result=new Result();
				result.setResultName(element2.attribute(0).getValue());
				result.setResultType(element2.attribute(1).getValue());
				result.setResultValue(element2.attribute(2).getValue());
				resultlist.put(element2.attribute(0).getValue(), result);
				//System.out.println(result.getResultName()+" "+result.getResultType()+" "+result.getResultValue());
			}
			//��ȡÿһ��action�����intercrptor-ref��ǩ
			xpath="//sc-configuration//controller//action[@name='"+element.attribute(0).getValue()+"']//interceptor-ref";
			List<String> list3=document.selectNodes(xpath);
			Iterator iterator3=list3.iterator();
			ArrayList<InterceptorCollection> interceptors=new ArrayList<>();
			while(iterator3.hasNext()) {
				Element element3=(Element)iterator3.next();
				//����interceptor
				xpath="//sc-configuration//interceptor[@name='"+element3.attribute(0).getValue()+"']";
				List<String> list4=document.selectNodes(xpath);
				Iterator iterator4=list4.iterator();
				while(iterator4.hasNext()) {
					Element element4=(Element)iterator4.next();
					InterceptorCollection interceptor=new InterceptorCollection();
					interceptor.setInterceptorName(element4.attribute(0).getValue());
					interceptor.setInterceptorClass(element4.attribute(1).getValue());
					interceptor.setInterceptorPredo(element4.attribute(2).getValue());
					interceptor.setInterceptorAfterdo(element4.attribute(3).getValue());
					interceptors.add( interceptor);
					//System.out.println(element4.attribute(1).getValue());
				}
				//System.out.println(element3.attribute(0).getValue());
			}
			actionsingle.setInterceptorCollection(interceptors);
			actionsingle.setActionrResult(resultlist);
			actionlist.add(actionsingle);
		}	
		
		//���������������ȷ��
		for (ActionCollection collec : actionlist) {
			String name=collec.getActionName();
			String classname=collec.getActionClass();
			String methodname=collec.getActionMethod();
			System.out.println(name+" "+classname+" "+methodname);
			HashMap<String, Result> map=collec.getActionrResult();
			Set map1=map.entrySet();
			Iterator iter=map1.iterator();
			while(iter.hasNext()) {
				Map.Entry mentry = (Map.Entry)iter.next(); 
				System.out.print("hashkey is: "+ mentry.getKey() + " & Value is: "); 
				Result result=(Result)mentry.getValue();
				System.out.println(result.getResultName()+" "+result.getResultType()+" "+result.getResultValue());
			}
			ArrayList<InterceptorCollection> inter=collec.getInterceptorCollection();
			for (InterceptorCollection cp : inter) {
				System.out.println(cp.getInterceptorName()+" "+cp.getInterceptorClass()+" "+cp.getInterceptorPredo()+""+cp.getInterceptorAfterdo());
			}
			System.out.println(" ");
		}
		return actionlist;
	}
	/**
	 * ���Ҷ�Ӧ��action
	 * **/
    public static ActionCollection getAction(String actionName,String path) {
    	ActionCollection actionsingle=new ActionCollection();
    	ArrayList<ActionCollection> actionlist=new ArrayList<>();//���е�action�����б�
    	try {
			actionlist=readXML(path);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			System.out.println("��������");
			e.printStackTrace();
		}
    	for (ActionCollection actionCollection : actionlist) {
			if(actionCollection.getActionName().equals(actionName)) {
				actionsingle=actionCollection;
				break;
			}
		}
    	return actionsingle;
    }
    /**
     * ��ȡdi.xml�ļ�
     * @throws DocumentException 
     * **/
    
    public static ArrayList<DIMapping> readDiXML(String path) throws DocumentException {
    	ArrayList<DIMapping> diList=new ArrayList<>();
    	SAXReader reader=new SAXReader();
		Document document=reader.read(new File(path));
		String xpath="//sc-di//bean";
		//�������bean�ڵ�
		List<String> list=document.selectNodes(xpath);
		//�������е�bean�ڵ�
		Iterator iterator=list.iterator();
		while(iterator.hasNext()) {
			DIMapping diMapping=new DIMapping();
			Element element=(Element)iterator.next();
			diMapping.setName(element.attribute(0).getValue());
			diMapping.setClassPath(element.attribute(1).getValue());
			//���bean�ڵ������
			xpath="//sc-di//bean[@id='"+element.attribute(0).getValue()+"']//field";
			List<String> list1=document.selectNodes(xpath);
			Iterator iter=list1.iterator();
			ArrayList<HashMap<String, String>> bean_ref=new ArrayList<>();
			while(iter.hasNext()) {
				HashMap<String, String> map=new HashMap<>();
				Element element2=(Element)iter.next();
				map.put(element2.attribute(1).getValue(), element2.attribute(0).getValue());
				bean_ref.add(map);
			}
			diMapping.setBean_ref(bean_ref);
			diList.add(diMapping);
		}
    	return diList;
    }
    /**
     * ���bean�ڵ�
     * **/
    public static DIMapping getBeanFromDi(String name,ArrayList<DIMapping> diMappings) {
    	DIMapping diMapping=new DIMapping();
    	for (DIMapping di : diMappings) {
			if(name.equals(di.getName()))
				diMapping=di;
		}
    	return diMapping;
    }
    /**
     * xmlת����html�ļ�
     * @throws TransformerFactoryConfigurationError 
     * @throws TransformerException 
     * @throws IOException 
     * **/
    public static void convertXMLToHtml(String xmlPath,String htmlPath,String xslPath) throws TransformerFactoryConfigurationError, TransformerException, IOException {
    	//����xml�ļ�������
    	FileInputStream file=new FileInputStream(xmlPath);
    	Source source=new StreamSource(file);
    	//����xsl�ļ�������
    	FileInputStream newfile=new FileInputStream(xslPath);
    	Source template=new StreamSource(newfile);
    	//�������
    	PrintStream stream=new PrintStream(new File(htmlPath));
    	javax.xml.transform.Result result=new StreamResult(stream);
    	//����xsl�ļ�����ת������
    	Transformer transformer=TransformerFactory.newInstance().newTransformer(template);
    	transformer.transform(source, result);
    	newfile.close();
    	file.close();
    	
    }
	/*public static void main(String[] args) throws DocumentException, ClassNotFoundException, InstantiationException, IllegalAccessException, TransformerFactoryConfigurationError, TransformerException, IOException {
		//ActionCollection actionCollection=new ActionCollection();
		String path="E:\\java\\UseSC\\src\\di.xml";
		String name="login";
		DIMapping diMapping=new DIMapping();
		List<DIMapping> diMappings=readDiXML(path);
		diMapping=getBeanFromDi(name, (ArrayList<DIMapping>) diMappings);
		System.out.println(diMapping.getName()+" "+diMapping.getClassPath());
		List<HashMap<String, String>> beanref=diMapping.getBean_ref();
		for (HashMap<String, String> hashMap : beanref) {
			Set map=hashMap.entrySet();
			Iterator iterator=map.iterator();
			while(iterator.hasNext()) {
				Map.Entry mentry = (Map.Entry)iterator.next(); 
				System.out.println("key:"+mentry.getKey()+"  value:"+mentry.getValue());
			}
		}
		//actionCollection= getAction(name,path);
		//System.out.println(actionCollection.getActionName());
		String xmlpath="E:\\java\\UseSC\\WebContent\\page\\success_view.xml";
		String xslpath="E:\\java\\UseSC\\WebContent\\page\\success_view.xsl";
		String htmlpath="E:\\java\\UseSC\\WebContent\\page\\success_view.html";
		int i=xmlpath.indexOf("_view.xml");
		if(i>0) {
		convertXMLToHtml(xmlpath, htmlpath, xslpath);
		}
	}*/
}
