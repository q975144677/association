package com.association.workflow.iface;

import com.association.workflow.condition.ConditionForCategory;
import com.association.workflow.model.CategoryDO;
import component.Proto;

import java.util.List;

//todo 不重要
public interface CategoryIface {

    /**
     * 增加分类
     */
    Proto<Boolean> createNewCategory(CategoryDO category);

    /**
     * 删除分类
     */
    Proto<Boolean> deleteNewCategory(CategoryDO category);

    /**
     * 查找分类
     * 数量不多 不分页
     */
    Proto<List<CategoryDO>> queryCategory(ConditionForCategory condition);

    /**
     * 修改分类名
     */
    Proto<Boolean> updateCategory(CategoryDO category);
}
