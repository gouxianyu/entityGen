package com.moy.auto.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DBUtil {
	private String DRIVER=Config.DRIVER;
	private String URL=Config.URL;
	private String USER=Config.USER;
	private String PASSWORD=Config.PASSWORD;
    
	private Connection conn=null;
    
    public Connection getConn() {
    	
		try {
			Class.forName(DRIVER);
			conn=DriverManager.getConnection(URL,USER,PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
    
    public void close() {
		try {
			if (!conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
