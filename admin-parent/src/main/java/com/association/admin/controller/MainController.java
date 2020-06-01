package com.association.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.association.admin.component.RedisUtil;
import com.association.common.all.util.log.ILogger;
import com.association.common.all.util.log.OSSHelper;
import com.association.config.iface.ConfigIface;
import com.association.config.model.SchoolDTO;
import com.association.user.model.UserDO;
import com.association.workflow.condition.*;
import com.association.workflow.enumerations.EnumForActivityStatus;
import com.association.workflow.enumerations.EnumForApproveStatus;
import com.association.workflow.enumerations.EnumForApproveType;
import com.association.workflow.iface.ActivityIface;
import com.association.workflow.iface.ApproveIface;
import com.association.workflow.iface.AssociationIface;
import com.association.workflow.model.*;
import com.association.user.Iface.UserIface;
import component.BasicComponent;
import component.HttpRequestException;
import component.PaginProto;
import component.Proto;
import jdk.nashorn.internal.runtime.PrototypeObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.Association;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.stream.Collectors;

@RestController
@RequestMapping("main")
public class MainController extends BasicComponent {
    @Autowired
    ActivityIface activityIface;
    @Autowired
    AssociationIface associationIface;
    @Autowired
    ApproveIface approveIface;
    @Autowired
    RedisUtil redis;
    @Autowired
    UserIface userIface;
    @Autowired
    ILogger logger;
    @Autowired
    ConfigIface configIface;

    public static final int time = 5 * 60;

    @PostMapping("activities")
    public Proto<?> activities(@RequestBody ConditionForActivity condition) {
        return getResult(activityIface.queryActivity(condition));
    }

    @PostMapping("activityUsers")
    public Proto<?> activityUsers(@RequestBody ConditionForActivity condition) {
        return getResult(activityIface.queryActivityUsers(condition));
    }

    @PostMapping("associations")
    public Proto<?> associations(@RequestBody ConditionForAssociation condition) {
        return getResult(associationIface.queryAssociation(condition));
    }

    @PostMapping("associationUsers")
    public Proto<?> associationUsers(@RequestBody ConditionForAssociation condition) {
        return getResult(associationIface.getAssociationMates(condition));
    }

    @PostMapping("createNewActivity")
    public Proto<?> createNewActivity(@RequestBody ActivityDO activityDO, HttpServletRequest request) {

        Operator operator = getOperator(request);
        activityDO.setActivityStatus(EnumForActivityStatus.STARTING.getCode());
        activityDO.setUpdateUserGuid(operator.getGuid());
        activityDO.setUpdateUserName(operator.getName());
        activityDO.setCreateUserGuid(operator.getGuid());
        activityDO.setCreateUserName(operator.getName());
        return getResult(activityIface.createNewActivity(activityDO));
    }
@PostMapping("finishActivity")
public Proto<Boolean> finishActivity(@RequestBody ActivityDO activityDO){
        if(StringUtils.isEmpty(activityDO.getGuid())){
            throwDefaultHttpRequestException();
        }
        activityDO.setActivityStatus(EnumForActivityStatus.END.getCode());
        return activityIface.updateActivity(activityDO);
}
    @PostMapping("updateActivity")
    public Proto<?> updateActivity(@RequestBody ActivityDO activityDO, HttpServletRequest request) {
        if (StringUtils.isEmpty(activityDO.getGuid())) {
            throw new HttpRequestException(HttpRequestException.PARAM_ERROR, HttpStatus.OK.value());
        }
        Operator operator = getOperator(request);
        activityDO.setUpdateUserGuid(operator.getGuid());
        activityDO.setUpdateUserName(operator.getName());
        return getResult(activityIface.updateActivity(activityDO));
    }

