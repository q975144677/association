package com.association.workflow.model;

import com.association.workflow.enumerations.EnumForActivityStatus;
import com.association.workflow.enumerations.EnumForApproveStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ActivityDO {
    private String guid ;
    private String name ;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime ;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime ;
    private Integer schoolId ;
    private String createUserGuid;
    private String updateUserGuid;
    private String createUserName ;
    private String updateUserName;
    private String reason ;
    private String detail ;
    private Integer activityStatus;
    private String associationGuid ;
    private String place ;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromDate ;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date toDate ;
    private String delayReason ;
    private String cancelReason ;
    private String approveMoney ;
    //pay 相关估计不能用了
    private String payAccount ;
    private Integer payType ;
    private Integer approveStatus ;
    private String image ;
    public String getApproveStatusName() {
        return approveStatus == null ? null : EnumForApproveStatus.parse(approveStatus).getInfo();
    }
    public String getActivityStatusName(){
        return activityStatus == null ? null : EnumForActivityStatus.parse(activityStatus).getInfo();
    }
}
