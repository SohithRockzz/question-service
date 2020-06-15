package com.ent.qbthon.questionare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import com.ent.qbthon.questionare.entity.Event;
import com.ent.qbthon.questionare.model.EventUserDetails;

@Component
@Mapper(componentModel = "spring")
public interface EventUserDetailsMapper {
	
	@Mappings({
		@Mapping(target = "slot", source = "eventList.slot"),
		@Mapping(target = "date", source = "eventList.date"),
		@Mapping(target = "skills", source = "eventList.skills")
	})
	EventUserDetails  map(Event eventList);
	
	 
}