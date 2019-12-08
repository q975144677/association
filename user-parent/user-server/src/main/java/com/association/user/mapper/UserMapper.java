package com.association.user.mapper;

import com.associtaion.user.condition.ConditionForUser;
import com.associtaion.user.model.UserDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    List<UserDO> queryUsers(@Param("condition")ConditionForUser condition,@Param("limit")Integer limit , @Param("offset")Integer offset);

    Long countUsers(@Param("condition")ConditionForUser condition);

    Boolean createNewUser(@Param("condition")UserDO user);

    Boolean updateUser(@Param("condition")UserDO user);

    UserDO getUser(@Param("condition")ConditionForUser condition);

}
