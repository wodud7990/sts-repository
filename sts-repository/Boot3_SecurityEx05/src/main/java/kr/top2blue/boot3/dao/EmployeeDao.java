package kr.top2blue.boot3.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.top2blue.boot3.vo.Employee;

@Mapper
public interface EmployeeDao {
	void insert(Employee employee);
	void update(Employee employee);
	void delete(String empId);
	List<Employee> selectAll();
	Employee selectByempId(String empId);
}