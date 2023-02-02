package kp.company.controller;

import static kp.Constants.DEPARTMENT_ATTR_NAME;
import static kp.Constants.DEP_NOT_FOUND_MSG;
import static kp.Constants.DEP_OR_EMP_NOT_FOUND_MSG;
import static kp.Constants.EMPLOYEES_CONFIRM_DELETE_VIEW_NAME;
import static kp.Constants.EMPLOYEES_EDIT_VIEW_NAME;
import static kp.Constants.EMPLOYEES_LIST_VIEW_NAME;
import static kp.Constants.EMPLOYEE_ATTR_NAME;
import static kp.Constants.REDIR_LIST_EMPLOYEES_VIEW_NAME_FUN;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import kp.company.domain.Department;
import kp.company.domain.Employee;
import kp.company.domain.Title;
import kp.company.exception.CompanyException;
import kp.company.service.CompanyService;

/**
 * The web controller for the {@link Employee}.
 * 
 */
@Controller
public class EmployeeController {
	private static final Log logger = LogFactory.getLog(MethodHandles.lookup().lookupClass().getName());

	private final CompanyService companyService;

	/**
	 * The constructor.
	 * 
	 * @param companyService the {@link CompanyService}
	 */
	public EmployeeController(CompanyService companyService) {
		super();
		this.companyService = companyService;
	}

	/**
	 * Gets the list of the {@link Title}s.
	 * 
	 * @return the list of titles
	 */
	@ModelAttribute("titles")
	public List<Title> listTitles() {

		return CompanyService.getTitleList();
	}

