package io.yac.budget.schedule.temporal.expression;

import java.util.Date;

/**
 * Created by geoffroy on 15/02/2016.
 */
public interface TemporalExpression {

    boolean includes(Date date);

    TemporalExpressionType getTemporalExpressionType();

    enum TemporalExpressionType {
        WEEKLY("WEEKLY"), DAILY("DAILY"), MONTHLY("MONTHLY"), YEARLY("YEARLY");

        private String externalName;

        TemporalExpressionType(String externalName) {
            this.externalName = externalName;
        }

        public static TemporalExpressionType fromExternalName(String val) {
            for (TemporalExpressionType temporalExpressionType : TemporalExpressionType.values()) {
                if (temporalExpressionType.getExternalName().equalsIgnoreCase(val)) {
                    return temporalExpressionType;
                }
            }
            return null;
        }

        public String getExternalName() {
            return externalName;
        }

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
