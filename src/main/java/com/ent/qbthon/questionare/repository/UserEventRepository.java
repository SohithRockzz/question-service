package com.ent.qbthon.questionare.repository;

import java.util.List;
import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.ent.qbthon.questionare.entity.UserEvent;

@EnableScan
public interface UserEventRepository extends CrudRepository<UserEvent, String> {
	Optional<UserEvent> findById(String id);

	UserEvent findByEventIdAndUserId(String eventId, String userId);

	List<UserEvent> findByUserId(String userId);

	List<UserEvent> findByEventIdAndRole(String eventId, String string);
}
