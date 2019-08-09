package net.upflux.comparator.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.upflux.comparator.model.FileTo;
import net.upflux.comparator.repository.FilesToRepository;

@Service
public class FileToService {
	
	@Autowired
    private FilesToRepository filesToRepository;
	
	public boolean createFile(FileTo fileTo) {		
		if (fileTo != null && !fileTo.getName().trim().isEmpty()) {
			this.filesToRepository.save(fileTo);
			return true;
		}
		
		return false;
	}
	
	public boolean deleteFile(String name) {	
		Optional<FileTo> file = this.filesToRepository.findByName(name);
		
		if (file.isPresent()) {
			this.filesToRepository.deleteById(file.get().getId());
			return true;
		} else {
			return false;
		}
	}
	
	public Optional<FileTo> readFile(String name) {
		return (this.filesToRepository.findByName(name).isPresent() ? this.filesToRepository.findByName(name) : null);
	}
	
	public List<FileTo> readFiles() {		
		return (this.filesToRepository.findAll().isEmpty() ? null : this.filesToRepository.findAll());
	}
	
	public String verifyUpdate(FileTo fileTo, String method) {		
		Optional<FileTo> file = this.filesToRepository.findByName(fileTo.getName().trim());
		
		if (file.isPresent()) {
			if (file.get().equals(fileTo))
				return "NO CHANGES";
			else {
				fileTo.setId(file.get().getId());
				this.filesToRepository.save(fileTo);
				
				return "UPDATED";
			}
		} else {
			if (!method.equalsIgnoreCase("PUT")) {
				this.filesToRepository.save(fileTo);
				
				Optional<FileTo> newFile = this.filesToRepository.findByName(fileTo.getName().trim());
				
				if (newFile.isPresent())
					return "CREATED";
			}
		}
		
		return "PROBLEM";
	}

}
