package ejb.session.stateless;

import entity.MessageOfTheDayEntity;
import java.util.List;
import util.exception.InputDataValidationException;



public interface MessageOfTheDayControllerRemote
{
    MessageOfTheDayEntity createNewMessageOfTheDay(MessageOfTheDayEntity newMessageOfTheDayEntity) throws InputDataValidationException;
    
    List<MessageOfTheDayEntity> retrieveAllMessagesOfTheDay();
}
