package entity;

import entity.ProductEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-03-21T20:46:24")
@StaticMetamodel(ShoppingCartLineEntity.class)
public class ShoppingCartLineEntity_ { 

    public static volatile SingularAttribute<ShoppingCartLineEntity, Integer> quantity;
    public static volatile SingularAttribute<ShoppingCartLineEntity, Long> shoppingCartLineId;
    public static volatile SingularAttribute<ShoppingCartLineEntity, ProductEntity> productEntity;

}