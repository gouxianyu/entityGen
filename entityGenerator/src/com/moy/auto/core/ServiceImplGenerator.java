package com.moy.auto.core;

import com.moy.auto.util.Config;
import com.moy.auto.util.OutputUtil;
import com.moy.auto.util.StringUtil;

public class ServiceImplGenerator  {
	private StringBuffer sb;
	private String packageName=Config.packageOutPath+".service.impl";//包名
	private AutoGenerator ag=AutoGenerator.getAutoGenerator();
	
	public void generateMapper() {
		for (int i = 0; i < ag.tables.size(); i++) {
			sb=new StringBuffer();
			generateClass(i);
			OutputUtil.outputFile(packageName, sb+"", ag.classNames.get(i)+"ServiceImpl", "java");
		}
	}
	
	public void generateMapper(String[] tables) {
		for (int i = 0; i < tables.length; i++) {
			for (int j = 0; j < ag.tables.size(); j++) {
				if(tables[i].equalsIgnoreCase(ag.tables.get(j))) {
					sb=new StringBuffer();
					generateClass(i);
					OutputUtil.outputFile(packageName, sb+"", ag.classNames.get(j)+"ServiceImpl", "java");
				}
			}
		}
	}
	//生成实现类
	public void generateClass(int no) {
		sb.append("package "+packageName+";\r\n");
		sb.append("import "+Config.packageOutPath+".domain."+ag.classNames.get(no)+";\r\n");//导入实体
		sb.append("import "+Config.packageOutPath+".mapper."+ag.classNames.get(no)+"Mapper;\r\n");//导入dao
		sb.append("import "+Config.packageOutPath+".service."+ag.classNames.get(no)+"Service;\r\n");//导入service
		sb.append("import "+Config.packageOutPath+".dto.Condition;\r\n");
		//导入第三方包
		sb.append("import java.util.Collections;\r\n" + 
				"import java.util.Comparator;\r\n" + 
				"import java.util.List;\r\n" + 
				"import java.util.SortedSet;\r\n" + 
				"\r\n" + 
				"import javax.servlet.http.HttpServletRequest;\r\n" + 
				"\r\n" + 
				"import org.springframework.beans.factory.annotation.Autowired;\r\n" + 
				"import org.springframework.stereotype.Service;\r\n" + 
				"import org.springframework.transaction.annotation.Transactional;\r\n" + 
				"import org.springframework.web.multipart.MultipartFile;\r\n\r\n");
		//
		sb.append("@Service(\""+ag.classNames.get(no)+"Service\")\r\n");
		sb.append("public class "+ag.classNames.get(no)+"ServiceImpl implements "+ag.classNames.get(no)+"Service{\r\n\r\n");
		sb.append("\t@Autowired\r\n" + 
				"	private "+ag.classNames.get(no)+"Mapper "+ag.classNames.get(no).substring(0,1).toLowerCase()+"m;\r\n\r\n");
		
		generateInsert(no);
		generateDelete(no);
		generateUpdate(no);
		generateSelect(no);
		
		sb.append("\r\n}");
	}
	
	public void generateInsert(int no) {
		String className=ag.classNames.get(no);
		String mapper=ag.classNames.get(no).substring(0,1).toLowerCase()+"m";
		/*----------------分割线------------------*/
		sb.append("\t@Transactional\r\n");
		sb.append("\t@Override\r\n");
		sb.append("\tpublic int insert("+className+" "+StringUtil.toLowerCaseFirstOne(className)+"){\r\n");
		
		sb.append("\t\treturn "+mapper+".insert("+StringUtil.toLowerCaseFirstOne(className)+");\r\n");
		
		sb.append("\t}\r\n\r\n");
		
		/*----------------分割线------------------*/
		sb.append("\t@Transactional\r\n");
		sb.append("\t@Override\r\n");
		sb.append("\tpublic int insertSelective("+className+" "+StringUtil.toLowerCaseFirstOne(className)+"){\r\n");
		sb.append("\t\treturn "+mapper+".insertSelective("+StringUtil.toLowerCaseFirstOne(className)+");\r\n");
		sb.append("\t}\r\n\r\n");
	}
	
