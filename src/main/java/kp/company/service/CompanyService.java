package kp.company.service;

import kp.company.domain.Department;
import kp.company.domain.Employee;
import kp.company.domain.Title;
import kp.company.exception.CompanyException;

import java.util.Collection;
import java.util.List;

/**
 * The interface for the company service.
 */
public interface CompanyService {

    /**
     * Retrieves the list of {@link Title}s.
     *
     * @return an unmodifiable list of {@link Title}s
     */
    static List<Title> getTitleList() {
        return List.of(Title.ANALYST, Title.DEVELOPER, Title.MANAGER);
    }

    /**
     * Reloads the company service data.
     */
    void reloadData();

    /**
     * Retrieves the {@link Collection} of {@link Department}s.
     *
     * @return a {@link Collection} of {@link Department}s
     */
    Collection<Department> getDepartmentCollection();

    /**
     * Finds a {@link Department} by its id.
     *
     * @param departmentId the id of the {@link Department}
     * @return the {@link Department}
     * @throws CompanyException if the department is not found
     */
    Department findDepartment(long departmentId) throws CompanyException;

    /**
     * Creates a new {@link Department}.
     *
     * @return the new {@link Department}
     */
    Department createDepartment();

    /**
     * Saves the given {@link Department}.
     *
     * @param department the {@link Department} to save
     */
    void saveDepartment(Department department);

    /**
     * Deletes a {@link Department} by its id.
     *
     * @param departmentId the id of the {@link Department}
     * @throws CompanyException if the department is not found
     */
    void deleteDepartment(long departmentId) throws CompanyException;

    /**
     * Finds an {@link Employee} by the id of the {@link Department} and the id of the {@link Employee}.
     *
     * @param departmentId the id of the {@link Department}
     * @param employeeId   the id of the {@link Employee}
     * @return the {@link Employee}
     * @throws CompanyException if the employee is not found
     */
    Employee findEmployee(long departmentId, long employeeId) throws CompanyException;

    /**
     * Creates a new {@link Employee} for the given department id.
     *
     * @param departmentId the id of the {@link Department}
     * @return the new {@link Employee}
     * @throws CompanyException if the employee cannot be created
     */
    Employee createEmployee(long departmentId) throws CompanyException;

    /**
     * Saves the given {@link Employee}.
     *
     * @param employee the {@link Employee} to save
     * @throws CompanyException if the employee cannot be saved
     */
    void saveEmployee(Employee employee) throws CompanyException;

    /**
     * Deletes an {@link Employee} by the id of the {@link Department} and the id of the {@link Employee}.
     *
     * @param departmentId the id of the {@link Department}
     * @param employeeId   the id of the {@link Employee}
     * @throws CompanyException if the employee is not found
     */
    void deleteEmployee(long departmentId, long employeeId) throws CompanyException;
}