package com.moy.auto.core;

import com.moy.auto.util.Config;
import com.moy.auto.util.OutputUtil;
import com.moy.auto.util.StringUtil;

public class MappingGenerator {
	
	private StringBuffer sb;
	private String packageName=Config.packageOutPath+".mapper";//包名
	private AutoGenerator ag=AutoGenerator.getAutoGenerator();
	
	public void generateMapping() {
		for (int i = 0; i < ag.tables.size(); i++) {
			sb=new StringBuffer();
			//generateClass(i);
			generateXml(i);
			OutputUtil.outputFile(packageName, sb+"", ag.classNames.get(i)+"Mapping", "xml");
		}
	}
	
	public void generateMapping(String[] tables) {
		for (int i = 0; i < tables.length; i++) {
			for (int j = 0; j < ag.tables.size(); j++) {
				if(tables[i].equalsIgnoreCase(ag.tables.get(j))) {
					sb=new StringBuffer();
					//generateClass(i);
					generateXml(i);
					OutputUtil.outputFile(packageName, sb+"", ag.classNames.get(j)+"Mapping", "xml");
				}
			}
		}
	}
	
	public void generateXml(int no) {
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n"+
		"<!DOCTYPE mapper\r\n"+
		"PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\r\n"+
		"\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n"+
		"<mapper namespace=\""+packageName+"."+""+ag.classNames.get(no)+"Mapper\">\r\n");
		
		generateInsert(no);
		generateDelete(no);
		generateUpdate(no);
		generateSelect(no);
		
