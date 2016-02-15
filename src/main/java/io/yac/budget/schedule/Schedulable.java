package io.yac.budget.schedule;

import java.util.Date;

/**
 * Created by geoffroy on 15/02/2016.
 */
public interface Schedulable {

    boolean isOccuringOn(Date date);
}
