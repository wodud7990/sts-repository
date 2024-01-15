package kr.top2blue.boot3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.top2blue.boot3.dao.EmployeeDao;
import kr.top2blue.boot3.vo.Employee;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeDao employeeDao;

	@Transactional
	@Override
	public void updateEmployee(Employee emp) {
		employeeDao.update(emp);
	}

	@Transactional
	@Override
	public void deleteEmployee(Employee emp) {
		employeeDao.delete(emp.getEmpId());
	}

	@Transactional
	@Override
	public void insertEmployee(Employee employee) {
		employeeDao.insert(employee);
	}

	public List<Employee> getAllEmployees() {
		return employeeDao.selectAll();
	}

	@Override
	public void getEmployeeById(String empId) {
		Employee employee = employeeDao.selectByempId(empId);
		System.out.println(employee);
	}

}