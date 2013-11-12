package cmu.sv.sensor.test;

import static org.junit.Assert.assertEquals;
import models.SensorCategory;
import models.dao.SensorCategoryDaoImplementation;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class SensorCategoryDaoTest extends AbstractTest{
	private static SensorCategoryDaoImplementation sensorCategoryDaoImplementation;
	
	@BeforeClass
	public static void subSetup() {
		sensorCategoryDaoImplementation = new SensorCategoryDaoImplementation();
		sensorCategoryDaoImplementation.setSimpleJdbcTemplate(jdbcTemplate);
	}
	
	@Test
	public void testAddSensorCategory() {
		String purpose = "for test";
		sensorCategoryDaoImplementation.addSensorCategory("testSensorCategoryName", purpose);
		SensorCategory sc = sensorCategoryDaoImplementation.getSensorCategory("testSensorCategoryName");
		assertEquals(purpose, sc.getPurpose());
		assertEquals(2, sensorCategoryDaoImplementation.getAllSensorCategories().size());
		
		sensorCategoryDaoImplementation.addSensorCategory("testSensorCategoryName2", "for test 2");
		assertEquals(3, sensorCategoryDaoImplementation.getAllSensorCategories().size());
		
		//negative test for adding the same sensor_category_name
		sensorCategoryDaoImplementation.addSensorCategory("testSensorCategoryName", "for test 2");
		assertEquals(3, sensorCategoryDaoImplementation.getAllSensorCategories().size());
	}
	
	@Test
	@Ignore
	public void testGetSensorCategory() {
		
	}
	@Test
	@Ignore
	public void testGetAllSensorCategory() {
		
	}
}
