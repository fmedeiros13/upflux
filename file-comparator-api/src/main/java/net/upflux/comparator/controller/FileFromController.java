package net.upflux.comparator.controller;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.upflux.comparator.model.FileFrom;
import net.upflux.comparator.service.FileFromService;

@CrossOrigin("*")
@RestController
@RequestMapping("/file/v01/from")
public class FileFromController {
	
	@Autowired
    private FileFromService fileFromService;
	
	private static final Logger LOGGER = Logger.getLogger(FileFromController.class.getPackage().getName());
	
	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<FileFrom> createFileFrom(@Valid @RequestBody FileFrom fileFrom) {
		
		String response = fileFromService.verifyUpdate(fileFrom, "POST");
		
		if (response.equalsIgnoreCase("UPDATED")) {
			LOGGER.info("'" + fileFrom.getName() + "' ---> FILE NAME ALREADY EXISTS INTO DATABASE. UPDATING... DONE!");
			return ResponseEntity.status(204).build();
		} else if (response.equalsIgnoreCase("NO CHANGES")) {
			LOGGER.info("'" + fileFrom.getName() + "' ---> FILE NAME WITH NO CHANGES INTO DATABASE.");
			return ResponseEntity.status(304).body(fileFrom);
		} else if (response.equalsIgnoreCase("CREATED")) {
			LOGGER.info("'" + fileFrom.getName() + "' ---> FILE CREATED INTO DATABASE.");
			return ResponseEntity.status(201).body(fileFrom);
		}
		
		return ResponseEntity.badRequest().body(fileFrom);
	}
	
	@DeleteMapping("/delete/{name}")
	public ResponseEntity<FileFrom> deleteFileFrom(@PathVariable String name) {
		boolean deleted = fileFromService.deleteFile(name);
		
		if (deleted)
			return ResponseEntity.ok().build();
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<FileFrom> readFileFrom(@PathVariable String name) {
		Optional<FileFrom> fileFrom = fileFromService.readFile(name);
		
		if (fileFrom == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(fileFrom.get());
	}
	
	@GetMapping
	public ResponseEntity<List<FileFrom>> readFilesFrom() {
		List<FileFrom> files = fileFromService.readFiles();
		
		if (files == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(files);
	}
	
	@PutMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<FileFrom> updateFileFrom(@Valid @RequestBody FileFrom fileFrom) {
		
		String response = fileFromService.verifyUpdate(fileFrom, "PUT");
		
		if (response.equalsIgnoreCase("UPDATED")) {
			LOGGER.info("'" + fileFrom.getName() + "' ---> FILE NAME ALREADY EXISTS INTO DATABASE. UPDATE DONE!");
			return ResponseEntity.status(204).build();
		} else if (response.equalsIgnoreCase("NO CHANGES")) {
			LOGGER.info("'" + fileFrom.getName() + "' ---> FILE NAME WITH NO CHANGES INTO DATABASE.");
			return ResponseEntity.status(304).body(fileFrom);
		} 

		LOGGER.severe("'" + fileFrom.getName() + "' ---> FILE DOES NOT EXISTS INTO DATABASE. THE UPDATE WAS CANCELED!");
		return ResponseEntity.status(403).body(fileFrom);		
	}

}
