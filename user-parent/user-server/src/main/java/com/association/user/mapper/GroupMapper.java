package com.association.user.mapper;

import java.util.List;

public interface GroupMapper {

    List<String> getUserGuidsByGroupGuid(String guid);
}
