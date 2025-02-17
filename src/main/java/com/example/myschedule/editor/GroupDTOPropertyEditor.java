package com.example.myschedule.editor;

import com.example.myschedule.converter.GroupDTOConverter;

import java.beans.PropertyEditorSupport;

public class GroupDTOPropertyEditor extends PropertyEditorSupport {
    private final GroupDTOConverter converter;
    public GroupDTOPropertyEditor(GroupDTOConverter converter) {
        this.converter = converter;
    }
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(converter.convert(text));
    }

}
