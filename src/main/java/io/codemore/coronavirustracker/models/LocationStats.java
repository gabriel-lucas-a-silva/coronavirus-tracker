package io.codemore.coronavirustracker.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationStats {

    private String state;

    private String country;

    private int latestTotal;

}
