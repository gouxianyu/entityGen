package com.moy.auto.util;

public class StringUtil {
	/**
	 * 将数据库类型转换为java类型
	 * @param sqlType 数据库类型
	 * @return
	 */
	public static String typeTransfer(String sqlType) {
		if( sqlType == null || sqlType.trim().length() == 0 ) return sqlType;
        sqlType = sqlType.toLowerCase();
        switch(sqlType){
            case "nvarchar":return "String";
            case "char":return "String";
            case "varchar":return "String";
            case "text":return "String";
            case "longtext":return "String";
            case "nchar":return "String";
            
            case "blob":return "byte[]";
            case "int":return "Integer";
            case "int unsigned":return "Long";
            case "integer":return "Integer";
            case "integer unsigned":return "Long";
            case "tinyint":return "Integer";
            case "tinyint unsigned":return "Integer";
            case "smallint":return "Integer";
            case "smallint unsigned":return "Integer";
            case "mediumint":return "Integer";
            case "mediumint unsigned":return "Integer";
            case "bit":return "Boolean";
            case "bigint":return "java.math.BigInteger";
            case "bigint unsigned":return "java.math.BigInteger";
            case "float":return "Float";
            case "double":return "Double";
            case "decimal":return "java.math.BigDecimal";
            case "boolean":return "Boolean";
            case "id":return "Long";
            case "date":return "java.util.Date";
            case "datetime":return "java.util.Date";
            case "year":return "java.util.Date";
            case "time":return "java.sql.Time";
            case "timestamp":return "java.sql.Timestamp";
            case "numeric":return "java.math.BigDecimal";
            case "real":return "java.math.BigDecimal";
            case "money":return "Double";
            case "smallmoney":return "Double";
            case "image":return "byte[]";
            default:
                System.out.println("-----------------》转化失败：未发现的类型"+sqlType);
                break;
        }
        return sqlType;
	}
	
	/**
	 * 获取字符串的驼峰命名
	 * @param str 原始字符串
	 * @param firstUpper 首字母是否转换为大写
	 * @return
	 */
	public static String getTransStr(String str, boolean firstUpper) {
       StringBuilder sb=new StringBuilder();
       String[] res=str.split("_");
       for (int i = 0; i < res.length; i++) {
    	   res[i]=res[i].toLowerCase();
    	   res[i]=toUpperCaseFirstOne(res[i]);
    	   sb.append(res[i]);
       }
       String result=(firstUpper)?sb.toString():toLowerCaseFirstOne(sb.toString());
       return result;
    }
	
	/**
	 * 首字母转换为小写
	 * @param s 原始字符串
	 * @return
	 */
	public static String toLowerCaseFirstOne(String s){
	  if(Character.isLowerCase(s.charAt(0)))
	    return s;
	  else
	    return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}


	/**
	 * 首字母转换为大写
	 * @param s 原始字符串
	 * @return
	 */
	public static String toUpperCaseFirstOne(String s){
	  if(Character.isUpperCase(s.charAt(0)))
	    return s;
	  else
	    return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}
}
