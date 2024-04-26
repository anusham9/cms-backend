package net.javaguides.cms.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ClientService clientService;

  @Test
  @WithMockUser(roles = "EMPLOYEE")
  public void testCreateClient() throws Exception {
    ClientDTO newClient = new ClientDTO(1, "John", "Doe", "johndoe", "johndoe@example.com",
        "1234567890",
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse("1990-01-01T00:00:00.000+00:00"),
        Status.Pending);

    when(clientService.createClient(any())).thenReturn(newClient);

    mockMvc.perform(post("/cms/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newClient)))
        .andExpect(status().isCreated())
        .andDo(print());
  }


  @Test
  @WithMockUser(roles = "CLIENT")
  public void testCreateClientError() throws Exception {
    ClientDTO newClient = new ClientDTO(1, "John", "Doe", "johndoe", "johndoe@example.com",
        "1234567890",
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse("1990-01-01T00:00:00.000+00:00"),
        Status.Pending);

    when(clientService.createClient(any())).thenReturn(newClient);

    mockMvc.perform(post("/cms/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newClient)))
        .andExpect(status().isForbidden());
  }

 @Test
  @WithMockUser(roles={"EMPLOYEE"})
  public void getAllClients() throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    List<ClientDTO> expectedClients = Arrays.asList(
        new ClientDTO(6, "Vamsi1", "Mannava1", "vmannava", "vmannava2003@gmail.com","68389399202", dateFormat.parse("2003-09-16T00:00:00.000+00:00"), Status.Pending),
        new ClientDTO(8, "John", "Doe", "john_doe", "john_doe@gmail.com", "3993939203",dateFormat.parse("2004-09-16T00:00:00.000+00:00"),
            Status.Rejected),
        new ClientDTO(10, "Alice", "Smith", "asmith", "asmith@example.com", "98765432101", dateFormat.parse("2005-09-16T00:00:00.000+00:00"), Status. Pending)
    );

    when(clientService.getClients()).thenReturn(expectedClients);

    mockMvc.perform(get("/cms/clients"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json("[{'id':6,'firstName':'Vamsi1','lastName':'Mannava1','username':'vmannava','email':'vmannava2003@gmail.com','ssn':'68389399202','dateOfBirth':'2003-09-16T00:00:00.000+00:00','status':'Pending'},{'id':8,'firstName':'John','lastName':'Doe','username':'john_doe','email':'john_doe@gmail.com','ssn':'3993939203','dateOfBirth':'2004-09-16T00:00:00.000+00:00','status':'Rejected'},{'id':10,'firstName':'Alice','lastName':'Smith','username':'asmith','email':'asmith@example.com','ssn':'98765432101','dateOfBirth':'2005-09-16T00:00:00.000+00:00','status':'Pending'}]"));

    verify(clientService, times(1)).getClients();
  }


  @Test
  @WithMockUser(roles={"CLIENT"})
  public void getAllClientsError() throws Exception {
    mockMvc.perform(get("/cms/clients"))
        .andExpect(status().isForbidden());  // Expecting 403 Forbidden
  }

  @Test
  @WithMockUser(roles={"EMPLOYEE"})
  public void testGetClientById() throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    Long clientId = 1L; // Example client ID
    ClientDTO clientDTO = new ClientDTO(); // Replace with the actual constructor or a builder pattern
    clientDTO.setId(clientId);
    clientDTO.setFirstName("John");
    clientDTO.setLastName("Doe");
    clientDTO.setEmail("johndoe@gmail.com");
    clientDTO.setUsername("john_doe");
    clientDTO.setSSN("2939392040");
    clientDTO.setDateOfBirth(dateFormat.parse("2004-09-16T00:00:00.000+00:00"));
    clientDTO.setStatus(Status.valueOf("Pending"));

    when(clientService.getClientById(clientId)).thenReturn(clientDTO);


    String expectedJson = """
        {"id":1,
        "firstName":"John",
        "lastName":"Doe",
        "email":"johndoe@gmail.com",
        "username":"john_doe",
        "ssn":"2939392040",
        "dateOfBirth":"2004-09-16T00:00:00.000+00:00",
        "status":"Pending"}
        """;


    mockMvc.perform(get("/cms/clients/{id}", clientId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(expectedJson));

    verify(clientService, times(1)).getClientById(clientId);

  }


  @Test
  @WithMockUser(roles={"CLIENT"}) // This role should trigger a 403
  public void testGetClientByIdError() throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    Long clientId = 1L;
    ClientDTO clientDTO = new ClientDTO(clientId, "John", "Doe", "john_doe", "johndoe@gmail.com", "2939392040", dateFormat.parse("2004-09-16T00:00:00.000+00:00"), Status.Pending);

    // This setup may be omitted as the method is expected not to be called
    when(clientService.getClientById(clientId)).thenReturn(clientDTO);

    mockMvc.perform(get("/cms/clients/{id}", clientId))
        .andExpect(status().isForbidden()); // Expecting 403 Forbidden

  }


  @Test
  @WithMockUser(roles={"EMPLOYEE"})
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
    updatedClient.setStatus(Status.Approved); // Assuming Status is an enum

    // Ensure the mocked service method call matches your controller's actual service call
    when(clientService.updateStatusToApproved(clientId)).thenReturn(updatedClient);

    String expectedJson = """
      {
        "id": 1,
        "firstName": "John",
        "lastName": "Doe",
        "email": "johndoe@example.com",
        "username": "john_doe",
        "ssn": "123456789",
        "dateOfBirth": "2004-09-16T00:00:00.000+00:00", // Ensure the date format matches the JSON output
        "status": "Approved"
      }
    """;
    // Perform the PATCH request and verify the results
    mockMvc.perform(patch("/cms/clients/{id}/approve", clientId) // Ensure the URL matches the controller path
            .with(csrf())
            .with(SecurityMockMvcRequestPostProcessors.user("employee2").roles("EMPLOYEE"))
            .contentType(MediaType.APPLICATION_JSON)) // Include if your endpoint expects a content type
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(expectedJson));

  }



  @Test
  @WithMockUser(roles = {"EMPLOYEE"})
  public void deleteClientTest() throws Exception {
    Long clientId = 1L; // Example client ID

    // No need to do when().thenReturn() because deleteClient() is a void method

    mockMvc.perform(MockMvcRequestBuilders.delete("/cms/clients/{id}", clientId))
        .andExpect(status().isOk())
        .andExpect(content().string("Deleted client from system successfully"));

    // Verify that the service method was called with the correct parameter
    verify(clientService).deleteClient(clientId);
  }



  @Test
  @WithMockUser(roles = {"CLIENT"})
  public void deleteClientTestError() throws Exception {
    Long clientId = 1L; // Example client ID

    // No need to do when().thenReturn() because deleteClient() is a void method

    mockMvc.perform(MockMvcRequestBuilders.delete("/cms/clients/{id}", clientId))
        .andExpect(status().isForbidden());
  }

  @org.junit.jupiter.api.Test
  public void editProfileTest() throws Exception {
    Long clientId = 1L; // Example client ID
    ClientDTO updatedClient = new ClientDTO(1L, "Jane", "Doe", "janedoe", "janedoe@example.com", "1234567890",
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse("1990-02-01T00:00:00.000+00:00"),
        Status.Pending);

    ClientDTO returnedClient = new ClientDTO(1L, "Jane", "Doe", "janedoe", "janedoe@example.com", "1234567890",
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse("1990-02-01T00:00:00.000+00:00"),
        Status.Pending);

    when(clientService.updateClient(eq(clientId), any(ClientDTO.class))).thenReturn(returnedClient);

    ObjectMapper objectMapper = new ObjectMapper();
    String updatedClientJson = objectMapper.writeValueAsString(updatedClient);
    String expectedJson = """

{
  "id": 1,
  "firstName": "Jane",
  "lastName": "Doe",
  "username": "janedoe",
  "email": "janedoe@example.com",
  "ssn": "1234567890",
  "dateOfBirth": "1990-02-01T00:00:00.000+00:00",
  "status": "Pending"
}""";

    mockMvc.perform(MockMvcRequestBuilders.patch("/cms/profile/{id}", clientId)
            .with(csrf())
            .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(updatedClientJson)
            .with(csrf())) // Include csrf() if your application uses CSRF protection
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson));

    verify(clientService).updateClient(eq(clientId), any(ClientDTO.class));
  }


  @org.junit.jupiter.api.Test
  public void getProfileTest() throws Exception {
    Long clientId = 1L; // Example client ID
    ClientDTO clientDTO = new ClientDTO(1L, "Jane", "Doe", "janedoe", "janedoe@example.com", "1234567890",
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse("1990-02-01T00:00:00.000+00:00"),
        Status.Rejected);

    when(clientService.getClientById(clientId)).thenReturn(clientDTO);

    String expectedJson = """
       {
           "id": 1,
           "firstName": "Jane",
           "lastName": "Doe",
           "username": "janedoe",
           "email": "janedoe@example.com",
           "ssn": "1234567890",
           "dateOfBirth": "1990-02-01T00:00:00.000+00:00",
           "status": "Rejected"
       }
""";


    mockMvc.perform(MockMvcRequestBuilders.get("/cms/profile/{id}", clientId)
            .with(csrf())
            .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT")))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson));

    verify(clientService).getClientById(clientId);
  }

  @org.junit.jupiter.api.Test
  public void changePassword_Success_Test() throws Exception {
    Long clientId = 1L; // Example client ID
    PasswordChangeDto passwordChangeDto = new PasswordChangeDto("oldPassword", "newPassword");

    when(clientService.changePassword(eq(clientId), any(PasswordChangeDto.class))).thenReturn(true);

    ObjectMapper objectMapper = new ObjectMapper();
    String passwordChangeJson = objectMapper.writeValueAsString(passwordChangeDto);

    mockMvc.perform(MockMvcRequestBuilders.patch("/cms/profile/{id}/change-password", clientId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(passwordChangeJson)
            .with(csrf())
            .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT"))) // Include csrf() if your application uses CSRF protection
        .andExpect(status().isOk())
        .andExpect(content().string("Password updated successfully"));

    verify(clientService).changePassword(eq(clientId), any(PasswordChangeDto.class));
  }


}

