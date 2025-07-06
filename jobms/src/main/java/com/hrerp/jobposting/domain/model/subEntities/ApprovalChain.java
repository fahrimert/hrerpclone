package com.hrerp.jobposting.domain.model.subEntities;

import java.time.LocalDateTime;

public class ApprovalChain {
    //bunu daha implement etmiyorum daha ne anlama geldiğini çözemedim

    private  String approvingManager;
    private  String  approvingFinanceManager;

    private LocalDateTime managerApprovingDate;
    private  LocalDateTime financeManagerApprovingDate;
}