		sb.append("</mapper>");
	}
	
	public void generateInsert(int no) {
		
		String className=ag.classNames.get(no);
		String table=ag.tables.get(no);

		/*----------------分割线------------------*/
		sb.append("\t<insert id=\"insert\" parameterType=\""+className+"\">\r\n");
		sb.append("\t\tinsert into "+table+"(");
		for (int i=0;i<ag.colNames[no].length;i++) {
			String col=ag.colNames[no][i];
			if(i==ag.colNames[no].length-1) {
				sb.append(col);
			}else {
				sb.append(col+",");
			}
		}
		sb.append(")\r\n");
		sb.append("\t\tvalues(");
		for (int i=0;i<ag.attrNames[no].length;i++) {
			String col=ag.attrNames[no][i];
			if(i==ag.attrNames[no].length-1) {
				sb.append("#{"+col+",jdbcType="+ag.colTypes[no][i]+"}");
			}else {
				sb.append("#{"+col+",jdbcType="+ag.colTypes[no][i]+"}"+",");
			}
		}
		sb.append(")\r\n");
		sb.append("\t</insert>\r\n");
		
		/*----------------分割线------------------*/
		sb.append("\t<insert id=\"insertSelective\" parameterType=\""+className+"\">\r\n");
		sb.append("\t\tinsert into "+table+"\r\n");
		sb.append("\t\t<trim suffixOverrides=\",\" prefix=\"(\" suffix=\")\">\r\n");
		for (int i=0;i<ag.attrNames[no].length;i++) {
			String attr=ag.attrNames[no][i];
			String col=ag.colNames[no][i];
			
			sb.append("\t\t\t <if test=\""+attr+" != null\" >"+col+",</if>\r\n");
		}
		sb.append("\t\t</trim>\r\n");
		
		sb.append("\t\t<trim suffixOverrides=\",\" prefix=\"values(\" suffix=\")\" >\r\n");
		for (int i=0;i<ag.attrNames[no].length;i++) {
			String attr=ag.attrNames[no][i];
			
			sb.append("\t\t\t <if test=\""+attr+" != null\" >#{"+attr+",jdbcType="+ag.colTypes[no][i]+"},</if>\r\n");
		}
		sb.append("\t\t</trim>\r\n");
		sb.append("\t</insert>\r\n");
		
	}
	
	public void generateDelete(int no) {
		String pk=ag.pks.get(no);
		String pkType=getPkType(pk, no);
		String className=ag.classNames.get(no);
		String table=ag.tables.get(no);
		/*----------------分割线------------------*/
		sb.append("\t<delete id=\"delete\" parameterType=\""+className+"\">\r\n");
		sb.append("\t\tdelete from "+table+"\r\n");
		sb.append("\t\t<where>\r\n");
		for (int i=0;i<ag.attrNames[no].length;i++) {
			String attr=ag.attrNames[no][i];
			String col=ag.colNames[no][i];
			sb.append("\t\t\t <if test=\""+StringUtil.toLowerCaseFirstOne(className)+"."+attr+" != null\" >and "+col+"=#{"+StringUtil.toLowerCaseFirstOne(className)+"."+attr+",jdbcType="+ag.colTypes[no][i]+"}</if>\r\n");
		}
		sb.append("\t\t</where>\r\n");
		sb.append("\t</delete>\r\n");
		
		/*----------------分割线------------------*/
		sb.append("\t<delete id=\"deleteByPrimaryKey\">\r\n");
		sb.append("\t\tdelete from "+table+" where "+pk+"=#{"+StringUtil.getTransStr(pk, false)+",jdbcType="+pkType+"}\r\n");
		sb.append("\t</delete>\r\n");
		
	}

	public void generateUpdate(int no) {
		String pk=ag.pks.get(no);
		String pkType=getPkType(pk, no);
		String className=ag.classNames.get(no);
		String table=ag.tables.get(no);

		/*----------------分割线------------------*/
		sb.append("\t<update id=\"updateByPrimaryKey\" parameterType=\""+className+"\">\r\n");
		sb.append("\t\tupdate "+table+"\r\n");
		
		for (int i=0;i<ag.attrNames[no].length;i++) {
			String attr=ag.attrNames[no][i];
			String col=ag.colNames[no][i];
			if(i==ag.attrNames[no].length-1) {
				sb.append("\t\t\tset "+col+"=#{"+StringUtil.toLowerCaseFirstOne(className)+"."+attr+",jdbcType="+ag.colTypes[no][i]+"}\r\n");
			}else {
				sb.append("\t\t\tset "+col+"=#{"+StringUtil.toLowerCaseFirstOne(className)+"."+attr+",jdbcType="+ag.colTypes[no][i]+"},\r\n");
			}
		}
		sb.append("\t\twhere "+pk+"=#{"+StringUtil.toLowerCaseFirstOne(className)+"."+StringUtil.getTransStr(pk, false)+",jdbcType="+pkType+"}\r\n");
		sb.append("\t</update>\r\n");
		
		/*----------------分割线------------------*/
		sb.append("\t<update id=\"updateByPrimaryKeySelective\" parameterType=\""+className+"\">\r\n");
		sb.append("\t\tupdate "+table+" \r\n");
		sb.append("\t\t\t<set>\r\n");
		for (int i=0;i<ag.attrNames[no].length;i++) {
			String attr=ag.attrNames[no][i];
			String col=ag.colNames[no][i];
			if(!col.equals(pk)) {
			if(i==ag.attrNames[no].length-1) {
				sb.append("\t\t\t<if test=\""+attr+" != null\">"+col+"=#{"+attr+",jdbcType="+ag.colTypes[no][i]+"}</if>\r\n");
			}else {
				sb.append("\t\t\t<if test=\""+attr+" != null\">"+col+"=#{"+attr+",jdbcType="+ag.colTypes[no][i]+"},</if>\r\n");
			}
			}
		}
		sb.append("\t\t\t</set>\r\n");
		sb.append("\t\twhere "+pk+"=#{"+StringUtil.toLowerCaseFirstOne(className)+"."+StringUtil.getTransStr(pk, false)+",jdbcType="+pkType+"}\r\n");
		sb.append("\t</update>\r\n");
	}

	public void generateSelect(int no) {
		String pk=ag.pks.get(no);
		String pkType=getPkType(pk, no);
		String className=ag.classNames.get(no);
		String table=ag.tables.get(no);
		
		/*----------------分割线------------------*/
		sb.append("\t<select id=\"selectByPrimaryKey\" resultType=\""+className+"\">\r\n");
		sb.append("\t\tselect * from "+table+" where "+pk+"=#{"+StringUtil.getTransStr(pk, false)+",jdbcType="+pkType+"}\r\n");
		sb.append("\t</select>\r\n");
		
		/*----------------分割线------------------*/
		sb.append("\t<select id=\"select\" resultType=\""+className+"\">\r\n");
		sb.append("\t\tselect * from "+table+"\r\n");
		sb.append("\t\t<where>\r\n");
		
		for (int i=0;i<ag.attrNames[no].length;i++) {
			String attr=ag.attrNames[no][i];
			String col=ag.colNames[no][i];
			sb.append("\t\t\t <if test=\""+className+"."+attr+" != null\" >and "+col+"=#{"+className+"."+attr+",jdbcType="+ag.colTypes[no][i]+"}</if>\r\n");
		}
		
		sb.append("\t\t</where>\r\n");
		sb.append("\t\t<if test=\"Condition.order!=null\">order by ${Condition.order}</if>\r\n");
		sb.append("\t\t<if test=\"Condition.pageSize!=-1 and Condition.currentPageNum!=-1\">limit #{Condition.start},#{Condition.pageSize}</if>\r\n");
		
		sb.append("\t</select>\r\n");
		
		/*----------------分割线------------------*/
		sb.append("\t<select id=\"fuzzySelect\" resultType=\""+className+"\">\r\n");
		sb.append("\t\tselect * from "+table+"\r\n");
		sb.append("\t\t<where>\r\n");
		
		for (int i=0;i<ag.attrNames[no].length;i++) {
			String attr=ag.attrNames[no][i];
			String col=ag.colNames[no][i];
			sb.append("\t\t\t <if test=\""+className+"."+attr+" != null\" >or "+col+" like \"%\"#{"+className+"."+attr+",jdbcType="+ag.colTypes[no][i]+"}\"%\"</if>\r\n");
		}
		
		sb.append("\t\t</where>\r\n");
		sb.append("\t\t<if test=\"Condition.order!=null\">order by ${Condition.order}</if>\r\n");
		sb.append("\t\t<if test=\"Condition.pageSize!=null and Condition.currentPageNum!=null\">limit #{Condition.start},#{Condition.pageSize}</if>\r\n");
		
		sb.append("\t</select>\r\n");
		
		/*----------------分割线------------------*/
		sb.append("\t<select id=\"selectCount\" resultType=\"long\">\r\n");
		sb.append("\t\tselect count(*) from "+table+"\r\n");
		sb.append("\t\t<where>\r\n");
		
		for (int i=0;i<ag.attrNames[no].length;i++) {
			String attr=ag.attrNames[no][i];
			String col=ag.colNames[no][i];
			sb.append("\t\t\t <if test=\""+className+"."+attr+" != null\" >and "+col+"=#{"+className+"."+attr+",jdbcType="+ag.colTypes[no][i]+"}</if>\r\n");
		}
		
		sb.append("\t\t</where>\r\n");
		sb.append("\t</select>\r\n");
		
		/*----------------分割线------------------*/
		sb.append("\t<select id=\"fuzzySelectCount\" resultType=\"long\">\r\n");
		sb.append("\t\tselect count(*) from "+table+"\r\n");
		sb.append("\t\t<where>\r\n");
		
		for (int i=0;i<ag.attrNames[no].length;i++) {
			String attr=ag.attrNames[no][i];
			String col=ag.colNames[no][i];
			sb.append("\t\t\t <if test=\""+className+"."+attr+" != null\" >or "+col+" like \"%\"#{"+className+"."+attr+",jdbcType="+ag.colTypes[no][i]+"}\"%\"</if>\r\n");
		}
		
		sb.append("\t\t</where>\r\n");
		sb.append("\t</select>\r\n");
		
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
