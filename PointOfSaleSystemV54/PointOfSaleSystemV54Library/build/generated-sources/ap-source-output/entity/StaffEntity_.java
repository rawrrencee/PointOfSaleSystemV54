package entity;

import entity.SaleTransactionEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import util.enumeration.AccessRightEnum;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-03-24T21:16:11")
@StaticMetamodel(StaffEntity.class)
public class StaffEntity_ { 

    public static volatile SingularAttribute<StaffEntity, String> firstName;
    public static volatile SingularAttribute<StaffEntity, String> lastName;
    public static volatile SingularAttribute<StaffEntity, AccessRightEnum> accessRightEnum;
    public static volatile SingularAttribute<StaffEntity, String> password;
    public static volatile SingularAttribute<StaffEntity, String> salt;
    public static volatile ListAttribute<StaffEntity, SaleTransactionEntity> saleTransactionEntities;
    public static volatile SingularAttribute<StaffEntity, Long> staffId;
    public static volatile SingularAttribute<StaffEntity, String> username;

}