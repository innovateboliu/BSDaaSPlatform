package controllers;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.User;
import models.dao.UserDao;

import org.codehaus.jackson.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import play.mvc.Controller;
import play.mvc.Result;
//import models.cmu.sv.sensor.SensorReading;

import com.google.gson.Gson;

public class UserController extends Controller {
		
	private static ApplicationContext context;
	private static UserDao UserDao;
	
	private static void checkDao(){
		if (context == null) {
			context = new ClassPathXmlApplicationContext("application-context.xml");
		}
		if (UserDao == null) {
			UserDao = (UserDao) context.getBean("userDaoImplementation");
		}
	}

	public static Result addUser() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		checkDao();

		Gson gson = new Gson();
		User user = gson.fromJson(request().body().asJson().toString(), User.class);

		
		ArrayList<String> error = new ArrayList<String>();
		
		boolean result = UserDao.addUser(user.getUserName(), user.getUserProfile());

		if(!result){
			error.add(user.getUserName());
		}
		// Can this error have more than one name in it? I don't understand why error needs to be a list.
		if(error.size() == 0){
			System.out.println("user saved");
			return ok("user saved");
		}
		else{
			System.out.println("user not saved: " + error.toString());
			return ok("user not saved: " + error.toString());
		}
	}
	
	public static Result getUser(String userName, String format) {
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		User user = UserDao.getUser(userName);
		if(user == null){
			return notFound("no devices found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(user);		
		} else {			
				ret = toCsv(Arrays.asList(user));
		}
		return ok(ret);
	}
	
	private static String toCsv(List<User> users) {
		StringWriter sw = new StringWriter();
		CellProcessor[] processors = new CellProcessor[] {
				new Optional(),
				new Optional(),
				};
		ICsvBeanWriter writer = new CsvBeanWriter(sw, CsvPreference.STANDARD_PREFERENCE);
		try {
			final String[] header = new String[] { "userName",  "userProfile"};
			writer.writeHeader(header);
			for (User user : users) {
				writer.write(user, header, processors);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sw.getBuffer().toString();
	}


}