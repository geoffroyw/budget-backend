package io.yac.budget.schedule.temporal.expression;

import java.time.*;
import java.util.Date;

/**
 * Created by geoffroy on 15/02/2016.
 */
public class FirstDayOfWeekTemporalExpression implements TemporalExpression {
    @Override
    public boolean includes(Date date) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        return localDate.getDayOfWeek() == DayOfWeek.MONDAY;
    }

    @Override
    public TemporalExpressionType getTemporalExpressionType() {
        return TemporalExpressionType.WEEKLY;
    }
}
