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

import net.upflux.comparator.model.FileDiff;
import net.upflux.comparator.model.FileFrom;
import net.upflux.comparator.model.FileTo;
import net.upflux.comparator.service.FileDiffService;

@CrossOrigin("*")
@RestController
@RequestMapping("/file/v01/diff")
public class FileDiffController {

	private FileDiff fileDiff;
	
	@Autowired
    private FileDiffService fileDiffService;
	
	private Optional<FileFrom> fileFrom;
	
	private Optional<FileTo> fileTo;
	
	private static final Logger LOGGER = Logger.getLogger(FileDiffController.class.getPackage().getName());
	
	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<FileDiff> createFileDiff(@Valid @RequestBody FileDiff fileDiff) {
		
		String response = fileDiffService.verifyUpdate(fileDiff, "POST");
		
		if (response.equalsIgnoreCase("UPDATED")) {
			LOGGER.info("'" + fileDiff.getName() + "' ---> FILE NAME ALREADY EXISTS INTO DATABASE. UPDATING... DONE!");
			return ResponseEntity.status(204).build();
		} else if (response.equalsIgnoreCase("NO CHANGES")) {
			LOGGER.info("'" + fileDiff.getName() + "' ---> FILE NAME WITH NO CHANGES INTO DATABASE.");
			return ResponseEntity.status(304).body(fileDiff);
		} else if (response.equalsIgnoreCase("CREATED")) {
			LOGGER.info("'" + fileDiff.getName() + "' ---> FILE CREATED INTO DATABASE.");
			return ResponseEntity.status(201).body(fileDiff);
		}
		
		return ResponseEntity.badRequest().body(fileDiff);
	}
	
	@DeleteMapping("/delete/{name}")
	public ResponseEntity<FileDiff> deleteFileDiff(@PathVariable String name) {
		boolean deleted = fileDiffService.deleteFile(name);
		
		if (deleted)
			return ResponseEntity.ok().build();
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<FileDiff> readFileDiff(@PathVariable String name) {
		Optional<FileDiff> fileDiff = fileDiffService.readFile(name);
		
		if (fileDiff == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(fileDiff.get());
	}
	
	@GetMapping("/from/{name}")
	public ResponseEntity<FileDiff> readFileDiffFrom(@PathVariable String name) {
		return this.readFileDiffTo(name);
	}
	
	@GetMapping("/to/{name}")
	public ResponseEntity<FileDiff> readFileDiffTo(@PathVariable String name) {
		
		this.fileDiff = fileDiffService.verifyCompare(name, this.fileFrom, this.fileTo);
		
		if (this.fileDiff == null)
			return ResponseEntity.notFound().build();
			
		String response = fileDiffService.saveChangesOnDB();
			
		if (response.equalsIgnoreCase("UPDATED")) {
			return ResponseEntity.status(204).body(this.fileDiff);
		} else if (response.equalsIgnoreCase("NO CHANGES")) {
			return ResponseEntity.status(304).body(this.fileDiff);
		} else if (response.equalsIgnoreCase("CREATED")) {
			return ResponseEntity.status(201).body(this.fileDiff);
		}

		return ResponseEntity.badRequest().body(this.fileDiff);
		
	}
	
	@GetMapping
	public ResponseEntity<List<FileDiff>> readFilesDiff() {
		List<FileDiff> files = this.fileDiffService.readFiles();
		
		if (files == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(files);
	}
	
	@PutMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<FileDiff> updateFileDiff(@Valid @RequestBody FileDiff fileDiff) {
		
		String response = fileDiffService.verifyUpdate(fileDiff, "PUT");
		
		if (response.equalsIgnoreCase("UPDATED")) {
			LOGGER.info("'" + fileDiff.getName() + "' ---> FILE NAME ALREADY EXISTS INTO DATABASE. UPDATE DONE!");
			return ResponseEntity.status(204).build();
		} else if (response.equalsIgnoreCase("NO CHANGES")) {
			LOGGER.info("'" + fileDiff.getName() + "' ---> FILE NAME WITH NO CHANGES INTO DATABASE.");
			return ResponseEntity.status(304).body(fileDiff);
		} 

		LOGGER.severe("'" + fileDiff.getName() + "' ---> FILE DOES NOT EXISTS INTO DATABASE. THE UPDATE WAS CANCELED!");
		return ResponseEntity.status(403).body(fileDiff);		
	}

}
