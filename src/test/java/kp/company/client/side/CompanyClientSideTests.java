package kp.company.client.side;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import kp.company.client.side.base.ClientSideTestsBase;

/**
 * Client side tests for company.<br>
 * The server is <b>started</b>.
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CompanyClientSideTests extends ClientSideTestsBase {

	/**
	 * The constructor.
	 */
	CompanyClientSideTests() {
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
		final String requestUrl = String.format("http://localhost:%s/", port);
		// WHEN
		final String result = restTemplate.getForObject(requestUrl, String.class);
		// THEN
		Assertions.assertThat(result).contains("<meta http-equiv=\"Refresh\" content=\"0; URL=/company\">");
	}

	/**
	 * Should get company.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldGetHomePage() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/company", port);
		// WHEN
		final String result = restTemplate.getForObject(requestUrl, String.class);
		// THEN
		Assertions.assertThat(result).contains(accessor.getMessage("company"))//
				.contains(accessor.getMessage("departments"));
	}
}