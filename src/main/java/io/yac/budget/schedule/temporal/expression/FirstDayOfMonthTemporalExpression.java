package io.yac.budget.schedule.temporal.expression;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by geoffroy on 15/02/2016.
 */
public class FirstDayOfMonthTemporalExpression implements TemporalExpression {
    @Override
    public boolean includes(Date date) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        return localDate.getDayOfMonth() == 1;
    }

    @Override
    public TemporalExpressionType getTemporalExpressionType() {
        return TemporalExpressionType.MONTHLY;
    }
}
