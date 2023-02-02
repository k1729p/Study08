package kp.company.client.side;

import static kp.TestConstants.ABSENT_ID;
import static kp.TestConstants.CHANGED_EMPLOYEE_FIRST_NAME;
import static kp.TestConstants.CHANGED_EMPLOYEE_LAST_NAME;
import static kp.TestConstants.EXPECTED_DEPARTMENT_NAME;
import static kp.TestConstants.EXPECTED_EMPLOYEE_FIRST_NAME;
import static kp.TestConstants.EXPECTED_EMPLOYEE_LAST_NAME;
import static kp.TestConstants.TEST_DEPARTMENT_ID_PARAM;
import static kp.TestConstants.TEST_EMPLOYEE_ID_PARAM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import kp.company.client.side.base.ClientSideTestsBase;
import kp.company.domain.Title;
import kp.company.service.CompanyService;

/**
 * Client side tests for employee.<br>
 * The server is <b>started</b>.
 *
 */
//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class EmployeeClientSideTests extends ClientSideTestsBase {
	/**
	 * The constructor.
	 */
	EmployeeClientSideTests() {
		super();
	}

	/**
	 * Should list employees.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldListEmployees() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/listEmployees?departmentId=%s", port,
				TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
		// THEN
		assertEquals(HttpStatus.OK, response.getStatusCode());
		final String responseBody = response.getBody();
		assertThat(responseBody).contains(accessor.getMessage("employees"))//
				.contains(EXPECTED_DEPARTMENT_NAME)//
				// there is given employee in the list
				.contains(EXPECTED_EMPLOYEE_FIRST_NAME)//
				.contains(EXPECTED_EMPLOYEE_LAST_NAME)//
				.contains(CompanyService.getTitleList().get(0).getName())//
				.contains(accessor.getMessage("addEmployee"));
	}

	/**
	 * Should start employee adding.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldStartAddingEmployee() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/startEmployeeAdding?departmentId=%s", port,
				TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
		// THEN
		assertEquals(HttpStatus.OK, response.getStatusCode());
		final String responseBody = response.getBody();
		assertThat(responseBody).contains(accessor.getMessage("addEmployee"))//
				.contains(accessor.getMessage("firstName"))//
				.contains(accessor.getMessage("lastName"))//
				.contains(accessor.getMessage("title"))//
				.contains(accessor.getMessage("save"));
	}

	/**
	 * Should start employee editing.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldStartEditingEmployee() throws Exception {
		// GIVEN
		final String requestUrl = String.format(
				"http://localhost:%s/startEmployeeEditing?departmentId=%s&employeeId=%s", port,
				TEST_DEPARTMENT_ID_PARAM, TEST_EMPLOYEE_ID_PARAM);
		// WHEN
		final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
		// THEN
		assertEquals(HttpStatus.OK, response.getStatusCode());
		final String responseBody = response.getBody();
		assertThat(responseBody).contains(accessor.getMessage("editEmployee"))//
				.contains(EXPECTED_EMPLOYEE_FIRST_NAME)//
				.contains(EXPECTED_EMPLOYEE_LAST_NAME)//
				.contains(CompanyService.getTitleList().get(0).getName())//
				.contains(accessor.getMessage("save"));
	}

	/**
	 * Should not start editing non-existent employee.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldGetNotFoundErrorOnEditingAbsentEmployee() throws Exception {
		// GIVEN
		final String requestUrl = String.format(
				"http://localhost:%s/startEmployeeEditing?departmentId=%s&employeeId=%s", port,
				TEST_DEPARTMENT_ID_PARAM, ABSENT_ID);
		// WHEN
		final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
		// THEN
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	/**
	 * Should save employee.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldSaveEmployee() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/finishEmployeeEditing", port);
		final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
		paramMap.add("save", "");
		paramMap.add("id", TEST_EMPLOYEE_ID_PARAM);
		paramMap.add("firstName", CHANGED_EMPLOYEE_FIRST_NAME);
		paramMap.add("lastName", CHANGED_EMPLOYEE_LAST_NAME);
		paramMap.add("title", Title.ANALYST.name().toUpperCase());
		paramMap.add("departmentId", TEST_DEPARTMENT_ID_PARAM);
		final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				paramMap, new HttpHeaders());
		// WHEN
		final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
		// THEN
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		assertEquals("/listEmployees", response.getHeaders().getLocation().getPath());
		assertEquals("departmentId=" + TEST_DEPARTMENT_ID_PARAM, response.getHeaders().getLocation().getQuery());

		// GIVEN
		final String requestUrlRedir = String.format("http://localhost:%s/listEmployees?departmentId=%s", port,
				TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResponseEntity<String> responseRedir = restTemplate.getForEntity(requestUrlRedir, String.class);
		// THEN
		assertEquals(HttpStatus.OK, responseRedir.getStatusCode());
		final String responseBody = responseRedir.getBody();
		assertThat(responseBody).contains(accessor.getMessage("employees"))//
				.contains(EXPECTED_DEPARTMENT_NAME)//
				// there is given employee in the list
				.contains(CHANGED_EMPLOYEE_FIRST_NAME)//
				.contains(CHANGED_EMPLOYEE_LAST_NAME)//
				.contains(CompanyService.getTitleList().get(0).getName())//
				.contains(accessor.getMessage("addEmployee"));
	}

	/**
	 * Should validate employee and show validation error.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldValidateEmployeeAndShowValidationError() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/finishEmployeeEditing", port);
		final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
		paramMap.add("save", "");
		paramMap.add("id", TEST_EMPLOYEE_ID_PARAM);
		paramMap.add("title", Title.ANALYST.name().toUpperCase());
		paramMap.add("departmentId", TEST_DEPARTMENT_ID_PARAM);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.ACCEPT_LANGUAGE, Locale.US.toLanguageTag());
		final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				paramMap, httpHeaders);
		// WHEN
		final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
		// THEN
		assertEquals(HttpStatus.OK, response.getStatusCode());
		final String responseBody = response.getBody();
		assertThat(responseBody).contains(accessor.getMessage("editEmployee"))//
				// validation error
				.contains("must not be blank")//
				.contains(accessor.getMessage("save"));
	}

	/**
	 * Should cancel employee editing.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldCancelEditingEmployee() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/finishEmployeeEditing", port);
		final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
		paramMap.add("cancel", "");
		paramMap.add("id", TEST_EMPLOYEE_ID_PARAM);
		paramMap.add("firstName", CHANGED_EMPLOYEE_FIRST_NAME);
		paramMap.add("lastName", CHANGED_EMPLOYEE_LAST_NAME);
		paramMap.add("title", Title.ANALYST.name().toUpperCase());
		paramMap.add("departmentId", TEST_DEPARTMENT_ID_PARAM);
		final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				paramMap, new HttpHeaders());
		// WHEN
		final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
		// THEN
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		assertEquals("/listEmployees", response.getHeaders().getLocation().getPath());
		assertEquals("departmentId=" + TEST_DEPARTMENT_ID_PARAM, response.getHeaders().getLocation().getQuery());

		// GIVEN
		final String requestUrlRedir = String.format("http://localhost:%s/listEmployees?departmentId=%s", port,
				TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResponseEntity<String> responseRedir = restTemplate.getForEntity(requestUrlRedir, String.class);
		// THEN
		assertEquals(HttpStatus.OK, responseRedir.getStatusCode());
		final String responseBody = responseRedir.getBody();
		assertThat(responseBody).contains(accessor.getMessage("employees"))//
				.contains(EXPECTED_DEPARTMENT_NAME)//
				// canceled employee is not found in the list
				.doesNotContain(CHANGED_EMPLOYEE_FIRST_NAME)//
				.doesNotContain(CHANGED_EMPLOYEE_LAST_NAME)//
				.contains(EXPECTED_EMPLOYEE_FIRST_NAME)//
				.contains(EXPECTED_EMPLOYEE_LAST_NAME)//
				.contains(CompanyService.getTitleList().get(0).getName())//
				.contains(accessor.getMessage("addEmployee"));
	}

	/**
	 * Should start employee deleting.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldStartDeletingEmployee() throws Exception {
		// GIVEN
		final String requestUrl = String.format(
				"http://localhost:%s/startEmployeeDeleting?departmentId=%s&employeeId=%s", port,
				TEST_DEPARTMENT_ID_PARAM, TEST_EMPLOYEE_ID_PARAM);
		// WHEN
		final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
		// THEN
		assertEquals(HttpStatus.OK, response.getStatusCode());
		final String responseBody = response.getBody();
		assertThat(responseBody).contains(accessor.getMessage("deleteEmployee"))//
				.contains(EXPECTED_EMPLOYEE_FIRST_NAME)//
				.contains(EXPECTED_EMPLOYEE_LAST_NAME)//
				.contains(CompanyService.getTitleList().get(0).getName())//
				.contains(accessor.getMessage("delete"));
	}

	/**
	 * Should delete employee.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldDeleteEmployee() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/finishEmployeeDeleting", port);
		final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
		paramMap.add("delete", "");
		paramMap.add("id", TEST_EMPLOYEE_ID_PARAM);
		paramMap.add("departmentId", TEST_DEPARTMENT_ID_PARAM);
		final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				paramMap, new HttpHeaders());
		// WHEN
		final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
		// THEN
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		assertEquals("/listEmployees", response.getHeaders().getLocation().getPath());
		assertEquals(response.getHeaders().getLocation().getQuery(), "departmentId=" + TEST_DEPARTMENT_ID_PARAM);

		// GIVEN
		final String requestUrlRedir = String.format("http://localhost:%s/listEmployees?departmentId=%s", port,
				TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResponseEntity<String> responseRedir = restTemplate.getForEntity(requestUrlRedir, String.class);
		// THEN
		assertEquals(HttpStatus.OK, responseRedir.getStatusCode());
		final String responseBody = responseRedir.getBody();
		assertThat(responseBody).contains(accessor.getMessage("employees"))//
				.contains(EXPECTED_DEPARTMENT_NAME)//
				// deleted employee is not found in the list
				.doesNotContain(EXPECTED_EMPLOYEE_FIRST_NAME)//
				.doesNotContain(EXPECTED_EMPLOYEE_LAST_NAME)//
				.contains(accessor.getMessage("addEmployee"));
	}

	/**
	 * Should cancel employee deleting.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldCancelDeletingEmployee() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/finishEmployeeDeleting", port);
		final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
		paramMap.add("cancel", "");
		paramMap.add("id", TEST_EMPLOYEE_ID_PARAM);
		paramMap.add("departmentId", TEST_DEPARTMENT_ID_PARAM);
		final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				paramMap, new HttpHeaders());
		// WHEN
		final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
		// THEN
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		assertEquals("/listEmployees", response.getHeaders().getLocation().getPath());
		assertEquals("departmentId=" + TEST_DEPARTMENT_ID_PARAM, response.getHeaders().getLocation().getQuery());

		// GIVEN
		final String requestUrlRedir = String.format("http://localhost:%s/listEmployees?departmentId=%s", port,
				TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResponseEntity<String> responseRedir = restTemplate.getForEntity(requestUrlRedir, String.class);
		// THEN
		assertEquals(HttpStatus.OK, responseRedir.getStatusCode());
		final String responseBody = responseRedir.getBody();
		assertThat(responseBody).contains(accessor.getMessage("employees"))//
				.contains(EXPECTED_DEPARTMENT_NAME)//
				// there is not deleted employee in the list
				.contains(EXPECTED_EMPLOYEE_FIRST_NAME)//
				.contains(EXPECTED_EMPLOYEE_LAST_NAME)//
				.contains(CompanyService.getTitleList().get(0).getName())//
				.contains(accessor.getMessage("addEmployee"));
	}
}
