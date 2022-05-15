package io.codemore.coronavirustracker.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationStat {

    private String country;

    private String latestTotalCases;

    private String diffFromPrevDay;

}
