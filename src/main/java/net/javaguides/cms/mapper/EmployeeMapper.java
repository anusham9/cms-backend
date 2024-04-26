package net.javaguides.cms.mapper;

import net.javaguides.cms.dto.EmployeeDto;
import net.javaguides.cms.entity.Employee;

/**
 * Utility class for mapping between Employee entity and EmployeeDTO objects.
 * This class provides methods to convert an Employee entity to an EmployeeDTO and vice versa.
 */
public class EmployeeMapper {

  /**
   * Converts an Employee entity to an EmployeeDTO.
   * This method maps data from an Employee entity to an EmployeeDTO object.
   *
   * @param employee the Employee entity to convert
   * @return the converted EmployeeDTO
   */
  public static EmployeeDto mapToEmployeeDto(Employee employee) {
    if (employee == null) {
      return null;
    }

    EmployeeDto employeeDto = new EmployeeDto();
    employeeDto.setId(employee.getId());
    employeeDto.setFirstName(employee.getFirstName());
    employeeDto.setLastName(employee.getLastName());
    employeeDto.setUsername(employee.getUsername());  // Assuming username is part of User
    employeeDto.setEmail(employee.getEmail());
    employeeDto.setDepartment(employee.getDepartment());

    return employeeDto;
  }

  /**
   * Converts an EmployeeDTO to an Employee entity.
   * This method maps data from an EmployeeDTO to an Employee entity.
   *
   * @param employeeDto the EmployeeDTO to convert
   * @return the converted Employee entity
   */
  public static Employee mapToEmployee(EmployeeDto employeeDto) {
    if (employeeDto == null) {
      return null;
    }

    Employee employee = new Employee();
    employee.setId(employeeDto.getId());
    employee.setFirstName(employeeDto.getFirstName());
    employee.setLastName(employeeDto.getLastName());
    employee.setUsername(employeeDto.getUsername());  // Set username, handle with care
    employee.setEmail(employeeDto.getEmail());
    employee.setDepartment(employeeDto.getDepartment());

    return employee;
  }
}