package io.yac.rates.provider.ecb;

import io.yac.rates.provider.ecb.iface.Cube;
import io.yac.rates.provider.ecb.iface.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 16/05/2016.
 */
@Service
public class ECBRateServiceFacade {

    private static final Logger LOG = LoggerFactory.getLogger(ECBRateServiceFacade.class);

    private static final String ECB_RATES_ENDPOINT = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    public List<ECBRate> fetchRates() {

        RestTemplate restTemplate = new RestTemplate();
        Envelope ecbRates = restTemplate.getForObject(ECB_RATES_ENDPOINT, Envelope.class);
        LOG.info("Response from ECB " + ecbRates);
        return processServiceResponse(ecbRates);

    }

    List<ECBRate> processServiceResponse(Envelope envelope) {
        Cube rootCubeWithTimeInfo = envelope.getCube().getCube().get(0);
        List<Cube> cubeList = rootCubeWithTimeInfo.getCube();
        Date date = Date.from(
                LocalDate.parse(rootCubeWithTimeInfo.getTime(), DateTimeFormatter.ISO_DATE)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());


        return cubeList.stream()
                .map((cube) -> ECBRate.builder().date(date).currency(cube.getCurrency()).amount(cube.getRate()).build())
                .collect(Collectors.toList());

    }
}
