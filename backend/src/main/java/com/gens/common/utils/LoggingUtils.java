package com.gens.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingUtils {

    @Autowired
    private ObjectMapper objectMapper;

    public String prettifyJson(Object object) {
        String json = StringUtils.EMPTY;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Exception captured: {}", e.getMessage());
            return null;
        }
        return json;
    }
}