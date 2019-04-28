package sc.ustc.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import sc.ustc.model.ClassInfo;
import sc.ustc.model.DbTableInfo;
import sc.ustc.model.Jdbc;
import sc.ustc.model.ORMapping;

public class Configuration {
	private ORMapping orMapping;

	
	public Configuration(String path) throws DocumentException {
		init(path);
	}

	public ORMapping getOrMapping() {
		return orMapping;
	}

	public void setOrMapping(ORMapping orMapping) {
		this.orMapping = orMapping;
	}
	public void init(String path) throws DocumentException {
		orMapping=new ORMapping();
		SAXReader reader=new SAXReader();
		Document document=reader.read(new File(path));
		String xpath="//OR-Mappings//jdbc//propetry";
		//获得jdbc的属性值
		List<String> list=document.selectNodes(xpath);
		List<String> collection=new ArrayList<>();
		Iterator iterator=list.iterator();
		while(iterator.hasNext()) {
			Element element=(Element)iterator.next();
			collection.add(element.element("value").getText());
		}
		Jdbc jdbc=new Jdbc();
		jdbc.setDriver_class(collection.get(0));
		jdbc.setUrl_path(collection.get(1));
		jdbc.setDb_username(collection.get(2));
		jdbc.setDb_userpassword(collection.get(3));
		orMapping.setJdbc(jdbc);
		//System.out.println(jdbc.getDb_username()+" "+jdbc.getDb_userpassword()+" "+jdbc.getDriver_class()+" "+jdbc.getUrl_path());
		//获得class的属性值
		Element root=document.getRootElement();
		List<Element> list1=root.elements("class");
		List<ClassInfo> classInfos=new ArrayList<>();
		Iterator iterator2=list1.iterator();
		while(iterator2.hasNext()) {
			ClassInfo classInfo=new ClassInfo();
			Element element=(Element)iterator2.next();
			classInfo.setName(element.element("name").getText());
			classInfo.setTable(element.element("table").getText());
			classInfo.setId_name(element.element("id").element("name").getText());
			//System.out.println(classInfo.getId_name());
			//获得class里面关于数据库表 的信息
			@SuppressWarnings("unchecked")
			List<Element> list3=element.elements("property");
			//System.out.println(list3.size());
			List<DbTableInfo> dbTableInfos=new ArrayList<>();
			Iterator iterator3=list3.iterator();
			while(iterator3.hasNext()) {
				Element element2=(Element)iterator3.next();
				DbTableInfo dbTableInfo=new DbTableInfo();
				dbTableInfo.setName(element2.element("name").getText());
				dbTableInfo.setColumn(element2.element("column").getText());
				dbTableInfo.setType(element2.element("type").getText());
				dbTableInfo.setLazy(element2.element("lazy").getText());
				dbTableInfos.add(dbTableInfo);
				//System.out.println(dbTableInfo.getName()+" "+dbTableInfo.getColumn()+" "+dbTableInfo.getType()+" "+dbTableInfo.getLazy());
			}
			classInfo.setDbTableInfos(dbTableInfos);
			classInfos.add(classInfo);
		}
		orMapping.setClassInfos(classInfos);		
	}
	public Conversation createConversation() {
		Conversation conversation = Conversation.getInstance();
		conversation.setOrMapping(orMapping);
		return conversation;
	}
	public void printOrmapping() {
		Jdbc jdbc=orMapping.getJdbc();
		System.out.println(jdbc.getDb_username()+" "+jdbc.getDb_userpassword()+" "+jdbc.getDriver_class()+" "+jdbc.getUrl_path());
		List<ClassInfo> classInfos=orMapping.getClassInfos();
		for (ClassInfo classInfo : classInfos) {
			System.out.println(classInfo.getName()+" "+classInfo.getTable()+" "+classInfo.getId_name());
			List<DbTableInfo> dbTableInfos=classInfo.getDbTableInfos();
			for (DbTableInfo dbTableInfo : dbTableInfos) {
				System.out.println(dbTableInfo.getName()+" "+dbTableInfo.getColumn()+" "+dbTableInfo.getType()+" "+dbTableInfo.getLazy());
			}
		}
		
	}
}
