package iob.logic.instances;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import iob.Location;

public class IobLocationToStringConverter implements AttributeConverter<Location, String> {
	private ObjectMapper jackson;

	public IobLocationToStringConverter() {
		this.jackson = new ObjectMapper();
	}

	@Override
	public String convertToDatabaseColumn(Location location) {
		try {
			return this.jackson.writeValueAsString(location);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Location convertToEntityAttribute(String str) {
		try {
			return this.jackson.readValue(str, Location.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