    @PostMapping("createAssociation")
    public Proto<?> createAssociation(@RequestBody AssociationDO associationDO, HttpServletRequest request) {
        Operator operator = getOperator(request);
        associationDO.setCreateUserGuid(operator.getGuid());
        associationDO.setCreateUserName(operator.getName());
        Proto<SchoolDTO> proto = configIface.schools();
        if (proto == null || proto.getData() == null) {
            return getResult(Boolean.FALSE);
        }
        SchoolDTO schoolDTO = proto.getData();
        if (CollectionUtils.isEmpty(schoolDTO.getAreas())) {
            return getResult(Boolean.FALSE);
        }
        schoolDTO.getAreas().stream().flatMap(area -> area.getSchools().stream()).forEach(school -> {
                    if (Integer.valueOf(school.getCode()).equals(associationDO.getSchoolId())) {
                        associationDO.setSchoolName(school.getName());
                    }
                }
        );
        return getResult(associationIface.createNewAssociation(associationDO));
    }

    //创建社团 approveType 传 5
    @PostMapping("addApprove")
    public Proto<Boolean> addApprove(@RequestBody ApproveDO approveDO, HttpServletRequest request) {
//        System.out.println("****************************" + JSONObject.toJSONString(approveDO));
        Operator operator = getOperator(request);
        approveDO.setCreateUserGuid(operator.getGuid());
        approveDO.setCreateUserName(operator.getName());
        approveDO.setApproveStatus(EnumForApproveStatus.START.getCode());
        approveDO.setSchoolId(operator.getSchoolId());
        approveDO.setSchoolName(operator.getSchoolName());
        return getResult(approveIface.addApprove(approveDO));
    }

    // type 5
    @PostMapping("queryApprove")
    public Proto<List<ApproveDO>> queryApprove(@RequestBody ConditionForApprove condition, HttpServletRequest request) {
        Operator operator = new Operator();
        operator.getRoleGuid();
        // todo 校验role 是否为admin / 教师
        //    EnumForApproveType
        condition.setSchoolId(operator.getSchoolId());
        if (Integer.valueOf(EnumForApproveType.CREATE_ASSOCIATION.getCode()).equals(condition.getApproveType())) {
            return getResult(approveIface.queryApprove(condition));
        }

        return fail("未知类型");
    }

    @PostMapping("queryApproveNew")
    public PaginProto<List<ApproveDO>> queryApproveNew(@RequestBody ConditionForApprove condition, HttpServletRequest request) {
        Operator operator = getOperator(request);
        List<ApproveDO> approveDOS = new ArrayList<>();
        if ("1234".equals(operator.getRoleGuid())) { // 应当由 user 服务提供
            // 判断是否建社申请 or 入社申请
            //判断是否社长
            ConditionForAssociation conditionForAssociation = new ConditionForAssociation();
            conditionForAssociation.setAssociationLeaderGuid(operator.getGuid());
            conditionForAssociation.setPageIndex(1);
            conditionForAssociation.setPageSize(100);
            PaginProto<List<AssociationDO>> paginForAssociation = associationIface.queryAssociation(conditionForAssociation);
            if (paginForAssociation != null && paginForAssociation.getData() != null) {
                List<AssociationDO> res = paginForAssociation.getData();
                if (CollectionUtils.isEmpty(res)) {
                    // 非社长
                    condition.setCreateUserGuid(operator.getGuid());
                    PaginProto<List<ApproveDO>> paginProto = approveIface.queryApprove(condition);
                    return paginProto;
                } else {
                    if (Integer.valueOf(EnumForApproveType.CREATE_ASSOCIATION.getCode()).equals(condition.getApproveType())) {
                        // 建社
                        condition.setCreateUserGuid(operator.getGuid());
                        PaginProto<List<ApproveDO>> paginProto = approveIface.queryApprove(condition);
                        return paginProto;
                    } else if (Integer.valueOf(EnumForApproveType.JOIN_ASSOCIATION.getCode()).equals(condition.getApproveType())) {
                        condition.setApproveGuids(res.stream().map(associationDO -> associationDO.getGuid()).collect(Collectors.toList()));
                        return approveIface.queryApprove(condition);
                    }
                }
            }

        }
        if ("2234".equals(operator.getRoleGuid())) { // 应当由 user 服务提供
            if (Integer.valueOf(EnumForApproveType.CREATE_ASSOCIATION.getCode()).equals(condition.getApproveType())) {
                condition.setSchoolId(condition.getSchoolId());
                return approveIface.queryApprove(condition);
            }
        }

        if ("4234".equals(operator.getRoleGuid())) {
            return approveIface.queryApprove(condition);
        }

        return paginFail();
    }

