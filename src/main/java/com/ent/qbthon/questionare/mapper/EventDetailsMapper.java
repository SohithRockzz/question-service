package com.ent.qbthon.questionare.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import com.ent.qbthon.questionare.entity.Event;
import com.ent.qbthon.questionare.model.EventDetails;
import com.ent.qbthon.questionare.model.EventUserDetails;

@Component
@Mapper(componentModel = "spring")
public interface EventDetailsMapper {
	
	@Mappings({
		@Mapping(target = "id", source = "eventList.id"),
		@Mapping(target = "slot", source = "eventList.slot"),
		@Mapping(target = "date", source = "eventList.date"),
		@Mapping(target = "skills", source = "eventList.skills")
	})
	 EventDetails  map(Event eventList);
	 List<EventDetails>  map(List<Event> eventList);
	 
}
