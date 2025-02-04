package com.example.myschedule.converter;

import java.beans.PropertyEditorSupport;
public class TeacherDTOPropertyEditor extends PropertyEditorSupport {
    private final TeacherDTOConverter converter;
    public TeacherDTOPropertyEditor(TeacherDTOConverter converter) {
        this.converter = converter;
    }
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(converter.convert(text));
    }

}
