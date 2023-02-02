package kp.company.mvc;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import kp.company.controller.CompanyController;
import kp.company.mvc.base.MVCTestsBase;

/**
 * Application tests for company with server-side support.<br>
 * The server is <b>not started</b>.
 *
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(CompanyController.class)
class CompanyMVCTests extends MVCTestsBase {
	/**
	 * The constructor.
	 */
	CompanyMVCTests() {
		super();
	}

	/**
	 * Should forward from root to index page.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldForwardFromRootToIndexPage() throws Exception {
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders/*-*/
				.get("/");
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andExpect(MockMvcResultMatchers.forwardedUrl("index.html"));
	}

	/**
	 * Should get company.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldGetHomePage() throws Exception {
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders/*-*/
				.get("/company");
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("company"))));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("departments"))));
	}
}