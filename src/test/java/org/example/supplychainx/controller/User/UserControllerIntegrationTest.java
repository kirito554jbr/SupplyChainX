package org.example.supplychainx.controller.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.supplychainx.DTO.UserRequestDTO;
import org.example.supplychainx.DTO.UserResponseDTO;
import org.example.supplychainx.Mappers.UserMapper;
import org.example.supplychainx.Model.Role;
import org.example.supplychainx.Model.User;
import org.example.supplychainx.Repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.aop.auto=false"
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private  UserRepository userRepository;

    private UserRequestDTO testUserRequest;
    private UserRequestDTO userToSave;
    @Autowired
    private UserMapper userMapper;
    private User existingUser;

    @BeforeAll
    public void setUp() {
        // Setup test user request
        userRepository.deleteAll();
        testUserRequest = new UserRequestDTO();
        testUserRequest.setFirstName("Integration");
        testUserRequest.setLastName("Test");
        testUserRequest.setEmail("integration.test@example.com");
        testUserRequest.setPassword("password123");
        testUserRequest.setRole(Role.ADMIN);
        existingUser = userRepository.save(userMapper.toEntityRequest(testUserRequest));
        System.out.println(existingUser.getIdUser());

//         existingUser = userRepository.findAll().get(0);
        System.out.println("Setup complete");
    }

//    @AfterAll
//    static void tearDown() {
//     userRepository.deleteAll();
//    }

    @Test
    @Order(1)
    @Transactional
    void testCreateUser() throws Exception {

        userToSave = new UserRequestDTO();
        userToSave.setFirstName("IntegrationCreate");
        userToSave.setLastName("TestCreate");
        userToSave.setEmail("integrationCreate.test@example.com");
        userToSave.setPassword("password123");
        userToSave.setRole(Role.ADMIN);

        // Act - Perform the POST request to create a user
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idUser").exists())
                .andExpect(jsonPath("$.firstName").value("IntegrationCreate"))
                .andExpect(jsonPath("$.lastName").value("TestCreate"))
                .andExpect(jsonPath("$.email").value("integrationCreate.test@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andReturn();

        // Assert - Verify the user was actually saved in the database
        String responseContent = result.getResponse().getContentAsString();
        UserResponseDTO createdUser = objectMapper.readValue(responseContent, UserResponseDTO.class);

        User userInDb = userRepository.findById(createdUser.getIdUser()).orElse(null);
        assertThat(userInDb).isNotNull();
        assertThat(userInDb.getFirstName()).isEqualTo("IntegrationCreate");
        assertThat(userInDb.getLastName()).isEqualTo("TestCreate");
        assertThat(userInDb.getEmail()).isEqualTo("integrationCreate.test@example.com");
        assertThat(userInDb.getRole()).isEqualTo(Role.ADMIN);

//        // Store for cleanup
//            existingUser = userInDb;
    }

    @Test
    @Order(2)
    @Transactional
    void testUpdateUser() throws Exception {

        System.out.println("Existing user ID before update: " + existingUser.getIdUser());

        UserRequestDTO updateRequest = new UserRequestDTO();
        updateRequest.setFirstName("Updated");
        updateRequest.setLastName("User");
        updateRequest.setEmail("updated.user@example.com");
        updateRequest.setPassword("newpassword123");
        updateRequest.setRole(Role.ADMIN);


        mockMvc.perform(put("/api/users/" + existingUser.getIdUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idUser").value(existingUser.getIdUser()))
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.email").value("updated.user@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));


        User updatedUserInDb = userRepository.findById(existingUser.getIdUser()).orElse(null);
        assertThat(updatedUserInDb).isNotNull();
        assertThat(updatedUserInDb.getFirstName()).isEqualTo("Updated");
        assertThat(updatedUserInDb.getLastName()).isEqualTo("User");
        assertThat(updatedUserInDb.getEmail()).isEqualTo("updated.user@example.com");
        assertThat(updatedUserInDb.getRole()).isEqualTo(Role.ADMIN);

    }

    @Test
    @Order(3)
    void TestgetUserById() throws Exception {

        mockMvc.perform(get("/api/users/" + existingUser.getIdUser())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idUser").value(existingUser.getIdUser()))
                .andExpect(jsonPath("$.firstName").value(existingUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(existingUser.getLastName()))
                .andExpect(jsonPath("$.email").value(existingUser.getEmail()))
                .andExpect(jsonPath("$.role").value(existingUser.getRole().toString()));

    }

    @Test
    @Order(4)
    void TestgetUserByIdNotFound() throws Exception {

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());

    }

    @Test
    @Order(5)
    void TestgetUserByEmail() throws Exception {

        mockMvc.perform(get("/api/users/email?email=" + existingUser.getEmail())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idUser").value(existingUser.getIdUser()))
                .andExpect(jsonPath("$.firstName").value(existingUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(existingUser.getLastName()))
                .andExpect(jsonPath("$.email").value(existingUser.getEmail()))
                .andExpect(jsonPath("$.role").value(existingUser.getRole().toString()));

    }

    @Test
    @Order(6)
    void TestgetAllUsers() throws Exception {

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idUser").exists())
                .andExpect(jsonPath("$[0].firstName").exists())
                .andExpect(jsonPath("$[0].lastName").exists())
                .andExpect(jsonPath("$[0].email").exists())
                .andExpect(jsonPath("$[0].role").exists());

    }

    @Test
    @Order(7)
    @Transactional
    void TestdeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/" + existingUser.getIdUser())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        User deletedUser = userRepository.findById(existingUser.getIdUser()).orElse(null);
        assertThat(deletedUser).isNull();
    }

}

