package com.example.myschedule.converter;

import java.beans.PropertyEditorSupport;

public class ClassroomDTOPropertyEditor extends PropertyEditorSupport {
    private final ClassroomDTOConverter converter;
    public ClassroomDTOPropertyEditor(ClassroomDTOConverter converter) {
        this.converter = converter;
    }
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(converter.convert(text));
    }

}
