package net.upflux.comparator.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.upflux.comparator.model.FileFrom;

public interface FilesFromRepository extends MongoRepository<FileFrom, String> {

	Optional<FileFrom> findByName(String name);

}
