package com.example.myschedule.converter;

import com.example.myschedule.entity.DayOfWeek;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DayOfWeekConverter implements Converter<String, DayOfWeek> {

    @Override
    public DayOfWeek convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("Day of the week is empty!");
        }

        try {
            int dayNumber = Integer.parseInt(source.trim());
            if (dayNumber >= 1 && dayNumber <= 7) {
                return DayOfWeek.values()[dayNumber - 1];
            }
        } catch (NumberFormatException e) {
            try {
                return DayOfWeek.valueOf(source.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid day of the week: " + source);
            }
        }

        throw new IllegalArgumentException("Invalid day of the week: " + source);
    }
}
