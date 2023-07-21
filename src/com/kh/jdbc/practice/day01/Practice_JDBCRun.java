package com.kh.jdbc.practice.day01;

import java.sql.*;

public class Practice_JDBCRun {
	public static void main(String[] args) {
		/*
		 *  JDBC 코딩 절차
		 *  1. 드라이버 등록(ojdbc6.jar파일 관련) 
		 *  2. DBMS 연결 생성 
		 *  3. Statement 객체 생성(쿼리문 실행 준비) 
		 *   - new Statement();가 아닌 연결을 통한 객체 생성 
		 *  4.SQL 전송(쿼리문 실행)
		 *  5. 결과 받기(ResultSet으로 바로 받아버림)
		 *  6. 자원해제(close())
		 */
		
		// 1. 드라이버 등록 Class.forName(""); 작성 -> try-catch문 생성 
		try {
			// 사용할 변수들 먼저 선언, 초기화
			String driverName = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String user = "KH";
			String password = "KH";
			String query = "SELECT * FROM EMPLOYEE";
			// 1. 드라이버 등록 
			Class.forName(driverName);
			// 2. DBMS 연결 생성 
			Connection conn = 
					DriverManager.getConnection(url, user, password);
			// DriverManager 작성 후 add catch 
			// 3. 쿼리문 실행 준비(Statement 객체 생성) : 연결을 통해 객체 생성
			Statement stmt = conn.createStatement();
			// 4. 쿼리문 실행(SELECT면 ResultSet), 5. 결과값 받기(ResultSet은 테이블 형태)
			ResultSet rset = stmt.executeQuery(query);
			// 후처리 필요 - DB에서 가져온 데이터를 사용하기 위함.
			while(rset.next()) {
				System.out.printf("직원명 : %s, 급여 : %s\n"
						, rset.getString("EMP_NAME")
						, rset.getInt("SALARY"));
			}
			// 6. 자원해제 
			rset.close();
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
