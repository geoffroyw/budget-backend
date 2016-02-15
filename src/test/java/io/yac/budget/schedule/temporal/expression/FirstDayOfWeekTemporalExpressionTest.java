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
public class FirstDayOfWeekTemporalExpressionTest {

    @Test
    public void getTemporalExpressionType_is_WEEKLY() {
        FirstDayOfWeekTemporalExpression temporalExpression = new FirstDayOfWeekTemporalExpression();

        assertThat(temporalExpression.getTemporalExpressionType(), is(TemporalExpressionType.WEEKLY));
    }

    @Test
    public void includes_is_true_if_day_of_week_is_monday() {
        //15 of feb 2015 is on monday
        Date monday = Date.from(LocalDate.of(2015, 2, 16).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        FirstDayOfWeekTemporalExpression temporalExpression = new FirstDayOfWeekTemporalExpression();

        assertThat(temporalExpression.includes(monday), is(true));
    }

    @Test
    public void includes_is_false_if_day_of_week_is_not_monday() {
        Date notMonday = Date.from(LocalDate.of(2015, 2, 18).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        FirstDayOfWeekTemporalExpression temporalExpression = new FirstDayOfWeekTemporalExpression();

        assertThat(temporalExpression.includes(notMonday), is(false));
    }
}