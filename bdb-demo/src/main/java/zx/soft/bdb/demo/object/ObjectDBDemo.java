package zx.soft.bdb.demo.object;

import java.io.File;
import java.io.UnsupportedEncodingException;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

/**
 * 对象的读写 
 * @author wanggang
 *
 */
public class ObjectDBDemo {

	public static void main(String[] args) throws UnsupportedEncodingException {

		// Open the environment, creating one if it does not exist
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true);
		Environment myDbEnvironment = new Environment(new File("testdb"), envConfig);
		// Open the database, creating one if it does not exist
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		Database myDatabase = myDbEnvironment.openDatabase(null, "TestDatabase", dbConfig);

		//C  
		String key = "key-rensanning-Object";
		Person value = new Person(9527, "rensanning", true);

		DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("utf-8"));
		DatabaseEntry valEntry = new DatabaseEntry();
		PersonTupleBinding personBinding = new PersonTupleBinding();
		personBinding.objectToEntry(value, valEntry);

		OperationStatus status = myDatabase.put(null, keyEntry, valEntry);
		System.out.println("Put Person Status: " + status);

		//R  
		DatabaseEntry valGet = new DatabaseEntry();
		status = myDatabase.get(null, keyEntry, valGet, LockMode.DEFAULT);
		if (status == OperationStatus.SUCCESS) {
			value = personBinding.entryToObject(valGet);
			System.out.println("Read Person Value:" + value.getId() + "\t" + value.getName() + "\t" + value.isSex());
		}

		myDatabase.close();
		myDbEnvironment.sync();
		myDbEnvironment.cleanLog();
		myDbEnvironment.close();
	}

}
