package com.ent.qbthon.questionare.entity;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@DynamoDBTable(tableName = "QBTHON_QUESTIONARE")
public class Questionare {
	@DynamoDBHashKey(attributeName = "question_id")
	@DynamoDBAutoGeneratedKey
	private String id;
	
	@DynamoDBAttribute(attributeName = "blooms_tax")
	private String blooms;
	
	@DynamoDBAttribute(attributeName = "question_difficulty")
	private String difficulty;
	
	@DynamoDBAttribute(attributeName = "question_category")
	private String category;
	
	@DynamoDBAttribute(attributeName = "question_update_time")
	private Date updateTime;
	
	
	@DynamoDBAttribute(attributeName = "question_source")
	private String source;
	
	@DynamoDBAttribute(attributeName = "question_multiple_answer")
	private String multiple;
	
	@DynamoDBAttribute(attributeName = "question_topic")
	private String topic;
	
	@DynamoDBAttribute(attributeName = "question_description")
	private String description;
	
	@DynamoDBAttribute(attributeName = "question_option_one")
	private String optionOne;
	
	@DynamoDBAttribute(attributeName = "question_score_one")
	private String scoreOne;
	
	@DynamoDBAttribute(attributeName = "question_option_two")
	private String optionTwo;
	
	@DynamoDBAttribute(attributeName = "question_score_two")
	private String scoreTwo;
	
	@DynamoDBAttribute(attributeName = "question_option_four")
	private String optionFour;
	
	@DynamoDBAttribute(attributeName = "question_score_four")
	private String scoreFour;
	
	@DynamoDBAttribute(attributeName = "question_option_three")
	private String optionThree;
	
	@DynamoDBAttribute(attributeName = "question_score_three")
	private String scoreThree;
	
	@DynamoDBAttribute(attributeName = "question_assigned_sme")
	private String assignedSme;
	
	@DynamoDBAttribute(attributeName = "question_type")
	private String type;
	
	@DynamoDBAttribute(attributeName = "question_status")
	private String status;
	
	@DynamoDBAttribute(attributeName = "question_comment")
	private String comment;
	
	@DynamoDBAttribute(attributeName = "question_user_id")
	private String userId;
	
	@DynamoDBAttribute(attributeName = "question_event_id")
	private String eventId;
}
