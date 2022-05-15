package io.codemore.coronavirustracker.controllers;

import io.codemore.coronavirustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static io.codemore.coronavirustracker.utils.DecimalFormatUtils.decimalFormatter;

@Controller
public class HomeController {

    @Autowired
    private CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("locationStats", coronaVirusDataService.getAllStats());
        model.addAttribute("totalReportedCases", decimalFormatter(coronaVirusDataService.getTotalReportedCases()));
        model.addAttribute("totalNewCases", decimalFormatter(coronaVirusDataService.getTotalNewCases()));

        return "home";
    }

}
