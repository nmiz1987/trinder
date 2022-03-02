package iob.logic.activities;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import iob.ActivityId;

public class IobActivityIdToStringConverter implements AttributeConverter<ActivityId, String> {
	private ObjectMapper jackson;

	public IobActivityIdToStringConverter() {
		this.jackson = new ObjectMapper();
	}

	@Override
	public String convertToDatabaseColumn(ActivityId activityId) {
		try {
			return this.jackson.writeValueAsString(activityId);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ActivityId convertToEntityAttribute(String str) {
		try {
			return this.jackson.readValue(str, ActivityId.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
