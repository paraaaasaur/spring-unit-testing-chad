package com.herbivore.springmvc.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollegeStudentDtoSerializationTest {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


	@Test
	void shouldSerializeOnlyExpectedFields() throws JsonProcessingException {
		var dto = new CollegeStudentDto(1, "Hello", "World", "hw@gmail.com");
		String json = OBJECT_MAPPER.writeValueAsString(dto);
		Map<String, Object> jsonMap = OBJECT_MAPPER.readValue(json, new TypeReference<>() {});

		assertTrue(jsonMap.containsKey("id"));
		assertTrue(jsonMap.containsKey("firstname"));
		assertTrue(jsonMap.containsKey("lastname"));
		assertTrue(jsonMap.containsKey("emailAddress"));
		assertEquals(4,  jsonMap.size(),
				CollegeStudentDto.class.getName() + " only serializes exactly 4 fields");
	}
}
