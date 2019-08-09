package net.upflux.comparator.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Document(collection = "to-collection")
@Data
public class FileTo implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Id
    private String id;

	private String date;

	@NotEmpty
    private String name;

    private String content;
    
    @Override
    public String toString() {
    	return "name = '" + this.name + "', content = '" + this.content + "', date = " + this.date;
    }
    
    public FileTo(String name, String content) {
        this.name = name;
        this.content = content;
    }
    
    @BsonIgnore
    @JsonIgnore
    public String getProperties() {
    	return "content [optional], date [optional], id [optional], name [required]";
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileTo other = (FileTo) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

}
