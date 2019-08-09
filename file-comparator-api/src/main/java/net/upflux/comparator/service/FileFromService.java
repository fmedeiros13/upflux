package net.upflux.comparator.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.upflux.comparator.model.FileFrom;
import net.upflux.comparator.repository.FilesFromRepository;

@Service
public class FileFromService {
	
	@Autowired
    private FilesFromRepository filesFromRepository;
	
	public boolean createFile(FileFrom fileFrom) {		
		if (fileFrom != null && !fileFrom.getName().trim().isEmpty()) {
			this.filesFromRepository.save(fileFrom);
			return true;
		}
		
		return false;
	}
	
	public boolean deleteFile(String name) {	
		Optional<FileFrom> file = this.filesFromRepository.findByName(name.trim());
		
		if (file.isPresent()) {		
			this.filesFromRepository.deleteById(file.get().getId());
			return true;
		} else {
			return false;
		}
	}
	
	public Optional<FileFrom> readFile(String name) {
		return (this.filesFromRepository.findByName(name).isPresent() ? this.filesFromRepository.findByName(name) : null);
	}
	
	public List<FileFrom> readFiles() {		
		return (this.filesFromRepository.findAll().isEmpty() ? null : this.filesFromRepository.findAll());
	}
	
	public String verifyUpdate(FileFrom fileFrom, String method) {		
		Optional<FileFrom> file = this.filesFromRepository.findByName(fileFrom.getName().trim());
		
		if (file.isPresent()) {
			if (file.get().equals(fileFrom))
				return "NO CHANGES";
			else {
				fileFrom.setId(file.get().getId());
				this.filesFromRepository.save(fileFrom);
				
				return "UPDATED";
			}
		} else {
			if (!method.equalsIgnoreCase("PUT")) {
				this.filesFromRepository.save(fileFrom);
				
				Optional<FileFrom> newFile = this.filesFromRepository.findByName(fileFrom.getName().trim());
				
				if (newFile.isPresent())
					return "CREATED";
			}
		}
		
		return "PROBLEM";
	}

}
