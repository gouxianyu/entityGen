package com.moy.auto.core;

import com.moy.auto.util.Config;
import com.moy.auto.util.OutputUtil;
import com.moy.auto.util.StringUtil;

public class MapperGenerator {
	private StringBuffer sb;
	private String packageName=Config.packageOutPath+".mapper";//包名
	private AutoGenerator ag=AutoGenerator.getAutoGenerator();
	
	public void generateMapper() {
		for (int i = 0; i < ag.tables.size(); i++) {
			sb=new StringBuffer();
			generateClass(i);
			OutputUtil.outputFile(packageName, sb+"", ag.classNames.get(i)+"Mapper", "java");
		}
	}
	
	public void generateMapper(String[] tables) {
		for (int i = 0; i < tables.length; i++) {
			for (int j = 0; j < ag.tables.size(); j++) {
				if(tables[i].equalsIgnoreCase(ag.tables.get(j))) {
					sb=new StringBuffer();
					generateClass(i);
					OutputUtil.outputFile(packageName, sb+"", ag.classNames.get(j)+"Mapper", "java");
				}
			}
		}
	}
	//生成类
	public void generateClass(int no) {
		
		sb.append("package "+packageName+";\r\n");
		sb.append("import "+Config.packageOutPath+".domain."+ag.classNames.get(no)+";\r\n");
		sb.append("import "+Config.packageOutPath+".dto.Condition;\r\n");
		sb.append("import java.util.List;\r\n");
		sb.append("import org.apache.ibatis.annotations.Param;\r\n");
		sb.append("\r\npublic interface "+ag.classNames.get(no)+"Mapper{\r\n\r\n");
		generateInterface(no);
		sb.append("\r\n}");
		
	}
	//生成接口方法
	public void generateInterface(int no) {
		//System.out.println(ag.pks.size());
		String pk=ag.pks.get(no);
		String pkType=getPkType(pk, no);
		String className=ag.classNames.get(no);
		
		sb.append("\tpublic int insert("+className+" "+StringUtil.toLowerCaseFirstOne(className)+");\r\n\r\n");
		sb.append("\tpublic int insertSelective("+className+" "+StringUtil.toLowerCaseFirstOne(className)+");\r\n\r\n");
		
		sb.append("\tpublic int updateByPrimaryKey("+className+" "+StringUtil.toLowerCaseFirstOne(className)+");\r\n\r\n");
		sb.append("\tpublic int updateByPrimaryKeySelective("+className+" "+StringUtil.toLowerCaseFirstOne(className)+");\r\n\r\n");
		
		sb.append("\tpublic int delete("+className+" "+StringUtil.toLowerCaseFirstOne(className)+");\r\n\r\n");
		sb.append("\tpublic int deleteByPrimaryKey("+StringUtil.typeTransfer(pkType)+" "+StringUtil.getTransStr(pk, false)+");\r\n\r\n");
		
		
		
		sb.append("\tpublic "+className+" selectByPrimaryKey("+StringUtil.typeTransfer(pkType)+" "+StringUtil.getTransStr(pk, false)+");\r\n\r\n");
		sb.append("\tpublic List<"+className+"> select(@Param(\""+className+"\")"+className+" "+StringUtil.toLowerCaseFirstOne(className)+",@Param(\"Condition\")Condition condition);\r\n\r\n");
		sb.append("\tpublic List<"+className+"> fuzzySelect(@Param(\""+className+"\")"+className+" "+StringUtil.toLowerCaseFirstOne(className)+",@Param(\"Condition\")Condition condition);\r\n\r\n");
		sb.append("\tpublic long selectCount(@Param(\""+className+"\")"+className+" "+StringUtil.toLowerCaseFirstOne(className)+",@Param(\"Condition\")Condition condition);\r\n\r\n");
		sb.append("\tpublic long fuzzySelectCount(@Param(\""+className+"\")"+className+" "+StringUtil.toLowerCaseFirstOne(className)+",@Param(\"Condition\")Condition condition);\r\n\r\n");
		
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
