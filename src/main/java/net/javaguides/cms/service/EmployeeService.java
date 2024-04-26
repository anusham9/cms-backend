package net.javaguides.cms.service;

import java.util.List;
import net.javaguides.cms.dto.EmployeeDto;
import net.javaguides.cms.dto.PasswordChangeDto;

public interface EmployeeService {

  /**
   * Creates a new employee in the system.
   *
   * @param clientDTO The employee data transfer object containing the details of the employee to be created.
   * @return The created EmployeeDto with the generated ID and other details as persisted.
   */

  EmployeeDto createEmployee(EmployeeDto clientDTO);

  /**
   * Retrieves an employee by their ID.
   *
   * @param employeeId The unique identifier of the employee.
   * @return The EmployeeDto containing all details of the found employee or null if no employee is found.
   */

  EmployeeDto getEmployeeById(Long employeeId);

  /**
   * Retrieves a list of all employees in the system.
   *
   * @return A list of EmployeeDto representing all employees.
   */

  List<EmployeeDto> getEmployees();

  /**
   * Updates the details of an existing employee.
   *
   * @param clientId The ID of the employee to update.
   * @param updatedClient The updated employee details encapsulated in an EmployeeDto.
   * @return The updated EmployeeDto.
   */

  EmployeeDto updateEmployee(Long clientId, EmployeeDto updatedClient);

  /**
   * Deletes an employee by their ID.
   *
   * @param employeeId The unique identifier of the employee to be deleted.
   */

  void deleteEmployee(Long employeeId);

  /**
   * Changes the password of an employee.
   *
   * @param employeeId The unique identifier of the employee whose password is to be changed.
   * @param passwordChangeDto The DTO containing the new password details.
   * @return true if the password was successfully changed, false otherwise.
   */


  boolean changePassword(Long employeeId, PasswordChangeDto passwordChangeDto);


}