package net.upflux.comparator.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Document(collection = "diff-collection")
@Data
public class FileDiff implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Id
    private String id;
    private String id_from;
    private String id_to;

    private String date;
    private String date_from;
    private String date_to;

	@NotEmpty
	private String name;
	private String name_from;
	private String name_to;

	private String content;
	private String content_from;
	private String content_to;
    
    public FileDiff() {
    	
    }
    
    public FileDiff(String name, String content) {
        this.name = name;
        this.content = content;
    }
    
    @BsonIgnore
    @JsonIgnore
    public String getProperties() {
    	return "content [optional], content_from [optional], content_to [optional], date [optional], date_from [optional], "
    			+ "date_to [optional], id [optional], id_from [optional], id_to [optional], name [required], name_from [optional], name_to [optional]";
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileDiff other = (FileDiff) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (content_from == null) {
			if (other.content_from != null)
				return false;
		} else if (!content_from.equals(other.content_from))
			return false;
		if (content_to == null) {
			if (other.content_to != null)
				return false;
		} else if (!content_to.equals(other.content_to))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (date_from == null) {
			if (other.date_from != null)
				return false;
		} else if (!date_from.equals(other.date_from))
			return false;
		if (date_to == null) {
			if (other.date_to != null)
				return false;
		} else if (!date_to.equals(other.date_to))
			return false;
		if (id_from == null) {
			if (other.id_from != null)
				return false;
		} else if (!id_from.equals(other.id_from))
			return false;
		if (id_to == null) {
			if (other.id_to != null)
				return false;
		} else if (!id_to.equals(other.id_to))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (name_from == null) {
			if (other.name_from != null)
				return false;
		} else if (!name_from.equals(other.name_from))
			return false;
		if (name_to == null) {
			if (other.name_to != null)
				return false;
		} else if (!name_to.equals(other.name_to))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((content_from == null) ? 0 : content_from.hashCode());
		result = prime * result + ((content_to == null) ? 0 : content_to.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((date_from == null) ? 0 : date_from.hashCode());
		result = prime * result + ((date_to == null) ? 0 : date_to.hashCode());
		result = prime * result + ((id_from == null) ? 0 : id_from.hashCode());
		result = prime * result + ((id_to == null) ? 0 : id_to.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((name_from == null) ? 0 : name_from.hashCode());
		result = prime * result + ((name_to == null) ? 0 : name_to.hashCode());
		return result;
	}

}
