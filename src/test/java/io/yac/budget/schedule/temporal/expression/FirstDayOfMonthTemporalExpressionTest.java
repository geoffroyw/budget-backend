package io.yac.budget.schedule.temporal.expression;

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
public class FirstDayOfMonthTemporalExpressionTest {

    @Test
    public void getTemporalExpressionType_is_MONTHLY() {
        FirstDayOfMonthTemporalExpression temporalExpression = new FirstDayOfMonthTemporalExpression();

        assertThat(temporalExpression.getTemporalExpressionType(), is(TemporalExpressionType.MONTHLY));
    }

    @Test
    public void includes_is_true_if_first_day_of_month() {
        Date monday = Date.from(LocalDate.of(2015, 2, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        FirstDayOfMonthTemporalExpression temporalExpression = new FirstDayOfMonthTemporalExpression();

        assertThat(temporalExpression.includes(monday), is(true));
    }

    @Test
    public void includes_is_false_if_day_is_not_first_day_of_month() {
        Date notFirstDayOfMonth = Date.from(LocalDate.of(2015, 2, 18).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        FirstDayOfMonthTemporalExpression temporalExpression = new FirstDayOfMonthTemporalExpression();

        assertThat(temporalExpression.includes(notFirstDayOfMonth), is(false));
    }
}