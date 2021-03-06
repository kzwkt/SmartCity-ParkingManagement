package data.members;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;

import data.management.DatabaseManager;
import util.Validation;

public class Driver {

	/**
	 * @author Inbal Matityahu
	 * @since 5/2/16 This class represent a single driver
	 */
	
	// The driver's id (the id of the table). Should be a unique value.
	private String id;
	
	// The driver's car id
	private String carId;
	
	// The driver's email
	private String email;

	// The driver's password. Through which the driver can login
	private String password;
	
	// last driver's parking slot 
	private String lastSlot;

	private DatabaseManager dbm;
	
	private final String objectClass = "Driver";
	
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/* Constructors */

	// Create a new driver. Will result in a new driver in the DB.
	public Driver(final String id, final String email, final String carId, final String password, final String lastSlot,DatabaseManager manager) throws ParseException {
		LOGGER.info("Create a new driver by id, email, car id, password");
		this.dbm = manager;
		dbm.initialize();
		
		validateArgument(id, email, carId, password);
		Map<String, Object> fields = new HashMap<String, Object>(), keyValues = new HashMap<String, Object>();
		fields.put("email", email);
		fields.put("carId", carId);
		fields.put("password", password);
		fields.put("lastSlot", lastSlot);
		keyValues.put("id", id);
		
		dbm.insertObject(objectClass, keyValues, fields);
	}
	
	public Driver(final ParseObject obj) throws ParseException {
		LOGGER.info("Create a new driver by Parse object");

		id = obj.getString("id");
		email = obj.getString("email");
		carId = obj.getString("carId");
		password = obj.getString("password");
		lastSlot = obj.getString("lastSlot");
		
	}
	
	public Driver(final String id , DatabaseManager manager) throws ParseException {
		LOGGER.info("Create a new driver by id");
		this.dbm = manager;
		dbm.initialize();

		Map<String, Object> keys = new HashMap<>();
		keys.put("id", id);
		Map<String,Object> returnV = dbm.getObjectFieldsByKey(objectClass, keys);
		
		this.email=returnV.get("email") + "";
		this.carId= returnV.get("carId") + "";
		this.password= returnV.get("password") + "";
		this.id=id;
		this.lastSlot=returnV.get("lastSlot") + "";
	}
	

	/* Getters */

	public String getId() {
		dbm.initialize();
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("id", id);
		return dbm.getObjectFieldsByKey(objectClass, key).get("id") + "";
	}
	
	public String getEmail() {
		dbm.initialize();
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("id", id);
		return dbm.getObjectFieldsByKey(objectClass, key).get("email") + "";
	}
	
	public String getCarId() {
		dbm.initialize();
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("id", id);
		return dbm.getObjectFieldsByKey(objectClass, key).get("carId") + "";
	}
	
	public String getPassword() {
		dbm.initialize();
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("id", id);
		return dbm.getObjectFieldsByKey(objectClass, key).get("password") + "";
	}
	
	public String getLastSlot() {
		dbm.initialize();
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("id", id);
		return dbm.getObjectFieldsByKey(objectClass, key).get("lastSlot") + "";
	}
	
	/* Setters */

	public void setId(final String newId) throws ParseException, IllegalArgumentException {
			if(!checkId(newId))
				throw new IllegalArgumentException("illegal argument!");
			Map<String, Object> newFields = new HashMap<String, Object>();
			newFields.put("id", newId);
			newFields.put("email", this.email);
			newFields.put("carId", this.carId);
			newFields.put("password", this.password);
			newFields.put("lastSlot", this.lastSlot);
			Map<String, Object> keys = new HashMap<String, Object>();
			keys.put("id", this.id);
			dbm.update(objectClass, keys, newFields);
	}

