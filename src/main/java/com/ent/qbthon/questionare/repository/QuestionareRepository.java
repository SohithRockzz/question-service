package com.ent.qbthon.questionare.repository;

import java.util.List;
import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.ent.qbthon.questionare.entity.Questionare;

@EnableScan
public interface QuestionareRepository extends CrudRepository<Questionare, String> {
	Optional<Questionare> findById(String id);

	List<Questionare> findByEventIdAndUserId(String eventId, String userId);

	List<Questionare> findByEventId(String eventId);

	List<Questionare> findByEventIdAndStatus(String eventId, String questionStatus);

	List<Questionare> findAllByEventIdAndAssignedSme(String eventId, String assignedSme);
	
}