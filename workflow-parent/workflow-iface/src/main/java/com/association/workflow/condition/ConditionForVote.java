package com.association.workflow.condition;

import lombok.Data;

@Data
public class ConditionForVote {
    private String guid ;
    private String voteGuid ;
    private Integer voteType ;
    private Integer voteCount ;
}
