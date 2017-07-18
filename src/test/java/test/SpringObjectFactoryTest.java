package test;

import javax.sql.DataSource;

import org.junit.Test;

import sample.SpringObjectFactory;

public class SpringObjectFactoryTest {

	@Test
	public void testGetDataSource() {
		SpringObjectFactory<DataSource> springObjectFactory = new SpringObjectFactory<DataSource>();

		springObjectFactory.setBeanName("dataSource");

		DataSource instance = springObjectFactory.getInstance();
		System.out.println("instance:" + instance);
	}
}
