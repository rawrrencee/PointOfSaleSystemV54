package entity;

import entity.CategoryEntity;
import entity.TagEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-03-21T20:46:24")
@StaticMetamodel(ProductEntity.class)
public class ProductEntity_ { 

    public static volatile SingularAttribute<ProductEntity, BigDecimal> unitPrice;
    public static volatile SingularAttribute<ProductEntity, Long> productId;
    public static volatile SingularAttribute<ProductEntity, CategoryEntity> categoryEntity;
    public static volatile SingularAttribute<ProductEntity, Integer> quantityOnHand;
    public static volatile ListAttribute<ProductEntity, TagEntity> tagEntities;
    public static volatile SingularAttribute<ProductEntity, String> name;
    public static volatile SingularAttribute<ProductEntity, String> description;
    public static volatile SingularAttribute<ProductEntity, Integer> reorderQuantity;
    public static volatile SingularAttribute<ProductEntity, Integer> productRating;
    public static volatile SingularAttribute<ProductEntity, String> skuCode;

}