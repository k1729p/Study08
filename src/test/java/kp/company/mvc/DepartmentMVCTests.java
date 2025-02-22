package kp.company.mvc;

import kp.company.controller.DepartmentController;
import kp.company.mvc.base.MVCTestsBase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.invoke.MethodHandles;

import static kp.TestConstants.*;

/**
 * Application tests for a department with server-side support.
 */
@WebMvcTest(DepartmentController.class)
class DepartmentMVCTests extends MVCTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @TestBean(methodName = "createDepartmentController")
    private DepartmentController departmentController;

    /**
     * Should list departments.
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldListDepartments() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/listDepartments");
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(
                        Matchers.containsString(accessor.getMessage("departments"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(
                        Matchers.containsString(accessor.getMessage("addDepartment"))));
        logger.info("shouldListDepartments():");
    }

    /**
     * Should start department adding.
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldStartAddingDepartment() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/startDepartmentAdding");
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(
                        Matchers.containsString(accessor.getMessage("addDepartment"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("name"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("save"))));
        logger.info("shouldStartAddingDepartment():");
    }

    /**
     * Should start department editing.
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldStartEditingDepartment() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/startDepartmentEditing")
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(
                        Matchers.containsString(accessor.getMessage("editDepartment"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("save"))));
        logger.info("shouldStartEditingDepartment():");
    }

    /**
     * Should not start editing non-existent department.
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldGetNotFoundErrorOnEditingAbsentDepartment() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/startDepartmentEditing")
                .param("departmentId", ABSENT_ID);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
        logger.info("shouldGetNotFoundErrorOnEditingAbsentDepartment():");
    }

    /**
     * Should save department.
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldSaveDepartment() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/finishDepartmentEditing")
                .param("save", "")
                .param("id", TEST_DEPARTMENT_ID_PARAM)
                .param("name", CHANGED_DEPARTMENT_NAME);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        /*-
         * The HTTP response status code 302 'Temporary Redirect' is a common way of performing URL redirection.
         */
        resultActions.andExpect(MockMvcResultMatchers.status().isFound());
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl("/listDepartments"));

        // GIVEN
        final MockHttpServletRequestBuilder requestBuilderRedirect = MockMvcRequestBuilders
                .get("/listDepartments");
        // WHEN
        final ResultActions resultActionsRedirect = mockMvc.perform(requestBuilderRedirect);
        // THEN
        resultActionsRedirect.andExpect(MockMvcResultMatchers.status().isOk());
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("departments"))));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(CHANGED_DEPARTMENT_NAME)));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addDepartment"))));
        logger.info("shouldSaveDepartment():");
    }

    /**
     * Should validate department and show validation error.
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldValidateDepartmentAndShowValidationError() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/finishDepartmentEditing")
                .param("save", "")
                .param("id", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.model().attributeHasErrors("department"));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(
                        Matchers.containsString(accessor.getMessage("editDepartment"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString("must not be blank")));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("save"))));
        logger.info("shouldValidateDepartmentAndShowValidationError():");
    }

    /**
     * Should cancel department editing.
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldCancelEditingDepartment() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/finishDepartmentEditing")
                .param("cancel", "")
                .param("id", TEST_DEPARTMENT_ID_PARAM)
                .param("name", CHANGED_DEPARTMENT_NAME);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isFound());
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl("/listDepartments"));

        // GIVEN
        final MockHttpServletRequestBuilder requestBuilderRedirect = MockMvcRequestBuilders
                .get("/listDepartments");
        // WHEN
        final ResultActions resultActionsRedirect = mockMvc.perform(requestBuilderRedirect);
        // THEN
        resultActionsRedirect.andExpect(MockMvcResultMatchers.status().isOk());
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("departments"))));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.not(Matchers.containsString(CHANGED_DEPARTMENT_NAME))));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addDepartment"))));
        logger.info("shouldCancelEditingDepartment():");
    }

    /**
     * Should start department deleting.
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldStartDeletingDepartment() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/startDepartmentDeleting")
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.content()
                .string(Matchers.containsString(accessor.getMessage("deleteDepartment"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("delete"))));
        logger.info("shouldStartDeletingDepartment():");
    }

    /**
     * Should delete department.
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldDeleteDepartment() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/finishDepartmentDeleting")
                .param("delete", "")
                .param("id", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isFound());
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl("/listDepartments"));

        // GIVEN
        final MockHttpServletRequestBuilder requestBuilderRedirect = MockMvcRequestBuilders
                .get("/listDepartments");
        // WHEN
        final ResultActions resultActionsRedirect = mockMvc.perform(requestBuilderRedirect);
        // THEN
        resultActionsRedirect.andExpect(MockMvcResultMatchers.status().isOk());
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("departments"))));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.not(Matchers.containsString(EXPECTED_DEPARTMENT_NAME))));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addDepartment"))));
        logger.info("shouldDeleteDepartment():");
    }

    /**
     * Should cancel department deleting.
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldCancelDeletingDepartment() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/finishDepartmentDeleting")
                .param("cancel", "")
                .param("id", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isFound());
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl("/listDepartments"));

        // GIVEN
        final MockHttpServletRequestBuilder requestBuilderRedirect = MockMvcRequestBuilders
                .get("/listDepartments");
        // WHEN
        final ResultActions resultActionsRedirect = mockMvc.perform(requestBuilderRedirect);
        // THEN
        resultActionsRedirect.andExpect(MockMvcResultMatchers.status().isOk());
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(
                        Matchers.containsString(accessor.getMessage("departments"))));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(
                        Matchers.containsString(accessor.getMessage("addDepartment"))));
        logger.info("shouldCancelDeletingDepartment():");
    }

}
