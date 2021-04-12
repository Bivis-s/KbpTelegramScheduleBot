package bot.db.daos;

import bot.db.subscription.Subscription;
import bot.parser.KbpDbLinkedParser;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.schedule_bot_model.errors.SourceWasntFoundError;
import by.bivis.schedule_bot_model.objects.db_objects.SourceDao;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
@Getter
@Setter
public class KbpSourceDao extends BaseDaoImpl<Source> implements SourceDao<Source> {
    private SubscriptionDao subscriptionDao;
    private KbpDbLinkedParser linkedParser;

    @Override
    public List<Source> getSignedSources(long userId) {
        List<Source> sources = new ArrayList<>();
        for (Subscription subscription : subscriptionDao.getSignedSubscriptions(userId)) {
            sources.add(get(subscription.getSourceId()));
        }
        return sources;
    }

    @Override
    public List<Source> getSourcesByCategoryAndSubcategory(String category, String subcategory) {
        List<Source> sources = getAll();
        List<Source> selectedSources = new ArrayList<>();
        for (Source source : sources) {
            if ((source.getType().getValue().equals(category)) &&
                    (String.valueOf(source.getValue().charAt(0)).equals(subcategory))) {
                selectedSources.add(source);
            }
        }
        return selectedSources;
    }

    @Override
    public Source getSelectedSource(long userId, String category, String subcategory, String sourceName) {
        List<Source> sources = getSourcesByCategoryAndSubcategory(category, subcategory);
        for (Source source : sources) {
            if (source.getValue().equals(sourceName)) {
                return source;
            }
        }
        String errorMessage = "Source wasn't found";
        throw new SourceWasntFoundError(errorMessage);
    }

    private Source getSourceFromListByName(List<Source> sources, String sourceName) {
        for (Source source : sources) {
            log.fatal(source.getValue());
            if (source.getValue().equals(sourceName)) {
                return source;
            }
        }
        String errorMessage = "Source wasn't found";
        log.error(errorMessage);
        throw new SourceWasntFoundError(errorMessage);
    }

    @Override
    public Source getSourceFromSubscriptionsByName(long userId, String sourceName) {
        return getSourceFromListByName(getSignedSources(userId), sourceName);
    }

    @Override
    public Source getSourceByName(String sourceName) {
        return getSourceFromListByName(getAll(), sourceName);
    }

    // категория - предмет, преподаватель, аудитория т.п.
    @Override
    public Set<String> getSourceCategorySet() {
        Set<String> categories = new TreeSet<>();
        for (Source source : linkedParser.getSources()) {
            categories.add(source.getType().getValue());
        }
        return categories;
    }

    // подкатегория - первая буква имени источника
    @Override
    public Set<String> getSourceSubcategoryList(String category) {
        Set<String> subcategories = new TreeSet<>();
        for (Source source : linkedParser.getSources()) {
            if (category.equals(source.getType().getValue())) {
                subcategories.add(String.valueOf(source.getValue().charAt(0)));
            }
        }
        return subcategories;
    }

    @Override
    public Set<Source> getSources(String category, String subcategory) {
        Set<Source> sources = new TreeSet<>();
        for (Source source : linkedParser.getSources()) {
            String sourceSubcategory = String.valueOf(source.getValue().charAt(0));
            if ((category.equals(source.getType().getValue())) && (subcategory.equals(sourceSubcategory))) {
                sources.add(source);
            }
        }
        return sources;
    }

    @Override
    protected Class<Source> getGenericClass() {
        return Source.class;
    }
}
