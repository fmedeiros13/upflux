package net.upflux.comparator.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.upflux.comparator.model.FileTo;

public interface FilesToRepository extends MongoRepository<FileTo, String> {

	Optional<FileTo> findByName(String name);

}
