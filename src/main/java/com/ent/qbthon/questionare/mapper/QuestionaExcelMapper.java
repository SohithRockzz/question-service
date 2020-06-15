package com.ent.qbthon.questionare.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import com.ent.qbthon.questionare.entity.Questionare;
import com.ent.qbthon.questionare.model.QuestionarExcel;

@Component
@Mapper(componentModel = "spring")
public interface QuestionaExcelMapper {
	@Mappings({
		//@Mapping(target = "Id", source = "questionarList.id"),
		@Mapping(target = "blooms_Taximony", source = "questionarList.blooms"),
		@Mapping(target = "difficulty", source = "questionarList.difficulty"),
		@Mapping(target = "category", source = "questionarList.category"),
		@Mapping(target = "source", source = "questionarList.source"),
		@Mapping(target = "multiple", source = "questionarList.multiple"),
		@Mapping(target = "topic", source = "questionarList.topic"),
		@Mapping(target = "question", source = "questionarList.description"),
		@Mapping(target = "option_One", source = "questionarList.optionOne"),
		@Mapping(target = "score_One", source = "questionarList.scoreOne"),
		@Mapping(target = "option_Two", source = "questionarList.optionTwo"),
		@Mapping(target = "score_Two", source = "questionarList.scoreTwo"),
		@Mapping(target = "option_Four", source = "questionarList.optionFour"),
		@Mapping(target = "score_Four", source = "questionarList.scoreFour"),
		@Mapping(target = "option_Three", source = "questionarList.optionThree"),
		@Mapping(target = "score_Three", source = "questionarList.scoreThree"),
		@Mapping(target = "status", source = "questionarList.status"),
		@Mapping(target = "associate_Id", source = "questionarList.userId")
	})
	QuestionarExcel  map(Questionare questionarList);
	List<QuestionarExcel>  map(List<Questionare> questionarList);
}
