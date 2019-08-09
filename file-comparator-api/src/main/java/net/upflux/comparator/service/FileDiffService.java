package net.upflux.comparator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.upflux.comparator.model.FileDiff;
import net.upflux.comparator.model.FileFrom;
import net.upflux.comparator.model.FileTo;
import net.upflux.comparator.repository.FilesDiffRepository;

@Service
public class FileDiffService {
	
	private FileDiff fileDiff;
	
	@Autowired
    private FileFromService fileFromService;
	
	@Autowired
    private FileToService fileToService;
	
	@Autowired
    private FilesDiffRepository filesDiffRepository;
	
	private ArrayList<String> jsonFiles;
	
	private static final Logger LOGGER = Logger.getLogger(FileDiffService.class.getPackage().getName());
	
	public void compare(String name, FileFrom fileFrom, FileTo fileTo) {
		
		this.getJsonFormat(fileFrom, fileTo);
		
//		TESTES
//		StringTokenizer stFileFrom = new StringTokenizer(this.jsonFiles.get(0), "\n");
//		StringTokenizer stFileTo = new StringTokenizer(this.jsonFiles.get(1), "\n");
//		String linhaFrom = "";
//		String linhaTo = "";
		
		Gson gson = new Gson();
		JsonObject jsonFilePropertiesFrom = gson.toJsonTree(new JsonParser().parse(this.jsonFiles.get(0))).getAsJsonObject();		
	    JsonObject jsonFilePropertiesTo = gson.toJsonTree(new JsonParser().parse(this.jsonFiles.get(1))).getAsJsonObject();
		
//	    TESTES
//	    for (Map.Entry<String, ?> jsonFilePropertyTo : jsonFilePropertiesTo.entrySet()) {
//	    	System.out.println("********** jsonFilePropertyTo.key = " + jsonFilePropertyTo.getKey());
//	    	System.out.println("********** jsonFilePropertyTo.value = " + jsonFilePropertyTo.getValue());	    	
//	    }
		
	    this.fileDiff = this.setDiffFile(jsonFilePropertiesFrom, jsonFilePropertiesTo, fileFrom);
	    
//	    TESTES
//		while ( stFileFrom.hasMoreTokens() || stFileTo.hasMoreTokens() ) {
//			if ( (stFileFrom != null) && (stFileTo != null) ) {
//				
//				linhaFrom = stFileFrom.nextToken();
//				linhaTo = stFileTo.nextToken();
//				
//				if (!linhaFrom.equalsIgnoreCase(linhaTo)) {
//					System.out.println(linhaFrom + " <---- EXISTS INTO JSON FILE on /v01/diff/from/" + fileFrom.getName());
//					System.out.println(linhaTo + " ----> EXISTS INTO JSON FILE on /v01/diff/to/" + fileTo.getName());
//				} else {
//					//O conteudo aqui neste else sao iguais, por isto tanto faz imprimir ou um ou o outro
//					System.out.println(linhaFrom);
//				}
//			} else if ( (stFileFrom != null) && (stFileTo == null) ) {
//				//O conteudo aqui neste else if apresenta dados apenas para o arquivo de origem em FROM
//				linhaFrom = stFileFrom.nextToken();
//				System.out.println(linhaFrom + " <---- EXISTS INTO JSON FILE on /v01/diff/from/" + fileFrom.getName());
//				System.out.println("[EMPTY LINE] ----> EXISTS INTO JSON FILE on /v01/diff/to/" + fileTo.getName());
//			} else {
//				//O conteudo aqui neste else apresenta dados apenas para o arquivo de origem em TO
//				linhaTo = stFileTo.nextToken();
//				System.out.println("[EMPTY LINE] ----> EXISTS INTO JSON FILE on /v01/diff/from/" + fileFrom.getName());
//				System.out.println(linhaTo + " ----> EXISTS INTO JSON FILE on /v01/diff/to/" + fileTo.getName());
//			}
//		}
	}
	
