package entity;

import entity.CustomerEntity;
import entity.ShoppingCartLineEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-03-24T20:10:00")
@StaticMetamodel(ShoppingCartEntity.class)
public class ShoppingCartEntity_ { 

    public static volatile SingularAttribute<ShoppingCartEntity, CustomerEntity> customerEntity;
    public static volatile SingularAttribute<ShoppingCartEntity, Long> shoppingCartId;
    public static volatile ListAttribute<ShoppingCartEntity, ShoppingCartLineEntity> shoppingCartLineEntities;

}