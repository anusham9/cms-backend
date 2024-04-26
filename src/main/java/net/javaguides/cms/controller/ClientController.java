package net.javaguides.cms.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import net.javaguides.cms.dto.ClientDTO;
import net.javaguides.cms.dto.PasswordChangeDto;
import net.javaguides.cms.repository.RoleRepository;
import net.javaguides.cms.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controller for managing client profiles within the system. This controller handles all HTTP requests
 * related to client operations such as creating, retrieving, updating, approving, rejecting, and deleting client profiles.
 * Access to these operations is restricted based on user roles, ensuring that only authorized users can perform specific actions.
 *
 * @see ClientService for business logic and data access related to client operations.
 */

@RestController
@RequestMapping("/cms")
@AllArgsConstructor

public class ClientController {
  private ClientService clientService;

  /**
   * Creates a new client in the system. This endpoint requires the user to have an 'EMPLOYEE' role.
   * The method takes a {@link ClientDTO} object, creates a new client profile, and returns the created profile
   * with a status code of 201 (Created).
   *
   * @param clientDTO the client data transfer object containing information about the new client (cannot be null).
   * @return a {@link ResponseEntity} containing the newly created {@link ClientDTO} and the HTTP status.
   */

  @PostMapping("/clients")
  public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {

    ClientDTO savedClient = clientService.createClient(clientDTO);
    return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
  }

  /**
   * Retrieves a list of all clients in the system. Restricted to users with the 'EMPLOYEE' role. This method fetches and returns all client profiles,
   * allowing employees to view all registered clients.
   *
   * @return a {@link ResponseEntity} containing a list of {@link ClientDTO} and the HTTP status code.
   */

  @GetMapping("/clients")
  public ResponseEntity<List<ClientDTO>> getAllClients() {
    List<ClientDTO> clients = clientService.getClients();

    return new ResponseEntity<>(clients, HttpStatus.OK);
  }


  /**
   * Retrieves a client by their unique identifier. Accessible to users with 'EMPLOYEE' roles.
   * Returns the client details if found or an appropriate HTTP status code if the client does not exist.
   *
   * @param clientId the unique ID of the client to retrieve.
   * @return a {@link ResponseEntity} containing the {@link ClientDTO} if found, along with the HTTP status code.
   */


  @GetMapping("/clients/{id}")
  //Build get employee REST API
  public ResponseEntity<ClientDTO> getClientById(@PathVariable("id") Long clientId) {
    ClientDTO clientDTO = clientService.getClientById(clientId);
    return new ResponseEntity<>(clientDTO,HttpStatus.OK);
  }


  /**
   * Updates the information of an existing client. Requires 'CLIENT' or 'EMPLOYEE' role. This method allows updating client details
   * and returns the updated client profile.
   *
   * @param clientId the ID of the client to update.
   * @param updatedClient the updated client data as a {@link ClientDTO}.
   * @return a {@link ResponseEntity} with the updated client information and the corresponding HTTP status code.
   */


  @PutMapping("/client/{id}")
  public ResponseEntity<ClientDTO> updateClient(@PathVariable("id") Long clientId, @RequestBody ClientDTO updatedClient) {
    ClientDTO clientUpdate = clientService.updateClient(clientId, updatedClient);

    return new ResponseEntity<>(clientUpdate, HttpStatus.OK);
  }


  /**
   * Approves a client's status based on their unique identifier. Only accessible by users with the 'EMPLOYEE' role.
   * This method updates the client's status to 'approved' and returns the updated client profile.
   *
   * @param clientId the unique ID of the client whose status is to be approved.
   * @return a {@link ResponseEntity} containing the updated {@link ClientDTO} and the HTTP status code.
   */

  @PatchMapping("/clients/{id}/approve")
  public ResponseEntity<ClientDTO> approveClientStatus(@PathVariable("id") Long clientId) {
    ClientDTO clientDTO = clientService.updateStatusToApproved(clientId);
    return new ResponseEntity<>(clientDTO, HttpStatus.OK);
  }

  /**
   * Rejects a client's profile based on their unique identifier. Only accessible by users with the 'EMPLOYEE' role.
   * This method updates the client's status to 'rejected' and returns the updated client profile.
   *
   * @param clientId the unique ID of the client whose status is to be rejected.
   * @return a {@link ResponseEntity} containing the updated {@link ClientDTO} and the HTTP status code.
   */


  @PatchMapping("/clients/{id}/reject")
  public ResponseEntity<ClientDTO> rejectClientStatus(@PathVariable("id") Long clientId) {
    ClientDTO clientDTO = clientService.updateStatusToRejected(clientId);
    return new ResponseEntity<>(clientDTO, HttpStatus.OK);
  }

  /**
   * Deletes a client profile based on their unique identifier. Accessible only by users with the 'EMPLOYEE' role.
   * This method removes the client from the system and returns a confirmation message.
   *
   * @param clientId the unique ID of the client to be deleted.
   * @return a {@link ResponseEntity} with a confirmation message and the HTTP status code.
   */

  @DeleteMapping("/clients/{id}")
  public ResponseEntity<String> deleteClient(@PathVariable("id") Long clientId) {

    clientService.deleteClient(clientId);

    return ResponseEntity.ok("Deleted client from system successfully");
  }


  //CLIENTS ROLES

  /**
   * Updates a client's profile based on their unique identifier. Accessible only by users with the 'CLIENT' role.
   * This method allows clients to update their data.
   *
   * @param clientId the unique ID of the client to be updated.
   * @return a {@link ResponseEntity} containing the updated {@link ClientDTO} and the HTTP status code.
   */
  @PatchMapping("/profile/{id}")
  public ResponseEntity<ClientDTO> editProfile(@PathVariable("id") Long clientId, @RequestBody ClientDTO updatedClient) {
    ClientDTO clientDTO = clientService.updateClient(clientId, updatedClient);
    System.out.println(updatedClient);
    return new ResponseEntity<>(clientDTO, HttpStatus.OK);

  }

  /**
   * Retrieves a client's profile based on their unique identifier. Accessible only by users with the 'CLIENT' role.
   * This method allows clients to access their profile.
   *
   * @param clientId the unique ID of the client to be updated.
   * @return a {@link ResponseEntity} containing the updated {@link ClientDTO} and the HTTP status code.
   */
  @GetMapping("/profile/{id}")
  public ResponseEntity<ClientDTO> getProfile(@PathVariable("id") Long clientId) {
    ClientDTO clientDTO = clientService.getClientById(clientId);

    return new ResponseEntity<>(clientDTO, HttpStatus.OK);
  }

  /**
   * Changes the password of a user identified by their ID.
   *
   * Receives a PATCH request and updates the user's password based on the provided {@link PasswordChangeDto}.
   * A successful update returns a response with "Password updated successfully", otherwise, it returns
   * a BAD_REQUEST status indicating the password update failed.
   *
   * @param id The unique identifier of the user whose password is to be changed.
   * @param passwordChangeDto The password change details, encapsulated within a {@link PasswordChangeDto} object.
   * @return A {@link ResponseEntity<String>} object containing the outcome message.
   * It returns a 200 OK status on success or a 400 BAD_REQUEST status on failure.
   */


  @PatchMapping("/profile/{id}/change-password")
  public ResponseEntity<String> changePassword(@PathVariable("id") Long id, @RequestBody PasswordChangeDto passwordChangeDto) {

    boolean updated = clientService.changePassword(id, passwordChangeDto);
    if (updated) {
      return ResponseEntity.ok("Password updated successfully");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password update failed");
    }
  }
}

