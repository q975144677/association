package com.association.workflow.service.external;

import com.alibaba.fastjson.JSONObject;
import com.association.config.iface.ConfigIface;
import com.association.config.model.SchoolDTO;
import com.association.workflow.condition.ConditionForAssociation;
import com.association.workflow.condition.ConditionForAssociationUser;
import com.association.workflow.iface.AssociationIface;
import com.association.workflow.mapper.AssociationMapper;
import com.association.workflow.mapper.AssociationUserMapper;
import com.association.workflow.model.ActivityDO;
import com.association.workflow.model.AssociationDO;
import com.association.workflow.model.AssociationUserDO;
import com.association.user.Iface.UserIface;
import com.association.user.condition.ConditionForUser;
import com.association.user.model.UserDO;
import component.BasicComponent;
import component.PaginProto;
import component.Proto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AssociationServiceImpl extends BasicComponent implements AssociationIface {
    @Autowired
    AssociationMapper associationMapper;
    @Autowired
    AssociationUserMapper associationUserMapper;
    @Autowired
    UserIface userIface;
@Autowired
    ConfigIface configIface;
    @Override
    public Proto<Boolean> createNewAssociation(AssociationDO association) {
        SchoolDTO.School  school= configIface.schools().getData().getAreas().stream().flatMap(x->x.getSchools().stream()).filter(x->Integer.valueOf(x.getCode()).equals(association.getSchoolId())).findAny().orElse(null);
        if(school == null){
            return getResult(false);
        }
        association.setSchoolName(school.getName());
        return getResult(associationMapper.createNewAssociation(association));
    }

    @Override
    public Proto<Boolean> updateAssociation(AssociationDO associationDO) {
        return getResult(associationMapper.updateAssociation(associationDO));
    }
    public static void main(String[] args){
        ActivityDO activityDO = new ActivityDO();
        activityDO.setGuid("1");
        activityDO.setDetail("1");
        activityDO.setDelayReason("1");
        activityDO.setCancelReason("1");
        activityDO.setAssociationGuid("1");
        activityDO.setActivityStatus(1);
        activityDO.setSchoolId(1);
        activityDO.setReason("1");
        activityDO.setName("1");
        activityDO.setCreateTime(new Date());
        activityDO.setFromDate(new Date());
        activityDO.setToDate(new Date());
        System.out.println(JSONObject.toJSONString(activityDO));
    }

    @Override
    public Proto<List<UserDO>> getAssociationMates(ConditionForAssociation condition) {
        ConditionForAssociationUser conditionForAssociationUser = new ConditionForAssociationUser();
        conditionForAssociationUser.setAssociationGuid(condition.getGuid());
        List<AssociationUserDO> associationUserDOS = associationUserMapper.findAssociationUser(conditionForAssociationUser);
        List<String> userGuids = associationUserDOS.stream().map(au -> au.getUserGuid()).collect(Collectors.toList());
        ConditionForUser conditionForUser = new ConditionForUser();
        conditionForUser.setGuids(userGuids);
        return userIface.getUsers(conditionForUser);
    }

    @Override
    public PaginProto<List<AssociationDO>> queryAssociation(ConditionForAssociation condition) {
        condition.prepare(null);
        PaginProto.Page page = new PaginProto.Page();
        Long recordCount = associationMapper.countAssociation(condition);
        Integer pageCount = (int) Math.max(1, Math.ceil((double) recordCount / (double) condition.getPageSize()));
        page.setPageCount(pageCount);
        page.setPageIndex(condition.getPageIndex());
        page.setPageSize(condition.getPageSize());
        page.setRecordCount(Math.toIntExact(recordCount));
        return getPaginResult(associationMapper.queryAssociation(condition), page);
    }

    @Override
    public Proto<List<AssociationUserDO>> queryAssociationUser(ConditionForAssociationUser condition) {
        return getResult(associationUserMapper.findAssociationUser(condition));
    }

    @Override
    public Proto<AssociationDO> getAssociationSipmle(ConditionForAssociation condition) {
        AssociationDO associationDO = associationMapper.getAssociationSimple(condition);
        String leaderGuid = associationDO.getAssociationLeaderGuid();
        ConditionForUser conditionForUser = new ConditionForUser();
        conditionForUser.setGuid(leaderGuid);
        Proto<UserDO> userDOProto = userIface.getUser(conditionForUser);
        if (userDOProto.getCode() == HttpStatus.OK.value() && userDOProto.getData() != null) {
            associationDO.setLeaderName(userDOProto.getData().getName());
        }
        return getResult(associationDO);
    }

    @Override
    public Proto<Boolean> isLeader(ConditionForAssociation condition) {
//        AssociationDO associationDO = ;
        return getResult(associationMapper.getAssociationSimple(condition) != null);
    }

    @Override
    public Proto<Boolean> quitAssociation(AssociationUserDO associationUserDO) {
        return getResult(associationUserMapper.deleteRef(associationUserDO.getAssociationGuid(), associationUserDO.getUserGuid()));
    }
}
