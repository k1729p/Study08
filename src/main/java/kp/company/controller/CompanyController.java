package kp.company.controller;

import java.lang.invoke.MethodHandles;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The web controller for the company.
 * 
 */
@Controller
public class CompanyController {
	private static final Log logger = LogFactory.getLog(MethodHandles.lookup().lookupClass().getName());

	/**
	 * The constructor.
	 */
	public CompanyController() {
		super();
	}

	/**
	 * Gets the company.
	 * 
	 * @return the view name
	 */
	@GetMapping("/company")
	public String company() {
		logger.info("company():");
		return "company/home";
	}
}