package com.redditclone;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercises the primary API flows against an in-memory database.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RedditCloneApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Verifies users can register and login with JWT-backed responses.
     */
    @Test
    void signupAndLoginReturnToken() throws Exception {
        signup("alice", "alice@example.com");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"alice","password":"password123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.username", is("alice")));
    }

    /**
     * Verifies write endpoints reject unauthenticated requests.
     */
    @Test
    void protectedEndpointRejectsAnonymousUser() throws Exception {
        mockMvc.perform(post("/api/communities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Java","description":"Spring developers"}
                                """))
                .andExpect(status().isForbidden());
    }

    /**
     * Verifies creating a community, post, vote switch, and comment works end-to-end.
     */
    @Test
    void authenticatedUserCanCreateContentVoteAndComment() throws Exception {
        String token = signup("bob", "bob@example.com");

        Long communityId = json(mockMvc.perform(post("/api/communities")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Spring Boot","description":"Backend engineering"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug", is("spring-boot")))
                .andReturn().getResponse().getContentAsString()).get("id").asLong();

        Long postId = json(mockMvc.perform(post("/api/posts")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"First post","content":"Hello API","communityId":%d}
                                """.formatted(communityId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("First post")))
                .andReturn().getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(post("/api/votes")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"postId":%d,"type":"UPVOTE"}
                                """.formatted(postId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score", is(1)));

        mockMvc.perform(post("/api/votes")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"postId":%d,"type":"DOWNVOTE"}
                                """.formatted(postId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score", is(-1)));

        mockMvc.perform(post("/api/posts/%d/comments".formatted(postId))
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"content":"Nice discussion starter"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorUsername", is("bob")));

        mockMvc.perform(get("/api/posts/%d/comments".formatted(postId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content", is("Nice discussion starter")));
    }

    /**
     * Registers a test user and extracts the returned JWT.
     */
    private String signup(String username, String email) throws Exception {
        String body = mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"%s","username":"%s","password":"password123"}
                                """.formatted(email, username)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        return json(body).get("token").asText();
    }

    /**
     * Parses JSON text for concise test assertions.
     */
    private JsonNode json(String body) throws Exception {
        return objectMapper.readTree(body);
    }

    /**
     * Formats a JWT as an Authorization header value.
     */
    private String bearer(String token) {
        return "Bearer " + token;
    }
}
