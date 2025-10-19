package com.algaworks.algaposts.textprocessorservice.api.config.web;

import io.hypersistence.tsid.TSID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTSIDConverter implements Converter<String, TSID> {

    @Override
    public TSID convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null; // Or throw an IllegalArgumentException if null/empty is not allowed
        }
        return TSID.from(source);
    }
}
