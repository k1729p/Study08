package kp.company.service;

import kp.company.domain.Department;
import kp.company.domain.Employee;
import kp.company.exception.CompanyException;
import kp.utils.SampleDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static kp.Constants.*;

/**
 * Implementation of {@link CompanyService}.
 */
@Service
public class CompanyServiceImpl implements CompanyService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Map<Long, Department> departmentMap = new TreeMap<>();

    private final AtomicLong departmentIdSequence = new AtomicLong(1);

    private final AtomicLong employeeIdSequence = new AtomicLong(1);

    /**
     * Default constructor.
     */
    public CompanyServiceImpl() {
        reloadData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reloadData() {
        SampleDataset.generateDepartments(departmentMap, departmentIdSequence, employeeIdSequence);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Department> getDepartmentCollection() {
        return departmentMap.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Department findDepartment(long departmentId) throws CompanyException {

        if (!departmentMap.containsKey(departmentId)) {
            final String message = DEP_NOT_FOUND_FUN.apply(departmentId);
            logger.error("findDepartment(): {}", message);
            throw new CompanyException(message);
        }
        return departmentMap.get(departmentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Department createDepartment() {
        return new Department();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveDepartment(Department department) {

        if (department.getId() == 0) {
            department.setId(departmentIdSequence.getAndIncrement());
            departmentMap.put(department.getId(), department);
        } else if (Optional.ofNullable(department.getName()).map(String::trim).isPresent()) {
            Optional.of(department.getId()).map(departmentMap::get)
                    .ifPresent(dep -> dep.setName(department.getName()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDepartment(long departmentId) throws CompanyException {

        if (!departmentMap.containsKey(departmentId)) {
            final String message = DEP_NOT_FOUND_FUN.apply(departmentId);
            logger.error("deleteDepartment(): {}", message);
            throw new CompanyException(message);
        }
        departmentMap.remove(departmentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Employee findEmployee(long departmentId, long employeeId) throws CompanyException {

        if (!departmentMap.containsKey(departmentId)) {
            final String message = DEP_NOT_FOUND_FUN.apply(departmentId);
            logger.error("findEmployee(): {}", message);
            throw new CompanyException(message);
        }
        final Optional<Employee> employeeOpt = Optional.ofNullable(departmentMap.get(departmentId))
                .map(dep -> dep.getEmployee(employeeId));
        if (employeeOpt.isEmpty()) {
            final String message = EMP_NOT_FOUND_FUN.apply(employeeId);
            logger.error("findEmployee(): {}", message);
            throw new CompanyException(message);
        }
        return employeeOpt.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Employee createEmployee(long departmentId) throws CompanyException {

        if (!departmentMap.containsKey(departmentId)) {
            final String message = DEP_NOT_FOUND_FUN.apply(departmentId);
            logger.error("createEmployee(): {}", message);
            throw new CompanyException(message);
        }
        final Employee employee = new Employee();
        employee.setDepartmentId(departmentId);
        employee.setTitle(CompanyService.getTitleList().getFirst());
        return employee;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveEmployee(Employee employee) throws CompanyException {

        final long departmentId = Objects.nonNull(employee) ? employee.getDepartmentId() : -1;
        if (!departmentMap.containsKey(departmentId)) {
            final String message = DEP_NOT_FOUND_FUN.apply(departmentId);
            logger.error("saveEmployee(): {}", message);
            throw new CompanyException(message);
        }
        if (Objects.nonNull(employee) && employee.getId() == 0) {
            employee.setId(employeeIdSequence.getAndIncrement());
        }
        departmentMap.get(departmentId).putEmployee(employee);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEmployee(long departmentId, long employeeId) throws CompanyException {

        if (!departmentMap.containsKey(departmentId)) {
            final String message = DEP_NOT_FOUND_FUN.apply(departmentId);
            logger.error("deleteEmployee(): {}", message);
            throw new CompanyException(message);
        }
        final Optional<Employee> employeeOpt = Optional.ofNullable(departmentMap.get(departmentId))
                .map(dep -> dep.getEmployee(employeeId));
        if (employeeOpt.isEmpty()) {
            final String message = EMP_NOT_FOUND_FUN.apply(employeeId);
            logger.error("deleteEmployee(): {}", message);
            throw new CompanyException(message);
        }
        departmentMap.get(departmentId).removeEmployee(employeeId);
    }
}