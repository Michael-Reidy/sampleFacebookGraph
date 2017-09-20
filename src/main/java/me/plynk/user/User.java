package me.plynk.user;

import javax.validation.constraints.NotNull;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.*;
import lombok.Builder;
import lombok.experimental.*;

import java.io.Serializable;
import lombok.*;
import lombok.Builder;
import lombok.experimental.*;

@DynamoDBTable(tableName = "User")
@Data
@Accessors(fluent = true)
public class User implements Serializable {

	@DynamoDBHashKey(attributeName = "facebookKey")
	@NotNull(message = "facebookKey must not be empty")
	private String facebookKey;

	private String fullName;
}
