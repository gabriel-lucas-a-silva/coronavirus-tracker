package io.codemore.coronavirustracker.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationStat {

    private String country;

    private int latestTotalCases;

    private int diffFromPrevDay;

}
