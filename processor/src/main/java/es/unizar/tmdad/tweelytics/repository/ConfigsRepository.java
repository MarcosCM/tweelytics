package es.unizar.tmdad.tweelytics.repository;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

import es.unizar.tmdad.tweelytics.domain.ComponentConfig;

public interface ConfigsRepository extends MongoRepository<ComponentConfig, BigInteger>{

	ComponentConfig findByComponent(String component);
}
