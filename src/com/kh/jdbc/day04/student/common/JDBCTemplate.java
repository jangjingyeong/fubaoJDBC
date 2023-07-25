package com.kh.jdbc.day04.student.common;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.Properties;

public class JDBCTemplate {
	private Properties prop; // java.util 
	
	// 디자인 패턴 : 각기 다른 소프트웨어 모듈이나 기능을 가진 응용 SW를 
	// 개발할 때 공통되는 설계 문제를 해결하기 위하여 사용되는 패턴임.
	// ==> 효율적인 방식을 위함!
	// 패턴의 종류 : 생성패턴, 구조패턴, 행위패턴, ... 
	// 1. 생성패턴 : 싱클톤 패턴, 추상팩토리, 팩토리 메서드, ...
	// 2. 구조패턴 : 컴포지트, 데코레이트, ...
	// 3. 행위패턴 : 옵저버, 스테이트, 전략, 템플릿 메서드, ... 

	/*
	 * public class Singletone {
	 * 		private static Singleton instance;
	 * 
	 * 		private Singletone() {}
	 * 
	 * 		public static Singleton getInstance() {
	 * 			if(instance == null) {
	 * 				instance = new Singletone();
	 * 			}
	 * 		}
	 * }
	 * 
	 */
	private static JDBCTemplate instance;
	private static Connection conn;
	
	private JDBCTemplate() {}
		// 이미 만들어져 있는지 체크하고 
		// 만들어져 있으면 그거 사용하라고 객체 만들 때 코드를 적어줘야함 
	// 생성자에서도 할 수 있지만 메소드 생성해서 할 수도 있음 
	
	// 무조건 딱 한번만 생성되고 없을 때에만 생성한다.
	// 이미 존재하면 존재하는 객체를 사용함
	public static JDBCTemplate getInstance() {
		// 이미 만들어져 있는지 체크하고 
		if(instance == null) { // JDBC객체 만들어져있니?
			// 안 만들어져 있으면 만들어서 사용 
			instance = new JDBCTemplate();// JDBC객체 생성
		} 
		// 만들어져 있으면 그거 사용 			
		return instance; // JDBC객체 
	}


	public Connection createConnection() {
		Connection conn = null;
		try {
			prop = new Properties();
			Reader reader = new FileReader("resources/dev.properties"); // 문자기반스트림
			// FileReader 쓸 때 checkedException
			prop.load(reader);
			String driverName = prop.getProperty("driverName");
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			if(conn == null || conn.isClosed()) {
				Class.forName(driverName);
				conn = DriverManager.getConnection(url, user, password);
				conn.setAutoCommit(false); // 오토커밋 풀어주세요! 
			}
			// DBCP(DataBase Connection Pool) : 자주 사용하는 연결 
			// 연결 담당객체가 한 번만 생성되어야 함 
			// 싱글톤 기법
		} catch (ClassNotFoundException | SQLException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	// 연결을 통해서 커밋을 함 
	public static void commit(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void rollback(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void close() {
		if(conn != null) {
			try {
				if(!conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
