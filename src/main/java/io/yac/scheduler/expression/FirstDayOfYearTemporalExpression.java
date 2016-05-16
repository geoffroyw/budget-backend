package io.yac.scheduler.expression;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by geoffroy on 16/05/2016.
 */
public class FirstDayOfYearTemporalExpression implements TemporalExpression {
    @Override
    public boolean includes(Date date) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        return localDate.getDayOfYear() == 1;
    }

    @Override
    public TemporalExpressionType getTemporalExpressionType() {
        return TemporalExpressionType.YEARLY;
    }
}
