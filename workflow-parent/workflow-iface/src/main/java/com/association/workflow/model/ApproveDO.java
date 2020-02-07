package com.association.workflow.model;

import com.association.workflow.enumerations.EnumForApproveStatus;
import com.association.workflow.enumerations.EnumForApproveType;
import lombok.Data;

import java.util.Date;

@Data
public class ApproveDO {

    private String guid;
    // 若为 加入社团请求 则为社团id
    // 若为 加入活动请求 则为活动id
    private String approveGuid ;
    private Integer approveStatus;
    private Integer approveType;
    private Date createTime;
    private Date updateTime;
    private String createUserGuid;
    private String updateUserGuid;
    private String createUserName;
    private String updateUserName;
    private String reason ;
    private String detail ;
    private Integer schoolId ;
    private String schoolName ;
    @Data
    public static class DetailForJoinAssociation{
        private String associationGuid ;
        private String associationName ;
    }
    @Data
    public static class DetailForCreateAssociation{
        private String associationName ;
    }
    @Data
    public static  class DetailForCreateActivity{
        private ActivityDO activityDO ;
    }
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
