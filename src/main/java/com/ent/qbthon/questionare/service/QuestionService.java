package com.ent.qbthon.questionare.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ent.qbthon.questionare.entity.Questionare;
import com.ent.qbthon.questionare.entity.User;
import com.ent.qbthon.questionare.model.QuestionarExcel;


public interface QuestionService {
	public boolean saveQuestion(String userId, String eventId, MultipartFile userQuestionExcelDataFile,String question) throws IOException;
	public Map<String,List<Questionare>> getQuestions(String eventId);
	public List<QuestionarExcel> getExcelData(String eventId);
	public boolean updateQuestion(Questionare questionare);
	public boolean updateReviewQuestionStatus(String questionId,String reviewStatus,String comment);
	public List<User> getEarlyBirds(String eventId);
	public Map<String,Map<String,Long>> getEventSpecificData(String eventId);
	public Map<String,List<Questionare>> getUserQuestions(String eventId, String userId);
}
