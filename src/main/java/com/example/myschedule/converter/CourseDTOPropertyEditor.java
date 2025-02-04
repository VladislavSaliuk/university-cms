package com.example.myschedule.converter;

import java.beans.PropertyEditorSupport;

public class CourseDTOPropertyEditor extends PropertyEditorSupport {
    private final CourseDTOConverter converter;
    public CourseDTOPropertyEditor(CourseDTOConverter converter) {
        this.converter = converter;
    }
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(converter.convert(text));
    }

}