	public String saveChangesOnDB() {
		
		Optional<FileDiff> file = this.filesDiffRepository.findByName(this.fileDiff.getName().trim());
		
		if (file.isPresent()) {		
			if (file.get().equals(this.fileDiff))
				return "NO CHANGES";
			else {
				this.fileDiff.setId(file.get().getId());
				this.filesDiffRepository.save(this.fileDiff);
				
				return "UPDATED";
			}
		} else {			
			this.filesDiffRepository.save(this.fileDiff);
			
			Optional<FileDiff> newFile = this.filesDiffRepository.findByName(this.fileDiff.getName().trim());
			
			if (newFile.isPresent())
				return "CREATED";
		}
		
		return "PROBLEM";
	}
	
	public FileDiff setDiffFile(JsonObject jsonFilePropertiesFrom, JsonObject jsonFilePropertiesTo, FileFrom file) {
		
		Map<String, String> compareFrom = new HashMap<>();
		this.fileDiff = new FileDiff();
		
	    for (Map.Entry<String, ?> jsonFilePropertyFrom : jsonFilePropertiesFrom.entrySet())    	
	    	compareFrom.put(jsonFilePropertyFrom.getKey().toString(), jsonFilePropertyFrom.getValue().toString());
	    
	    String auditValueFrom = "";
	    String auditValueTo = "";
	    String sameValues = "";
		
	    for (Entry<String, JsonElement> jsonFilePropertyTo2 : jsonFilePropertiesTo.entrySet()) {
	    	
	    	jsonFilePropertiesFrom.entrySet().iterator().next().getKey();
	    	
	    	if (compareFrom.containsKey(jsonFilePropertyTo2.getKey())) {
	    		
	    		if ( (compareFrom.get(jsonFilePropertyTo2.getKey()) != null) && (jsonFilePropertyTo2.getValue() != null) ) {
	    			
	    			if (jsonFilePropertyTo2.getValue().toString().equalsIgnoreCase(compareFrom.get(jsonFilePropertyTo2.getKey()).toString())) {
	    				
	    				//Quando os conteudos sao iguais em FROM e TO, seta os atributos padroes
	    				sameValues = jsonFilePropertyTo2.getValue().toString().replaceAll("\"", "").replaceAll("\'", "");
		    			
	    				if (jsonFilePropertyTo2.getKey().toString().equalsIgnoreCase("NAME"))
	    					this.fileDiff.setName(sameValues);
	    				else if (jsonFilePropertyTo2.getKey().toString().equalsIgnoreCase("ID"))
		    				this.fileDiff.setId(sameValues);
	    				else if (jsonFilePropertyTo2.getKey().toString().equalsIgnoreCase("DATE"))
		    				this.fileDiff.setDate(sameValues);
		    			else
		    				this.fileDiff.setContent(sameValues);
		    			
		    		} else {
		    			
		    			//Quando os conteudos sao diferentes em FROM e TO, setamos os atributos customizados indicando as diferencas
		    			auditValueFrom = compareFrom.get(jsonFilePropertyTo2.getKey()).toString().replaceAll("\"", "").replaceAll("\'", "").concat(" <---- EXISTS INTO JSON FILE on /v01/diff/from/" + file.getName());
		    			auditValueTo = jsonFilePropertyTo2.getValue().toString().replaceAll("\"", "").replaceAll("\'", "").concat(" ----> EXISTS INTO JSON FILE on /v01/diff/to/" + file.getName());
		    			
		    			if (jsonFilePropertyTo2.getKey().toString().equalsIgnoreCase("NAME")) {
		    				this.fileDiff.setName_from(auditValueFrom);
		    				this.fileDiff.setName_to(auditValueTo);
		    			} else if (jsonFilePropertyTo2.getKey().toString().equalsIgnoreCase("ID")) {
		    				this.fileDiff.setId_from(auditValueFrom);
		    				this.fileDiff.setId_to(auditValueTo);
		    			} else if (jsonFilePropertyTo2.getKey().toString().equalsIgnoreCase("DATE")) {
		    				this.fileDiff.setDate_from(auditValueFrom);
		    				this.fileDiff.setDate_to(auditValueTo);
		    			} else {
		    				this.fileDiff.setContent_from(auditValueFrom);
		    				this.fileDiff.setContent_to(auditValueTo);
		    			}
		    			
		    		}
	    			
	    		}

	    	}
	    }
	    
//	    TESTES
//	    System.out.println(this.fileDiff.getId());
//	    System.out.println(this.fileDiff.getDate());
//	    System.out.println(this.fileDiff.getName());
//	    System.out.println(this.fileDiff.getContent());
//	    
//	    System.out.println(this.fileDiff.getId_from());
//	    System.out.println(this.fileDiff.getDate_from());
//	    System.out.println(this.fileDiff.getName_from());
//	    System.out.println(this.fileDiff.getContent_from());
//	    
//	    System.out.println(this.fileDiff.getId_to());
//	    System.out.println(this.fileDiff.getDate_to());
//	    System.out.println(this.fileDiff.getName_to());
//	    System.out.println(this.fileDiff.getContent_to());
	    
	    compareFrom.clear();
	    
	    return this.fileDiff;
	}
	
