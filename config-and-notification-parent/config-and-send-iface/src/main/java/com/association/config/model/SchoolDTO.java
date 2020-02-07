package com.association.config.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class SchoolDTO {
    public List<Area> areas ;
    @Setter
    @Getter
    private static SchoolDTO instance ;
    @Data
    public  static class Area{
        private int code ;
        private String name ;
        private List<School> schools;
    }
    @Data
    public static class School{
        private int code ;
        private String name ;
    }
}