	/**
	 * Gets the list of the {@link Employee}s.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @param model        the {@link Model}
	 * @return the view name
	 */
	@GetMapping("/listEmployees")
	public String listEmployees(long departmentId, Model model) {

		Department department = null;
		try {
			department = companyService.findDepartment(departmentId);
		} catch (CompanyException ex) {
			logger.error(String.format("listEmployees(): CompanyException[%s], department id[%d]", ex.getMessage(),
					departmentId));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_NOT_FOUND_MSG, ex);
		}
		model.addAttribute(DEPARTMENT_ATTR_NAME, department);
		logger.info(String.format("listEmployees(): department id[%d]", department.getId()));
		return EMPLOYEES_LIST_VIEW_NAME;
	}

	/**
	 * Starts the adding of the {@link Employee}.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @param model        the {@link Model}
	 * @return the view name
	 */
	@GetMapping("/startEmployeeAdding")
	public String startEmployeeAdding(long departmentId, Model model) {

		Employee employee = null;
		try {
			employee = companyService.createEmployee(departmentId);
		} catch (CompanyException ex) {
			logger.error(String.format("startEmployeeAdding(): CompanyException[%s], department id[%d]",
					ex.getMessage(), departmentId));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_NOT_FOUND_MSG, ex);
		}
		model.addAttribute(EMPLOYEE_ATTR_NAME, employee);
		logger.info(String.format("startEmployeeAdding(): department id[%d]", departmentId));
		return EMPLOYEES_EDIT_VIEW_NAME;
	}

	/**
	 * Starts the editing of the {@link Employee}.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @param employeeId   the id of the {@link Employee}
	 * @param model        the {@link Model}
	 * @return the view name
	 */
	@GetMapping("/startEmployeeEditing")
	public String startEmployeeEditing(long departmentId, long employeeId, Model model) {

		Employee employee = null;
		try {
			employee = companyService.findEmployee(departmentId, employeeId);
		} catch (CompanyException ex) {
			logger.error(String.format("startEmployeeEditing(): CompanyException[%s], department id[%d]",
					ex.getMessage(), departmentId));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_OR_EMP_NOT_FOUND_MSG, ex);
		}
		model.addAttribute(EMPLOYEE_ATTR_NAME, employee);
		logger.info(
				String.format("startEmployeeEditing(): department id[%d], employee id[%d]", departmentId, employeeId));
		return EMPLOYEES_EDIT_VIEW_NAME;
	}

	/**
	 * Saves the {@link Employee}.
	 * 
	 * @param employee      the {@link Employee}
	 * @param bindingResult the {@link BindingResult}
	 * @return the view name
	 */
	@PostMapping(value = "/finishEmployeeEditing", params = { "save" })
	public String saveEmployee(@Valid Employee employee, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			final List<String> messagesList = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage)
					.toList();
			logger.info(String.format("saveEmployee(): error messages%s", messagesList));
			return EMPLOYEES_EDIT_VIEW_NAME;
		}
		try {
			companyService.saveEmployee(employee);
		} catch (CompanyException ex) {
			logger.error(String.format("saveEmployee(): CompanyException[%s]", ex.getMessage()));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_NOT_FOUND_MSG, ex);
		}
		logger.info(String.format("saveEmployee(): department id[%d], employee id[%d], title name[%s]",
				employee.getDepartmentId(), employee.getId(), employee.getTitle().getName()));
		return REDIR_LIST_EMPLOYEES_VIEW_NAME_FUN.apply(employee.getDepartmentId());
	}

	/**
	 * Cancels the editing of the {@link Employee}.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @param employeeId   the id of the {@link Employee}
	 * @return the view name
	 */
	@PostMapping(value = "/finishEmployeeEditing", params = { "cancel" })
	public String cancelEmployeeEditing(long departmentId, @RequestParam("id") long employeeId) {

		logger.info(
				String.format("cancelEmployeeEditing(): department id[%d], employee id[%d]", departmentId, employeeId));
		return REDIR_LIST_EMPLOYEES_VIEW_NAME_FUN.apply(departmentId);
	}

	/**
	 * Starts the deleting of the {@link Employee}.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @param employeeId   the id of the {@link Employee}
	 * @param model        the {@link Model}
	 * @return the view name
	 */
	@GetMapping("/startEmployeeDeleting")
	public String startEmployeeDeleting(long departmentId, long employeeId, Model model) {

		Employee employee = null;
		try {
			employee = companyService.findEmployee(departmentId, employeeId);
		} catch (CompanyException ex) {
			logger.error(String.format("startEmployeeDeleting(): CompanyException[%s], department id[%d]",
					ex.getMessage(), departmentId));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_OR_EMP_NOT_FOUND_MSG, ex);
		}
		model.addAttribute(EMPLOYEE_ATTR_NAME, employee);
		logger.info(
				String.format("startEmployeeDeleting(): department id[%d], employee id[%d]", departmentId, employeeId));
		return EMPLOYEES_CONFIRM_DELETE_VIEW_NAME;
	}

	/**
	 * Deletes the {@link Employee}.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @param employeeId   the id of the {@link Employee}
	 * @return the view name
	 */
	@PostMapping(value = "/finishEmployeeDeleting", params = { "delete" })
	public String deleteEmployee(long departmentId, @RequestParam("id") long employeeId) {

		try {
			companyService.deleteEmployee(departmentId, employeeId);
		} catch (CompanyException ex) {
			logger.error(String.format("deleteEmployee(): CompanyException[%s], department id[%d]", ex.getMessage(),
					departmentId));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_OR_EMP_NOT_FOUND_MSG, ex);
		}
		logger.info(String.format("deleteEmployee(): department id[%d], employee id[%d]", departmentId, employeeId));
		return REDIR_LIST_EMPLOYEES_VIEW_NAME_FUN.apply(departmentId);
	}

	/**
	 * Cancels the deleting of the {@link Employee}.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @param employeeId   the id of the {@link Employee}
	 * @return the view name
	 */
	@PostMapping(value = "/finishEmployeeDeleting", params = { "cancel" })
	public String cancelEmployeeDeleting(long departmentId, @RequestParam("id") long employeeId) {

		logger.info(String.format("cancelEmployeeDeleting(): department id[%d], employee id[%d]", departmentId,
				employeeId));
		return REDIR_LIST_EMPLOYEES_VIEW_NAME_FUN.apply(departmentId);
	}
}
