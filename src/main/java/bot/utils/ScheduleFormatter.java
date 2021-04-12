package bot.utils;

import by.bivis.kbp.parser.objects.Source;
import by.bivis.kbp.parser.objects.schedule.Schedule;
import by.bivis.kbp.parser.objects.schedule.ScheduleCell;
import by.bivis.kbp.parser.objects.schedule.ScheduleColumn;
import by.bivis.kbp.parser.objects.schedule.ScheduleLesson;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static bot.utils.DateUtils.getDayNameByDayNumber;

@Log4j2
public class ScheduleFormatter {
    private ScheduleFormatter() {
    }

    private static String formatLesson(ScheduleLesson lesson) {
        StringBuilder sb = new StringBuilder();
        List<Source> sourceList = lesson.getSourceList();
        for (int i = 0; i < sourceList.size(); i++) {
            sb.append(sourceList.get(i).getValue());
            if (i != sourceList.size() - 1) {
                sb.append(" | ");
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    private static String formatCell(ScheduleCell cell, int cellNumber) {
        if (cell.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(cellNumber).append(". ");
        List<ScheduleLesson> lessons = cell.getLessons();
        for (ScheduleLesson lesson : lessons) {
            sb.append(formatLesson(lesson));
        }
        return sb.toString();
    }

    private static String formatColumn(ScheduleColumn column) {
        StringBuilder sb = new StringBuilder();
        sb.append(getDayNameByDayNumber(column.getDayNumber()));
        if (column.isApproved()) sb.append(" | Утверждено");
        sb.append("\n");
        List<ScheduleCell> cells = column.getCellList();
        for (int i = 0; i < cells.size(); i++) {
            sb.append(formatCell(cells.get(i), i + 1));
        }
        return sb.toString();
    }

    private static String formatColumnList(List<ScheduleColumn> columns) {
        StringBuilder sb = new StringBuilder();
        for (ScheduleColumn column : columns) {
            sb.append(formatColumn(column)).append("\n");
        }
        return sb.toString();
    }

    public static String formatTodayAndTomorrowSchedule(Schedule schedule) {
        return formatColumnList(schedule.getTodayAndTomorrowSchedule());
    }

    public static String formatExtendedSchedule(Schedule schedule) {
        return formatColumnList(schedule.getExtendedSchedule());

    }
}
