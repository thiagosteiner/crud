package com.steiner.repository;

import com.steiner.domain.MyEntity;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the MyEntity entity.
 */
@SuppressWarnings("unused")
public interface MyEntityRepository extends MongoRepository<MyEntity,String> {

}
