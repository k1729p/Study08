package kp.company.controller;

import static kp.Constants.DEPARTMENTS_CONFIRM_DELETE_VIEW_NAME;
import static kp.Constants.DEPARTMENTS_EDIT_VIEW_NAME;
import static kp.Constants.DEPARTMENTS_LIST_VIEW_NAME;
import static kp.Constants.DEPARTMENT_ATTR_NAME;
import static kp.Constants.DEP_NOT_FOUND_MSG;
import static kp.Constants.REDIRECT_LIST_DEPARTMENTS_VIEW_NAME;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import kp.company.domain.Department;
import kp.company.exception.CompanyException;
import kp.company.service.CompanyService;

/**
 * The web controller for the {@link Department}.
 * 
 */
@Controller
public class DepartmentController {
	private static final Log logger = LogFactory.getLog(MethodHandles.lookup().lookupClass().getName());

	private final CompanyService companyService;

	/**
	 * The constructor.
	 * 
	 * @param companyService the {@link CompanyService}
	 */
	public DepartmentController(CompanyService companyService) {
		super();
		this.companyService = companyService;
	}

	/**
	 * Gets the list of the {@link Department}s.
	 * 
	 * @param model the {@link Model}
	 * @return the view name
	 */
	@GetMapping("/listDepartments")
	public String listDepartments(Model model) {

		model.addAttribute("departmentCollection", companyService.getDepartmentCollection());
		logger.info("listDepartments():");
		return DEPARTMENTS_LIST_VIEW_NAME;
	}

	/**
	 * Starts the adding of the {@link Department}.
	 * 
	 * @param model the {@link Model}
	 * @return the view name
	 */
	@GetMapping("/startDepartmentAdding")
	public String startDepartmentAdding(Model model) {

		final Department department = companyService.createDepartment();
		model.addAttribute(DEPARTMENT_ATTR_NAME, department);
		logger.info("startDepartmentAdding():");
		return DEPARTMENTS_EDIT_VIEW_NAME;
	}

	/**
	 * Starts the editing of the {@link Department}.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @param model        the {@link Model}
	 * @return the view name
	 */
	@GetMapping("/startDepartmentEditing")
	public String startDepartmentEditing(long departmentId, Model model) {

		Department department;
		try {
			department = companyService.findDepartment(departmentId);
		} catch (CompanyException ex) {
			logger.error(String.format("startDepartmentEditing(): CompanyException[%s], department id[%d]",
					ex.getMessage(), departmentId));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_NOT_FOUND_MSG, ex);
		}
		model.addAttribute(DEPARTMENT_ATTR_NAME, department);
		logger.info(String.format("startDepartmentEditing(): department id[%d]", department.getId()));
		return DEPARTMENTS_EDIT_VIEW_NAME;
	}

	/**
	 * Saves the {@link Department}.
	 * 
	 * @param department    the {@link Department}
	 * @param bindingResult the {@link BindingResult}
	 * @return the view name
	 */
	@PostMapping(value = "/finishDepartmentEditing", params = { "save" })
	public String saveDepartment(@Valid Department department, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			final List<String> messagesList = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage)
					.toList();
			logger.info(String.format("saveDepartment(): error messages%s", messagesList));
			return DEPARTMENTS_EDIT_VIEW_NAME;
		}
		companyService.saveDepartment(department);
		logger.info(String.format("saveDepartment(): department id[%d]", department.getId()));
		return REDIRECT_LIST_DEPARTMENTS_VIEW_NAME;
	}

	/**
	 * Cancels the editing of the {@link Department}.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @return the view name
	 */
	@PostMapping(value = "/finishDepartmentEditing", params = { "cancel" })
	public String cancelDepartmentEditing(@RequestParam("id") long departmentId) {

		logger.info(String.format("cancelDepartmentEditing(): department id[%d]", departmentId));
		return REDIRECT_LIST_DEPARTMENTS_VIEW_NAME;
	}

	/**
	 * Starts the deleting of the {@link Department}.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @param model        the {@link Model}
	 * @return the view name
	 */
	@GetMapping("/startDepartmentDeleting")
	public String startDepartmentDeleting(long departmentId, Model model) {

		Department department;
		try {
			department = companyService.findDepartment(departmentId);
		} catch (CompanyException ex) {
			logger.error(String.format("startDepartmentDeleting(): CompanyException[%s], department id[%d]",
					ex.getMessage(), departmentId));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_NOT_FOUND_MSG, ex);
		}
		model.addAttribute(DEPARTMENT_ATTR_NAME, department);
		logger.info(String.format("startDepartmentDeleting(): department id[%d]", departmentId));
		return DEPARTMENTS_CONFIRM_DELETE_VIEW_NAME;
	}

	/**
	 * Deletes the {@link Department}.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @return the view name
	 */
	@PostMapping(value = "/finishDepartmentDeleting", params = { "delete" })
	public String deleteDepartment(@RequestParam("id") long departmentId) {

		try {
			companyService.deleteDepartment(departmentId);
		} catch (CompanyException ex) {
			logger.error(String.format("deleteDepartment(): CompanyException[%s], department id[%d]", ex.getMessage(),
					departmentId));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_NOT_FOUND_MSG, ex);
		}
		logger.info(String.format("deleteDepartment(): department id[%d]", departmentId));
		return REDIRECT_LIST_DEPARTMENTS_VIEW_NAME;
	}

	/**
	 * Cancels the deleting of the {@link Department}.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @return the view name
	 */
	@PostMapping(value = "/finishDepartmentDeleting", params = { "cancel" })
	public String cancelDepartmentDeleting(@RequestParam("id") long departmentId) {

		logger.info(String.format("cancelDepartmentDeleting(): department id[%d]", departmentId));
		return REDIRECT_LIST_DEPARTMENTS_VIEW_NAME;
	}
}
