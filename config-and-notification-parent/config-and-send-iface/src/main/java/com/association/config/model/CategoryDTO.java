package com.association.config.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Data
public class CategoryDTO {
    List<Category> categories;
    @Getter
    @Setter
    public static CategoryDTO instance;
    @Data
    public static class Category{
        int code ;
        String name ;
    }
}
