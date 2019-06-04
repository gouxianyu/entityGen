package com.moy.auto.core;

import com.moy.auto.util.Config;
import com.moy.auto.util.OutputUtil;
import com.moy.auto.util.StringUtil;

public class ControllerGenerator {
	private StringBuffer sb;
	private String packageName=Config.packageOutPath+".controller";//包名
	private AutoGenerator ag=AutoGenerator.getAutoGenerator();
	
	public void generateController() {
		for (int i = 0; i < ag.tables.size(); i++) {
			sb=new StringBuffer();
			generateClass(i);
			OutputUtil.outputFile(packageName, sb+"", ag.classNames.get(i)+"Controller", "java");
		}
	}
	
	public void generateController(String[] tables) {
		for (int i = 0; i < tables.length; i++) {
			for (int j = 0; j < ag.tables.size(); j++) {
				if(tables[i].equalsIgnoreCase(ag.tables.get(j))) {
					sb=new StringBuffer();
					generateClass(i);
					OutputUtil.outputFile(packageName, sb+"", ag.classNames.get(j)+"Controller", "java");
				}
			}
		}
	}
	
	public void generateClass(int no) {
		
		String pk=ag.pks.get(no);
		String pkType=getPkType(pk, no);
		String className=ag.classNames.get(no);
		String service=ag.classNames.get(no).substring(0,1).toLowerCase()+"s";
		
		sb.append("package "+packageName+";\r\n");
		sb.append("import "+Config.packageOutPath+".domain."+ag.classNames.get(no)+";\r\n");//导入实体
		sb.append("import "+Config.packageOutPath+".service."+ag.classNames.get(no)+"Service;\r\n");//导入service
		
		sb.append("import "+Config.packageOutPath+".dto.Condition;\r\n");
		//导入第三方包
		sb.append("import java.io.IOException;\r\n" + 
				"import java.util.List;\r\n" + 
				"\r\n" + 
				"import javax.servlet.http.HttpServletRequest;\r\n" + 
				"import javax.servlet.http.HttpServletResponse;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"import org.springframework.beans.factory.annotation.Autowired;\r\n" + 
				"import org.springframework.stereotype.Controller;\r\n" + 
				"import org.springframework.web.bind.annotation.CrossOrigin;\r\n" + 
				"import org.springframework.web.bind.annotation.RequestMapping;\r\n" + 
				"import org.springframework.web.bind.annotation.ResponseBody;\r\n" + 
				"import org.springframework.web.multipart.MultipartFile;\r\n\r\n");
		//
		sb.append("@CrossOrigin(origins = \"*\", maxAge = 3600)\r\n");
		sb.append("@Controller\r\n");
		sb.append("@RequestMapping(\"/"+StringUtil.toLowerCaseFirstOne(ag.classNames.get(no))+"\")\r\n");
		sb.append("public class "+ag.classNames.get(no)+"Controller{\r\n\r\n");
		sb.append("\t@Autowired\r\n" + 
				"	private "+ag.classNames.get(no)+"Service "+ag.classNames.get(no).substring(0,1).toLowerCase()+"s;\r\n\r\n");
		
		/*
		 * 测试方法
		 */
		
		sb.append("\t@RequestMapping(\"/selectByPrimaryKey\")\r\n");
		sb.append("\t@ResponseBody\r\n");
		sb.append("\tpublic "+className+" selectByPrimaryKey("+StringUtil.typeTransfer(pkType)+" "+StringUtil.getTransStr(pk, false)+"){\r\n");
		sb.append("\t\treturn "+service+".selectByPrimaryKey("+StringUtil.getTransStr(pk, false)+");\r\n");
		sb.append("\t}\r\n");

		sb.append("\r\n}");
	}
	
	public String getPkType(String pk,int no) {
		int i=0;
		for (String pkName : ag.colNames[no]) {
			if(pkName.equalsIgnoreCase(pk)) {
				return ag.colTypes[no][i];
			}
			i++;
		}
		return "";
	}
}
