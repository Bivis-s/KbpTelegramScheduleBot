package bot.controllers;

import bot.db.objects.Note;
import bot.db.objects.TelegramUser;
import bot.values.Commands;
import by.bivis.kbp.parser.objects.News;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.kbp.parser.objects.schedule.Schedule;
import by.bivis.schedule_bot_model.controllers.ScheduleBotController;
import by.bivis.schedule_bot_model.models.ScheduleBotModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TelegramBotController extends ScheduleBotController<TelegramUser, News, Source, Schedule, Note> {

    public TelegramBotController(ScheduleBotModel<TelegramUser, News, Source, Schedule, Note> model) {
        super(model);
    }

    @Override
    public void handle(TelegramUser user, String command) {
        switch (command) {
            case "/start":
            case Commands.INFO:
            case Commands.INFO_DIM:
                getModel().sendHelloMessageToView(user);
                break;
            case Commands.NEWS:
                getModel().sendParsingInProcessMessageToView(user);
                getModel().sendNewsToView(user);
                break;
            case Commands.SUBSCRIBE:
            case Commands.SUBSCRIBE_DIM:
                getModel().sendParsingInProcessMessageToView(user);
                getModel().sendSourceCategoriesToView(user);
                break;
            case Commands.GET:
            case Commands.GET_DIM:
                getModel().sendSubscriptionsToView(user);
                break;
            case Commands.GET_ALL:
            case Commands.GET_ALL_DIM:
                getModel().sendSubscriptionsToSeeExtendedScheduleToView(user);
                break;
            case Commands.REMOVE:
            case Commands.REMOVE_DIM:
                getModel().sendSubscriptionsToRemoveToView(user);
                break;
            case Commands.SEE:
                getModel().sendParsingInProcessMessageToView(user);
                getModel().sendSourceCategoriesToSeeWithoutSubscriptionToView(user);
                break;
            case Commands.NOTES:
                getModel().sendNotesToView(user);
                break;
            case Commands.NOTES_CLEAR:
                getModel().getUserDao().cleanNotes(getModel().getUserId(user));
                getModel().sendDeleteNotesToView(user);
                break;
            default:
                handleCommandByUserState(user, command);
        }
    }

    @Override
    public void handleCommandByUserState(TelegramUser user, String command) {
        user = getModel().getUserDao().get(user.getId());
        switch (user.getState()) {
            case PICK_SOURCE_CATEGORY:
                getModel().updateUserSelectedSourceCategory(user, command);
                getModel().sendSourcesSubcategoryByCategoryToView(user, command);
                break;
            case PICK_SOURCE_CATEGORY_TO_SEE_WITHOUT_SUBSCRIPTION:
                getModel().updateUserSelectedSourceCategory(user, command);
                getModel().sendSourcesSubcategoryByCategoryToSeeWithoutSubscriptionToView(user, command);
                break;
            case SOURCES_SUBCATEGORIES:
                getModel().updateUserSelectedSourceSubcategory(user, command);
                getModel().sendSelectedSourceToView(user);
                break;
            case SOURCES_SUBCATEGORIES_TO_SEE_WITHOUT_SUBSCRIPTION:
                getModel().updateUserSelectedSourceSubcategory(user, command);
                getModel().sendSelectedSourceToSeeWithoutSubscriptionToView(user);
                break;
            case SOURCES:
                getModel().addSubscriptionToUser(user, command);
                break;
            case SEE_SCHEDULE_WITHOUT_SUBSCRIPTION:
                getModel().sendExtendedScheduleToView(user,
                        getModel().getSourceDao().getSourceByName(command));
                break;
            case PICK_SIGNED_SOURCE:
                getModel().sendTodayAndTomorrowScheduleToView(user,
                        getModel().getSourceDao().getSourceFromSubscriptionsByName(user.getId(), command));
                break;
            case PICK_SIGNED_SOURCE_EXTENDED:
                getModel().sendExtendedScheduleToView(user,
                        getModel().getSourceDao().getSourceFromSubscriptionsByName(user.getId(), command));
                break;
            case PICK_SIGNED_SOURCE_TO_REMOVE:
                getModel().removeSubscriptionFromUser(user, command);
                break;
            case NOTES:
                getModel().addNoteToUser(user, command);
                break;
            default:
                break;
        }
    }
}
