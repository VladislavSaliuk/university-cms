package com.example.myschedule.converter;

import com.example.myschedule.entity.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StatusConverter implements Converter<String, Status> {
    @Override
    public Status convert(String source) {
        if (source == null) {
            return null;
        }

        for (Status status : Status.values()) {
            if (status.getStatusName().equalsIgnoreCase(source)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Unknown status: " + source);
    }
}
