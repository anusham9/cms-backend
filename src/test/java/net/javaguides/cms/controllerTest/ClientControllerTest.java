package net.javaguides.cms.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import net.javaguides.cms.Enums.Status;
import net.javaguides.cms.dto.ClientDTO;
import net.javaguides.cms.dto.PasswordChangeDto;
import net.javaguides.cms.service.ClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for client-related operations within the CMS application.
 * Uses Mockito for mocking dependencies and Spring's MockMvc to perform and assert web requests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private ClientService clientService;

  /**
   * Test for creating a client successfully with the role EMPLOYEE.
   * Asserts that the response status is CREATED and prints the result.
   * @throws Exception if there's an error during request processing.
   */
  @Test
  @WithMockUser(
      roles = {"EMPLOYEE"}
  )
  public void testCreateClient() throws Exception {
    ClientDTO newClient = new ClientDTO(1L, "John", "Doe", "johndoe", "johndoe@example.com", "1234567890", (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")).parse("1990-01-01T00:00:00.000+00:00"), Status.Pending);
    Mockito.when(this.clientService.createClient(ArgumentMatchers.any())).thenReturn(newClient);
    this.mockMvc.perform(MockMvcRequestBuilders.post("/cms/clients").contentType(MediaType.APPLICATION_JSON).content((new ObjectMapper()).writeValueAsString(newClient))).andExpect(MockMvcResultMatchers.status().isCreated()).andDo(MockMvcResultHandlers.print());
  }

  /**
   * Test for attempting to create a client with the role CLIENT, expecting a Forbidden response.
   * Asserts that the response status is FORBIDDEN.
   * @throws Exception if there's an error during request processing.
   */

  @Test
  @WithMockUser(
      roles = {"CLIENT"}
  )
  public void testCreateClientError() throws Exception {
    ClientDTO newClient = new ClientDTO(1L, "John", "Doe", "johndoe", "johndoe@example.com", "1234567890", (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")).parse("1990-01-01T00:00:00.000+00:00"), Status.Pending);
    Mockito.when(this.clientService.createClient(ArgumentMatchers.any())).thenReturn(newClient);
    this.mockMvc.perform(MockMvcRequestBuilders.post("/cms/clients").contentType(MediaType.APPLICATION_JSON).content((new ObjectMapper()).writeValueAsString(newClient))).andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  /**
   * Test for retrieving all clients successfully with the role EMPLOYEE.
   * Asserts that the response contains the expected JSON body of clients.
   * @throws Exception if there's an error during request processing.
   */

  @Test
  @WithMockUser(
      roles = {"EMPLOYEE"}
  )
  public void getAllClients() throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    List<ClientDTO> expectedClients = Arrays.asList(
        new ClientDTO(6L, "Vamsi1", "Mannava1", "vmannava", "vmannava2003@gmail.com", "68389399202", dateFormat.parse("2003-09-16T00:00:00.000+00:00"), Status.Pending),
        new ClientDTO(8L, "John", "Doe", "john_doe", "john_doe@gmail.com", "3993939203", dateFormat.parse("2004-09-16T00:00:00.000+00:00"), Status.Rejected),
        new ClientDTO(10L, "Alice", "Smith", "asmith", "asmith@example.com", "98765432101", dateFormat.parse("2005-09-16T00:00:00.000+00:00"), Status.Pending));
    Mockito.when(this.clientService.getClients()).thenReturn(expectedClients);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cms/clients"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content()
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers
            .content().json("[{'id':6,'firstName':'Vamsi1','lastName':'Mannava1','username':'vmannava','email':'vmannava2003@gmail.com'"
                + ",'ssn':'68389399202','dateOfBirth':'2003-09-16T00:00:00.000+00:00','status':'Pending'},"
                + "{'id':8,'firstName':'John','lastName':'Doe','username':'john_doe','email':'john_doe@gmail.com',"
                + "'ssn':'3993939203','dateOfBirth':'2004-09-16T00:00:00.000+00:00','status':'Rejected'},"
                + "{'id':10,'firstName':'Alice','lastName':'Smith','username':'asmith','email':'asmith@example.com',"
                + "'ssn':'98765432101','dateOfBirth':'2005-09-16T00:00:00.000+00:00','status':'Pending'}]"));
    (Mockito.verify(this.clientService, Mockito.times(1))).getClients();
  }
  /**
   * Test for retrieving all clients with the role CLIENT, expecting a Forbidden response.
   * Asserts that the response status is FORBIDDEN.
   * @throws Exception if there's an error during request processing.
   */
  @Test
  @WithMockUser(
      roles = {"CLIENT"}
  )
  public void getAllClientsError() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cms/clients")).andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  /**
   * Test for retrieving a client by ID successfully with the role EMPLOYEE.
   * Asserts that the response contains the expected JSON body of the client.
   * @throws Exception if there's an error during request processing.
   */

  @Test
  @WithMockUser(
      roles = {"EMPLOYEE"}
  )
  public void testGetClientById() throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    Long clientId = 1L;
    ClientDTO clientDTO = new ClientDTO();
    clientDTO.setId(clientId);
    clientDTO.setFirstName("John");
    clientDTO.setLastName("Doe");
    clientDTO.setEmail("johndoe@gmail.com");
    clientDTO.setUsername("john_doe");
    clientDTO.setSSN("2939392040");
    clientDTO.setDateOfBirth(dateFormat.parse("2004-09-16T00:00:00.000+00:00"));
    clientDTO.setStatus(Status.valueOf("Pending"));
    Mockito.when(this.clientService.getClientById(clientId)).thenReturn(clientDTO);
    String expectedJson = "{\"id\":1,\n\"firstName\":\"John\",\n\"lastName\":\"Doe\",\n\"email\":\"johndoe@gmail.com\",\n\"username\":\"john_doe\",\n\"ssn\":\"2939392040\",\n\"dateOfBirth\":\"2004-09-16T00:00:00.000+00:00\",\n\"status\":\"Pending\"}\n";
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cms/clients/{id}", clientId)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.content().json(expectedJson));
    (Mockito.verify(this.clientService, Mockito.times(1))).getClientById(clientId);
  }

  /**
   * Test for attempting to retrieve a client by ID with the role CLIENT, expecting a Forbidden response.
   * Asserts that the response status is FORBIDDEN.
   * @throws Exception if there's an error during request processing.
   */
  @Test
  @WithMockUser(
      roles = {"CLIENT"}
  )
  public void testGetClientByIdError() throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    Long clientId = 1L;
    ClientDTO clientDTO = new ClientDTO(clientId, "John", "Doe", "john_doe", "johndoe@gmail.com", "2939392040", dateFormat.parse("2004-09-16T00:00:00.000+00:00"), Status.Pending);
    Mockito.when(this.clientService.getClientById(clientId)).thenReturn(clientDTO);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cms/clients/{id}", clientId)).andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  /**
   * Test for updating a client's status to Approved with the role EMPLOYEE
   * Asserts that it contains the expected JSON body of the client.
   * @throws Exception if there's an error during request processing.
   */
  @Test
  @WithMockUser(
      roles = {"EMPLOYEE"}
  )
  public void testUpdateClientApprove() throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    Long clientId = 1L;
    ClientDTO updatedClient = new ClientDTO();
    updatedClient.setId(clientId);
    updatedClient.setFirstName("John");
    updatedClient.setLastName("Doe");
    updatedClient.setEmail("johndoe@example.com");
    updatedClient.setUsername("john_doe");
    updatedClient.setSSN("123456789");
    updatedClient.setDateOfBirth(dateFormat.parse("2004-09-16T00:00:00.000+00:00"));
    updatedClient.setStatus(Status.Approved);
    Mockito.when(this.clientService.updateStatusToApproved(clientId)).thenReturn(updatedClient);
    String expectedJson = "  {\n    \"id\": 1,\n    \"firstName\": \"John\",\n    \"lastName\": \"Doe\",\n    \"email\": \"johndoe@example.com\",\n    \"username\": \"john_doe\",\n    \"ssn\": \"123456789\",\n    \"dateOfBirth\": \"2004-09-16T00:00:00.000+00:00\", // Ensure the date format matches the JSON output\n    \"status\": \"Approved\"\n  }\n";
    this.mockMvc.perform(MockMvcRequestBuilders.patch("/cms/clients/{id}/approve", clientId)
    .contentType(MediaType.APPLICATION_JSON)).
    andExpect(MockMvcResultMatchers.status().isOk()).
        andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).
        andExpect(MockMvcResultMatchers.content().json(expectedJson));
  }

  /**
   * Test for deleting a client with the role EMPLOYEE
   * Asserts that client gives a deleted message
   * @throws Exception if there's an error during request processing.
   */

  @Test
  @WithMockUser(
      roles = {"EMPLOYEE"}
  )
  public void deleteClientTest() throws Exception {
    Long clientId = 1L;
    this.mockMvc.perform(MockMvcRequestBuilders.delete("/cms/clients/{id}",clientId)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string("Deleted client from system successfully"));
    (Mockito.verify(this.clientService)).deleteClient(clientId);
  }

