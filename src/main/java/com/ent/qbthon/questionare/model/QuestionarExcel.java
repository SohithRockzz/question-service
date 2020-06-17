package com.ent.qbthon.questionare.model;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class QuestionarExcel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private String Id;
	private String blooms_Taximony;
	
	private String stack;

	private String difficulty;

	private String category;

	private String source;

	private String multiple;

	private String topic;

	private String question;

	private String option_One;

	private String score_One;

	private String option_Two;

	private String score_Two;

	private String option_Four;

	private String score_Four;

	private String option_Three;

	private String score_Three;

	private String status;
	
	private String associate_Id;

}