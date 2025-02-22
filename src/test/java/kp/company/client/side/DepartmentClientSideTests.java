package kp.company.client.side;

import kp.TestConstants;
import kp.company.client.side.base.ClientSideTestsBase;
import kp.company.controller.DepartmentController;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.invoke.MethodHandles;
import java.util.Locale;

import static kp.TestConstants.ABSENT_ID;
import static kp.TestConstants.TEST_DEPARTMENT_ID_PARAM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Client-side tests for the department.
 */
class DepartmentClientSideTests extends ClientSideTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @TestBean(methodName = "createDepartmentController")
    private DepartmentController departmentController;

    /**
     * Tests listing departments.
     */
    @Test
    void shouldListDepartments() {
        // GIVEN
        final String requestUrl = "http://localhost:%d/listDepartments".formatted(port);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("departments"))
                .contains(TestConstants.EXPECTED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("addDepartment"));
        logger.info("shouldListDepartments():");
    }

    /**
     * Tests starting department addition.
     */
    @Test
    void shouldStartAddingDepartment() {
        // GIVEN
        final String requestUrl = "http://localhost:%d/startDepartmentAdding".formatted(port);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("addDepartment"))
                .contains(accessor.getMessage("name"))
                .contains(accessor.getMessage("save"));
        logger.info("shouldStartAddingDepartment():");
    }

    /**
     * Tests starting department editing.
     */
    @Test
    void shouldStartEditingDepartment() {
        // GIVEN
        final String requestUrl = "http://localhost:%d/startDepartmentEditing?departmentId=%s".formatted(
                port, TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("editDepartment"))
                .contains(TestConstants.EXPECTED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("save"));
        logger.info("shouldStartEditingDepartment():");
    }

    /**
     * Tests handling non-existent department editing.
     */
    @Test
    void shouldGetNotFoundErrorOnEditingAbsentDepartment() {
        // GIVEN
        final String requestUrl = "http://localhost:%d/startDepartmentEditing?departmentId=%s".formatted(
                port, ABSENT_ID);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        logger.info("shouldGetNotFoundErrorOnEditingAbsentDepartment():");
    }

    /**
     * Tests saving a department.
     */
    @Test
    void shouldSaveDepartment() {
        // GIVEN
        final String requestUrl = "http://localhost:%d/finishDepartmentEditing".formatted(port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("save", "");
        paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
        paramMap.add("name", TestConstants.CHANGED_DEPARTMENT_NAME);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, new HttpHeaders());
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("departments"))
                .contains(TestConstants.CHANGED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("addDepartment"));
        logger.info("shouldSaveDepartment():");
    }

    /**
     * Tests validating a department and showing validation errors.
     */
    @Test
    void shouldValidateDepartmentAndShowValidationError() {
        // GIVEN
        final String requestUrl = "http://localhost:%d/finishDepartmentEditing".formatted(port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("save", "");
        paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT_LANGUAGE, Locale.US.toLanguageTag());
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, httpHeaders);
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("editDepartment"))
                .contains("must not be blank")
                .contains(accessor.getMessage("save"));
        logger.info("shouldValidateDepartmentAndShowValidationError():");
    }

    /**
     * Tests canceling department editing.
     */
    @Test
    void shouldCancelEditingDepartment() {
        // GIVEN
        final String requestUrl = "http://localhost:%d/finishDepartmentEditing".formatted(port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("cancel", "");
        paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
        paramMap.add("name", TestConstants.CHANGED_DEPARTMENT_NAME);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, new HttpHeaders());
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("departments"))
                .doesNotContain(TestConstants.CHANGED_DEPARTMENT_NAME)
                .contains(TestConstants.EXPECTED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("addDepartment"));
        logger.info("shouldCancelEditingDepartment():");
    }

    /**
     * Tests starting department deletion.
     */
    @Test
    void shouldStartDeletingDepartment() {
        // GIVEN
        final String requestUrl = "http://localhost:%d/startDepartmentDeleting?departmentId=%s".formatted(
                port, TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("deleteDepartment"))
                .contains(TestConstants.EXPECTED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("delete"));
        logger.info("shouldStartDeletingDepartment():");
    }

    /**
     * Tests deleting a department.
     */
    @Test
    void shouldDeleteDepartment() {
        // GIVEN
        final String requestUrl = "http://localhost:%d/finishDepartmentDeleting".formatted(port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("delete", "");
        paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, new HttpHeaders());
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("departments"))
                .doesNotContain(TestConstants.EXPECTED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("addDepartment"));
        logger.info("shouldDeleteDepartment():");
    }

    /**
     * Tests canceling department deletion.
     */
    @Test
    void shouldCancelDeletingDepartment() {
        // GIVEN
        final String requestUrl = "http://localhost:%d/finishDepartmentDeleting".formatted(port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("cancel", "");
        paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, new HttpHeaders());
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("departments"))
                .contains(TestConstants.EXPECTED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("addDepartment"));
        logger.info("shouldCancelDeletingDepartment():");
    }
}
