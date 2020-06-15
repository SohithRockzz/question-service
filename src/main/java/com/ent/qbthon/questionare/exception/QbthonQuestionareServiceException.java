package com.ent.qbthon.questionare.exception;

public class QbthonQuestionareServiceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public QbthonQuestionareServiceException(String errorMessage, Throwable t) {
		super(errorMessage, t);
	}

	public QbthonQuestionareServiceException(String errorMessage) {
		super(errorMessage);
	}
	

}
