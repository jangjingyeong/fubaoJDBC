package com.kh.jdbc.day02.student.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.kh.jdbc.day02.student.model.vo.Student;

public class StudentDAO { // dao는 오라클에 있는 전체 정보를 가져와 객체화하는 것 
	// 이건 계속 쓰일 거라 전역변수로 옮겨줌 
//	String driverName = "oracle.jdbc.driver.OracleDriver";
//	String url = "jdbc:oracle:thin:@localhost:1521:XE"; //xe 대소문자 상관없음 
//	String user = "STUDENT";
//	String password = "STUDENT";
	// private final 잘 안바뀌니까 상수로 바꿔줌, 상수는 대문자로 써야해서 바꿔줌 
	private final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	private final String URL = "jdbc:oracle:thin:@localhost:1521:XE"; 
	private final String USER = "STUDENT";
	private final String PASSWORD = "STUDENT";
	
	// JDBC 코딩이 그대로 들어감 
	public List<Student> selectAll() {
//	public static void main(String[] args) {       // 동작되는지 확인 먼저 
		/*
		 * 1. 드라이버 등록
		 * 2. DB 연결 생성
		 * 3. 쿼리문 실행 준비
		 * 4. 쿼리문 실행, 5. 결과 받기
		 * 6. 자원해제(close()) 
		 */
		String query = "SELECT * FROM STUDENT_TBL"; // SELECT - ResultSet 
		List<Student> sList = null;
		Student student = null;
		try {
			// 1. 드라이버 등록
			Class.forName(DRIVER_NAME); 
			// 2. DB 연결 생성(DriverManager)
			Connection conn =
					DriverManager.getConnection(URL, USER, PASSWORD);
			// 3. 쿼리문 실행 준비 
			Statement stmt = 
					conn.createStatement();
			// 4. 쿼리문 실행, 5. 결과 받기
			ResultSet rset = 
					stmt.executeQuery(query);
			sList = new ArrayList<Student>();
			// 후처리
			while(rset.next()) {
				// rset에서 가져온 것들을 Student 객체에 담아서 사용할 수 있도록 해줌  
				student = rsetToStudent(rset);
				sList.add(student); // sList에 Student 정보들 저장 
				
//				System.out.printf("아이디 : %s, 이름 : %s"   // 확인용 
//						, rset.getString("STUDENT_ID")
//						, rset.getString("STUDENT_NAME"));
			}
			// 6. 자원해제(close())
			rset.close();
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return sList;
	}

	public List<Student> selectAllByName(String studentName) {
		String query = "SELECT * FROM STUDENT_TBL WHERE STUDENT_NAME = '"+studentName+"'";
		List<Student> sList = null;
		Student student = null;
		try {
			Class.forName(DRIVER_NAME);
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			sList = new ArrayList<Student>();
			// 후처리
			while(rset.next()) {
				// rset에서 가져온 것들을 Student 객체에 담아서 사용할 수 있도록 해줌  
				student = rsetToStudent(rset);
				sList.add(student); // sList에 Student 정보들 저장 
			}
			
			rset.close();
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return sList;
	}

	public Student selectOneById(String studentId) {
					 // SELECT* FROM STUDENT_TBL WHERE STUDENT_ID = 'khuser01'
		String query = "SELECT * FROM STUDENT_TBL WHERE STUDENT_ID = '"+studentId+"'";
		Student student = null;
		try {
			Class.forName(DRIVER_NAME);
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			// 후처리
			// while(rset.next()) {} 결과값 여러개일 때는 while문 ★★
			if(rset.next()) { // 결과값 한개라서 if문 씀 ★★
				student = rsetToStudent(rset);
			}
			
			rset.close();
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException | SQLException e) {
		}
		return student;
	}

	public int insertStudent(Student student) {
			/* 
			 *  1. 드라이버 등록 - Referenced Libraries에 ojdbc6.jar 확인 
			 *  2. DB 연결 생성
			 *  3. 쿼리문 실행 준비
			 *  4. 쿼리문 실행, 5. 결과 받기
			 *  6. 자원해제 
			 */
			// INSERT INTO STUDENT_TBL VALUES('khuser01', 'pass01', '일용자', 'M', 11, 'khuser01@kh.com', 
			// '01022222222', '서울시 중구 남대문로 120', '독서, 수영', DEFAULT); 
			// ★★ 변수로 쓰기 위해서 '"+student.getStudentId()+"' 홋따옴표 안에 쌍따옴표 안에 +로 
			String query = "INSERT INTO STUDENT_TBL VALUES('"+student.getStudentId()+"', "
					+ "'"+student.getStudentPwd()+"', "
							+ "'"+student.getStudentName()+"', "
									+ "'"+student.getGender()+"', "
											+ ""+student.getAge()+", "
													+ "'"+student.getEmail()+"', "
															+ "'"+student.getPhone()+"', "
																	+ "'"+student.getAddress()+"', "
																			+ "'"+student.getHobby()+"', "
																					+ "DEFAULT)";
			int result = -1; // 동작을 안 하면 -1이 되도록 해줌
			try {
				// 1. 드라이버 등록 (try-catch)문 생성
				Class.forName(DRIVER_NAME); // 드라이버명 중요! 
				// 2. DB 연결 생성
				Connection conn = 
				DriverManager.getConnection(URL, USER, PASSWORD); // Add try-catch
				// 3. 쿼리문 실행 준비
				Statement stmt = conn.createStatement();
				// 4. 쿼리문 실행, 5. 결과 받기 ★★
	//			stmt.executeQuery(query); // SELECT용 ★★ 
				result = stmt.executeUpdate(query); // DML(INSERT, UPDATR, DELETE)용 ★★
				// 6. 자원해제 
				stmt.close();
				conn.close();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			} 
			return result;
		}

	public int updateStudent(Student student) {
		String query = "UPDATE STUDENT_TBL SET "
				+ "STUDENT_PWD = '"+student.getStudentPwd()+"', "
						+ "EMAIL = '"+student.getEmail()+"', "
								+ "PHONE = '"+student.getPhone()+"', "
										+ "ADDRESS = '"+student.getAddress()+"', "
												+ "HOBBY = '"+student.getHobby()+"' "
														+ "WHERE STUDENT_ID = '"+student.getStudentId()+"'";
		int result = -1;
		try {
			Class.forName(DRIVER_NAME);
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt = conn.createStatement();
			result = stmt.executeUpdate(query);
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int deleteStudent(String studentId) {
		String query = "DELETE FROM STUDENT_TBL WHERE STUDENT_ID = '"+studentId+"'";
		int result = -1;
		try {
			Class.forName(DRIVER_NAME);
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt = conn.createStatement();
			result = stmt.executeUpdate(query);
			
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Student rsetToStudent(ResultSet rset) throws SQLException {
		Student student = new Student();
		student.setStudentId(rset.getString("STUDENT_ID"));
		student.setStudentPwd(rset.getString("STUDENT_PWD"));
		student.setStudentName(rset.getString("STUDENT_NAME"));
		//  문자는 문자열에 문자로 잘라서 사용, charAt() 메소드 사용 
		student.setGender(rset.getString("GENDER").charAt(0));
		student.setAge(rset.getInt("AGE"));
		student.setEmail(rset.getString("EMAIL"));
		student.setPhone(rset.getString("PHONE"));
		student.setAddress(rset.getString("ADDRESS"));
		student.setHobby(rset.getString("HOBBY"));
		student.setEnrollDate(rset.getDate("ENROLL_DATE"));
		return student;
	}
	
}
