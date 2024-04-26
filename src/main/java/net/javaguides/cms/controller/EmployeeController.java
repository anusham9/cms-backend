package net.javaguides.cms.controller;


import java.util.List;
import lombok.AllArgsConstructor;
import net.javaguides.cms.dto.EmployeeDto;
import net.javaguides.cms.dto.PasswordChangeDto;
import net.javaguides.cms.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cms")
@AllArgsConstructor

public class EmployeeController {

  private EmployeeService employeeService;

   /**
   * Creates a new employee using the provided employee data transfer object (DTO).
   * 
   * @param employeeDto the DTO containing data for the new employee
   * @return a {@link ResponseEntity} containing the created employee DTO and the HTTP status code
   */ 
  @PostMapping("/employees")
  public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
    EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);

    return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
  }

  /**
   * Retrieves a list of all employees.
   * 
   * @return a {@link ResponseEntity} containing a list of employee DTOs and the HTTP status code
   */
  @GetMapping("/employees")
  public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
    List<EmployeeDto> allEmployees = employeeService.getEmployees();

    return new ResponseEntity<>(allEmployees, HttpStatus.OK);
  }

  //Build GET API for one employee

  /**
   * Retrieves the profile of a specific employee identified by their unique ID.
   * 
   * @param employeeId the unique ID of the employee to retrieve
   * @return a {@link ResponseEntity} containing the employee DTO and the HTTP status code
   */
  @GetMapping("/employees/{id}")
  public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") Long employeeId) {
    EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);

    return new ResponseEntity<>(employeeDto,HttpStatus.OK);
  }

  /**
   * Deletes an employee from the system based on their unique ID.
   * 
   * @param employeeId the unique ID of the employee to delete
   * @return a {@link ResponseEntity} with a confirmation message and the HTTP status code
   */
  @DeleteMapping("/employees/{id}")
  public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId) {
    employeeService.deleteEmployee(employeeId);

    return new ResponseEntity<>("Employee deleted from system!", HttpStatus.OK);
  }

 /**
   * Updates the details of an existing employee identified by their unique ID.
   * 
   * @param employeeId Long unique ID of the employee to update
   * @param updatedEmployee the employee DTO containing updated fields
   * @return a {@link ResponseEntity} containing the updated employee DTO and the HTTP status code
   */
  
  @PutMapping("/employees/{id}")
  public ResponseEntity<EmployeeDto> updateEmployeeRecord(@PathVariable("id") Long employeeId, @RequestBody EmployeeDto updatedEmployee) {
    EmployeeDto employeeDto = employeeService.updateEmployee(employeeId, updatedEmployee);
    return new ResponseEntity<>(employeeDto,HttpStatus.OK);
  }

  /**
   * Changes the password for an employee identified by their unique ID.
   * 
   * @param id Long unique ID of the employee whose password is to be changed
   * @param passwordChangeDto the DTO containing the new password details
   * @return a {@link ResponseEntity} with a success or failure message and the HTTP status code
   */
  
  @PatchMapping("/employees/{id}/change-password")
  public ResponseEntity<String> changePassword(@PathVariable("id") Long id, @RequestBody PasswordChangeDto passwordChangeDto) {

    boolean updated = employeeService.changePassword(id, passwordChangeDto);
    if (updated) {
      return ResponseEntity.ok("Password updated successfully");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password update failed");
    }
  }
}
