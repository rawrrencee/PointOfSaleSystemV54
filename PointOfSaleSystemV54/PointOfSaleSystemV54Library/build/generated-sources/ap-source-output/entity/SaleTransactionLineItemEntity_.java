package entity;

import entity.ProductEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-03-21T20:46:24")
@StaticMetamodel(SaleTransactionLineItemEntity.class)
public class SaleTransactionLineItemEntity_ { 

    public static volatile SingularAttribute<SaleTransactionLineItemEntity, Long> saleTransactionLineItemId;
    public static volatile SingularAttribute<SaleTransactionLineItemEntity, BigDecimal> unitPrice;
    public static volatile SingularAttribute<SaleTransactionLineItemEntity, Integer> serialNumber;
    public static volatile SingularAttribute<SaleTransactionLineItemEntity, Integer> quantity;
    public static volatile SingularAttribute<SaleTransactionLineItemEntity, ProductEntity> productEntity;
    public static volatile SingularAttribute<SaleTransactionLineItemEntity, BigDecimal> subTotal;

}