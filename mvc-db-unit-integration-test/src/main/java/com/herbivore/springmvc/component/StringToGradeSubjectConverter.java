package com.herbivore.springmvc.component;

import com.herbivore.springmvc.model.Grade;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToGradeSubjectConverter implements Converter<String, Grade.Subject> {
	@Override
	public Grade.Subject convert(@NotNull String source) {
		return Enum.valueOf(
				Grade.Subject.class,
				source.trim().toUpperCase()
		);
	}
}