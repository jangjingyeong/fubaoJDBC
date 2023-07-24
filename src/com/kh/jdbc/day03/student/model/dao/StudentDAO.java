package com.kh.jdbc.day03.student.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.kh.jdbc.day03.student.model.vo.Student;

public class StudentDAO {
	private final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	private final String URL = "jdbc:oracle:thin:@localhost:1521:XE"; 
	private final String USER = "STUDENT";
	private final String PASSWORD = "STUDENT";

	public Student selectLoginInfo(Student student) {
		String query = "SELECT * FROM STUDENT_TBL WHERE STUDENT_ID = ? AND STUDENT_PWD = ?";
		Student result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			//입력값이 있을 때는 PreparedStatement 사용 권장 
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, student.getStudentId());
			pstmt.setString(2, student.getStudentPwd()); // 시작은 1로 하고 마지막 수는 물음표의 갯수와 같다(물음표 = 위치홀더) 
//			Statement stmt = conn.createStatement(query);
			rset = pstmt.executeQuery();
//			ResultSet rset = stmt.executeQuery(query);
			if(rset.next()) {
				result = rsetToStudent(rset);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { // close도 try-catch 해줌! 
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	


	public List<Student> selectAll() {
		String query = "SELECT * FROM STUDENT_TBL"; 
		// 쿼리문을 보고 실행할 메소드와 리턴타입을 정한다 
		List<Student> sList = new ArrayList<Student>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			// 후처리 
			while(rset.next()) {
				Student student = rsetToStudent(rset);
				sList.add(student);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return sList;
	}
	
	public Student selectOneById(String studentId) {
		// 1. 위치홀더 셋팅
		// 2. PreparedStatement 객체 생성 with query
		// 3. 입력값 셋팅
		// 4. 쿼리문 실행 및 결과 받기(feat. method()) 
		String query = "SELECT * FROM STUDENT_TBL WHERE STUDENT_ID = ?"; // ? : 위치홀더
		Student student = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
//			Statement stmt = conn.createStatement();
//			ResultSet rset = stmt.executeQuery(query);
			pstmt = conn.prepareStatement(query); // 쿼리문 필요
			pstmt.setString(1, studentId);
			rset = pstmt.executeQuery(); // 쿼리문 안필요
			if(rset.next()) {
				student = rsetToStudent(rset);
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return student;
	}
	
	

	public List<Student> selectAllByName(String studentName) {
		String query = "SELECT * FROM STUDENT_TBL WHERE STUDENT_NAME = ?";
		List<Student> sList = null;
		Student student = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, studentName);
			rset = pstmt.executeQuery();
//			Statement stmt = conn.createStatement();
//			ResultSet rset = stmt.executeQuery(query);
			sList = new ArrayList<Student>();
			while(rset.next()) {
				student = rsetToStudent(rset);
				sList.add(student);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return sList;
	}

	public int insertStudent(Student student) {
//		String query = "INSERT INTO STUDENT_TBL VALUES('"+student.getStudentId()+"', "
//				+ "'"+student.getStudentPwd()+"', "
//						+ "'"+student.getStudentName()+"', "
//								+ "'"+student.getGender()+"', "
//										+ ""+student.getAge()+", "
//												+ "'"+student.getEmail()+"', "
//														+ "'"+student.getPhone()+"', "
//																+ "'"+student.getAddress()+"', "
//																		+ "'"+student.getHobby()+"', "
//																				+ "DEFAULT)";
		String query = "INSERT INTO STUDENT_TBL VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, DEFAULT)";
		// '' 홋따옴표 안에 "" 쌍따옴표 안에 ++ 플러스 안에 가져올 값 적어주기  
		int result = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, student.getStudentId());
			pstmt.setString(2, student.getStudentPwd());
			pstmt.setString(3, student.getStudentName());
			// setChar 이런 게 없어서 String값으로 바꿔주기 위해 +"" 하거나 
			// String.valueOf(student.getGender()) 로 형변환 
			pstmt.setString(4, student.getGender()+""); 
			pstmt.setInt(5, student.getAge());
			pstmt.setString(6, student.getEmail());
			pstmt.setString(7, student.getPhone());
			pstmt.setString(8, student.getAddress());
			pstmt.setString(9, student.getHobby());
			
			result = pstmt.executeUpdate();
			
//			Statement stmt = conn.createStatement();
//			result = stmt.executeUpdate(query);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	
	
	public int updateStudent(Student student) {
//		String query = "UPDATE STUDENT_TBL SET STUDENT_PWD = '"+student.getStudentPwd()+"', "
//				+ "EMAIL = '"+student.getEmail()+"', "
//						+ "PHONE = '"+student.getPhone()+"', "
//								+ "ADDRESS = '"+student.getAddress()+"', "
//										+ "HOBBY = '"+student.getHobby()+"' "
//												+ "WHERE STUDENT_ID = '"+student.getStudentId()+"'";
		String query = "UPDATE STUDENT_TBL SET STUDENT_PWD = ?, EMAIL = ?, PHONE = ?, ADDRESS = ?, HOBBY = ? WHERE STUDENT_ID = ?";
		int result = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, student.getStudentPwd());
			pstmt.setString(2, student.getEmail());
			pstmt.setString(3, student.getPhone());
			pstmt.setString(4, student.getAddress());
			pstmt.setString(5, student.getHobby());
			pstmt.setString(6, student.getStudentId());
			result = pstmt.executeUpdate();
//			Statement stmt = conn.createStatement();
//			result = stmt.executeUpdate(query);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int deleteStudent(String studentId) {
		String query = "DELETE FROM STUDENT_TBL WHERE STUDENT_ID = ?";
		int result = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, studentId);
			result = pstmt.executeUpdate();
//			Statement stmt = conn.createStatement();
//			result = stmt.executeUpdate(query);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private Student rsetToStudent(ResultSet rset) throws SQLException {
		Student student = new Student();
		student.setStudentId(rset.getString(1)); // 컬럼명 말고 컬럼의 순서로도 할 수 있음 
		student.setStudentPwd(rset.getString("STUDENT_PWD"));
		student.setStudentName(rset.getString("STUDENT_NAME"));
		//  문자는 문자열에 문자로 잘라서 사용, charAt() 메소드 사용 
		student.setGender(rset.getString("GENDER").charAt(0));
		student.setAge(rset.getInt("AGE")); // setAge가 int라서 getInt 
		student.setEmail(rset.getString("EMAIL"));
		student.setPhone(rset.getString("PHONE"));
		student.setAddress(rset.getString("ADDRESS"));
		student.setHobby(rset.getString("HOBBY"));
		student.setEnrollDate(rset.getDate("ENROLL_DATE")); // setEnrollDate가 Date라서 getDate
		return student;
	}

	

}
