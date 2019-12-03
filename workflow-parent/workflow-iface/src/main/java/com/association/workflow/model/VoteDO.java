package com.association.workflow.model;


import lombok.Data;

import java.util.Date;
//todo 放在缓存里 ，job 定时刷
@Data
public class VoteDO {
    private String guid ;
    private String voteGuid ;
    private Integer voteType ;
    private Integer voteCount ;
}
