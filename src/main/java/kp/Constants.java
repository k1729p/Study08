package kp;

import java.util.function.LongFunction;

import kp.company.exception.CompanyException;

/**
 * The constants.
 *
 */
@SuppressWarnings("doclint:missing")
public final class Constants {
	public static final long DEP_INDEX_LOWER_BOUND = 1;
	public static final long DEP_INDEX_UPPER_BOUND = 2;
	public static final long EMP_INDEX_LOWER_BOUND = 1;
	public static final long EMP_INDEX_UPPER_BOUND = 3;

	public static final LongFunction<String> DEP_NAME_FUN = depIndex -> String.format("D-Name-%02d", depIndex);
	public static final LongFunction<String> EMP_F_NAME_FUN = empIndex -> String.format("EF-Name-%d", empIndex);
	public static final LongFunction<String> EMP_L_NAME_FUN = empIndex -> String.format("EL-Name-%d", empIndex);

	public static final String DEPARTMENTS_LIST_VIEW_NAME = "departments/list";
	public static final String DEPARTMENTS_EDIT_VIEW_NAME = "departments/edit";
	public static final String REDIRECT_LIST_DEPARTMENTS_VIEW_NAME = "redirect:/listDepartments";
	public static final String DEPARTMENTS_CONFIRM_DELETE_VIEW_NAME = "departments/confirmDelete";
	public static final String EMPLOYEES_LIST_VIEW_NAME = "employees/list";
	public static final String EMPLOYEES_EDIT_VIEW_NAME = "employees/edit";
	public static final LongFunction<String> REDIRECT_LIST_EMPLOYEES_VIEW_NAME_FUN = departmentId -> String
			.format("redirect:/listEmployees?departmentId=%s", departmentId);
	public static final String EMPLOYEES_CONFIRM_DELETE_VIEW_NAME = "employees/confirmDelete";

	public static final String DEPARTMENT_ATTR_NAME = "department";
	public static final String EMPLOYEE_ATTR_NAME = "employee";

	public static final String DEP_NOT_FOUND_MSG = "Department not found";
	public static final String DEP_OR_EMP_NOT_FOUND_MSG = "Department or employee not found";
	public static final LongFunction<CompanyException> DEP_NOT_FOUND_EXC_FUN = departmentId -> new CompanyException(
			String.format("Not found department with id[%d]", departmentId));
	public static final LongFunction<CompanyException> EMP_NOT_FOUND_EXC_FUN = employeeId -> new CompanyException(
			String.format("Not found employee with id[%d]", employeeId));

	private Constants() {
		throw new IllegalStateException("Utility class");
	}
}
