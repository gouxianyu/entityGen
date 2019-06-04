package com.moy.auto.core;

import com.moy.auto.util.Config;
import com.moy.auto.util.OutputUtil;

public class EntityGenerator{
	
	private StringBuffer sb;
	private String packageName=Config.packageOutPath+".domain";//包名
	private AutoGenerator ag=AutoGenerator.getAutoGenerator();
	
	public void generateEntity() {
		//System.out.println(ag.tables.size());
		for (int i = 0; i < ag.tables.size(); i++) {
			sb=new StringBuffer();
			importPackage(i);
			generateClass(i);
			OutputUtil.outputFile(packageName, sb+"", ag.classNames.get(i), "java");
		}
	}

	public void generateEntity(String[] tables) {
		
		for (int i = 0; i < tables.length; i++) {
			
			for (int j = 0; j < ag.tables.size(); j++) {
				if(tables[i].equalsIgnoreCase(ag.tables.get(j))) {
					sb=new StringBuffer();
					importPackage(j);
					generateClass(j);
					OutputUtil.outputFile(packageName, sb+"", ag.classNames.get(j), "java");
				}
			}
		}
	}
	//导入包,no为表的编号
	public void importPackage(int no) {
		sb.append("package "+packageName+";\r\n");
		
		sb.append("import org.springframework.stereotype.Component;\r\n");
		sb.append("@Component\r\n");
		
//		for (int j = 0; j < ag.colTypes[no].length; j++) {
//			if(ag.colTypes[no][j].equalsIgnoreCase("datetime")) {
//				sb.append("import java.util.Date;\r\n");
//			}
//			if(ag.colTypes[no][j].equalsIgnoreCase("image")||ag.colTypes[no][j].equalsIgnoreCase("text")) {
//				sb.append("import java.sql.*;\r\n");
//			}
//		}
			
	}
	
	//生成类
	public void generateClass(int no) {
		sb.append("public class "+ag.classNames.get(no)+"{\r\n");
		generateAttr(no);
		generateMethod(no);
		sb.append("}");
	}
	
	//生成属性
	public void generateAttr(int no) {
		for (int i = 0; i < ag.javaTypes[no].length; i++) {
			sb.append("\tprivate "+ag.javaTypes[no][i]+" "+ag.attrNames[no][i]+";");
			if(!ag.annotation[no][i].equals("")) {
				sb.append("//"+ag.annotation[no][i]);
			}
			sb.append("\r\n");
		}
		sb.append("\r\n");
	}
	
	//生成get/set
	public void generateMethod(int no) {
		for (int i = 0; i < ag.javaTypes[no].length; i++) {
			//getter
			sb.append("\tpublic "+ag.javaTypes[no][i]+" get"+ag.upAttrNames[no][i]+"(){\r\n");
			sb.append("\t\treturn "+ag.attrNames[no][i]+";\r\n");
			sb.append("\t}\r\n");
			//setter
			sb.append("\tpublic void "+"set"+ag.upAttrNames[no][i]+"("+ag.javaTypes[no][i]+" "+ag.attrNames[no][i]+"){\r\n");
			sb.append("\t\tthis."+ag.attrNames[no][i]+"="+ag.attrNames[no][i]+";\r\n");
			sb.append("\t}\r\n");
		}
	}
}
