package bot.parser;

import by.bivis.kbp.parser.objects.News;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.kbp.parser.objects.schedule.Schedule;
import by.bivis.kbp.parser.parsers.Parser;
import by.bivis.schedule_bot_model.objects.db_objects.NewsDao;
import by.bivis.schedule_bot_model.objects.db_objects.ScheduleDao;
import by.bivis.schedule_bot_model.objects.db_objects.SourceDao;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static bot.utils.DateUtils.getTimeDifferenceInSecs;
import static by.bivis.kbp.parser.utils.DateUtils.getKbpScheduleDayNumber;

@Log4j2
@NoArgsConstructor
@Getter(AccessLevel.PRIVATE)
@Setter
public class KbpDbLinkedParser {
    // 1 hour
    private static int newsExpirationTime = 3600;
    // 5 minutes
    private static int scheduleExpirationTime = 300;
    private NewsDao<News> newsDao;
    private SourceDao<Source> sourceDao;
    private ScheduleDao<Schedule, Source> scheduleDao;

    private List<News> getNewsFromDb() {
        log.info("Get news from db");
        return getNewsDao().getAll();
    }

    private Schedule getScheduleFromDb(Source source) {
        log.info("Get schedule from db for source " + source.getValue());
        return getScheduleDao().get(source);
    }

    private List<Source> getSourcesFromDb() {
        log.info("Get sources from db");
        return getSourceDao().getAll();
    }

    private List<News> getNewsFromParser() {
        log.info("Get news from parser");
        return Parser.getNews();
    }

    private Schedule getScheduleFromParser(Source source) {
        log.info("Get schedule from parser for source " + source.getValue());
        return Parser.getSchedule(source);
    }

    private List<Source> getSourcesFromParser() {
        log.info("Get sources from parser");
        return Parser.getAvailableSourceList();
    }

    private void saveNewsToDb(List<News> newsList) {
        log.info("Save news to db");
//        removeOldNewsFromDb();
        for (News news : newsList) {
            getNewsDao().saveOrUpdate(news);
        }
    }

    private void saveScheduleToDb(Schedule schedule) {
        log.info("Save schedule to db");
//        removeOldSchedulesFromDb();
        getScheduleDao().saveOrUpdate(schedule);
    }

    private void saveSourcesToDb(List<Source> sources) {
        log.info("Save schedule to db");
        for (Source source : sources) {
            getSourceDao().saveOrUpdate(source);
        }
    }

    public List<News> getNews() {
        List<News> newsList = getNewsFromDb();
        if (newsList.size() == 0 || getTimeDifferenceInSecs(newsList.get(0).getParsingDate()) > newsExpirationTime) {
            getNewsDao().deleteAll();
            newsList = getNewsFromParser();
            saveNewsToDb(newsList);
        }
        return newsList;
    }

    public Schedule getSchedule(Source source) {
        Schedule schedule = getScheduleFromDb(source);

        // if there is no schedule in the database, parse it from the site and save to the database
        // else if there is a schedule, but it has expired parse time, and it hasn't been approved,
        // parse it from the site and save to the database
        if (schedule == null) {
            schedule = getScheduleFromParser(source);
            saveScheduleToDb(schedule);
        } else if (getTimeDifferenceInSecs(schedule.getParsingDate()) > scheduleExpirationTime &&
                !schedule.getColumns().get(getKbpScheduleDayNumber(LocalDate.now()) + 1).isApproved()) {
            getScheduleDao().delete(schedule);
            schedule = getScheduleFromParser(source);
            saveScheduleToDb(schedule);
        }
        return schedule;
    }

    public List<Source> getSources() {
        List<Source> sources = getSourcesFromDb();
        GregorianCalendar calendar = new GregorianCalendar();
        if ((sources.size() == 0) ||
                ((calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER) && (calendar.get(Calendar.DAY_OF_MONTH) == 1))) {
            getSourceDao().deleteAll();
            sources = getSourcesFromParser();
            saveSourcesToDb(sources);
        }
        return sources;
    }
}
