package com.example.myschedule.converter;

import java.beans.PropertyEditorSupport;

public class DayOfWeekPropertyEditor extends PropertyEditorSupport {
    private DayOfWeekConverter converter;
    public DayOfWeekPropertyEditor(DayOfWeekConverter converter) {
        this.converter = converter;
    }
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(converter.convert(text));
    }

}
