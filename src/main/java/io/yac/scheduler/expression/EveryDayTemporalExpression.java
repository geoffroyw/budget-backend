package io.yac.scheduler.expression;

import java.util.Date;

/**
 * Created by geoffroy on 16/05/2016.
 */
public class EveryDayTemporalExpression implements TemporalExpression {
    @Override
    public boolean includes(Date date) {
        return true;
    }

    @Override
    public TemporalExpressionType getTemporalExpressionType() {
        return TemporalExpressionType.DAILY;
    }
}
