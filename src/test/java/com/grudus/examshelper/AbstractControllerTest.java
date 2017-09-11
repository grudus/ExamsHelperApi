package com.grudus.examshelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.configuration.security.filters.StatelessAuthenticationFilter;
import com.grudus.examshelper.configuration.security.token.TokenAuthenticationService;
import com.grudus.examshelper.users.roles.RoleName;
import com.grudus.examshelper.utils.RequestParam;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static com.grudus.examshelper.configuration.security.token.TokenAuthenticationService.AUTH_HEADER_NAME;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public abstract class AbstractControllerTest extends SpringBasedTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    protected AuthenticatedUser authentication;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(wac)
                .addFilters(new StatelessAuthenticationFilter(tokenAuthenticationService))
                .build();
    }

    @After
    public void cleanUp() {
        SecurityContextHolder.clearContext();
    }

    protected void login(RoleName... roles) {
        authentication = new AuthenticatedUser(addUserWithRoles(roles));
        setupContext();
    }

    protected ResultActions putWithParams(String url, RequestParam... params) throws Exception {
        return performRequestWithAuth(bindParams(MockMvcRequestBuilders.put(url), params));
    }

    protected ResultActions put(String url, Object requestBody) throws Exception {
        return performRequestWithAuth(MockMvcRequestBuilders.put(url)
                .contentType(APPLICATION_JSON)
                .content(toJson(requestBody)));
    }

    protected ResultActions post(String url, Object requestBody) throws Exception {
        return performRequestWithAuth(MockMvcRequestBuilders.post(url)
                .contentType(APPLICATION_JSON)
                .content(toJson(requestBody)));
    }

    protected <T> T post(String url, Object requestBody, Class<T> aClass) throws Exception {
        String json = performRequestWithAuth(MockMvcRequestBuilders.post(url)
                .contentType(APPLICATION_JSON)
                .content(toJson(requestBody)))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(json, aClass);
    }


    protected final ResultActions get(String url, RequestParam... params) throws Exception {
        MockHttpServletRequestBuilder get = bindParams(MockMvcRequestBuilders.get(url), params);
        return performRequestWithAuth(get);
    }

    private MockHttpServletRequestBuilder bindParams(MockHttpServletRequestBuilder request, RequestParam[] params) {
        return Stream.of(params).reduce(request,
                (req, param) -> req.param(param.getFirst(), param.getSecond()),
                (p, q) -> null);
    }

    protected ResultActions delete(String url) throws Exception {
        return performRequestWithAuth(MockMvcRequestBuilders.delete(url));
    }

    private ResultActions performRequestWithAuth(MockHttpServletRequestBuilder requestBuilders) throws Exception {
        String token = authentication.getUser().getToken();
        return mockMvc.perform(requestBuilders.header(AUTH_HEADER_NAME, token).principal(authentication));
    }


    private void setupContext() {
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(ctx);
        ctx.setAuthentication(authentication);
    }

    private byte[] toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(o);
    }
}
