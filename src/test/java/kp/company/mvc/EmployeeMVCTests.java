package kp.company.mvc;

import static kp.TestConstants.ABSENT_ID;
import static kp.TestConstants.CHANGED_EMPLOYEE_FIRST_NAME;
import static kp.TestConstants.CHANGED_EMPLOYEE_LAST_NAME;
import static kp.TestConstants.EXPECTED_DEPARTMENT_NAME;
import static kp.TestConstants.EXPECTED_EMPLOYEE_FIRST_NAME;
import static kp.TestConstants.EXPECTED_EMPLOYEE_LAST_NAME;
import static kp.TestConstants.TEST_DEPARTMENT_ID_PARAM;
import static kp.TestConstants.TEST_EMPLOYEE_ID_PARAM;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import kp.company.controller.EmployeeController;
import kp.company.domain.Title;
import kp.company.mvc.base.MVCTestsBase;
import kp.company.service.CompanyService;

/**
 * Application tests for employee with server-side support.<br>
 * The server is <b>not started</b>.
 *
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
class EmployeeMVCTests extends MVCTestsBase {
	/**
	 * The constructor.
	 */
	EmployeeMVCTests() {
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
		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders//
				.get("/listEmployees")//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("employees"))));
		resultActions
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
		// there is given employee in the list
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_FIRST_NAME)));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_LAST_NAME)));
		resultActions.andExpect(MockMvcResultMatchers.content()
				.string(Matchers.containsString(CompanyService.getTitleList().getFirst().getName())));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addEmployee"))));
	}

	/**
	 * Should start employee adding.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldStartAddingEmployee() throws Exception {
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders//
				.get("/startEmployeeAdding")//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addEmployee"))));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("firstName"))));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("lastName"))));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("title"))));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("save"))));
	}

	/**
	 * Should start employee editing.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldStartEditingEmployee() throws Exception {
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders//
				.get("/startEmployeeEditing")//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM)//
				.param("employeeId", TEST_EMPLOYEE_ID_PARAM);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("editEmployee"))));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_FIRST_NAME)));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_LAST_NAME)));
		resultActions.andExpect(MockMvcResultMatchers.content()
				.string(Matchers.containsString(CompanyService.getTitleList().getFirst().getName())));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("save"))));
	}

	/**
	 * Should start employee editing.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldGetNotFoundErrorOnEditingAbsentEmployee() throws Exception {
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders//
				.get("/startEmployeeEditing")//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM)//
				.param("employeeId", ABSENT_ID);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * Should save employee.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldSaveEmployee() throws Exception {
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders//
				.post("/finishEmployeeEditing")//
				.param("save", "")//
				.param("id", TEST_EMPLOYEE_ID_PARAM)//
				.param("firstName", CHANGED_EMPLOYEE_FIRST_NAME)//
				.param("lastName", CHANGED_EMPLOYEE_LAST_NAME)//
				.param("title", Title.ANALYST.name().toUpperCase())//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		resultActions.andExpect(MockMvcResultMatchers.status().isFound());
		resultActions.andExpect(MockMvcResultMatchers
				.redirectedUrl(String.format("/listEmployees?departmentId=%s", TEST_DEPARTMENT_ID_PARAM)));
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilderRedirect = MockMvcRequestBuilders//
				.get("/listEmployees")//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResultActions resultActionsRedirect = mockMvc.perform(requestBuilderRedirect);
		// THEN
		resultActionsRedirect.andExpect(MockMvcResultMatchers.status().isOk());
		resultActionsRedirect.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("employees"))));
		resultActionsRedirect
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
		// there is given employee in the list
		resultActionsRedirect.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(CHANGED_EMPLOYEE_FIRST_NAME)));
		resultActionsRedirect
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(CHANGED_EMPLOYEE_LAST_NAME)));
		resultActionsRedirect.andExpect(MockMvcResultMatchers.content()
				.string(Matchers.containsString(CompanyService.getTitleList().getFirst().getName())));
		resultActionsRedirect.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addEmployee"))));
	}

	/**
	 * Should validate employee and show validation error.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldValidateEmployeeAndShowValidationError() throws Exception {
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders//
				.post("/finishEmployeeEditing")//
				.param("save", "")//
				.param("id", TEST_EMPLOYEE_ID_PARAM)//
				.param("title", Title.ANALYST.name().toUpperCase())//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andExpect(MockMvcResultMatchers.model().attributeHasErrors("employee"));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("editEmployee"))));
		// validation error
		resultActions.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("must not be blank")));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("save"))));
	}

	/**
	 * Should cancel employee editing.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldCancelEditingEmployee() throws Exception {
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders//
				.post("/finishEmployeeEditing")//
				.param("cancel", "")//
				.param("id", TEST_EMPLOYEE_ID_PARAM)//
				.param("firstName", EXPECTED_EMPLOYEE_FIRST_NAME)//
				.param("lastName", EXPECTED_EMPLOYEE_LAST_NAME)//
				.param("title", Title.ANALYST.name().toUpperCase())//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		resultActions.andExpect(MockMvcResultMatchers.status().isFound());
		resultActions.andExpect(MockMvcResultMatchers
				.redirectedUrl(String.format("/listEmployees?departmentId=%s", TEST_DEPARTMENT_ID_PARAM)));

		// GIVEN
		final MockHttpServletRequestBuilder requestBuilderRedirect = MockMvcRequestBuilders//
				.get("/listEmployees")//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResultActions resultActionsRedirect = mockMvc.perform(requestBuilderRedirect);
		// THEN
		resultActionsRedirect.andExpect(MockMvcResultMatchers.status().isOk());
		resultActionsRedirect.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("employees"))));
		resultActionsRedirect
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
		// canceled employee is not found in the list
		resultActionsRedirect.andExpect(MockMvcResultMatchers.content()
				.string(Matchers.not(Matchers.containsString(CHANGED_EMPLOYEE_FIRST_NAME))));
		resultActionsRedirect.andExpect(MockMvcResultMatchers.content()
				.string(Matchers.not(Matchers.containsString(CHANGED_EMPLOYEE_LAST_NAME))));
		resultActionsRedirect.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_FIRST_NAME)));
		resultActionsRedirect.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_LAST_NAME)));
		resultActionsRedirect.andExpect(MockMvcResultMatchers.content()
				.string(Matchers.containsString(CompanyService.getTitleList().getFirst().getName())));
		resultActionsRedirect.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addEmployee"))));
	}

	/**
	 * Should start employee deleting.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldStartDeletingEmployee() throws Exception {
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders//
				.get("/startEmployeeDeleting")//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM)//
				.param("employeeId", TEST_EMPLOYEE_ID_PARAM);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("deleteEmployee"))));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_FIRST_NAME)));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_LAST_NAME)));
		resultActions.andExpect(MockMvcResultMatchers.content()
				.string(Matchers.containsString(CompanyService.getTitleList().getFirst().getName())));
		resultActions.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("delete"))));
	}

	/**
	 * Should delete employee.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldDeleteEmployee() throws Exception {
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders//
				.post("/finishEmployeeDeleting")//
				.param("delete", "")//
				.param("id", TEST_EMPLOYEE_ID_PARAM)//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		resultActions.andExpect(MockMvcResultMatchers.status().isFound());
		resultActions.andExpect(MockMvcResultMatchers
				.redirectedUrl(String.format("/listEmployees?departmentId=%s", TEST_DEPARTMENT_ID_PARAM)));

		// GIVEN
		final MockHttpServletRequestBuilder requestBuilderRedirect = MockMvcRequestBuilders//
				.get("/listEmployees")//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResultActions resultActionsRedirect = mockMvc.perform(requestBuilderRedirect);
		// THEN
		resultActionsRedirect.andExpect(MockMvcResultMatchers.status().isOk());
		resultActionsRedirect.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("employees"))));
		resultActionsRedirect
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
		// deleted employee is not found in the list
		resultActionsRedirect.andExpect(MockMvcResultMatchers.content()
				.string(Matchers.not(Matchers.containsString(EXPECTED_EMPLOYEE_FIRST_NAME))));
		resultActionsRedirect.andExpect(MockMvcResultMatchers.content()
				.string(Matchers.not(Matchers.containsString(EXPECTED_EMPLOYEE_LAST_NAME))));
		resultActionsRedirect.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addEmployee"))));
	}

	/**
	 * Should cancel employee deleting.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldCancelDeletingEmployee() throws Exception {
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders//
				.post("/finishEmployeeDeleting")//
				.param("cancel", "")//
				.param("id", TEST_EMPLOYEE_ID_PARAM)//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		resultActions.andExpect(MockMvcResultMatchers.status().isFound());
		resultActions.andExpect(MockMvcResultMatchers
				.redirectedUrl(String.format("/listEmployees?departmentId=%s", TEST_DEPARTMENT_ID_PARAM)));

		// GIVEN
		final MockHttpServletRequestBuilder requestBuilderRedirect = MockMvcRequestBuilders//
				.get("/listEmployees")//
				.param("departmentId", TEST_DEPARTMENT_ID_PARAM);
		// WHEN
		final ResultActions resultActionsRedirect = mockMvc.perform(requestBuilderRedirect);
		// THEN
		resultActionsRedirect.andExpect(MockMvcResultMatchers.status().isOk());
		resultActionsRedirect.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("employees"))));
		resultActionsRedirect
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
		// there is not deleted employee in the list
		resultActionsRedirect.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_FIRST_NAME)));
		resultActionsRedirect.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_LAST_NAME)));
		resultActionsRedirect.andExpect(MockMvcResultMatchers.content()
				.string(Matchers.containsString(CompanyService.getTitleList().getFirst().getName())));
		resultActionsRedirect.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addEmployee"))));
	}
}
