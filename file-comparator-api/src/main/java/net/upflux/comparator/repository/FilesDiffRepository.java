package net.upflux.comparator.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.upflux.comparator.model.FileDiff;

public interface FilesDiffRepository extends MongoRepository<FileDiff, String> {

	Optional<FileDiff> findByName(String name);

}
