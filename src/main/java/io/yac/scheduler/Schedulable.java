package io.yac.scheduler;

import java.util.Date;

/**
 * Created by geoffroy on 16/05/2016.
 */
public interface Schedulable {

    boolean isOccuringOn(Date date);
}
