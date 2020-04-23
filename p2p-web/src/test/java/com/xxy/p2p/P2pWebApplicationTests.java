package com.xxy.p2p;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
@RunWith(SpringRunner.class)
class P2pWebApplicationTests {

	@Test
	void contextLoads() {
	}


		@Autowired
		private DataSource dataSource;

		@Test
		public void test() throws SQLException {
			Connection data = dataSource.getConnection();

			System.out.println("------" + data.getClass());

			System.out.println("------" + dataSource.getClass());

			data.close();
		}


}
