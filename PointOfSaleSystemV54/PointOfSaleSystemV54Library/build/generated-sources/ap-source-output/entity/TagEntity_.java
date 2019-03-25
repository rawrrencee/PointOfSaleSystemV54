package entity;

import entity.ProductEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-03-25T15:14:11")
@StaticMetamodel(TagEntity.class)
public class TagEntity_ { 

    public static volatile ListAttribute<TagEntity, ProductEntity> productEntities;
    public static volatile SingularAttribute<TagEntity, Long> tagId;
    public static volatile SingularAttribute<TagEntity, String> name;

}