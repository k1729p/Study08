package kp.company.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a department in the company.
 */
public class Department {

    private long id;

    @NotBlank
    @Size(min = 1, max = 20)
    private String name;

    private final Map<Long, Employee> employeeMap = new TreeMap<>();

    /**
     * Gets the id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns an {@link Employee} by their id.
     *
     * @param employeeId the id of the {@link Employee}
     * @return the {@link Employee}
     */
    public Employee getEmployee(long employeeId) {
        return employeeMap.get(employeeId);
    }

    /**
     * Adds an {@link Employee} to the department.
     *
     * @param employee the {@link Employee} to put
     */
    public void putEmployee(Employee employee) {
        employeeMap.put(employee.getId(), employee);
    }

    /**
     * Removes an {@link Employee} from the department by their id.
     *
     * @param employeeId the id of the {@link Employee}
     */
    public void removeEmployee(long employeeId) {
        employeeMap.remove(employeeId);
    }

    /**
     * Returns a collection of all {@link Employee}s in the department.
     * <p>
     * This method is used on the template "employees/edit.html".
     * </p>
     *
     * @return the collection of the {@link Employee}s
     */
    public Collection<Employee> getEmployeeCollection() {
        return employeeMap.values();
    }
}