package com.ent.qbthon.questionare.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ent.qbthon.questionare.entity.Questionare;
import com.ent.qbthon.questionare.entity.User;
import com.ent.qbthon.questionare.entity.UserEvent;
import com.ent.qbthon.questionare.exception.QbthonQuestionareServiceException;
import com.ent.qbthon.questionare.mapper.QuestionaExcelMapper;
import com.ent.qbthon.questionare.mapper.UserEventListMapper;
import com.ent.qbthon.questionare.model.QuestionarExcel;
import com.ent.qbthon.questionare.repository.EventRepository;
import com.ent.qbthon.questionare.repository.QuestionareRepository;
import com.ent.qbthon.questionare.repository.UserEventRepository;
import com.ent.qbthon.questionare.repository.UserRepository;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	EventRepository eventRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserEventRepository userEventRepository;

	@Autowired
	UserEventListMapper userEventListMapper;

//	@Autowired
//	EventDetailsMapper eventDetailsMapper;

//	@Autowired
//	EventUserDetailsMapper evenUsertDetailsMapper;

//	@Autowired
//	EmailRestTemplate emailRestTemplate;

	@Autowired
	QuestionareRepository questionareRepository;
	@Autowired
	QuestionaExcelMapper questionaExcelMapper;

	private int smeListSize = 0;
	private int currentSmeIndex = 0;
	Random random = new Random();

	public boolean saveQuestion(String userId, String eventId, MultipartFile userQuestionExcelDataFile, String question)
			throws IOException {
		log.info("Inside saveQuestion method QuestionServiceImpl Service::");
		List<Questionare> questionareList = new ArrayList<>();
		List<Questionare> questionarexcelList = new ArrayList<>();
		boolean status = false;
		try {
			Gson gson = new Gson();
			if (!StringUtils.isEmpty(question) && !question.equals("null")) {
				questionareList.add(gson.fromJson(question, Questionare.class));
			}
			if (userQuestionExcelDataFile != null) {
				questionarexcelList = getIdFromExcel(userQuestionExcelDataFile, userId, eventId);
			}
			if (!CollectionUtils.isEmpty(questionarexcelList)) {
				questionareList.addAll(questionarexcelList);
			}

			List<UserEvent> userEventDetailList = userEventRepository.findByEventIdAndRole(eventId, "SME");
			List<UserEvent> filteredUserEventDetailList = userEventDetailList.stream()
					.filter(userEvent -> userEvent.getUserId() != userId).collect(Collectors.toList());
			this.smeListSize = userEventDetailList.size();
			questionareList.forEach(questionare -> {
				if (this.smeListSize > 0) {
					questionare.setAssignedSme(filteredUserEventDetailList.get(this.currentSmeIndex).getUserId());
					questionare.setStatus("Under Review");
					log.info("assigned sme->" + userEventDetailList.get(this.currentSmeIndex).getUserId()
							+ " which is at ->" + this.currentSmeIndex + " position");
					++this.currentSmeIndex;
				} else {
					throw new QbthonQuestionareServiceException("Something went wrong");
				}
				if (this.currentSmeIndex > this.smeListSize - 1) {
					this.currentSmeIndex = 0;
				}
			});

			if (!CollectionUtils.isEmpty(questionareList)) {
				questionareRepository.saveAll(questionareList);
				status = true;
			}

		}catch (QbthonQuestionareServiceException e) {
			throw e;

		} catch (Exception e) {
			log.error("Error in saveQuestionare", e);
			throw new QbthonQuestionareServiceException("Something went wrong", e);
		}

		return status;
	}

	public boolean updateReviewQuestionStatus(String questionId, String reviewStatus, String comment) {
		log.info("Inside updateReviewQuestionStatus method QuestionServiceImpl Service:::");
		boolean status = false;
		try {
			Questionare questionare = questionareRepository.findById(questionId).orElse(null);
			if (null != questionare) {
				questionare.setComment(comment);
				questionare.setStatus(reviewStatus);
				questionare.setUpdateTime(new Date());
				Questionare savedQuestionare = questionareRepository.save(questionare);
				if (savedQuestionare.getId() != null) {
					status = true;
				}
			}
		} catch (Exception e) {
			log.error("Error in updateReviewQuestionStatus", e);
			throw new QbthonQuestionareServiceException("Something went wrong", e);
		}
		return status;
	}

	public boolean updateQuestion(Questionare questionare) {
		log.info("Inside updateQuestion method QuestionServiceImpl Service:::");
		boolean status = false;
		try {
			if (questionare.getStatus().equals("Rejected")) {
				Questionare questionareById = questionareRepository.findById(questionare.getId()).orElse(null);
				if (null != questionareById) {
					questionareById.setDifficulty(questionare.getDifficulty());
					questionareById.setOptionOne(questionare.getOptionOne());
					questionareById.setOptionTwo(questionare.getOptionTwo());
					questionareById.setOptionThree(questionare.getOptionThree());
					questionareById.setOptionFour(questionare.getOptionFour());
					questionareById.setScoreOne(questionare.getScoreOne());
					questionareById.setScoreTwo(questionare.getScoreTwo());
					questionareById.setScoreThree(questionare.getScoreThree());
					questionareById.setScoreFour(questionare.getScoreFour());
					questionareById.setTopic(questionare.getTopic());
					questionareById.setCategory(questionare.getCategory());
					questionareById.setMultiple(questionare.getMultiple());

					Questionare savedQuestionare = questionareRepository.save(questionare);
					if (savedQuestionare.getId() != null) {
						status = true;
					}
				}
			} else {
				log.error("Error in updateQuestion");
				throw new QbthonQuestionareServiceException("Only Rejected Questions Can Be Updated");
			}
		}catch (QbthonQuestionareServiceException e) {
			throw e;

		} catch (Exception e) {
			log.error("Error in updateQuestion", e);
			throw new QbthonQuestionareServiceException("Something Went Wrong", e);
		}
		return status;
	}
	
	public Map<String, List<Questionare>> getQuestionsAssignedToSME(String eventId, String smeId) {
		log.info("Getting User Questions for User and Event Related:::");
		List<Questionare> questionareList = new ArrayList<>();
		Map<String, List<Questionare>> questionareStatusMap = new HashMap<>();
		try {
			questionareList = questionareRepository.findAllByEventIdAndAssignedSme(eventId, smeId);
			questionareStatusMap = getQuestionStatusMap( questionareList);
		}catch (Exception e) {
			log.error("Error in getUserQuestions", e);
			throw new QbthonQuestionareServiceException("Something went wrong", e);
		}
       return questionareStatusMap;
	}

	public Map<String, List<Questionare>> getUserQuestions(String eventId, String userId) {
		log.info("Getting User Questions for User and Event Related:::");
		List<Questionare> questionareList = new ArrayList<>();
		Map<String, List<Questionare>> questionareStatusMap = new HashMap<>();
		try {
			if (!StringUtils.isEmpty(eventId) && !StringUtils.isEmpty(userId)) {
				questionareList = questionareRepository.findByEventIdAndUserId(eventId, userId);
			} else if (!StringUtils.isEmpty(eventId) && StringUtils.isEmpty(userId)) {
				questionareList = questionareRepository.findByEventId(eventId);
			} else {
				log.error("Empty attributes in getUserQuestions");
				throw new QbthonQuestionareServiceException("Empty attributes in getUserQuestions");
			}
			questionareStatusMap = getQuestionStatusMap( questionareList);
		} catch (QbthonQuestionareServiceException e) {
			throw e;

		} catch (Exception e) {
			log.error("Error in getUserQuestions", e);
			throw new QbthonQuestionareServiceException("Something went wrong", e);
		}
		return questionareStatusMap;
	}
	
