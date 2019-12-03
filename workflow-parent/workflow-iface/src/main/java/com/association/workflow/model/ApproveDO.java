package com.association.workflow.model;

import com.association.workflow.enumerations.EnumForApproveStatus;
import com.association.workflow.enumerations.EnumForApproveType;
import lombok.Data;

import java.util.Date;

@Data
public class ApproveDO {

    private String guid;
    private String approveGuid ;
    private Integer approveStatus;
    private Integer approveType;
    private Date createTime;
    private Date updateTime;
    private String createUserGuid;
    private String updateUserGuid;
    private String createUserName;
    private String updateUserName;
    private String reason;

    public String getApproveStatusName() {
        return approveStatus == null ? null : EnumForApproveStatus.parse(approveStatus).getInfo();
    }

    public String getApproveTypeName() {
        return approveType == null ? null : EnumForApproveType.parse(approveType).getInfo();
    }


    public static void main(String[] args) {
        System.out.println(EnumForApproveType.parse(1));
        System.out.println(EnumForApproveType.parse(1111));

    }

}
