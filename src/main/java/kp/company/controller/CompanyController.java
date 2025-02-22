package kp.company.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.invoke.MethodHandles;

/**
 * The web controller for the company.
 */
@Controller
public class CompanyController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Retrieves the company.
     *
     * @return the view name
     */
    @GetMapping("/company")
    public String company() {
        logger.info("company():");
        return "company/home";
    }
}