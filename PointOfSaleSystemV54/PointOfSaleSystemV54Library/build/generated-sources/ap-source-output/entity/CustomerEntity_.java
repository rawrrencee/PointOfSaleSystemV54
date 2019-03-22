package entity;

import entity.SaleTransactionEntity;
import entity.ShoppingCartEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-03-22T18:10:36")
@StaticMetamodel(CustomerEntity.class)
public class CustomerEntity_ { 

    public static volatile SingularAttribute<CustomerEntity, String> firstName;
    public static volatile SingularAttribute<CustomerEntity, String> lastName;
    public static volatile SingularAttribute<CustomerEntity, String> password;
    public static volatile SingularAttribute<CustomerEntity, ShoppingCartEntity> shoppingCartEntity;
    public static volatile SingularAttribute<CustomerEntity, String> salt;
    public static volatile ListAttribute<CustomerEntity, SaleTransactionEntity> saleTransactionEntities;
    public static volatile SingularAttribute<CustomerEntity, Long> customerId;
    public static volatile SingularAttribute<CustomerEntity, String> email;

}