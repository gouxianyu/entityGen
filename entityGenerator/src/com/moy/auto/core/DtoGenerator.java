package com.moy.auto.core;

import com.moy.auto.util.Config;
import com.moy.auto.util.OutputUtil;

public class DtoGenerator {
	private StringBuffer sb;
	private String packageName=Config.packageOutPath+".dto";//包名
	
	public void generateDto() {
		generateCondition();
	}
	
	public void generateCondition() {
		sb=new StringBuffer();
		sb.append("package "+packageName+";\r\n");
		sb.append("public class Condition {\r\n" + 
				"	\r\n" + 
				"private String order;\r\n" + 
				"private int pageSize=-1;\r\n" + 
				"private int currentPageNum=-1;\r\n" + 
				"private int start;\r\n" + 
				"public String getOrder() {\r\n" + 
				"	return order;\r\n" + 
				"}\r\n" + 
				"public void setOrder(String order) {\r\n" + 
				"	this.order = order;\r\n" + 
				"}\r\n" + 
				"public int getPageSize() {\r\n" + 
				"	return pageSize;\r\n" + 
				"}\r\n" + 
				"public void setPageSize(int pageSize) {\r\n" + 
				"	this.pageSize = pageSize;\r\n" + 
				"}\r\n" + 
				"public int getCurrentPageNum() {\r\n" + 
				"	return currentPageNum;\r\n" + 
				"}\r\n" + 
				"public void setCurrentPageNum(int currentPageNum) {\r\n" + 
				"	this.currentPageNum = currentPageNum;\r\n" + 
				"}\r\n" + 
				"public int getStart() {\r\n" + 
				"	return (currentPageNum-1)*pageSize;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"}\r\n" + 
				"");
		OutputUtil.outputFile(packageName, sb+"", "Condition", "java");
	}
}
