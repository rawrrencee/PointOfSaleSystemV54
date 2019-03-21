package entity;

import entity.CategoryEntity;
import entity.ProductEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-03-21T20:46:24")
@StaticMetamodel(CategoryEntity.class)
public class CategoryEntity_ { 

    public static volatile ListAttribute<CategoryEntity, CategoryEntity> subCategoryEntities;
    public static volatile SingularAttribute<CategoryEntity, CategoryEntity> parentCategoryEntity;
    public static volatile ListAttribute<CategoryEntity, ProductEntity> productEntities;
    public static volatile SingularAttribute<CategoryEntity, String> name;
    public static volatile SingularAttribute<CategoryEntity, String> description;
    public static volatile SingularAttribute<CategoryEntity, Long> categoryId;

}