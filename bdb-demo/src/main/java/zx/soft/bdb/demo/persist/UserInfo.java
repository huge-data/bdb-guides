package zx.soft.bdb.demo.persist;

import java.io.Serializable;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class UserInfo implements Serializable {

	private static final long serialVersionUID = -8788291393812682797L;

	@PrimaryKey
	private String userId;
	private String userName;

	public UserInfo() {
		//
	}

	public UserInfo(String userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", userName=" + userName + "]";
	}

}
