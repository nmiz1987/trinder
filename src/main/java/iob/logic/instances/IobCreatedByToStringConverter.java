package iob.logic.instances;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import iob.CreatedBy;

public class IobCreatedByToStringConverter implements AttributeConverter<CreatedBy, String> {
	private ObjectMapper jackson;

	public IobCreatedByToStringConverter() {
		this.jackson = new ObjectMapper();
	}

	@Override
	public String convertToDatabaseColumn(CreatedBy createdBy) {
		try {
			return this.jackson.writeValueAsString(createdBy);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public CreatedBy convertToEntityAttribute(String str) {
		try {
			return this.jackson.readValue(str, CreatedBy.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
