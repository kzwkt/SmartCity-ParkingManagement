package logic;

import java.util.List;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import data.management.DBManager;
import data.members.StickersColor;
import data.members.User;
import Exceptions.LoginException;

/**
 * @Author DavidCohen55
 */

public class Login {
	private User user;

	public Login() {
		DBManager.initialize();
	}

	/***
	 * userLogin uses the car number and password of the user.
	 * 
	 * @param carNumber
	 * @param password
	 * @return true if it found a match in the DB, else false.
	 * @throws LoginException
	 *             is thrown if there is a problem
	 */
	public boolean userLogin(String carNumber, String password) throws LoginException {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("PMUser");
		query.whereEqualTo("carNumber", carNumber);
		try {
			List<ParseObject> carList = query.find();
			return carList != null && !carList.isEmpty() && password.equals(carList.get(0).getString("password"));
		} catch (ParseException e) {
			throw new LoginException("something went wrong");
		}
	}

	public String UserValueCheck(String name, String phone, String email, String car) {
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("PMUser");
		query.whereEqualTo("carNumber", car);
		try {
			int count = 0;
			if (query.find() != null) {
				for (@SuppressWarnings("unused")
				ParseObject ¢ : query.find())
					++count;
				if (count > 0)
					return "already exist";
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return name.matches(".*\\d.*") ? "user has integer"
				: phone.length() != 10 ? "phone need to be in size 10"
						: !phone.startsWith("05") ? "phone should start with 05"
								: phone.matches(".*[a-zA-z].*") ? "phone contains only integers"
										: (!email.matches(
												"[\\d\\w\\.]+@(campus.technion.ac.il|gmail.com|walla.com|hotmail.com|t2.technion.ac.il)"))
														? "invalid email address"
														: car.length() == 7 ? "Good Params"
																: "car need to be in size 7";
	}

	/***
	 * 
	 * @param name
	 *            shouldn't have integer in the name
	 * @param pass
	 * @param phone
	 *            need to be in size of 10, start with 05 and contain only
	 *            integers
	 * @param car
	 *            need to be in size of 7
	 * @param email
	 *            should be a valid email address
	 * @param type
	 *            should be one of the enum types
	 * 
	 * @return the id of the user in the DB
	 * @throws LoginException
	 *             is thrown if there is a problem with the user value according
	 *             to the UserValueCheck function
	 */
	public String userSignUp(String name, String pass, String phone, String car, String email, StickersColor type)
			throws LoginException {
		user = null;
		String $ = UserValueCheck(name, phone, email, car);
		if (!"Good Params".equals($))
			throw new LoginException($);
		try {
			user = new User(name, pass, phone, car, email, type, null);
			$ = user.getTableID();
		} catch (ParseException e) {
			$ = "";
		}
		return $;
	}

	/***
	 * Update the user that has the carNumber and update his row for the
	 * following values if they are correct
	 * 
	 * @param carNumber
	 * @param name
	 *            new name of the user
	 * @param phoneNumber
	 *            new phone number
	 * @param email
	 *            new email address
	 * @param newCar
	 *            new car number(in case of a switch) shouldn't be in the system
	 * @return true if everything is correct
	 * @throws LoginException
	 *             is thrown if there is a problem with the user value according
	 *             to the UserValueCheck function
	 */
	public boolean userUpdate(String carNumber, String name, String phoneNumber, String email, String newCar)
			throws LoginException {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("PMUser");
		query.whereEqualTo("carNumber", carNumber);
		try {

			List<ParseObject> userList = query.find();
			if (userList != null && !userList.isEmpty()) {
				String s = UserValueCheck(name, phoneNumber, email, newCar);
				if (!"Good Params".equals(s))
					throw new LoginException(s);
				userList.get(0).put("username", name);
				userList.get(0).put("phoneNumber", phoneNumber);
				userList.get(0).put("email", email);
				userList.get(0).put("carNumber", newCar);
				userList.get(0).save();
			}

		} catch (ParseException e) {
			throw new LoginException("something went wrong looking for carNumber: " + carNumber);
		}
		return true;
	}

	public void deleteUser() throws ParseException {
		user.DeleteUser();
	}
}
