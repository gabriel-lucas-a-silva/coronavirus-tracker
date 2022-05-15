package io.codemore.coronavirustracker.services;

import io.codemore.coronavirustracker.models.LocationStat;
import lombok.Data;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static String COUNTRY = "Country/Region";
    private List<LocationStat> allStats = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws Exception {
        List<LocationStat> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(VIRUS_DATA_URL))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        renderCsvData(newStats, response);
    }

    private void renderCsvData(final List<LocationStat> newStats, final HttpResponse<String> response) throws IOException {
        StringReader csvBodyReader = new StringReader(response.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build().parse(csvBodyReader);

        List<CSVRecord> recordsAsList = IterableUtils.toList(records);
        int lastTotalCasesReported = 0;
        for (int i = 0; i < recordsAsList.size(); i++) {
            CSVRecord record = recordsAsList.get(i);

            String country = record.get(COUNTRY);
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));

            String nextCountry = i == recordsAsList.size() - 1 ? "end of the list" : recordsAsList.get(i + 1).get(COUNTRY);
            String lastCountry = i == 0 ? "beginning of the list" : recordsAsList.get(i - 1).get(COUNTRY);
            if (country.equals(nextCountry)) {
                lastTotalCasesReported += latestCases;

            } else {
                LocationStat locationStat = LocationStat.builder()
                        .country(country)
                        .latestTotalCases(lastCountry.equals(country) ? lastTotalCasesReported : latestCases) // if last country is equals to my actual one, that means I have accumulative data
                        .diffFromPrevDay(latestCases - prevDayCases)
                        .build();

                newStats.add(locationStat);
                lastTotalCasesReported = 0;
            }
        }

        this.allStats = newStats;
    }

}
