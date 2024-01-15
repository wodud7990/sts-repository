package kr.top2blue.boot3.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.top2blue.boot3.dao.TestDAO;


@Service("testService")
public class TestServiceImpl implements TestService {

	@Autowired
	private TestDAO testDAO;

	@Override
	public String today() {
		String today = "";
		try {
			today = testDAO.today();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return today;
	}
}
