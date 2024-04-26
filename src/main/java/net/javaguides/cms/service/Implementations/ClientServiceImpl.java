package net.javaguides.cms.service.Implementations;

import java.util.Arrays;
import java.util.HashSet;
import lombok.AllArgsConstructor;
import net.javaguides.cms.Enums.Status;
import java.util.List;
import java.util.stream.Collectors;
import net.javaguides.cms.dto.ClientDTO;
import net.javaguides.cms.dto.PasswordChangeDto;
import net.javaguides.cms.entity.Client;
import net.javaguides.cms.entity.Role;
import net.javaguides.cms.exception.ResourceNotFoundException;
import net.javaguides.cms.mapper.ClientMapper;
import net.javaguides.cms.repository.ClientRepository;
import net.javaguides.cms.repository.RoleRepository;
import net.javaguides.cms.service.ClientService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

  private ClientRepository clientRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;
  //for new client

  @Transactional
  @Override
  public ClientDTO createClient(ClientDTO clientDTO) {
    Client client = ClientMapper.mapToClient(clientDTO);

    //initialize password
    String initialPassword = "defaultClientPassword";
    client.setPassword(passwordEncoder.encode(initialPassword));

    Role clientRole = roleRepository.findByName("ROLE_CLIENT");
    client.setRoles(new HashSet<>(Arrays.asList(clientRole)));
    Client savedClient = clientRepository.save(client);

    return ClientMapper.mapToClientDto(savedClient);
    //add error message here if someone tries to create a duplicate w/email and SSN
  }


  @Override
  public ClientDTO getClientById(Long clientId) {
    ClientDTO client = ClientMapper.mapToClientDto(clientRepository.findById(clientId).
        orElseThrow(()->new ResourceNotFoundException("Client does not exist by the given id "
        + clientId)));

    return client;
  }

  @Override
  public List<ClientDTO> getClients() {

    List<Client> clients = clientRepository.findAll();
//   return clients.stream().map(client -> {
//      return ClientMapper.mapToClientDto(client);
//    }).collect(Collectors.toList());

    return clients.stream().map(ClientMapper::mapToClientDto).collect(Collectors.toList());
  }


  //if client wants to update their intake
  @Transactional
  @Override
  public ClientDTO updateClient(Long clientId, ClientDTO updatedClient) {
    Client client = clientRepository.findById(clientId).orElseThrow(()->
        new ResourceNotFoundException("Client does not exist with given id"));

    client.setFirstName(updatedClient.getFirstName());
    client.setLastName(updatedClient.getLastName());
    client.setEmail(updatedClient.getEmail());

    //save this client object with save method
    //performs save and update operations
    Client updatedClientObject = clientRepository.save(client);
    return ClientMapper.mapToClientDto((updatedClientObject));
  }

  @Override
  public void deleteClient(Long clientId) {
    clientRepository.deleteById(clientId);
  }

  @Override
  public ClientDTO updateStatusToApproved(Long clientId) {
    Client client = clientRepository.findById(clientId)
        .orElseThrow(()->new ResourceNotFoundException("This client is not in our system"));

    client.setStatus(Status.Approved);
    return ClientMapper.mapToClientDto(client);
  }


  @Transactional
  @Override
  public ClientDTO updateStatusToRejected(Long clientId) {
    Client client = clientRepository.findById(clientId)
        .orElseThrow(()->new ResourceNotFoundException("This client is not in our system"));

    client.setStatus(Status.Rejected);
    return ClientMapper.mapToClientDto(client);
  }

  @Transactional
  @Override
  public boolean changePassword(Long id, PasswordChangeDto passwordChangeDto) {
    Client client = clientRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    // Check if old password matches
    if (!passwordEncoder.matches(passwordChangeDto.getOldPassword(), client.getPassword())) {
      return false;
    }

    // Set new password
    client.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
    //clientRepository.save(client);
    return true;
  }

}
