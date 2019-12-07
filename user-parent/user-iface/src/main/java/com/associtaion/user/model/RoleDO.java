package com.associtaion.user.model;

import com.association.common.all.util.log.ILogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.List;
import java.util.logging.Logger;

@Data
public class RoleDO {
    private String guid ;
    private String name ;
    private List<String> auths;
    private String authJson;

    private void setAuthByList(List<String> authIds){
        ILogger logger = new ILogger();
        ObjectMapper mapper = new ObjectMapper();
        try {
            authJson = mapper.writeValueAsString(authIds);
        } catch (JsonProcessingException e) {
//            e.printStackTrace();
            logger.error("parse authIds to String error : {}" , e .getMessage());
        }
    }

}
