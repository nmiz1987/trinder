package iob.logic.instances;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import iob.InstanceId;

public class IobInctanceIdToStringConverter implements AttributeConverter<InstanceId, String> {
	private ObjectMapper jackson;

	public IobInctanceIdToStringConverter() {
		this.jackson = new ObjectMapper();
	}

	@Override
	public String convertToDatabaseColumn(InstanceId instanceId) {
		try {
			return this.jackson.writeValueAsString(instanceId);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public InstanceId convertToEntityAttribute(String str) {
		try {
			return this.jackson.readValue(str, InstanceId.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