/**
 * Tests the getClientById endpoint when accessed by a user with the CLIENT role.
 * Asserts that the response contains the expected JSON body of the client.
 */
@Test
@WithMockUser(roles = {"CLIENT"})
public void editProfileTest() throws Exception {
  Long clientId = 1L;
  ClientDTO updatedClient = new ClientDTO(1L, "Jane", "Doe", "janedoe", "janedoe@example.com", "1234567890", (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")).parse("1990-02-01T00:00:00.000+00:00"), Status.Pending);
  ClientDTO returnedClient = new ClientDTO(1L, "Jane", "Doe", "janedoe", "janedoe@example.com", "1234567890", (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")).parse("1990-02-01T00:00:00.000+00:00"), Status.Pending);
  Mockito.when(this.clientService.updateClient(ArgumentMatchers.eq(clientId), ArgumentMatchers.any(ClientDTO.class))).thenReturn(returnedClient);
  ObjectMapper objectMapper = new ObjectMapper();
  String updatedClientJson = objectMapper.writeValueAsString(updatedClient);
  String expectedJson = "\n{\n  \"id\": 1,\n  \"firstName\": \"Jane\",\n  \"lastName\": \"Doe\",\n  \"username\": \"janedoe\",\n  \"email\": \"janedoe@example.com\",\n  \"ssn\": \"1234567890\",\n  \"dateOfBirth\": \"1990-02-01T00:00:00.000+00:00\",\n  \"status\": \"Pending\"\n}";
  this.mockMvc.perform(MockMvcRequestBuilders.patch("/cms/profile/{id}", clientId).
      contentType(MediaType.APPLICATION_JSON).content(updatedClientJson))
      .andExpect(MockMvcResultMatchers.status().isOk()).
      andExpect(MockMvcResultMatchers.content().json(expectedJson));
  (Mockito.verify(this.clientService)).updateClient(ArgumentMatchers.eq(clientId), ArgumentMatchers.any(ClientDTO.class));
}

  /**
   * Tests deleting functionality of the client with a role of CLIENT, expecting a Forbidden response.
   * Asserts that the response status is FORBIDDEN.
   * @throws Exception if there's an error during request processing.
   */
  @Test
  @WithMockUser(
      roles = {"CLIENT"}
  )
  public void deleteClientTestError() throws Exception {
    Long clientId = 1L;
    this.mockMvc.perform(MockMvcRequestBuilders.delete("/cms/clients/{id}", clientId)).
        andExpect(MockMvcResultMatchers.status().isForbidden());
  }

/**
 * Tests the functionality of editing a client's profile through the "/cms/profile/{id}" endpoint.
 * This test ensures that a client can successfully update their profile details and that the endpoint
 * responds with the correct JSON representation of the updated profile.
 *
 */
@Test
@WithMockUser(roles = {"CLIENT"})
  public void getProfileTest() throws Exception {
    Long clientId = 1L;
    ClientDTO clientDTO = new ClientDTO(1L, "Jane", "Doe", "janedoe", "janedoe@example.com", "1234567890", (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")).parse("1990-02-01T00:00:00.000+00:00"), Status.Rejected);
    Mockito.when(this.clientService.getClientById(clientId)).thenReturn(clientDTO);
    String expectedJson = "       {\n           \"id\": 1,\n           \"firstName\": \"Jane\",\n           \"lastName\": \"Doe\",\n           \"username\": \"janedoe\",\n           \"email\": \"janedoe@example.com\",\n           \"ssn\": \"1234567890\",\n           \"dateOfBirth\": \"1990-02-01T00:00:00.000+00:00\",\n           \"status\": \"Rejected\"\n       }\n";
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cms/profile/{id}",clientId)).
    andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().json(expectedJson));
    (Mockito.verify(this.clientService)).getClientById(clientId);
  }

  /**
   * Tests the successful password change functionality for a client through the "/cms/profile/{id}/change-password" endpoint.
   * This test verifies that the password can be changed successfully and that the appropriate success message is returned.
   * The test mocks the ClientService to simulate the password change operation returning a success status. It prepares a
   * PasswordChangeDto object with the old and new password details, serializes it into JSON, and sends this as the request
   * body using mockMvc. The response is expected to confirm the password update with a success message.
   *
   * @throws Exception if there's an error during the request processing.
   */

  @Test
  @WithMockUser(roles = {"CLIENT"})
  public void changePassword_Success_Test() throws Exception {
    Long clientId = 1L;
    PasswordChangeDto passwordChangeDto = new PasswordChangeDto("oldPassword", "newPassword");
    Mockito.when(this.clientService.changePassword(ArgumentMatchers.eq(clientId),ArgumentMatchers.any(PasswordChangeDto.class)))
        .thenReturn(true);

    ObjectMapper objectMapper = new ObjectMapper();
    String passwordChangeJson = objectMapper.writeValueAsString(passwordChangeDto);
    this.mockMvc.perform(MockMvcRequestBuilders.patch("/cms/profile/{id}/change-password", clientId)
        .contentType(MediaType.APPLICATION_JSON).content(passwordChangeJson))
            .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Password updated successfully"));
  }
}
