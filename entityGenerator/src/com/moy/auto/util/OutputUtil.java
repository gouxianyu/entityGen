package com.moy.auto.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class OutputUtil {
	
	static PrintWriter pw = null;
	/**
	 * 输出文件
	 * @param packageOutPath 完整的包名
	 * @param content 文件内容
	 * @param className 类名/文件名
	 * @param suffix 文件后缀
	 */
	public static void outputFile(String packageOutPath,String content,String className,String suffix) {
		//输出生成文件
        File directory = new File("");
        String dirName = directory.getAbsolutePath() + "/src/" + packageOutPath.replace(".", "/");
        File dir = new File(dirName);
        if (!dir.exists() && dir.mkdirs());
        String javaPath = dirName + "/" + className + "."+suffix;
        FileWriter fw = null;
		try {
			fw = new FileWriter(javaPath);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
        pw = new PrintWriter(fw);
        pw.println(content);
        pw.flush();
        System.out.println("生成"+className+"."+suffix+"成功");
        
    if (pw != null)
        pw.close();
	}
}
