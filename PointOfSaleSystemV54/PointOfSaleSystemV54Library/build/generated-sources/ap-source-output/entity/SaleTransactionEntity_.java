package entity;

import entity.CustomerEntity;
import entity.SaleTransactionLineItemEntity;
import entity.StaffEntity;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-03-21T20:46:24")
@StaticMetamodel(SaleTransactionEntity.class)
public class SaleTransactionEntity_ { 

    public static volatile SingularAttribute<SaleTransactionEntity, BigDecimal> totalAmount;
    public static volatile SingularAttribute<SaleTransactionEntity, Boolean> voidRefund;
    public static volatile SingularAttribute<SaleTransactionEntity, CustomerEntity> customerEntity;
    public static volatile SingularAttribute<SaleTransactionEntity, Integer> totalLineItem;
    public static volatile SingularAttribute<SaleTransactionEntity, Integer> totalQuantity;
    public static volatile SingularAttribute<SaleTransactionEntity, StaffEntity> staffEntity;
    public static volatile SingularAttribute<SaleTransactionEntity, Date> transactionDateTime;
    public static volatile SingularAttribute<SaleTransactionEntity, Long> saleTransactionId;
    public static volatile ListAttribute<SaleTransactionEntity, SaleTransactionLineItemEntity> saleTransactionLineItemEntities;

}