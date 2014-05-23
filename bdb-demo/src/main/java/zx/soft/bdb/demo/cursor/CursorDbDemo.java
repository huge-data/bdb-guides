package zx.soft.bdb.demo.cursor;

import java.io.File;
import java.io.UnsupportedEncodingException;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

/**
 * 游标操作 
 * @author wanggang
 *
 */
public class CursorDbDemo {

	public static void main(String[] args) throws DatabaseException, UnsupportedEncodingException {

		// Open the environment, creating one if it does not exist
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true);
		Environment myDbEnvironment = new Environment(new File("testdb"), envConfig);
		// Open the database, creating one if it does not exist
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		dbConfig.setTransactional(true);
		Database myDatabase = myDbEnvironment.openDatabase(null, "TestDatabase", dbConfig);

		//C  
		for (int i = 0; i < 5; i++) {
			myDatabase.put(null, new DatabaseEntry(("KEY" + (i + 1)).getBytes("utf-8")), new DatabaseEntry(
					("VALUE" + (i + 1)).getBytes("utf-8")));
		}

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry value = new DatabaseEntry();

		//D (by Cursor)  
		Transaction txn = myDatabase.getEnvironment().beginTransaction(null, null);
		Cursor cursor1 = myDatabase.openCursor(txn, null);
		OperationStatus result1 = cursor1.getFirst(key, value, null);
		while (result1 == OperationStatus.SUCCESS) {
			if ("VALUE3".equals(new String(value.getData(), "utf-8"))) {
				cursor1.delete();
			}
			result1 = cursor1.getNext(key, value, null);
		}

		if (cursor1 != null) {
			cursor1.close();
		}
		if (txn != null) {
			txn.commit();
		}

		//R (by Cursor)  
		Cursor cursor2 = myDatabase.openCursor(null, null);
		OperationStatus result2 = cursor2.getFirst(key, value, null);

		while (result2 == OperationStatus.SUCCESS) {
			System.out.println("Cursor Read Value:" + new String(value.getData(), "utf-8"));
			result2 = cursor2.getNext(key, value, null);
		}

		if (cursor2 != null) {
			cursor2.close();
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
