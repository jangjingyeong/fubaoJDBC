package com.kh.jdbc.day01.basic;

import java.sql.*; // sql에 있는 거 다 쓰겠다고 1번만 import

public class JDBCRun {
	
	public static void main(String[] args) {
		/*
		 *  JDBC 코딩 절차
		 *  1. 드라이버 등록 (ojdbc6.jar파일이랑 관련) 
		 *  2. DBMS 연결 생성
		 *  3. Statement 객체 생성(쿼리문 실행 준비)
		 *   - new Statement();가 아니라 연결을 통해 객체 생성함. 
		 *  4. SQL 전송(쿼리문 실행)
		 *  5. 결과 받기(ResultSet으로 바로 받아버림)
		 *  6. 자원해제(close())
		 */
		
		try {
			String url = "jdbc:oracle:thin:@localhost:1521:xe"; // @ip:port:dbname
			String user = "KH";
			String password = "KH";
			String query = "SELECT EMP_NAME, SALARY FROM EMPLOYEE";
			// 1. 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver"); // 이 코드가 있음으로써 jar파일을 쓸 수 있게 되는 것임 
			// 2. DBMS 연결 생성 
			Connection conn = 
					DriverManager.getConnection(url , user, password); // 이 연결도 checkedException
			// 3. 쿼리문 실행 준비(Statement 객체 생성) : 연결을 통해 객체 생성 
			Statement stmt = conn.createStatement();
			// 4. 쿼리문 실행(SELECT면 ResultSet), 5. 결과값 받기(resultset은 테이블형태) 
			ResultSet rset = stmt.executeQuery(query);
			// 후처리 필요 - 디비에서 가져온 데이터를 사용하기 위함.
			while(rset.next()) {
				System.out.printf("직원명 : %s, 급여 : %s\n"
						, rset.getString("EMP_NAME"), rset.getInt(2));
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
