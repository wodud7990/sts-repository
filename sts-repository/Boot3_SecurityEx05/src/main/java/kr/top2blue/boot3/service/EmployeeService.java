package kr.top2blue.boot3.service;

import java.util.List;

import kr.top2blue.boot3.vo.Employee;

public interface EmployeeService {
	void insertEmployee(Employee emp);
	void updateEmployee(Employee emp);
	void deleteEmployee(Employee emp);
	List<Employee> getAllEmployees();
	void getEmployeeById(String empid);
}