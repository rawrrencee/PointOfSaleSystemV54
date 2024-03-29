package ejb.session.stateless;

import entity.TagEntity;
import java.util.List;
import util.exception.CreateNewTagException;
import util.exception.DeleteTagException;
import util.exception.InputDataValidationException;
import util.exception.TagNotFoundException;
import util.exception.UpdateTagException;



public interface TagEntityControllerLocal 
{
    TagEntity createNewTagEntity(TagEntity newTagEntity) throws InputDataValidationException, CreateNewTagException;
    
    List<TagEntity> retrieveAllTags();
    
    TagEntity retrieveTagByTagId(Long tagId) throws TagNotFoundException;
    
    void updateTag(TagEntity tagEntity) throws InputDataValidationException, TagNotFoundException, UpdateTagException;
    
    void deleteTag(Long tagId) throws TagNotFoundException, DeleteTagException;
}
