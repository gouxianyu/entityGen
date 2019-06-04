package com.moy.auto.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import com.moy.auto.util.DBUtil;
import com.moy.auto.util.StringUtil;


public class AutoGenerator extends DBUtil {
	
	public ArrayList<String> tables;//表名
	public ArrayList<String> pks;//主鍵列
	
	public ArrayList<String> classNames;//类名
	public String[][] colNames;//列名
	public String[][] attrNames;//字段名
	public String[][] annotation;//注释
	public String[][] upAttrNames;//字段名首字母大写
	public String[][] colTypes;//列类型
	public String[][] javaTypes;//列所对应的Java类型
	public static AutoGenerator autoGenerator;
	
	public static synchronized AutoGenerator getAutoGenerator() {
		if(autoGenerator==null) {
			autoGenerator= new AutoGenerator();
		}
		return autoGenerator;
	}
	//单例模式
	private AutoGenerator() {
		this.tables=new ArrayList<String>();
		this.pks=new ArrayList<String>();
		this.classNames=new ArrayList<String>();
		
		Connection conn=getConn();//得到连接
		try {
            DatabaseMetaData dbMetaData = conn.getMetaData();//获取元数据
            ResultSet rs = dbMetaData.getTables(null, null, null,new String[] { "TABLE" });//获取表数据
            System.out.println("连接数据库成功...");
            while (rs.next()) {
            	String table=rs.getString("TABLE_NAME");
            	//System.out.println(table);
                tables.add(table);
                classNames.add(StringUtil.getTransStr(table, true));
                
            }
            System.out.println("加载表成功...");
            
            colNames=new String[tables.size()][];
            attrNames=new String[tables.size()][];
            upAttrNames=new String[tables.size()][];
            colTypes=new String[tables.size()][];
            javaTypes=new String[tables.size()][];
            annotation=new String[tables.size()][];
            
            for (int i=0;i<tables.size();i++) {
            	String tableName=tables.get(i);
            	
            	ResultSet pkInfo = dbMetaData.getPrimaryKeys(null, "%",tableName );
            	if (pkInfo==null) pks.add("");
            	while (pkInfo.next()){
            		pks.add(pkInfo.getString("COLUMN_NAME"));
                }
            	
                String sql = "select * from " + tableName+" limit 0,0";
                try {
                    PreparedStatement ps = conn.prepareStatement(sql);
                    rs = ps.executeQuery();
                    ResultSetMetaData meta = rs.getMetaData();
                    int columeCount = meta.getColumnCount();
                    colNames[i]=new String[columeCount];
                    colTypes[i]=new String[columeCount];
                    attrNames[i]=new String[columeCount];
                    javaTypes[i]=new String[columeCount];
                    upAttrNames[i]=new String[columeCount];
                    annotation[i]=new String[columeCount];
                    
                    ResultSet tableInfo = dbMetaData.getColumns(null,"%", tableName, "%");
                    int j=0;
                    while (tableInfo.next()){
                    	String colName=tableInfo.getString("COLUMN_NAME");
                    	String colType=tableInfo.getString("TYPE_NAME");
                    	annotation[i][j]=tableInfo.getString("REMARKS");
                        colNames[i][j]=colName;
                        attrNames[i][j]=StringUtil.getTransStr(colName, false);
                        upAttrNames[i][j]=StringUtil.getTransStr(colName, true);
                        colTypes[i][j]=colType;
                        javaTypes[i][j]=StringUtil.typeTransfer(colType);
                        j++;
                    }
                    System.out.println("加载"+tableName+"成功...");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
			close();//关闭连接
		}
	}
	/**
	 * 生成框架
	 */
	public void generateAll() {
		generateDto();
		generateEntity();
		generateDao();
		generateService();
		generateController();
	}
	
	public void generateDto() {
		DtoGenerator dtoGenerator=new DtoGenerator();
		dtoGenerator.generateDto();
	}
	
	public void generateEntity() {
		EntityGenerator entityGenerator=new EntityGenerator();
		entityGenerator.generateEntity();
	}

	public void generateEntity(String[] tables) {
		EntityGenerator entityGenerator=new EntityGenerator();
		entityGenerator.generateEntity(tables);
	}
	
	public void generateDao() {
		MapperGenerator mapperGenerator=new MapperGenerator();
		mapperGenerator.generateMapper();
		
		MappingGenerator mappingGenerator=new MappingGenerator();
		mappingGenerator.generateMapping();
	}
	
	public void generateDao(String[] tables) {
		MapperGenerator mapperGenerator=new MapperGenerator();
		mapperGenerator.generateMapper(tables);
		MappingGenerator mappingGenerator=new MappingGenerator();
		mappingGenerator.generateMapping(tables);
	}
	
	public void generateService() {
		ServiceGenerator serviceGenerator=new ServiceGenerator();
		serviceGenerator.generateMapper();
		ServiceImplGenerator serviceImplGenerator=new ServiceImplGenerator();
		serviceImplGenerator.generateMapper();
	}
	
	public void generateService(String[] tables) {
		ServiceGenerator serviceGenerator=new ServiceGenerator();
		serviceGenerator.generateMapper(tables);
		ServiceImplGenerator serviceImplGenerator=new ServiceImplGenerator();
		serviceImplGenerator.generateMapper(tables);
	}
	
	public void generateController() {
		ControllerGenerator controllerGenerator=new ControllerGenerator();
		controllerGenerator.generateController();
	}
	
	public void generateController(String[] tables) {
		ControllerGenerator controllerGenerator=new ControllerGenerator();
		controllerGenerator.generateController(tables);
	}
}
