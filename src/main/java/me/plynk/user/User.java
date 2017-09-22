package me.plynk.user;

import javax.validation.constraints.NotNull;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.io.Serializable;

@DynamoDBTable(tableName = "User")
public class User implements Serializable {

	private String facebookKey;

	private String fullName;

	@DynamoDBHashKey(attributeName="serverName")
	@NotNull(message = "facebookKey must not be empty")
	public String getFacebookKey() {
		return facebookKey;
	}

	public void setFacebookKey(String facebookKey) {
		this.facebookKey = facebookKey;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User)) return false;

		User user = (User) o;

		if (facebookKey != null ? !facebookKey.equals(user.facebookKey) : user.facebookKey != null) return false;
		return fullName != null ? fullName.equals(user.fullName) : user.fullName == null;
	}

	@Override
	public int hashCode() {
		int result = facebookKey != null ? facebookKey.hashCode() : 0;
		result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
		return result;
	}
}
