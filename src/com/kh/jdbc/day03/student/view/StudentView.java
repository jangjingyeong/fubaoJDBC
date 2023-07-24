package com.kh.jdbc.day03.student.view;

import java.util.List;
import java.util.Scanner;

import com.kh.jdbc.day03.student.controller.StudentController;
import com.kh.jdbc.day03.student.model.vo.Student;

public class StudentView {
	private StudentController controller;
	
	public StudentView() {
		controller = new StudentController();
	}
	
	public void startProgram() {
		List<Student> sList = null;
		Student student = null;
		end : 
		while(true) {
			int input = printMenu();
			switch(input) {
			case 1 : // 학생 전체 조회
				sList = controller.selectAllStudent();
				printAllStudents(sList);
				break;
			case 2 : // 학생 아이디로 조회
				String studentId = inputStudentId("검색");
				student = controller.selectOneById(studentId);
				printStudent(student);
				break;
			case 3 : // 학생 이름으로 조회
				String studentName = inputStudentName();
				sList = controller.selectAllByName(studentName);
				printAllStudents(sList);
				break;
			case 4 : // 학생 정보 등록
				student = inputStudent();
				int result = controller.insertStudent(student);
				if(result > 0) {
					displaySuccess("학생 정보 등록 성공");
				} else {
					displayError("학생 정보 등록 실패");
				}
				break;
			case 5 : // 학생 정보 수정 
				// 수정할 데이터가 있는지 먼저 확인 
				// SELECT * FROM STUDENT_TBL WHERE STUDENT_ID = 'khuser01' 
				studentId = inputStudentId("수정");
				// 있으면 나오고 없으면 안나오는 것을 결과값으로 체크 
				student = controller.selectOneById(studentId);
				if(student != null) {
					// 있는 거
					student = modifyStudent();
					student.setStudentId(studentId);
					result = controller.updateStudent(student);
					if(result > 0) {
						displaySuccess("학생 정보 수정 성공");
					} else {
						displayError("학생 정보 수정 실패");
					}
				} else {
					// 없는 거 
					displayError("해당 아이디는 존재하지 않습니다.");
				}
				break;
			case 6 : // 학생 정보 삭제
				studentId = inputStudentId("삭제");
				result = controller.deleteStudent(studentId);
				if(result > 0) {
					displaySuccess("학생 정보 삭제 성공");
				} else {
					displayError("학생 정보 삭제 실패");
				}
				break;
			case 9 : // 학생 로그인 
				// SELECT * FROM STUDENT_TBL WHERE STUDENT_ID = 'khuser01' AND STUDENT_PWD = '1234'
				student = inputLoginInfo();
				student = controller.studentLogin(student);
				if(student != null) {
					// 로그인 성공 
					displaySuccess("로그인 성공");
				} else {
					//로그인 실패 
					displayError("해당 정보가 존재하지 않습니다. ");
				}
				break;
			case 0 : // 프로그램 종료 
				break end;
			}
		}
	}
	
	
	private Student inputLoginInfo() {
		Scanner sc = new Scanner(System.in);
		System.out.println("===== 학생 로그인 =====");
		System.out.print("아이디 : ");
		String studentId = sc.next();
		System.out.print("비밀번호 : ");
		String studentPwd = sc.next();
		Student student = new Student(studentId, studentPwd);
//		System.out.println(student.toString());
		return student;
	}

	private Student modifyStudent() {
		Scanner sc = new Scanner(System.in);
		System.out.println("===== 학생 정보 수정 =====");
		System.out.print("비밀번호 : ");
		String studentPwd = sc.next();
		System.out.print("이메일 : ");
		String email = sc.next();
		System.out.print("전화번호 : ");
		String phone = sc.next();
		System.out.print("주소 : ");
		sc.nextLine(); // 전화번호 쓰고 친 엔터가 지워지게 공백제거, 개행제거 
		String address = sc.nextLine();
		System.out.print("취미(,로 구분) : ");
		String hobby = sc.next();
		Student student = new Student(studentPwd, email, phone, address, hobby);
		return student;
	}

	private int printMenu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("===== 학생 관리 프로그램 =====");
		System.out.println("9. 학생 로그인");
		System.out.println("1. 학생 전체 조회");
		System.out.println("2. 학생 아이디로 조회");
		System.out.println("3. 학생 이름으로 조회");
		System.out.println("4. 학생 정보 등록");
		System.out.println("5. 학생 정보 수정");
		System.out.println("6. 학생 정보 삭제");
		System.out.println("0. 프로그램 종료");
		System.out.print("메뉴 선택 : ");
		int input = sc.nextInt();
		return input;
	}

	private Student inputStudent() {
		Scanner sc = new Scanner(System.in);
		System.out.print("아이디 : ");
		String studentId = sc.next();
		System.out.print("비밀번호 : ");
		String studentPwd = sc.next();
		System.out.print("이름 : ");
		String studentName = sc.next();
		System.out.print("성별 : ");
		char gender = sc.next().charAt(0);
		System.out.print("나이 : ");
		int age = sc.nextInt();
		System.out.print("이메일 : ");
		String email = sc.next();
		System.out.print("전화번호 : ");
		String phone = sc.next();
		System.out.print("주소 : ");
		sc.nextLine(); // 전화번호 쓰고 친 엔터가 지워지게 공백제거, 개행제거 
		String address = sc.nextLine();
		System.out.print("취미(,로 구분) : ");
		String hobby = sc.next();
		// 생성자 안 썼으면 하나하나 다 써줘야함 
//		Student student = new Student();
//		student.setStudentId(StudentId);
//		student.setStudentPw(StudentPw);
		Student student = new Student(studentId, studentPwd, studentName, gender, age
				, email, phone, address, hobby);
		return student;
	}

	private String inputStudentId(String category) {
		Scanner sc = new Scanner(System.in);
		System.out.print(category + "할 아이디 입력 : ");
		String studentId = sc.next();
		return studentId;
	}

	private String inputStudentName() {
		Scanner sc = new Scanner(System.in);
		System.out.print("검색할 이름 입력 : ");
		String studentName = sc.next();
		return studentName;
	}

	private void printStudent(Student student) {
		System.out.println("===== 학생 정보 출력(아이디로 조회) =====");
		System.out.printf("이름 : %s, 나이 : %d, 아이디 : %s, 성별 : %s"
				+ ", 이메일 : %s, 전화번호 : %s, 주소 : %s, 취미 : %s"
				+ ", 가입날짜 : %s\n"
				, student.getStudentName()
				, student.getAge()
				, student.getStudentId()
				, student.getGender()
				, student.getEmail()
				, student.getPhone()
				, student.getAddress()
				, student.getHobby()
				, student.getEnrollDate());			
	}

	private void printAllStudents(List<Student> sList) {
		System.out.println("===== 학생 전체 조회 =====");
		for(Student student : sList) { // foreach 쓰면 인덱스 신경쓰지 않고 출력 가능 
			System.out.printf("이름 : %s, 나이 : %d, 아이디 : %s, 성별 : %s"
					+ ", 이메일 : %s, 전화번호 : %s, 주소 : %s, 취미 : %s"
					+ ", 가입날짜 : %s\n"
					, student.getStudentName()
					, student.getAge()
					, student.getStudentId()
					, student.getGender()
					, student.getEmail()
					, student.getPhone()
					, student.getAddress()
					, student.getHobby()
					, student.getEnrollDate());
			
		}
	}

	private void displaySuccess(String message) {
		System.out.println("[서비스 성공] : " + message);
	}

	private void displayError(String message) {
		System.out.println("[서비스 실패] : " + message);
	}
}
