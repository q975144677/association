package com.association.workflow.service.external;

import com.association.workflow.condition.ConditionForCategory;
import com.association.workflow.iface.CategoryIface;
import com.association.workflow.model.CategoryDO;
import component.Proto;

import java.util.List;

//从阿波罗上拉
public class CategoryServiceImpl implements CategoryIface {
    @Deprecated
    @Override
    public Proto<Boolean> createNewCategory(CategoryDO category) {
        return null;
    }

    @Deprecated
    @Override
    public Proto<Boolean> deleteNewCategory(CategoryDO category) {
        return null;
    }

    @Override
    public Proto<List<CategoryDO>> queryCategory(ConditionForCategory condition) {
        return null;
    }

    @Deprecated
    @Override
    public Proto<Boolean> updateCategory(CategoryDO category) {
        return null;
    }
}
