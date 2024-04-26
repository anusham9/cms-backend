package net.javaguides.cms.entity;

/**
 * Represents a client in the system.
 * This class extends {@link User}, inheriting user-related properties and functionalities.
 * It is annotated with JPA annotations to define its mapping to a database table.
 *
 */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends User{

  /**
   * The Social Security Number (SSN) of the client.
   * This field is unique for each client and cannot be null.
   */


  @Column(name = "SSN", nullable = false, unique = true)
  private String SSN;
  /**
   * The date of birth of the client.
   * This field is required and cannot be null.
   */

  @Column(name = "dateOfBirth",nullable = false)
  private Date dateOfBirth;

  /**
   * The current status of the client.
   * This field cannot be null and utilizes an enum for status values.
   */

  @Column(name = "Status", nullable = false)
  private net.javaguides.cms.Enums.Status Status;

}
