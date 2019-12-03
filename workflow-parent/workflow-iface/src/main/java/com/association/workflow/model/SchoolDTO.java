package com.association.workflow.model;

import lombok.Data;

import java.util.List;

@Data
public class SchoolDTO {
    private List<School> schools ;
    private static SchoolDTO instance ;
    @Data
    public static class School{
        private int code ;
        private String name ;
    }
}
