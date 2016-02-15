package io.yac.budget.schedule.temporal.expression;

import java.util.Date;

/**
 * Created by geoffroy on 15/02/2016.
 */
public interface TemporalExpression {

    boolean includes(Date date);

    TemporalExpressionType getTemporalExpressionType();

    enum TemporalExpressionType {
        WEEKLY, DAILY, MONTHLY, YEARLY
    }


    class TemporalExpressionFactory {
        public static TemporalExpression getInstance(TemporalExpressionType temporalExpressionType) {
            switch (temporalExpressionType) {
                case WEEKLY:
                    return new FirstDayOfWeekTemporalExpression();
                case DAILY:
                    return new EveryDayTemporalExpression();
                case MONTHLY:
                    return new FirstDayOfMonthTemporalExpression();
                case YEARLY:
                    return new FirstDayOfYearTemporalExpression();
                default:
                    throw new IllegalArgumentException("Unsupported TemporalExpressionType " + temporalExpressionType);
            }
        }
    }
}
