package zx.soft.bdb.demo.persist;

import java.io.File;
import java.util.Iterator;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

/**
 * 直接持久层应用（DPL：Direct Persistence Layer）
 * @author wanggang
 *
 */
public class PersistDBDemo {

	public static void main(String[] args) {

		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true);
		Environment myDbEnvironment = new Environment(new File("testdb"), envConfig);

		StoreConfig storeConfig = new StoreConfig();
		storeConfig.setAllowCreate(true);
		storeConfig.setTransactional(true);

		EntityStore store = new EntityStore(myDbEnvironment, "StoreDB", storeConfig);

		PrimaryIndex<String, UserInfo> pIndex = store.getPrimaryIndex(String.class, UserInfo.class);

		//C  
		pIndex.put(new UserInfo("001", "user001"));
		pIndex.put(new UserInfo("002", "user002"));
		pIndex.put(new UserInfo("003", "user003"));
		pIndex.put(new UserInfo("004", "user004"));
		pIndex.put(new UserInfo("005", "user005"));

		//R  
		UserInfo getData = pIndex.get("001");
		System.out.println("Read User 001:" + getData);

		//U  
		pIndex.put(new UserInfo("002", "user002222"));

		//Read ALL  
		EntityCursor<UserInfo> cursor = pIndex.entities();
		try {
			Iterator<UserInfo> i = cursor.iterator();
			while (i.hasNext()) {
				System.out.println("Cursor data:" + i.next());
			}
		} finally {
			cursor.close();
		}

		//D  
		String pkey = "003";
		boolean flag = pIndex.delete(pkey);
		System.out.println("delete object :" + pkey + " result:" + flag);

		//关闭store  
		if (store != null) {
			store.close();
			store = null;
		}

	}

}
