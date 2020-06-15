package com.ent.qbthon.questionare.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ent.qbthon.questionare.entity.Questionare;
import com.ent.qbthon.questionare.entity.User;
import com.ent.qbthon.questionare.model.QuestionarExcel;
import com.ent.qbthon.questionare.service.QuestionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/question")
public class QuestionController {
	@Autowired
	QuestionService questionService;

	@PostMapping(value = "/create", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Boolean> creatQuestion(@RequestParam(value="quesFile", required=false) MultipartFile quesFiles,
            @RequestParam(value="userId",required=false) String userId,
            @RequestParam(value="eventId",required=false) String eventId,
            @RequestParam(value="questionnaire", required=false) String question) throws IOException {
		log.info("Inside createQuestion Controller --->");
	           
		return new ResponseEntity<>(questionService.saveQuestion(userId,eventId, quesFiles,question), HttpStatus.OK);
	}
	
	@GetMapping(value = "/all", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Map<String,List<Questionare>>> getQuestions(@RequestParam(required = true) String eventId
			) {
		log.info("Inside getQuestions Controller --->");
		return new ResponseEntity<>(questionService.getQuestions(eventId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getUserQuestionList", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Map<String,List<Questionare>>> getUserQuestionsList(@RequestParam(required = true) String eventId,
			@RequestParam(required = true) String userId) {
		log.info("Inside getUserQuestions Controller --->");
		return new ResponseEntity<>(questionService.getUserQuestions(eventId,userId), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/download/all", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<QuestionarExcel>> getExcelData(@RequestParam(required = true) String eventId){
		log.info("Inside getExcelData Controller --->");
		return new ResponseEntity<>(questionService.getExcelData(eventId), HttpStatus.OK);
	}
	
	@PostMapping(value = "/update/status", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Boolean> updateReviewQuestionStatus(@RequestParam(required = true) String questionId,
			@RequestParam(required = true) String reviewStatus,
			@RequestParam(required = true) String comment){
		log.info("Inside updateReviewQuestionStatus Controller --->");
		return new ResponseEntity<>(questionService.updateReviewQuestionStatus( questionId,reviewStatus,comment), HttpStatus.OK);
	}
	
	@PostMapping(value = "/update/question", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Boolean> updateQuestion(@RequestBody(required=true) Questionare questionare){
		log.info("Inside updateReviewQuestionStatus Controller --->");
		return new ResponseEntity<>(questionService.updateQuestion(questionare), HttpStatus.OK);
	}
	
	@GetMapping(value = "/event/early/birds", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<User>> getEarlyBirds(@RequestParam(required = true) String eventId){
		log.info("Inside getEarlyBirds Controller --->");
		return new ResponseEntity<>(questionService.getEarlyBirds(eventId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/chart", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Map<String,Map<String,Long>>> getEventSpecificData(@RequestParam(required = true) String eventId){
		log.info("Inside getEventSpecificData Controller --->");
		return new ResponseEntity<>(questionService.getEventSpecificData(eventId), HttpStatus.OK);
	}

}
