package com.endava.petstore.converter;

import com.endava.petstore.enums.PetStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToPetStatusConverter implements Converter<String, PetStatus> {

    @Override
    public PetStatus convert(String source) {
        return PetStatus.valueOf(source);
    }
}
