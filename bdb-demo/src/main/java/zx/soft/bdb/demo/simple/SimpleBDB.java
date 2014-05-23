package zx.soft.bdb.demo.simple;

import java.io.File;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class SimpleBDB {

	public static void main(String[] args) throws Exception {

		// Open the environment, creating one if it does not exist
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true);
		Environment myDbEnvironment = new Environment(new File("/testdb"), envConfig);
		// Open the database, creating one if it does not exist
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		Database myDatabase = myDbEnvironment.openDatabase(null, "TestDatabase", dbConfig);

		// Writing records to the database
		String key = "myKey";
		String data = "myData";
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(data.getBytes("UTF-8"));
		myDatabase.put(null, theKey, theData);

		// Reading records from the database
		// Create two DatabaseEntry instances:
		// theKey is used to perform the search
		// theData will hold the value associated to the key, if found
		DatabaseEntry outData = new DatabaseEntry();

		// Call get() to query the database
		if (myDatabase.get(null, theKey, outData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			// Translate theData into a String.
			byte[] retData = outData.getData();
			String foundData = new String(retData, "UTF-8");
			System.out.println("key: '" + key + "' data: '" + foundData + "'.");
		} else {
			System.out.println("No record found with key '" + key + "'.");
		}

		// Deleting records
		myDatabase.delete(null, theKey);

		// Process all records in the database
		Cursor myCursor = null;

		try {
			myCursor = myDatabase.openCursor(null, null);

			// Cursors returns records as pairs of DatabaseEntry objects
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();

			// Retrieve records with calls to getNext() until the
			// return status is not OperationStatus.SUCCESS
			while (myCursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				String keyString = new String(foundKey.getData(), "UTF-8");
				String dataString = new String(foundData.getData(), "UTF-8");
				System.out.println("Key| Data : " + keyString + " | " + dataString + "");
			}
		} catch (DatabaseException de) {
			System.err.println("Error reading from database: " + de);
		} finally {
			try {
				if (myCursor != null) {
					myCursor.close();
				}
			} catch (DatabaseException dbe) {
				System.err.println("Error closing cursor: " + dbe.toString());
			}
		}

		if (myDatabase != null) {
			myDatabase.close();
		}
		if (myDbEnvironment != null) {
			myDbEnvironment.sync();
			myDbEnvironment.cleanLog();
			myDbEnvironment.close();
		}
	}

}
