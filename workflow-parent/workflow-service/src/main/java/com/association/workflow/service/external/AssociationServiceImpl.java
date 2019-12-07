package com.association.workflow.service.external;

import com.association.workflow.condition.ConditionForAssociation;
import com.association.workflow.iface.AssociationIface;
import com.association.workflow.model.AssociationDO;
import com.associtaion.user.model.UserDO;
import component.PaginProto;
import component.Proto;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class AssociationServiceImpl implements AssociationIface {
    @Override
    public Proto<Boolean> createNewAssociation(AssociationDO association) {
        return null;
    }

    @Override
    public Proto<Boolean> updateAssociation(AssociationDO associationDO) {
        return null;
    }

    @Override
    public Proto<UserDO> getAssociationMates(ConditionForAssociation condition) {
        return null;
    }

    @Override
    public PaginProto<List<AssociationDO>> queryAssociation(ConditionForAssociation condition) {
        return null;
    }
}
