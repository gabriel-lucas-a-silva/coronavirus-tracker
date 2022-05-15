package io.codemore.coronavirustracker.services;

import io.codemore.coronavirustracker.helpers.TestContainer;
import io.codemore.coronavirustracker.models.LocationStat;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class CoronaVirusDataServiceTest extends TestContainer {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    @InjectMocks
    private CoronaVirusDataService service;

    @Test
    public void shouldFetchVirusData() throws Exception {
        // TODO: remove external call, unit tests should be independent
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<LocationStat> myAllStats = service.renderCsvData(response);

        List<LocationStat> actualAllstats = service.fetchVirusData();

        assertThat(actualAllstats, is(notNullValue()));
        assertThat(actualAllstats, is(equalTo(myAllStats)));
        assertThat(actualAllstats.size(), is(equalTo(199)));
    }
}
