package zx.soft.bdb.demo.transaction;

import java.io.File;
import java.io.UnsupportedEncodingException;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Transaction;

/**
 * 事务处理 
 * @author wanggang
 *
 */
public class TransactionDBDemo {

	public static void main(String[] args) throws UnsupportedEncodingException {

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

		Transaction txn = myDatabase.getEnvironment().beginTransaction(null, null);
		try {
			for (int i = 0; i < 5; i++) {
				System.out.println(i);
				myDatabase.put(txn, new DatabaseEntry(("TXN-KEY" + (i + 1)).getBytes("utf-8")), new DatabaseEntry(
						("TXN-VALUE" + (i + 1)).getBytes("utf-8")));
			}
		} catch (DatabaseException e) {
			if (txn != null) {
				txn.abort();
				txn = null;
			}
			throw e;
		} finally {
			if (txn != null) {
				txn.commit();
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