	public void generateDelete(int no) {
		String pk=ag.pks.get(no);
		String pkType=getPkType(pk, no);
		String className=ag.classNames.get(no);
		String mapper=ag.classNames.get(no).substring(0,1).toLowerCase()+"m";
		/*----------------分割线------------------*/
		sb.append("\t@Transactional\r\n");
		sb.append("\t@Override\r\n");
		sb.append("\tpublic int delete("+className+" "+StringUtil.toLowerCaseFirstOne(className)+"){\r\n\r\n");
		sb.append("\t\treturn "+mapper+".delete("+StringUtil.toLowerCaseFirstOne(className)+");\r\n");
		sb.append("\t}\r\n\r\n");
		
		/*----------------分割线------------------*/
		sb.append("\t@Transactional\r\n");
		sb.append("\t@Override\r\n");
		sb.append("\tpublic int deleteByPrimaryKey("+StringUtil.typeTransfer(pkType)+" "+StringUtil.getTransStr(pk, false)+"){\r\n\r\n");
		sb.append("\t\treturn "+mapper+".deleteByPrimaryKey("+StringUtil.getTransStr(pk, false)+");\r\n");
		sb.append("\t}\r\n\r\n");
	}

	public void generateUpdate(int no) {
		String className=ag.classNames.get(no);
		String mapper=ag.classNames.get(no).substring(0,1).toLowerCase()+"m";
		
		/*----------------分割线------------------*/
		sb.append("\t@Transactional\r\n");
		sb.append("\t@Override\r\n");
		sb.append("\tpublic int updateByPrimaryKey("+className+" "+StringUtil.toLowerCaseFirstOne(className)+"){\r\n\r\n");
		sb.append("\t\treturn "+mapper+".updateByPrimaryKey("+StringUtil.toLowerCaseFirstOne(className)+");\r\n");
		sb.append("\t}\r\n\r\n");
		/*----------------分割线------------------*/
		sb.append("\t@Transactional\r\n");
		sb.append("\t@Override\r\n");
		sb.append("\tpublic int updateByPrimaryKeySelective("+className+" "+StringUtil.toLowerCaseFirstOne(className)+"){\r\n\r\n");
		sb.append("\t\treturn "+mapper+".updateByPrimaryKeySelective("+StringUtil.toLowerCaseFirstOne(className)+");\r\n");
		sb.append("\t}\r\n\r\n");
	}

	public void generateSelect(int no) {
		String pk=ag.pks.get(no);
		String pkType=getPkType(pk, no);
		String className=ag.classNames.get(no);
		String mapper=ag.classNames.get(no).substring(0,1).toLowerCase()+"m";
		/*----------------分割线------------------*/
		sb.append("\tpublic "+className+" selectByPrimaryKey("+StringUtil.typeTransfer(pkType)+" "+StringUtil.getTransStr(pk, false)+"){\r\n\r\n");
		sb.append("\t\treturn "+mapper+".selectByPrimaryKey("+StringUtil.getTransStr(pk, false)+");\r\n");
		sb.append("\t}\r\n\r\n");
		
		/*----------------分割线------------------*/
		sb.append("\tpublic List<"+className+"> select("+className+" "+StringUtil.toLowerCaseFirstOne(className)+",Condition condition){\r\n\r\n");
		sb.append("\t\treturn "+mapper+".select("+StringUtil.toLowerCaseFirstOne(className)+",condition);\r\n");
		sb.append("\t}\r\n\r\n");
		
		/*----------------分割线------------------*/
		sb.append("\tpublic List<"+className+"> fuzzySelect("+className+" "+StringUtil.toLowerCaseFirstOne(className)+",Condition condition){\r\n\r\n");
		sb.append("\t\treturn "+mapper+".fuzzySelect("+StringUtil.toLowerCaseFirstOne(className)+",condition);\r\n");
		sb.append("\t}\r\n\r\n");
		
		/*----------------分割线------------------*/
		sb.append("\tpublic long selectCount("+className+" "+StringUtil.toLowerCaseFirstOne(className)+",Condition condition){\r\n\r\n");
		sb.append("\t\treturn "+mapper+".selectCount("+StringUtil.toLowerCaseFirstOne(className)+",condition);\r\n");
		sb.append("\t}\r\n\r\n");
		
		/*----------------分割线------------------*/
		sb.append("\tpublic long fuzzySelectCount("+className+" "+StringUtil.toLowerCaseFirstOne(className)+",Condition condition){\r\n\r\n");
		sb.append("\t\treturn "+mapper+".fuzzySelectCount("+StringUtil.toLowerCaseFirstOne(className)+",condition);\r\n");
		sb.append("\t}\r\n\r\n");
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
