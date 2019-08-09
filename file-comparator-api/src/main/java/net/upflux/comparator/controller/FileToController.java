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

import net.upflux.comparator.model.FileTo;
import net.upflux.comparator.service.FileToService;

@CrossOrigin("*")
@RestController
@RequestMapping("/file/v01/to")
public class FileToController {
	
	@Autowired
    private FileToService fileToService;
	
	private static final Logger LOGGER = Logger.getLogger(FileToController.class.getPackage().getName());
	
	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<FileTo> createFileTo(@Valid @RequestBody FileTo fileTo) {
		
		String response = fileToService.verifyUpdate(fileTo, "POST");
		
		if (response.equalsIgnoreCase("UPDATED")) {
			LOGGER.info("'" + fileTo.getName() + "' ---> FILE NAME ALREADY EXISTS INTO DATABASE. UPDATING... DONE!");
			return ResponseEntity.status(204).build();
		} else if (response.equalsIgnoreCase("NO CHANGES")) {
			LOGGER.info("'" + fileTo.getName() + "' ---> FILE NAME WITH NO CHANGES INTO DATABASE.");
			return ResponseEntity.status(304).body(fileTo);
		} else if (response.equalsIgnoreCase("CREATED")) {
			LOGGER.info("'" + fileTo.getName() + "' ---> FILE CREATED INTO DATABASE.");
			return ResponseEntity.status(201).body(fileTo);
		}
		
		return ResponseEntity.badRequest().body(fileTo);
	}
	
	@DeleteMapping("/delete/{name}")
	public ResponseEntity<FileTo> deleteFileTo(@PathVariable String name) {
		
		boolean deleted = fileToService.deleteFile(name);
		
		if (deleted)
			return ResponseEntity.ok().build();
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<FileTo> readFileTo(@PathVariable String name) {
		Optional<FileTo> fileTo = fileToService.readFile(name);
		
		if (fileTo == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(fileTo.get());
	}
	
	@GetMapping
	public ResponseEntity<List<FileTo>> readFilesTo() {
		List<FileTo> files = fileToService.readFiles();
		
		if (files == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(files);
	}
	
	@PutMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<FileTo> updateFileTo(@Valid @RequestBody FileTo fileTo) {
		
		String response = fileToService.verifyUpdate(fileTo, "PUT");
		
		if (response.equalsIgnoreCase("UPDATED")) {
			LOGGER.info("'" + fileTo.getName() + "' ---> FILE NAME ALREADY EXISTS INTO DATABASE. UPDATE DONE!");
			return ResponseEntity.status(204).build();
		} else if (response.equalsIgnoreCase("NO CHANGES")) {
			LOGGER.info("'" + fileTo.getName() + "' ---> FILE NAME WITH NO CHANGES INTO DATABASE.");
			return ResponseEntity.status(304).body(fileTo);
		} 

		LOGGER.severe("'" + fileTo.getName() + "' ---> FILE DOES NOT EXISTS INTO DATABASE. THE UPDATE WAS CANCELED!");
		return ResponseEntity.status(403).body(fileTo);		
	}

}
