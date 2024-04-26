package net.javaguides.cms.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for Employee details.
 * This DTO is used to transfer employee data across different layers of the application
 * without exposing the entity directly.
 *
 * Includes:
 * - Basic user information inherited from the User entity such as username and email.
 * - Employee-specific information such as first name, last name, and department.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

  /**
   * Unique identifier for the Employee. This ID corresponds to the User ID.
   */
  private Long id;

  /**
   * Employee's first name.
   */
  private String firstName;

  /**
   * Employee's last name.
   */
  private String lastName;

  /**
   * Employee's username. Must be unique.
   */
  private String username;

  /**
   * Employee's email. Must be unique and not null.
   */
  private String email;

  /**
   * Employee's department.
   */
  private String department;

}