    @PostMapping("checkLeader")
    public Proto<Boolean> checkLeader(HttpServletRequest request) {
        Operator operator = getOperator(request);
        ConditionForAssociation condition = new ConditionForAssociation();
        condition.setAssociationLeaderGuid(operator.getGuid());
        condition.setPageIndex(1);
        condition.setPageSize(1);
        Proto<List<AssociationDO>> proto = associationIface.queryAssociation(condition);
        if (proto == null || CollectionUtils.isEmpty(proto.getData())) {
            return getResult(Boolean.FALSE);
        }
        return getResult(Boolean.TRUE);
    }

    @PostMapping("updateApprove")
    public Proto<Boolean> updateApprove(@RequestBody ApproveDO approveDO) {
//EnumForApproveStatus
        return getResult(approveIface.updateApprove(approveDO));
    }


    @PostMapping("queryAssociation")
    public PaginProto<List<AssociationDO>> queryAssociation(@RequestBody ConditionForAssociation condition, HttpServletRequest request) {
        Operator operator = getOperator(request);
        condition.setSchoolId(operator.getSchoolId());
        return associationIface.queryAssociation(condition);
    }

    @PostMapping("queryMyJoinApprove")
    // 1. approving
    // 2. pass
    public Proto<JSONObject> queryMyJoinApprove(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        Operator operator = getOperator(request);
        ConditionForApprove condition = new ConditionForApprove();
        condition.setCreateUserGuid(operator.getGuid());
        condition.setApproveStatus(EnumForApproveStatus.START.getCode());
        condition.setApproveType(EnumForApproveType.JOIN_ASSOCIATION.getCode());
        condition.setPageIndex(1);
        condition.setPageSize(1000);
        PaginProto<List<ApproveDO>> paginProto = approveIface.queryApprove(condition);
        if (paginProto != null && paginProto.getData() != null) {
            List<ApproveDO> approves = paginProto.getData();
            List<String> approving = approves.stream().map(approveDO -> approveDO.getApproveGuid()).collect(Collectors.toList());
            result.put("approving", approving);
        }

        ConditionForAssociationUser conditionForAssociationUser = new ConditionForAssociationUser();
        conditionForAssociationUser.setUserGuid(operator.getGuid());
        Proto<List<AssociationUserDO>> proto = associationIface.queryAssociationUser(conditionForAssociationUser);
        if (proto != null && proto.getData() != null) {
            List<AssociationUserDO> associationUserDOS = proto.getData();
            List<String> passedAssociationGuids = associationUserDOS.stream().map(asud -> asud.getAssociationGuid()).collect(Collectors.toList());
            result.put("pass", passedAssociationGuids);
        }
        return getResult(result);
    }

    @PostMapping("joinAssociation")
    public Proto<Boolean> joinAssociation(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String associationGuid = map.get("associationGuid");
        if (StringUtils.isEmpty(associationGuid)) {
            return getResult(false);
        }
        Operator operator = getOperator(request);
        ApproveDO approveDO = new ApproveDO();
        approveDO.setCreateUserGuid(operator.getGuid());
        approveDO.setCreateUserName(operator.getName());
        approveDO.setApproveGuid(associationGuid);
        approveDO.setApproveStatus(EnumForApproveStatus.START.getCode());
        approveDO.setApproveType(EnumForApproveType.JOIN_ASSOCIATION.getCode());
        ApproveDO.DetailForJoinAssociation detail = new ApproveDO.DetailForJoinAssociation();
        detail.setAssociationGuid(approveDO.getApproveGuid());
        ConditionForAssociation conditionForAssociation = new ConditionForAssociation();
        conditionForAssociation.setGuid(associationGuid);
        conditionForAssociation.setPageIndex(1);
        conditionForAssociation.setPageSize(1);
        Proto<List<AssociationDO>> proto = associationIface.queryAssociation(conditionForAssociation);
        if (proto == null && CollectionUtils.isEmpty(proto.getData())) {
            return getResult(false);
        }
        detail.setAssociationName(proto.getData().get(0).getName());
        return approveIface.addApprove(approveDO);
    }

