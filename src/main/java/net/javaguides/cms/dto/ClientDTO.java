package net.javaguides.cms.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for client details.
 * This class is used to transfer client data between processes or through network interfaces, abstracting
 * the client details from the entity model and providing a simplified client object.
 * It includes all necessary client information like name, username, email, SSN, date of birth, and status.
 *
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

  /**
   * The unique identifier for the client.
   */

  private long id;

  /**
   * The first name of the client.
   */

  private String firstName;
  /**
   * The last name of the client.
   */
  private String lastName;
  /**
   * The username of the client.
   */
  private String username;
  /**
   * The email address of the client.
   */
  private String email;

  /**
   * The Social Security Number (SSN) of the client.
   * This is a sensitive piece of data uniquely identifying the client.
   */
  private String SSN;

  /**
   * The date of birth of the client.
   */
  private Date dateOfBirth;

  /**
   * The current status of the client, represented by an enum.
   */

  private net.javaguides.cms.Enums.Status Status;

}