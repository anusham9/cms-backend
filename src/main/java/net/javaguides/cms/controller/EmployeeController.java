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

  //Build POST API to create new employee

  /**
   * Creates new employee
   * @param employeeDto
   * @return
   */
  @PostMapping("/employees")
  public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
    EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);

    return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
  }

  /**
   * Gets a list of all employees
   * @return
   */
  @GetMapping("/employees")
  public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
    List<EmployeeDto> allEmployees = employeeService.getEmployees();

    return new ResponseEntity<>(allEmployees, HttpStatus.OK);
  }

  //Build GET API for one employee

  /**
   * gets an employee's profile
   * @param employeeId
   * @return
   */

  @GetMapping("/employees/{id}")
  public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") Long employeeId) {
    EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);

    return new ResponseEntity<>(employeeDto,HttpStatus.OK);
  }

  /**
   * deletes an employee
   * @param employeeId
   * @return
   */
  @DeleteMapping("/employees/{id}")
  public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId) {
    employeeService.deleteEmployee(employeeId);

    return new ResponseEntity<>("Employee deleted from system!", HttpStatus.OK);
  }

  /**
   * Modifies the employee's details, like their first name, last name, or
   * @param employeeId
   * @param updatedEmployee
   * @return
   */
  @PutMapping("/employees/{id}")
  public ResponseEntity<EmployeeDto> updateEmployeeRecord(@PathVariable("id") Long employeeId, @RequestBody EmployeeDto updatedEmployee) {
    EmployeeDto employeeDto = employeeService.updateEmployee(employeeId, updatedEmployee);
    return new ResponseEntity<>(employeeDto,HttpStatus.OK);
  }

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
