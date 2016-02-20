package io.yac.budget.schedule.temporal.expression;

import io.yac.budget.schedule.temporal.expression.TemporalExpression.TemporalExpressionFactory;
import io.yac.budget.schedule.temporal.expression.TemporalExpression.TemporalExpressionType;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by geoffroy on 15/02/2016.
 */
public class TemporalExpressionFactoryTest {

    @Test
    public void returns_an_instance_of_everyDayTemporalExpression_if_type_is_DAYLY() {
        assertThat(TemporalExpressionFactory.getInstance(TemporalExpressionType.DAILY),
                is(instanceOf(EveryDayTemporalExpression.class)));
    }

    @Test
    public void returns_an_instance_of_FirstDayOfWeekTemporalExpression_if_type_is_WEEKLY() {
        assertThat(TemporalExpressionFactory.getInstance(TemporalExpressionType.WEEKLY),
                is(instanceOf(FirstDayOfWeekTemporalExpression.class)));
    }

    @Test
    public void returns_an_instance_of_FirstDayOfMonthTemporalExpression_if_type_is_MONTHLY() {
        assertThat(TemporalExpressionFactory.getInstance(TemporalExpressionType.MONTHLY),
                is(instanceOf(FirstDayOfMonthTemporalExpression.class)));
    }

    @Test
    public void returns_an_instance_of_FirstDayOfYearTemporalExpression_if_type_is_DAYLY() {
        assertThat(TemporalExpressionFactory.getInstance(TemporalExpressionType.YEARLY),
                is(instanceOf(FirstDayOfYearTemporalExpression.class)));
    }

}