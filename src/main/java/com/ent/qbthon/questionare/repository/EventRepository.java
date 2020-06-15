package com.ent.qbthon.questionare.repository;

import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.ent.qbthon.questionare.entity.Event;

@EnableScan
public interface EventRepository extends CrudRepository<Event, String> {
	Optional<Event> findById(String id);
	//List<Event> findAllById(List<String> eventIds);
}
