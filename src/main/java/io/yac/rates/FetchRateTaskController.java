package io.yac.rates;

import io.yac.rates.scheduler.FetchRateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by geoffroy on 27/11/2016.
 */
@RestController
@RequestMapping(value = "/utils/fetchRate")
public class FetchRateTaskController {

    private final FetchRateTask fetchRateTask;

    @Autowired
    public FetchRateTaskController(FetchRateTask fetchRateTask) {
        this.fetchRateTask = fetchRateTask;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String fetchRate() {
        fetchRateTask.fetchRate();
        return "Rates fetched";
    }

}
