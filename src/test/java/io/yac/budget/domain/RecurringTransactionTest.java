package io.yac.budget.domain;

import io.yac.budget.schedule.temporal.expression.TemporalExpression.TemporalExpressionType;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by geoffroy on 15/02/2016.
 */
public class RecurringTransactionTest {

    @Test
    public void isOccuringOn_is_true_if_temporalExpression_includes_returns_true() {
        //Temporal expression daily evaluates to true everyday
        RecurringTransaction recurringTransaction =
                RecurringTransaction.builder().temporalExpressionType(TemporalExpressionType.DAILY).build();
        assertThat(recurringTransaction.isOccuringOn(new Date()), is(true));
    }

    @Test
    public void isOccuringOn_is_false_if_temporalExpression_includes_returns_false() {
        //Temporal expression yearly evaluates to on first day of year
        Date notFirstDayOfYear =
                Date.from(LocalDate.of(2015, 2, 18).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        RecurringTransaction recurringTransaction =
                RecurringTransaction.builder().temporalExpressionType(TemporalExpressionType.YEARLY).build();
        assertThat(recurringTransaction.isOccuringOn(notFirstDayOfYear), is(false));
    }


}