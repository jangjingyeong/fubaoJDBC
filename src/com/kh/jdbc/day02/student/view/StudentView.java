package com.kh.jdbc.day02.student.view;

import java.util.*;

import com.kh.jdbc.day02.student.controller.StudentController;
import com.kh.jdbc.day02.student.model.vo.Student;


public class StudentView {
	
	private StudentController controller;
	
	public StudentView() {
		controller = new StudentController();
	}
	
	public void startProgram() {
		Student student = null;
		List<Student> sList;
		end :
		while(true) {
			int choice = printMenu();
			switch(choice) {
			case 1: // 학생 전체 조회
				// SELECT * FROM STUDENT_TBL
				sList = controller.printStudentList();
				if(!sList.isEmpty()) { // 해당 리스트의 값이 비어있는지 체크해주는 것  (null이 아니어야함. nullpointException 대신)
					showAllStudents(sList);
				} else {
					displayError("학생 정보가 조회되지 않습니다.");
				}
				break;
			case 2: // 학생 아이디로 조회
				// 아이디로 조회하는 쿼리문 생각해보기 (리턴형은 무엇으로?, 매개변수는 무엇으로?)
				String studentId = inputStudentId();
				// printStudentById() 메소드가 학생 정보를 조회, dao의 메소드는 selectOneById()로 명명
				student = controller.printStudentById(studentId);
				if(student != null) { // (null이 아니어야함. nullpointException 대신)
					// showStudent() 메소드로 학생 정보를 출력 
					showStudent(student);
				} else {
					displayError("학생 정보가 존재하지 않습니다.");
				}
				break;
			case 3: // 학생 이름으로 조회
				// 이름으로 조회하는 쿼리문 생각해보기(리턴형은 무엇? 매개변수 유뮤) 
				String studentName = inputStudentName();
				// printStudentByName, printStudentsByName (?) 
				// selectOneByName, selectAllByName (?)
				sList = controller.printStudentsByName(studentName);
				// showStudent, showAllStudents (?) 
				if(!sList.isEmpty()) {
					showAllStudents(sList);
				} else {
					displayError("학생 정보가 존재하지 않습니다.");
				}
				break;
			case 4: // 학생 정보 등록
				// INSERT INTO STUDENT_TBL VALUES('khuser01', 'pass01', '일용자', 'M', 11, 'khuser01@kh.com', 
				// '01022222222', '서울시 중구 남대문로 120', '독서, 수영', DEFAULT);
				student = inputStudent();
				int result = controller.insertStudent(student); // 후처리가 따로 없다 
				if(result > 0) {
					// 성공 메시지 출력
					displaySuccess("학생 정보 등록 성공");
				} else {
					// if((result == 0) 이면 변화 없음 그런 경우는 없음 
					// 실패 메시지 출력 
					displayError("학생 정보 등록 실패");
				}
				break;
			case 5: // 학생 정보 수정
				// 쿼리문 생각해보기 
				// UPDATE STUDENT_TBL SET STUDENT_PWD = 'pass11', EMAIL = 'khuser01@iei.or.kr', PHONE = '01025869654'
				// ,ADDRESS = '서울시 강남구', HOBBY = '코딩,수영' WHERE STUDENT_ID = 'khuser01';
				student = modifyStudent(); // 어떻게 수정할지 받음 
				result = controller.modifyStudent(student);
				if(result > 0) {
					// 성공 메시지 출력
					displaySuccess("학생 정보 수정 성공");
				} else {
					// 실패 메시지 출력
					displayError("학생 정보 수정 실패");
				}
				break;
			case 6: // 학생 정보 삭제
				// 쿼리문 생각해보기 (매개변수 필요 유무, 반환형?)
				// DELETE FROM STUDENT_TBL WHERE STUDENT_ID = 'khuser04'; // 1행이 삭제되었습니다. -> 반환형 int
				studentId = inputStudentId();
				result = controller.deleteStudent(studentId);
				if(result > 0) {
					// 성공 메시지 출력
					displaySuccess("학생 정보 삭제 성공");
				} else {
					// 실패 메시지 출력
					displayError("학생 정보 삭제 실패");
				}
				break;
			case 0:
				break end;
			}
		}

	}
	public int printMenu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("===== 학생 관리 프로그램 =====");
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

	private String inputStudentId() {
		Scanner sc = new Scanner(System.in);
		System.out.println("===== 학생 아이디로 조회 =====");
		System.out.print("학생 아이디 : ");
		String studentId = sc.next();
		return studentId;
	}

	private String inputStudentName() {
		Scanner sc = new Scanner(System.in);
		System.out.println("===== 학생 이름으로 조회 =====");
		System.out.print("학생 이름 : ");
		String studentName = sc.next();
		return studentName;
	}

	private Student inputStudent() {
			Scanner sc = new Scanner(System.in);
			System.out.print("아이디 : ");
			String studentId = sc.next();
			System.out.print("비밀번호 : ");
			String studentPw = sc.next();
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
			Student student = new Student(studentId, studentPw, studentName, gender, age
					, email, phone, address, hobby);
			return student;
		}

	private Student modifyStudent() {
		Scanner sc = new Scanner(System.in);
		System.out.println("===== 학생 정보 수정 =====");
		System.out.print("아이디 : ");
		String studentId = sc.next();
		System.out.print("비밀번호 : ");
		String studentPw = sc.next();
		System.out.print("이메일 : ");
		String email = sc.next();
		System.out.print("전화번호 : ");
		String phone = sc.next();
		System.out.print("주소 : ");
		sc.nextLine(); // 전화번호 쓰고 친 엔터가 지워지게 공백제거, 개행제거 
		String address = sc.nextLine();
		System.out.print("취미(,로 구분) : ");
		String hobby = sc.next();
		Student student = new Student(studentId, studentPw, email, phone, address, hobby);
		return student;
	}

	private void displaySuccess(String message) {
		System.out.println("[서비스 성공] : " + message);		
	}

	private void displayError(String message) {
		System.out.println("[서비스 실패] : " + message);
	}

	private void showAllStudents(List<Student> sList) {
			System.out.println("===== 학생 전체 정보 출력 =====");
			for(Student student : sList) {
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

		private void showStudent(Student student) {
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
}