package net.javaguides.cms.service;

import java.util.List;
import net.javaguides.cms.dto.ClientDTO;
import net.javaguides.cms.dto.PasswordChangeDto;

/**
 * Service interface for managing clients within the system.
 * Provides functionality to create, retrieve, update, and delete client information,
 * as well as to update client statuses.
 */
public interface ClientService {

  /**
   * Service interface for managing clients within the system.
   * Provides functionality to create, retrieve, update, and delete client information,
   * as well as to update client statuses.
   */

  ClientDTO createClient(ClientDTO clientDTO);

  /**
   * Service interface for managing clients within the system.
   * Provides functionality to create, retrieve, update, and delete client information,
   * as well as to a client statuses.
   */
  ClientDTO getClientById(Long clientId);
  /**
   * Retrieves a list of all clients in the system. This method is typically used by employees
   * to view all client entries.
   *
   * @return a list of client data transfer objects representing all clients
   */
  List<ClientDTO> getClients();

  /**
   * Updates the existing client's information with the provided updated data.
   *
   * @param clientId the unique identifier of the client to update
   * @param updatedClient the client data transfer object that contains the updated information
   * @return the updated client data transfer object
   */
  ClientDTO updateClient(Long clientId, ClientDTO updatedClient);

  /**
   * Deletes a client from the system based on the client's ID. Typically restricted to employee use.
   *
   * @param clientId the unique identifier of the client to be deleted
   */
  void deleteClient(Long clientId);

  /**
   * Approves a client's status in the system. This action is typically performed by an employee.
   *
   * @param clientId the unique identifier of the client whose status is to be approved
   * @return the updated client data transfer object with the status set to approved
   */
  ClientDTO updateStatusToApproved(Long clientId);

  /**
   * Rejects a client's status in the system. This action is typically performed by an employee.
   *
   * @param clientId the unique identifier of the client whose status is to be rejected
   * @return the updated client data transfer object with the status set to rejected
   */
  ClientDTO updateStatusToRejected(Long clientId);


  /**
   * Changes the password of a client.
   *
   * @param id The unique identifier of the employee whose password is to be changed.
   * @param passwordChangeDto The DTO containing the new password details.
   * @return true if the password was successfully changed, false otherwise.
   */

  boolean changePassword(Long id, PasswordChangeDto passwordChangeDto);

}