	public void setEmail(final String newEmail) throws ParseException {
		LOGGER.info("set email for driver");

		checkEmail(newEmail);
		Map<String, Object> newFields = new HashMap<String, Object>();
		newFields.put("id", this.id);
		newFields.put("email", newEmail);
		newFields.put("carId", this.carId);
		newFields.put("password", this.password);
		newFields.put("lastSlot", this.lastSlot);
		Map<String, Object> keys = new HashMap<String, Object>();
		keys.put("id", this.id);
		dbm.update(objectClass, keys, newFields);
	}
	
	public void setCarId(final String newCarId) throws ParseException {
		LOGGER.info("set car id for driver");
		
		checkCarId(newCarId);
		Map<String, Object> newFields = new HashMap<String, Object>();
		newFields.put("id", this.id);
		newFields.put("email", this.email);
		newFields.put("carId", newCarId);
		newFields.put("password", this.password);
		newFields.put("lastSlot", this.lastSlot);
		Map<String, Object> keys = new HashMap<String, Object>();
		keys.put("id", this.id);
		dbm.update(objectClass, keys, newFields);
	}
	
	public void setPassword(final String newPassword) throws ParseException {
		LOGGER.info("set password for driver");
		
		checkPassword(newPassword);
		Map<String, Object> newFields = new HashMap<String, Object>();
		newFields.put("id", this.id);
		newFields.put("email", this.email);
		newFields.put("carId", this.carId);
		newFields.put("password", newPassword);
		newFields.put("lastSlot", this.lastSlot);
		Map<String, Object> keys = new HashMap<String, Object>();
		keys.put("id", this.id);
		dbm.update(objectClass, keys, newFields);
	}
	
	public void setLastSlot(final String newSlot) throws ParseException {
		LOGGER.info("set last slot for driver");
		
		Map<String, Object> newFields = new HashMap<String, Object>();
		newFields.put("id", this.id);
		newFields.put("email", this.email);
		newFields.put("carId", this.carId);
		newFields.put("password", this.password);
		newFields.put("lastSlot", newSlot);
		Map<String, Object> keys = new HashMap<String, Object>();
		keys.put("id", this.id);
		dbm.update(objectClass, keys, newFields);
	}
	
	/* Methods */

	private void validateArgument(final String id, final String email, final String carId, final String password)
		throws IllegalArgumentException, ParseException {
		if (Validation.validateNewDriver(id, email, carId, password))
			return;
		LOGGER.severe("illegal arguments!");
		throw new IllegalArgumentException("illegal arguments!");
	}
	
	private boolean checkId(final String newId) throws ParseException, IllegalArgumentException {
		return newId != null && (!Validation.isIdExist(newId,this.dbm) || newId.equals(this.id));
	}
	
	private void checkEmail(final String email) throws IllegalArgumentException {
		if (email==null){
			LOGGER.severe("email can not be empty!");
			throw new IllegalArgumentException("email can not be empty!");
		}
		if (Validation.validateMail(email))
			return;
		LOGGER.severe("email is illegal!");
		throw new IllegalArgumentException("email is illegal!");		
	}
	
	private void checkCarId(final String carId) throws IllegalArgumentException {
		if (Validation.validateCarId(carId))
			return;
		LOGGER.severe("carId is illegal! Must contain 7 characters!");
		throw new IllegalArgumentException("carId is illegal! Must contain 7 characters!");
	}
	
	private void checkPassword(final String password) throws IllegalArgumentException {
		if (Validation.validatePassword(password, 1, 10))
			return;
		LOGGER.severe("password is illegal! Must contain between 1-10 characters!");
		throw new IllegalArgumentException("password is illegal! Must contain between 1-10 characters!");			
	}
	
	public void removeDriverFromDB() throws ParseException {
		LOGGER.info("remove driver from DB");
		dbm.initialize();
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("email", this.email);
		fields.put("carId", this.carId);
		fields.put("password", this.password);
		fields.put("lastSlot", this.lastSlot);
		fields.put("id", this.id);
		dbm.deleteObject(objectClass, fields);
	}
}
