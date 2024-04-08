package com.gens.common.configs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gens.common.constants.Constants;
import com.gens.common.model.GlobalTokenVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class GlobalTokenDecoder {

    public GlobalTokenVM parseToken(String idToken, String securityType) throws JsonProcessingException {
        log.debug("Parsing idToken =>");
        String[] splitToken = idToken.split("\\.");
        byte[] decodedBytes = Base64.getDecoder().decode(splitToken[1]);
        String payload = new String(decodedBytes, StandardCharsets.UTF_8);
        return createGlobalTokenVMObject(payload, securityType);
    }

    private GlobalTokenVM createGlobalTokenVMObject(String payload, String securityType) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        GlobalTokenVM globalTokenVM = new GlobalTokenVM();
        if (securityType.equalsIgnoreCase(Constants.INTERNAL)) {
            log.debug("Security: internal");
            globalTokenVM = objectMapper.readValue(payload, GlobalTokenVM.class);
        }
        return globalTokenVM;
    }
}