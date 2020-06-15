package com.ent.qbthon.questionare.entity;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
@DynamoDBTable(tableName = "QBTHON_EVENT")
public class Event {

	@DynamoDBHashKey(attributeName = "event_id")
	@DynamoDBAutoGeneratedKey
	private String id;
	
	@DynamoDBAttribute(attributeName = "event_date")
	@DynamoDBTyped(DynamoDBAttributeType.S)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date;
	
	@DynamoDBAttribute(attributeName = "event_slot")
	private String slot;
	
	@DynamoDBAttribute(attributeName = "event_skills")
	private String skills;

}