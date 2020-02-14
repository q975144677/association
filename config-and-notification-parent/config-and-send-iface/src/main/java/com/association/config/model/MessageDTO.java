package com.association.config.model;

import lombok.Data;

import java.util.List;

@Data
public class MessageDTO {
    private String from ;
    private String to ;
    private Integer msgType;
    private String content ;
    private String guid;
    private List<String> toGuids;
    private String fromName ;
    private String avatar ;
    Long timestamp = System.currentTimeMillis();
}
