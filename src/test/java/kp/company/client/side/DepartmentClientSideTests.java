package kp.company.client.side;

import static kp.TestConstants.ABSENT_ID;
import static kp.TestConstants.TEST_DEPARTMENT_ID_PARAM;
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

import kp.TestConstants;
import kp.company.client.side.base.ClientSideTestsBase;

/**
 * Client side tests for department.<br>
 * The server is <b>started</b>.
 *
 */
//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DepartmentClientSideTests extends ClientSideTestsBase {

	/**
	 * The constructor.
	 */
	DepartmentClientSideTests() {
		super();
	}

	/**
	 * Should list departments.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldListDepartments() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/listDepartments", port);
		// WHEN
		final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
		// THEN
		assertEquals(HttpStatus.OK, response.getStatusCode());
		final String responseBody = response.getBody();
		assertThat(responseBody).contains(accessor.getMessage("departments"))//
				// there is given department in the list
				.contains(TestConstants.EXPECTED_DEPARTMENT_NAME)//
				.contains(accessor.getMessage("addDepartment"));
	}

	/**
	 * Should start department adding.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldStartAddingDepartment() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/startDepartmentAdding", port);
		// WHEN
		final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
		// THEN
		assertEquals(HttpStatus.OK, response.getStatusCode());
		final String responseBody = response.getBody();
		assertThat(responseBody).contains(accessor.getMessage("addDepartment"))//
				.contains(accessor.getMessage("name"))//
				.contains(accessor.getMessage("save"));
	}

	/**
	 * Should start department editing.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldStartEditingDepartment() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/startDepartmentEditing?departmentId=%s", port,
				TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
		// THEN
		assertEquals(HttpStatus.OK, response.getStatusCode());
		final String responseBody = response.getBody();
		assertThat(responseBody).contains(accessor.getMessage("editDepartment"))//
				.contains(TestConstants.EXPECTED_DEPARTMENT_NAME)//
				.contains(accessor.getMessage("save"));
	}

	/**
	 * Should not start editing non-existent department.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldGetNotFoundErrorOnEditingAbsentDepartment() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/startDepartmentEditing?departmentId=%s", port,
				ABSENT_ID);
		// WHEN
		final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
		// THEN
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	/**
	 * Should save department.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldSaveDepartment() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/finishDepartmentEditing", port);
		final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
		paramMap.add("save", "");
		paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
		paramMap.add("name", TestConstants.CHANGED_DEPARTMENT_NAME);
		final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				paramMap, new HttpHeaders());
		// WHEN
		final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
		// THEN
		/*- The HTTP response status code 302 'Temporary Redirect' is a common way of performing URL redirection. */
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		assertEquals("/listDepartments", response.getHeaders().getLocation().getPath());

		// GIVEN
		final String requestUrlRedir = String.format("http://localhost:%s/listDepartments", port);
		// WHEN
		final ResponseEntity<String> responseRedir = restTemplate.getForEntity(requestUrlRedir, String.class);
		// THEN
		assertEquals(HttpStatus.OK, responseRedir.getStatusCode());
		final String responseBody = responseRedir.getBody();
		assertThat(responseBody).contains(accessor.getMessage("departments"))//
				// there is given saved department in the list
				.contains(TestConstants.CHANGED_DEPARTMENT_NAME)//
				.contains(accessor.getMessage("addDepartment"));
	}

	/**
	 * Should validate department and show validation error.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldValidateDepartmentAndShowValidationError() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/finishDepartmentEditing", port);
		final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
		paramMap.add("save", "");
		paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.ACCEPT_LANGUAGE, Locale.US.toLanguageTag());
		final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				paramMap, httpHeaders);
		// WHEN
		final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
		// THEN
		assertEquals(HttpStatus.OK, response.getStatusCode());
		final String responseBody = response.getBody();
		assertThat(responseBody).contains(accessor.getMessage("editDepartment"))//
				// validation error
				.contains("must not be blank")//
				.contains(accessor.getMessage("save"));
	}

	/**
	 * Should cancel department editing.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldCancelEditingDepartment() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/finishDepartmentEditing", port);
		final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
		paramMap.add("cancel", "");
		paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
		paramMap.add("name", TestConstants.CHANGED_DEPARTMENT_NAME);
		final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				paramMap, new HttpHeaders());
		// WHEN
		final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
		// THEN
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		assertEquals("/listDepartments", response.getHeaders().getLocation().getPath());

		// GIVEN
		final String requestUrlRedir = String.format("http://localhost:%s/listDepartments", port);
		// WHEN
		final ResponseEntity<String> responseRedir = restTemplate.getForEntity(requestUrlRedir, String.class);
		// THEN
		assertEquals(HttpStatus.OK, responseRedir.getStatusCode());
		final String responseBody = responseRedir.getBody();
		assertThat(responseBody).contains(accessor.getMessage("departments"))//
				// canceled department is not found in the list
				.doesNotContain(TestConstants.CHANGED_DEPARTMENT_NAME)//
				.contains(TestConstants.EXPECTED_DEPARTMENT_NAME)//
				.contains(accessor.getMessage("addDepartment"));
	}

	/**
	 * Should start department deleting.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldStartDeletingDepartment() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/startDepartmentDeleting?departmentId=%s", port,
				TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
		// THEN
		assertEquals(HttpStatus.OK, response.getStatusCode());
		final String responseBody = response.getBody();
		assertThat(responseBody).contains(accessor.getMessage("deleteDepartment"))//
				.contains(TestConstants.EXPECTED_DEPARTMENT_NAME)//
				.contains(accessor.getMessage("delete"));
	}

	/**
	 * Should delete department.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldDeleteDepartment() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/finishDepartmentDeleting", port);
		final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
		paramMap.add("delete", "");
		paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
		final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				paramMap, new HttpHeaders());
		// WHEN
		final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
		// THEN
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		assertEquals("/listDepartments", response.getHeaders().getLocation().getPath());

		// GIVEN
		final String requestUrlRedir = String.format("http://localhost:%s/listDepartments", port);
		// WHEN
		final ResponseEntity<String> responseRedir = restTemplate.getForEntity(requestUrlRedir, String.class);
		// THEN
		assertEquals(HttpStatus.OK, responseRedir.getStatusCode());
		final String responseBody = responseRedir.getBody();
		assertThat(responseBody).contains(accessor.getMessage("departments"))//
				// deleted department is not found in the list
				.doesNotContain(TestConstants.EXPECTED_DEPARTMENT_NAME)//
				.contains(accessor.getMessage("addDepartment"));
	}

	/**
	 * Should cancel department deleting.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldCancelDeletingDepartment() throws Exception {
		// GIVEN
		final String requestUrl = String.format("http://localhost:%s/finishDepartmentDeleting", port);
		final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
		paramMap.add("cancel", "");
		paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
		final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				paramMap, new HttpHeaders());
		// WHEN
		final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
		// THEN
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		assertEquals("/listDepartments", response.getHeaders().getLocation().getPath());

		// GIVEN
		final String requestUrlRedir = String.format("http://localhost:%s/listDepartments", port);
		// WHEN
		final ResponseEntity<String> responseRedir = restTemplate.getForEntity(requestUrlRedir, String.class);
		// THEN
		assertEquals(HttpStatus.OK, responseRedir.getStatusCode());
		final String responseBody = responseRedir.getBody();
		assertThat(responseBody).contains(accessor.getMessage("departments"))//
				// there is not deleted department in the list
				.contains(TestConstants.EXPECTED_DEPARTMENT_NAME)//
				.contains(accessor.getMessage("addDepartment"));
	}
}