	public boolean deleteFile(String name) {	
		Optional<FileDiff> file = this.filesDiffRepository.findByName(name.trim());
		
		if (file.isPresent()) {		
			this.filesDiffRepository.deleteById(file.get().getId());
			return true;
		} else {
			return false;
		}
	}
	
	private void getJsonFormat(FileFrom fileFrom, FileTo fileTo) {
		
		try {    	
	    	this.jsonFiles = new ArrayList<String>();
	    	
	        Gson gson = new Gson();
	        Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
	        
	        //converte objetos Java para JSON e retorna JSON como String
	        String json = gson.toJson(fileFrom);
	        
	        String prettyJsonString = gsonPretty.toJson(new JsonParser().parse(json));
	        
	        this.jsonFiles.add(prettyJsonString);
	        
	        json = gson.toJson(fileTo);
	        
	        prettyJsonString = gsonPretty.toJson(new JsonParser().parse(json));
	        
	        this.jsonFiles.add(prettyJsonString);
		} catch (Exception e) {
			LOGGER.severe("'" + fileTo.getName() + "' ---> FILE ONLY ACCEPT THESE PROPERTIES: " + fileTo.getProperties());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
	
	public Optional<FileDiff> readFile(String name) {
		return (this.filesDiffRepository.findByName(name).isPresent() ? this.filesDiffRepository.findByName(name) : null);
	}
	
	public List<FileDiff> readFiles() {		
		return (this.filesDiffRepository.findAll().isEmpty() ? null : this.filesDiffRepository.findAll());
	}
	
	public FileDiff verifyCompare(String name, Optional<FileFrom> fileFrom, Optional<FileTo> fileTo) {
		
		fileTo = this.fileToService.readFile(name);
		
		if (fileTo != null) {
			fileFrom = this.fileFromService.readFile(name);
			
			if (fileFrom != null) {
				//So compara se existirem os dois arquivos com o mesmo nome em FROM e em TO
				this.compare(name, fileFrom.get(), fileTo.get());
				return this.fileDiff;
			}
		}
		
//		Se nao ha o que comparar, retorna null para enviar um 404 ao cliente	
		return null;
	}
	
	public String verifyUpdate(FileDiff fileDiff, String method) {		
		Optional<FileDiff> file = this.filesDiffRepository.findByName(fileDiff.getName().trim());
		
		if (file.isPresent()) {
			if (file.get().equals(fileDiff))
				return "NO CHANGES";
			else {
				fileDiff.setId(file.get().getId());
				this.filesDiffRepository.save(fileDiff);
				
				return "UPDATED";
			}
		} else {
			if (!method.equalsIgnoreCase("PUT")) {
				this.filesDiffRepository.save(fileDiff);
				
				Optional<FileDiff> newFile = this.filesDiffRepository.findByName(fileDiff.getName().trim());
				
				if (newFile.isPresent())
					return "CREATED";
			}
		}
		
		return "PROBLEM";
	}

}
