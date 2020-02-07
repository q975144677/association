package com.association.workflow.service.external;

import com.alibaba.fastjson.JSONObject;
import com.association.config.iface.ConfigIface;
import com.association.workflow.condition.ConditionForApprove;
import com.association.workflow.enumerations.EnumForApproveStatus;
import com.association.workflow.enumerations.EnumForApproveType;
import com.association.workflow.enumerations.EnumForReidsKey;
import com.association.workflow.iface.ApproveIface;
import com.association.workflow.iface.AssociationIface;
import com.association.workflow.mapper.ApproveMapper;
import com.association.workflow.mapper.AssociationMapper;
import com.association.workflow.mapper.AssociationUserMapper;
import com.association.workflow.model.ActivityDO;
import com.association.workflow.model.ApproveDO;
import com.association.workflow.model.AssociationDO;
import com.association.workflow.model.AssociationUserDO;
import com.association.workflow.util.RedisLock;
import component.BasicComponent;
import component.PaginProto;
import component.Proto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ApproveServiceImpl extends BasicComponent implements ApproveIface {

    @Autowired
    ApproveMapper approveMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    AssociationUserMapper associationUserMapper;
    @Autowired
    ActivityServiceImpl activityService;
    @Autowired
    AssociationIface associationIface;
    @Autowired
    AssociationMapper associationMapper;
    @Autowired
    ConfigIface configIface;

    @Override
    @Transactional
    public Proto<Boolean> updateApprove(ApproveDO approve) {
        //todo 加入社团或者活动
        Integer toStatus = approve.getApproveStatus();
        String key = String.format(EnumForReidsKey.APPROVE_LOCK.getPattern(), approve.getGuid());
        RedisLock redisLock = new RedisLock(redisTemplate, key, 5);
        if (!redisLock.lock()) {
            return fail("有人正在操作了");
        }
        approve = approveMapper.getApprove(approve.getGuid());
        if (!Arrays.stream(new Integer[]{EnumForApproveStatus.SUCCESS.getCode(), EnumForApproveStatus.FAILURE.getCode()}).collect(Collectors.toList()).contains(toStatus)) {
            redisLock.unLock();
            return getResult(false);
        }
        Boolean result = Boolean.TRUE;
        approve.setApproveStatus(toStatus);
        if (Integer.valueOf(EnumForApproveStatus.SUCCESS.getCode()).equals(toStatus)) {
            if (Integer.valueOf(EnumForApproveType.JOIN_ASSOCIATION.getCode()).equals(approve.getApproveType())) {
                String approveUserGuid = approve.getCreateUserGuid();
//                String associationUserGuid = approve.getDetail();
                String associatonGuid = approve.getApproveGuid();
                AssociationUserDO auDo = new AssociationUserDO();
                auDo.setUserGuid(approveUserGuid);
                auDo.setAssociationGuid(associatonGuid);
                Boolean res = associationUserMapper.save(auDo);
                result = res;
            }
            if (Integer.valueOf(EnumForApproveType.HOLD_ACTIVITY.getCode()).equals(approve.getApproveType())) {
                //todo 插入活动
                ActivityDO activityDO = new ActivityDO();
                ApproveDO.DetailForCreateActivity createActivity = JSONObject.parseObject(approve.getDetail(), ApproveDO.DetailForCreateActivity.class);
                activityDO = createActivity.getActivityDO();
                activityService.createNewActivity(activityDO);
            }
            if (Integer.valueOf(EnumForApproveType.CREATE_ASSOCIATION.getCode()).equals(approve.getApproveType())) {
                //todo 新建社团
                String guid = UUID.randomUUID().toString().replaceAll("-","");
                AssociationDO associationDO = new AssociationDO();
                associationDO.setCreateUserGuid(approve.getCreateUserGuid());
                associationDO.setAssociationLeaderGuid(approve.getCreateUserGuid());
                ApproveDO.DetailForCreateAssociation createAssociation = JSONObject.parseObject(approve.getDetail(), ApproveDO.DetailForCreateAssociation.class);
                associationDO.setName(createAssociation.getAssociationName());
                associationDO.setSchoolId(approve.getSchoolId());
//                associationDO.setSchoolName(approve.getSchoolName());
                associationDO.setGuid(guid);
                Boolean res = associationMapper.createNewAssociation(associationDO);
//                Proto<Boolean> proto  = associationIface.createNewAssociation(associationDO);
               if(res){
                   AssociationUserDO associationUserDO = new AssociationUserDO();
                   associationUserDO.setAssociationGuid(guid);
                   associationUserDO.setUserGuid(approve.getCreateUserGuid());
                   Boolean resu = associationUserMapper.save(associationUserDO);
                    result = resu ;
               }else{
                   return fail("创建失败");
               }
            }
        }
        if (result) {
            result = approveMapper.updateApprove(approve);
        }
        redisLock.unLock();
        return getResult(result);
    }

    @Override
    public PaginProto<List<ApproveDO>> queryApprove(ConditionForApprove condition) {
        condition.prepare(null);
        Long recordCount = approveMapper.countApprove(condition);
        return getPaginResult(approveMapper.queryApproveList(condition), condition.prepare(recordCount));
    }

    @Override
    public Proto<Boolean> addApprove(ApproveDO approve) {
        return getResult(approveMapper.createNewApprove(approve));
    }
}
