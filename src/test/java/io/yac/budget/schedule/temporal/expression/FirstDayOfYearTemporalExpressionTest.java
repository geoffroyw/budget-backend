package io.yac.budget.schedule.temporal.expression;

import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by geoffroy on 15/02/2016.
 */
public class FirstDayOfYearTemporalExpressionTest {

    @Test
    public void getTemporalExpressionType_is_YEARLY() {
        FirstDayOfYearTemporalExpression temporalExpression = new FirstDayOfYearTemporalExpression();

        assertThat(temporalExpression.getTemporalExpressionType(), is(TemporalExpression.TemporalExpressionType.YEARLY));
    }

    @Test
    public void includes_is_true_if_first_day_of_year() {
        Date firstDayOfYear = Date.from(LocalDate.of(2015, 1, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        FirstDayOfYearTemporalExpression temporalExpression = new FirstDayOfYearTemporalExpression();

        assertThat(temporalExpression.includes(firstDayOfYear), is(true));
    }

    @Test
    public void includes_is_false_if_day_is_not_first_day_of_year() {
        Date notFirstDayOfYear = Date.from(LocalDate.of(2015, 2, 18).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        FirstDayOfYearTemporalExpression temporalExpression = new FirstDayOfYearTemporalExpression();

        assertThat(temporalExpression.includes(notFirstDayOfYear), is(false));
    }

}