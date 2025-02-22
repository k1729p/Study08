package kp.company.controller;

import jakarta.validation.Valid;
import kp.company.domain.Department;
import kp.company.domain.Employee;
import kp.company.domain.Title;
import kp.company.exception.CompanyException;
import kp.company.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.lang.invoke.MethodHandles;
import java.util.List;

import static kp.Constants.*;

/**
 * The web controller for managing {@link Employee} entities.
 */
@Controller
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final CompanyService companyService;

    /**
     * Parameterized constructor.
     *
     * @param companyService the {@link CompanyService} to be used
     */
    public EmployeeController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Retrieves the list of {@link Title}s.
     *
     * @return the list of titles
     */
    @ModelAttribute("titles")
    public List<Title> listTitles() {

        return CompanyService.getTitleList();
    }

    /**
     * Retrieves the list of {@link Employee}s for the specified department.
     *
     * @param departmentId the id of the {@link Department}
     * @param model        the {@link Model}
     * @return the view name
     */
    @GetMapping("/listEmployees")
    public String listEmployees(long departmentId, Model model) {

        Department department;
        try {
            department = companyService.findDepartment(departmentId);
        } catch (CompanyException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_NOT_FOUND_MSG, ex);
        }
        model.addAttribute(DEPARTMENT_ATTR_NAME, department);
        logger.info("listEmployees(): department id[{}]", department.getId());
        return EMPLOYEES_LIST_VIEW_NAME;
    }

    /**
     * Initiates the process of adding a new {@link Employee} to the specified department.
     *
     * @param departmentId the id of the {@link Department}
     * @param model        the {@link Model}
     * @return the view name
     */
    @GetMapping("/startEmployeeAdding")
    public String startEmployeeAdding(long departmentId, Model model) {

        Employee employee;
        try {
            employee = companyService.createEmployee(departmentId);
        } catch (CompanyException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_NOT_FOUND_MSG, ex);
        }
        model.addAttribute(EMPLOYEE_ATTR_NAME, employee);
        logger.info("startEmployeeAdding(): department id[{}]", departmentId);
        return EMPLOYEES_EDIT_VIEW_NAME;
    }

    /**
     * Initiates the process of editing an existing {@link Employee}.
     *
     * @param departmentId the id of the {@link Department}
     * @param employeeId   the id of the {@link Employee}
     * @param model        the {@link Model}
     * @return the view name
     */
    @GetMapping("/startEmployeeEditing")
    public String startEmployeeEditing(long departmentId, long employeeId, Model model) {

        Employee employee;
        try {
            employee = companyService.findEmployee(departmentId, employeeId);
        } catch (CompanyException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_OR_EMP_NOT_FOUND_MSG, ex);
        }
        model.addAttribute(EMPLOYEE_ATTR_NAME, employee);
        logger.info("startEmployeeEditing(): department id[{}], employee id[{}]", departmentId, employeeId);
        return EMPLOYEES_EDIT_VIEW_NAME;
    }

    /**
     * Saves the specified {@link Employee}.
     *
     * @param employee      the {@link Employee}
     * @param bindingResult the {@link BindingResult}
     * @return the view name
     */
    @PostMapping(value = "/finishEmployeeEditing", params = {"save"})
    public String saveEmployee(@Valid Employee employee, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final List<String> messagesList = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                    .toList();
            logger.info("saveEmployee(): error messages{}", messagesList);
            return EMPLOYEES_EDIT_VIEW_NAME;
        }
        try {
            companyService.saveEmployee(employee);
        } catch (CompanyException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_NOT_FOUND_MSG, ex);
        }
        logger.info("saveEmployee(): department id[{}}], employee id[{}], title name[{}]",
                employee.getDepartmentId(), employee.getId(), employee.getTitle().getName());
        return REDIRECT_LIST_EMPLOYEES_VIEW_NAME_FUN.apply(employee.getDepartmentId());
    }

    /**
     * Cancels the editing of the specified {@link Employee}.
     *
     * @param departmentId the id of the {@link Department}
     * @param employeeId   the id of the {@link Employee}
     * @return the view name
     */
    @PostMapping(value = "/finishEmployeeEditing", params = {"cancel"})
    public String cancelEmployeeEditing(long departmentId, @RequestParam("id") long employeeId) {

        logger.info("cancelEmployeeEditing(): department id[{}], employee id[{}]", departmentId, employeeId);
        return REDIRECT_LIST_EMPLOYEES_VIEW_NAME_FUN.apply(departmentId);
    }

    /**
     * Initiates the process of deleting the specified {@link Employee}.
     *
     * @param departmentId the id of the {@link Department}
     * @param employeeId   the id of the {@link Employee}
     * @param model        the {@link Model}
     * @return the view name
     */
    @GetMapping("/startEmployeeDeleting")
    public String startEmployeeDeleting(long departmentId, long employeeId, Model model) {

        Employee employee;
        try {
            employee = companyService.findEmployee(departmentId, employeeId);
        } catch (CompanyException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_OR_EMP_NOT_FOUND_MSG, ex);
        }
        model.addAttribute(EMPLOYEE_ATTR_NAME, employee);
        logger.info("startEmployeeDeleting(): department id[{}], employee id[{}]", departmentId, employeeId);
        return EMPLOYEES_CONFIRM_DELETE_VIEW_NAME;
    }

    /**
     * Deletes the specified {@link Employee}.
     *
     * @param departmentId the id of the {@link Department}
     * @param employeeId   the id of the {@link Employee}
     * @return the view name
     */
    @PostMapping(value = "/finishEmployeeDeleting", params = {"delete"})
    public String deleteEmployee(long departmentId, @RequestParam("id") long employeeId) {

        try {
            companyService.deleteEmployee(departmentId, employeeId);
        } catch (CompanyException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_OR_EMP_NOT_FOUND_MSG, ex);
        }
        logger.info("deleteEmployee(): department id[{}], employee id[{}]", departmentId, employeeId);
        return REDIRECT_LIST_EMPLOYEES_VIEW_NAME_FUN.apply(departmentId);
    }

    /**
     * Cancels the deleting of the specified {@link Employee}.
     *
     * @param departmentId the id of the {@link Department}
     * @param employeeId   the id of the {@link Employee}
     * @return the view name
     */
    @PostMapping(value = "/finishEmployeeDeleting", params = {"cancel"})
    public String cancelEmployeeDeleting(long departmentId, @RequestParam("id") long employeeId) {

        logger.info("cancelEmployeeDeleting(): department id[{}], employee id[{}]", departmentId, employeeId);
        return REDIRECT_LIST_EMPLOYEES_VIEW_NAME_FUN.apply(departmentId);
    }
}
