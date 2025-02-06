package com.example.myschedule.editor;

import com.example.myschedule.converter.StatusConverter;

import java.beans.PropertyEditorSupport;

public class StatusPropertyEditor extends PropertyEditorSupport {
    private StatusConverter converter;

    public StatusPropertyEditor(StatusConverter converter) {
        this.converter = converter;
    }
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(converter.convert(text));
    }
}