//	public Map<String, List<Questionare>> getQuestions(String eventId) {
//		log.info("Inside getQuestions method QuestionServiceImpl Service:::");
//		List<Questionare> questionareList = new ArrayList<>();
//		Map<String, List<Questionare>> questionareStatusMap = new HashMap<>();
//		try {
//			questionareList = questionareRepository.findByEventId(eventId);
//			questionareStatusMap = getQuestionStatusMap( questionareList);
//
//		} catch (Exception e) {
//			log.error("Error in getQuestions", e);
//			throw new QbthonQuestionareServiceException("Something went wrong", e);
//		}
//		return questionareStatusMap;
//	}
//	
	private Map<String, List<Questionare>> getQuestionStatusMap(List<Questionare> questionareList) {
		Map<String, List<Questionare>> questionareStatusMap = new HashMap<>();
		questionareStatusMap.put("Under Review", new ArrayList<>());
		questionareStatusMap.put("Rejected", new ArrayList<>());
		questionareStatusMap.put("Partially Accepted", new ArrayList<>());
		questionareStatusMap.put("Accepted", new ArrayList<>());

		if (questionareList.size() > 0) {
			questionareList.parallelStream().forEach(ques -> {
				if (ques.getStatus().equals("Under Review")) {
					questionareStatusMap.get("Under Review").add(ques);
				} else if (ques.getStatus().equals("Partially Accepted")) {
					questionareStatusMap.get("Partially Accepted").add(ques);
				} else if (ques.getStatus().equals("Accepted")) {
					questionareStatusMap.get("Accepted").add(ques);
				} else if (ques.getStatus().equals("Rejected")) {
					questionareStatusMap.get("Rejected").add(ques);
				}
			});
		}
		return questionareStatusMap;
	}

	public List<QuestionarExcel> getExcelData(String eventId) {
		List<Questionare> questionareList = new ArrayList<>();
		List<QuestionarExcel> questionareExcelList = new ArrayList<>();
		try {
			questionareList = questionareRepository.findByEventId(eventId);
			questionareExcelList = questionaExcelMapper.map(questionareList);
		} catch (Exception e) {
			log.error("Error in getQuestions", e);
			throw new QbthonQuestionareServiceException("Something went wrong", e);
		}
		return questionareExcelList;
	}

	public Map<String, Map<String, Long>> getEventSpecificData(String eventId) {
		Map<String, Map<String, Long>> graphEventSpecifcData = new HashMap<>();
		try {
			List<Questionare> eventStatusSpecificList = (List<Questionare>) questionareRepository
					.findByEventId(eventId);
			Map<String, Long> groupByStatusMap = eventStatusSpecificList.stream()
					.collect(Collectors.groupingBy(Questionare::getStatus, Collectors.counting()));
			Map<String, Long> groupByCategoryMap = eventStatusSpecificList.stream()
					.collect(Collectors.groupingBy(Questionare::getCategory, Collectors.counting()));
			groupByCategoryMap.putIfAbsent("Application", Long.parseLong("0"));
			groupByCategoryMap.putIfAbsent("Comprehension", Long.parseLong("0"));
			groupByCategoryMap.putIfAbsent("Analysis", Long.parseLong("0"));
			graphEventSpecifcData.put("StatusCount", groupByStatusMap);

			graphEventSpecifcData.put("CategoryCount", groupByCategoryMap);
		} catch (QbthonQuestionareServiceException e) {
			log.error("Error in saveEvent", e);
			throw e;
		} catch (Exception e) {
			log.error("Error in getEventDetails", e);
			throw new QbthonQuestionareServiceException("Something Went Wrong", e);
		}
		return graphEventSpecifcData;
	}

	public List<User> getEarlyBirds(String eventId) {
		List<User> userDescriptionList = new ArrayList<>();
		try {
			List<Questionare> acceptedQuestionsList = questionareRepository.findByEventIdAndStatus(eventId, "Accepted");
			Map<String, List<Questionare>> groupByUserIdMap = acceptedQuestionsList.stream()
					.collect(Collectors.groupingBy(Questionare::getUserId));
			if (groupByUserIdMap.size() > 0) {
				groupByUserIdMap.forEach((userId, questionList) -> {
					questionList.sort(Comparator.comparing(Questionare::getUpdateTime, (s1, s2) -> {
						return Long.compare(s1.getTime(), s2.getTime());
					}));
				});
				long timeOne = 0;
				long timeTwo = 0;
				String userOneId = "";
				String userTwoId = "";
				for (String userId : groupByUserIdMap.keySet()) {
					if (groupByUserIdMap.get(userId).size() >= 2) {
						if (timeOne == 0 && timeTwo == 0) {
							timeOne = groupByUserIdMap.get(userId).get(0).getUpdateTime().getTime();
							timeTwo = groupByUserIdMap.get(userId).get(1).getUpdateTime().getTime();
							userOneId = userId;
						} else if (groupByUserIdMap.get(userId).get(0).getUpdateTime().getTime() < timeOne
								&& groupByUserIdMap.get(userId).get(1).getUpdateTime().getTime() < timeTwo) {
							timeOne = groupByUserIdMap.get(userId).get(0).getUpdateTime().getTime();
							timeTwo = groupByUserIdMap.get(userId).get(1).getUpdateTime().getTime();
							userTwoId = userOneId;
							userOneId = userId;
						} else if (groupByUserIdMap.get(userId).get(0).getUpdateTime().getTime() > timeOne
								&& groupByUserIdMap.get(userId).get(1).getUpdateTime().getTime() < timeTwo) {
							timeOne = groupByUserIdMap.get(userId).get(0).getUpdateTime().getTime();
							timeTwo = groupByUserIdMap.get(userId).get(1).getUpdateTime().getTime();
							userTwoId = userOneId;
							userOneId = userId;
						}
					}
				}
				List<String> userIdList = new ArrayList<>();
				if (!StringUtils.isEmpty(userOneId) && StringUtils.isEmpty(userTwoId)) {
					userIdList.add(userOneId);
					userDescriptionList = (List<User>) userRepository.findAllById(userIdList);
				} else if (!StringUtils.isEmpty(userOneId) && !StringUtils.isEmpty(userTwoId)) {
					userIdList.add(userOneId);
					userIdList.add(userTwoId);
					userDescriptionList = (List<User>) userRepository.findAllById(userIdList);
				}

			}
		} catch (Exception e) {
			log.error("Error in getEarlyBirds", e);
			throw new QbthonQuestionareServiceException("Something went wrong", e);
		}
		return userDescriptionList;
	}

	public List<User> getEvoucherWinners(String eventId) {
		List<User> userDescriptionList = new ArrayList<>();
		try {
			List<Questionare> acceptedQuestionsList = questionareRepository.findByEventIdAndStatus(eventId, "Accepted");
			Map<String, Long> groupByUserIdMap = acceptedQuestionsList.stream()
					.filter(ques -> ques.getStatus().equals("Accepted"))
					.collect(Collectors.groupingBy(Questionare::getUserId, Collectors.counting()));
			if (groupByUserIdMap.size() > 0) {
				groupByUserIdMap.forEach((userId, count) -> {
					if (count >= 6) {
						userDescriptionList.add(userRepository.findById(userId).get());
					}
				});

			}
		} catch (Exception e) {
			log.error("Error in getEarlyBirds", e);
			throw new QbthonQuestionareServiceException("Something went wrong", e);
		}
		return userDescriptionList;
	}

	private List<Questionare> getIdFromExcel(MultipartFile excelDataFile, String userId, String eventId)
			throws IOException {
		log.info("Inside getIdFromExcel method QuestionServiceImpl Service:::");
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(excelDataFile.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(1);
		Iterator<Row> rowIterator = worksheet.iterator();
		List<Questionare> columndata = new ArrayList<>();

		while (rowIterator.hasNext()) {
			Questionare questionare = null;
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();

			questionare = row.getRowNum() > 0 ? new Questionare() : null;
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if (row.getRowNum() > 0) { // To filter column headings

					if (cell.getColumnIndex() == 1) {// To match column index
						questionare.setBlooms(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 2) {// To match column index
						questionare.setDifficulty(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 3) {// To match column index
						questionare.setCategory(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 4) {// To match column index
						questionare.setMultiple(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 5) {// To match column index
						questionare.setTopic(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 6) {// To match column index
						questionare.setDescription(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 7) {// To match column index
						questionare.setOptionOne(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 8) {// To match column index
						questionare.setScoreOne(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 9) {// To match column index
						questionare.setOptionTwo(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 10) {// To match column index
						questionare.setScoreTwo(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 11) {// To match column index
						questionare.setOptionThree(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 12) {// To match column index
						questionare.setScoreThree(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 13) {// To match column index
						questionare.setOptionFour(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 14) {// To match column index
						questionare.setScoreFour(getCellValue(cell));
					}

					if (cell.getColumnIndex() == 16) {// To match column index
						questionare.setSource(getCellValue(cell));
					}
					if (cell.getColumnIndex() == 17) {// To match column index
						questionare.setStack(getCellValue(cell));
					}
					questionare.setUserId(userId);

					questionare.setEventId(eventId);

				}
			}
			if (null != questionare) {
				columndata.add(questionare);
			}
		}
		return columndata;
	}

	private String getCellValue(Cell cell) {
		String indexValue = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			indexValue = String.valueOf(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			indexValue = cell.getStringCellValue();
			break;
		}
		return indexValue;
	}
}