    @PostMapping("getJoinAssociation")
    public Proto<List<AssociationDO>> getJoinAssociation(HttpServletRequest request) {
        Operator operator = getOperator(request);
        ConditionForAssociationUser condition = new ConditionForAssociationUser();
        condition.setUserGuid(operator.getGuid());
        Proto<List<AssociationUserDO>> protoForAU = associationIface.queryAssociationUser(condition);
        if (protoForAU != null && !CollectionUtils.isEmpty(protoForAU.getData())) {
            List<String> asIds = protoForAU.getData().stream().map(associationUserDO -> associationUserDO.getAssociationGuid()).collect(Collectors.toList());
            ConditionForAssociation conditionForAssociation = new ConditionForAssociation();
            conditionForAssociation.setAssociationGuids(asIds);
            Proto<List<AssociationDO>> proto = associationIface.queryAssociation(conditionForAssociation);
            return proto;
        }
        return getResult(Collections.EMPTY_LIST);
    }


    @PostMapping("changeAssociationDetail")
    public Proto<Boolean> changeAssociationDetail(@RequestBody AssociationDO associationDO, HttpServletRequest request) {
        if (StringUtils.isEmpty(associationDO.getGuid())) {
            throw new HttpRequestException("PARAM ERROR ", HttpStatus.OK.value());
        }
        Operator operator = getOperator(request);
        ConditionForAssociation condition = new ConditionForAssociation();
        condition.setGuid(associationDO.getGuid());
        Proto<AssociationDO> protoAssociation = associationIface.getAssociationSipmle(condition);
        AssociationDO association = protoAssociation.getData();
        if (StringUtils.isEmpty(association.getAssociationLeaderGuid()) || !association.getAssociationLeaderGuid().equals(operator.getGuid())) {
            return getResult(Boolean.FALSE);
        }
        return getResult(associationIface.updateAssociation(associationDO));
    }

    @PostMapping("associationMates")
    public Proto<List<UserDO>> associationMates(@RequestBody ConditionForAssociationUser condition) {
        if (StringUtils.isEmpty(condition.getAssociationGuid())) {
            throwDefaultHttpRequestException();
        }
        ConditionForAssociation conditionForAssociation = new ConditionForAssociation();
        conditionForAssociation.setGuid(condition.getGuid());
        return associationIface.getAssociationMates(conditionForAssociation);
    }

    @PostMapping("createActivity")
    public Proto<Boolean> createActivity(@RequestBody ActivityDO activityDO, HttpServletRequest request) {
        logger.info("JSON ACTIVITY : {} ", JSONObject.toJSONString(activityDO));
        Operator operator = getOperator(request);
        activityDO.setCreateUserGuid(operator.getGuid());
        activityDO.setCreateUserName(operator.getName());
        return getResult(activityIface.createNewActivity(activityDO));
    }

    @PostMapping("queryAssociationUser")
    public Proto<?> queryAssociationUser(@RequestBody ConditionForAssociationUser condition) {
        return getResult(associationIface.queryAssociationUser(condition));
    }

    @PostMapping("queryActivityUser")
    public Proto<?> queryActivityUser(@RequestBody ConditionForActivity condition) {
        return getResult(activityIface.queryActivityUsers(condition));
    }

    @PostMapping("quitAssociation")
    public Proto<Boolean> quitAssociation(@RequestBody AssociationUserDO associationUserDO, HttpServletRequest request) {
        Operator operator = getOperator(request);
        ConditionForAssociation condition = new ConditionForAssociation();
        condition.setAssociationLeaderGuid(operator.getGuid());
        Proto<List<AssociationDO>> proto = associationIface.queryAssociation(condition);
        if (!CollectionUtils.isEmpty(proto.getData())) {
            List<String> guids = proto.getData().stream().map(associationDO -> associationDO.getGuid()).collect(Collectors.toList());
            if (guids.contains(associationUserDO.getAssociationGuid())) {
                return fail("作为社长，不能退社");
            }
        }
        associationUserDO.setUserGuid(operator.getGuid());
        return getResult(associationIface.quitAssociation(associationUserDO));
    }

