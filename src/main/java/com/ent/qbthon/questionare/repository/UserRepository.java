package com.ent.qbthon.questionare.repository;

import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.ent.qbthon.questionare.entity.User;

@EnableScan
public interface UserRepository extends CrudRepository<User, String> {
	Optional<User> findById(String id);
}
