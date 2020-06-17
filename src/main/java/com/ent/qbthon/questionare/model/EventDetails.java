package com.ent.qbthon.questionare.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class EventDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date;

	private String slot;

	private String skills;

	private boolean nomination;
	
	

}