    @PostMapping("isLeader")
    public Proto<Boolean> isLeader(@RequestBody ConditionForAssociation condition, HttpServletRequest request) {
        Operator operator = getOperator(request);
        condition.setAssociationLeaderGuid(operator.getGuid());
        return associationIface.isLeader(condition);
    }

    @PostMapping("quitActivity")
    public Proto<Boolean> quitActivity(@RequestBody ConditionForActivityUser activityUserDO, HttpServletRequest request) {
        Operator operator = getOperator(request);
        activityUserDO.setUserGuid(operator.getGuid());
        return getResult(activityIface.quitActivity(activityUserDO));
    }

    @PostMapping("queryActivities")
    public Proto<?> queryActivities(@RequestBody ConditionForActivity conditionForActivity) {
        return getResult(activityIface.queryActivity(conditionForActivity));
    }

//    @PostMapping("createActivity")
//    public Proto<Boolean> createActivity(@RequestBody ActivityDO activityDO) {
//        return getResult(activityIface.createNewActivity(activityDO));
//    }

    @PostMapping("joinActivity")
    public Proto<Boolean> joinActivity(@RequestBody ActivityUserDO activityUserDO) {
        return getResult(activityIface.joinActivity(activityUserDO));
    }


    @PostMapping("getAssociation")
    public Proto<AssociationDO> getAssociation(@RequestBody ConditionForAssociation condition) {
        return getResult(associationIface.getAssociationSipmle(condition));
    }

    @PostMapping("addAssociationImage")
    public Proto<Boolean> addAssociationImage(String associationGuid, MultipartFile file, HttpServletRequest request) {
        if (StringUtils.isEmpty(associationGuid)) {
            throw new HttpRequestException("ERROR FOR UPDATE IMAGE", HttpStatus.OK.value());
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
        if (!OSSHelper.pushFile(file, filename)) {
            return fail("上传文件失败");
        }
        String url = OSSHelper.buildUrl(filename);
        AssociationDO associationDO = new AssociationDO();
        associationDO.setAvatarUrl(url);
        return associationIface.updateAssociation(associationDO);
    }

    @PostMapping("changeLeader")
    public Proto<Boolean> changeLeader(@RequestBody AssociationDO associationDO, HttpServletRequest request) {
        if (associationDO.getGuid() == null || associationDO.getAssociationLeaderGuid() == null) {
            return fail("参数错误");
        }
        Operator opeartor = getOperator(request);
        ConditionForAssociation condition = new ConditionForAssociation();
        condition.setGuid(associationDO.getGuid());
        condition.setAssociationLeaderGuid(opeartor.getGuid());
        Proto<Boolean> proto = associationIface.isLeader(condition);
        if (proto == null && !proto.getData()) {
            return fail("你不是社长嗷铁子");
        }
        return associationIface.updateAssociation(associationDO);
    }
    @PostMapping("uploadImage")
    public Proto<String> uploadImage(MultipartFile file){
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
        if (!OSSHelper.pushFile(file, filename)) {
            return fail("上传文件失败");
        }
        return getResult(OSSHelper.buildUrl(filename));
    }
//    @PostMapping("updateActivity")
//    public Proto<Boolean> updateActivity(@RequestBody ActivityDO activity){
//        return activityIface.updateActivity(activity);
//    }
    @PostMapping("addAssociationAvatar")
    public Proto<Boolean> addAssociationAvatar(MultipartFile file, AssociationDO association) {
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
        if (!OSSHelper.pushFile(file, filename)) {
            return fail("上传文件失败");
        }
        if (association == null || StringUtils.isEmpty(association.getGuid())) {
            ILogger.getInstance().info("PARAM ERROR : {} ", JSONObject.toJSONString(association));
            return getResult(Boolean.FALSE);
        }
        association.setAvatarUrl(OSSHelper.buildUrl(filename));
        associationIface.updateAssociation(association);
        return getResult(Boolean.TRUE);
    }

    public static enum RedisKey {
        DEFAULT_CODE,
        PASSWORD_CODE
    }
}

