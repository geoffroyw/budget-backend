package io.yac.budget.schedule.temporal.expression;

import io.yac.budget.schedule.temporal.expression.TemporalExpression.TemporalExpressionType;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by geoffroy on 15/02/2016.
 */
public class EveryDayTemporalExpressionTest {


    @Test
    public void temporalExpressionType_is_Daily() {
        EveryDayTemporalExpression temporalExpression = new EveryDayTemporalExpression();

        assertThat(temporalExpression.getTemporalExpressionType(), is(TemporalExpressionType.DAILY));
    }


    @Test
    public void includes_returns_true() {
        Date anyDate = new Date();

        EveryDayTemporalExpression temporalExpression = new EveryDayTemporalExpression();

        assertThat(temporalExpression.includes(anyDate), is(true));
    }
}