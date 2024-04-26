package net.javaguides.cms.mapper;

import net.javaguides.cms.Enums.Status;
import net.javaguides.cms.dto.ClientDTO;
import net.javaguides.cms.entity.Client;

/**
 * Utility class for mapping between Client entity and ClientDTO objects.
 * This class provides methods to convert a Client entity to a ClientDTO and vice versa,
 * facilitating data transfer across different layers of the application.
 */
public class ClientMapper {

  /**
   * Converts a Client entity to a ClientDTO.
   * This method maps data from a Client entity to a ClientDTO object.
   *
   * @param client the Client entity to convert
   * @return the converted ClientDTO
   */
  public static ClientDTO mapToClientDto(Client client) {
    if (client == null) {
      return null;
    }

    ClientDTO clientDto = new ClientDTO();
    clientDto.setId(client.getId());
    clientDto.setFirstName(client.getFirstName());
    clientDto.setLastName(client.getLastName());
    clientDto.setEmail(client.getEmail());
    clientDto.setSSN(client.getSSN());
    clientDto.setUsername(client.getUsername());
    clientDto.setDateOfBirth(client.getDateOfBirth());
    clientDto.setStatus(client.getStatus());

    return clientDto;
  }

  /**
   * Converts a ClientDTO to a Client entity.
   * This method maps data from a ClientDTO to a Client entity.
   *
   * @param clientDto the ClientDTO to convert
   * @return the converted Client entity
   */
  public static Client mapToClient(ClientDTO clientDto) {
    if (clientDto == null) {
      return null;
    }

    Client client = new Client();
    client.setId(clientDto.getId());
    client.setFirstName(clientDto.getFirstName());
    client.setLastName(clientDto.getLastName());
    client.setEmail(clientDto.getEmail());
    client.setSSN(clientDto.getSSN());
    client.setUsername(clientDto.getUsername());
    client.setDateOfBirth(clientDto.getDateOfBirth());
    client.setStatus(clientDto.getStatus() != null ? clientDto.getStatus() : Status.Pending);

    return client;
  }
}