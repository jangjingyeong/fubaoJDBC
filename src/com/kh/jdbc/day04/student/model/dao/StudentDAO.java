package com.kh.jdbc.day04.student.model.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.kh.jdbc.day04.student.common.JDBCTemplate;
import com.kh.jdbc.day04.student.model.vo.Student;

public class StudentDAO {
	/*
	 * 1. Checked Exception과 Unchecked Exception
	 * 2. 예외의 종류 Throwable - Exception(checked exception 한정)
	 * 3. 예외처리 처리 방법 : throws, try ~ catch
	 */
	private Properties prop;
	
	public StudentDAO() {
		prop = new Properties();
		Reader reader;
		try {
			reader = new FileReader("resources/query.properties");
			prop.load(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
				
	}
	
	/*
	 * 1. Statement
	 * - createStatement() 메소드를 통해서 객체 생성
	 * - execute*()를 실행할 때 쿼리문이 필요함
	 * - 쿼리문을 별도로 컴파일 하지 않아서 단순 실행일 경우 빠름
	 * - ex) 전체정보조회
	 * 
	 * 2. PreparedStatement
	 * - Statement를 상속받아서 만들어진 인터페이스
	 * - prepareStatement() 메소들를 통해서 객체 생성하는데 이때 쿼리문 필요
	 * - 쿼리문을 미리 컴파일하여 캐싱한 후 재사용하는 구조
	 * - 쿼리문을 컴파일 할때 위치홀더(?)를 이용하여 값이 들어가는 부분을 표시한 후 쿼리문 실행전에
	 * 값을 셋팅해주어야함.
	 * - 컴파일 하는 과정이 있어 느릴 수 있지만 쿼리문을 반복해서 실행할 때는 속도가 빠름
	 * - 전달값이 있는 쿼리문에 대해서 SqlInjection을 방어할 수 있는 보안기능이 추가됨
	 * - ex) 아이디로 정보조회, 이름으로 정보조회
	 * 
	 */
	
	public List<Student> selectAll(Connection conn) throws SQLException {
		Statement stmt = null;
		ResultSet rset = null;
		String query = prop.getProperty("selectAll");
		List<Student> sList = null;
		stmt = conn.createStatement();
		rset = stmt.executeQuery(query);
		sList = new ArrayList<Student>();
		// 후처리 
		while(rset.next()) {
			Student student = rsetToStudent(rset);
			sList.add(student);
		}
		rset.close();
		stmt.close();
//		try {
//			stmt = conn.createStatement();
//			rset = stmt.executeQuery(query);
//			sList = new ArrayList<Student>();
//			// 후처리 
//			while(rset.next()) {
//				Student student = rsetToStudent(rset);
//				sList.add(student);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				rset.close();
//				stmt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
		return sList;
	}
	
	
	public Student selectOneById(Connection conn, String studentId) {
			PreparedStatement pstmt = null;
			ResultSet rset = null;
			Student student = null;
			String query = prop.getProperty("selectOneById"); // 위치홀더 설정 
			try {
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, studentId);
				rset = pstmt.executeQuery();
				
				// 후처리 
				if(rset.next()) {
					student = rsetToStudent(rset);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					rset.close();
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return student;
		}


	public List<Student> selectAllByName(Connection conn, String studentName) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String query = prop.getProperty("selectAllByName");
		List<Student> sList = null;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, studentName); // 위치홀더 자리에 셋팅 
			rset = pstmt.executeQuery();
			sList = new ArrayList<Student>();
			// 후처리 
			while(rset.next()) {
				Student student = rsetToStudent(rset);
				sList.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sList;
	}


	public int insertStudent(Connection conn, Student student) {
		PreparedStatement pstmt = null;
		String query = prop.getProperty("insertStudent");
		int result = -1;
		try {
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}


	public int updateStudent(Connection conn, Student student) {
		PreparedStatement pstmt = null;
		String query = prop.getProperty("updateStudent");
		int result = -1;
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, student.getStudentPwd());
			pstmt.setString(2, student.getEmail());
			pstmt.setString(3, student.getPhone());
			pstmt.setString(4, student.getAddress());
			pstmt.setString(5, student.getHobby());
			pstmt.setString(6, student.getStudentId());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}


	public int deleteStudent(Connection conn, String studentId) {
		PreparedStatement pstmt = null;
		Student student = null;
		String query = prop.getProperty("deleteStudent");
		int result = -1;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, studentId);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}


	private Student rsetToStudent(ResultSet rset) throws SQLException {
		Student student = new Student();
		student.setStudentId(rset.getString(1)); // 컬럼명 말고 컬럼의 순서로도 할 수 있음 ★★
		student.setStudentPwd(rset.getString("STUDENT_PWD"));
		student.setStudentName(rset.getString("STUDENT_NAME"));
		//  문자는 문자열에 문자로 잘라서 사용, charAt() 메소드 사용 
		student.setGender(rset.getString("GENDER").charAt(0));
		student.setAge(rset.getInt("AGE")); // setAge가 int라서 getInt ★★
		student.setEmail(rset.getString("EMAIL"));
		student.setPhone(rset.getString("PHONE"));
		student.setAddress(rset.getString("ADDRESS"));
		student.setHobby(rset.getString("HOBBY"));
		student.setEnrollDate(rset.getDate("ENROLL_DATE")); // setEnrollDate가 Date라서 getDate ★★
		return student;
	}
	
	
	

}
