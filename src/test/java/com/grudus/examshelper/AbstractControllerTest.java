package com.grudus.examshelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.configuration.security.filters.StatelessAuthenticationFilter;
import com.grudus.examshelper.configuration.security.token.TokenAuthenticationService;
import com.grudus.examshelper.users.roles.RoleName;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import static com.grudus.examshelper.configuration.security.token.TokenAuthenticationService.AUTH_HEADER_NAME;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public abstract class AbstractControllerTest extends SpringBasedTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    protected MockMvc mockMvc;
    protected AuthenticatedUser authentication;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(wac)
                .addFilters(new StatelessAuthenticationFilter(tokenAuthenticationService))
                .build();
    }

    protected void login(RoleName... roles) {
        addRoles();
        authentication = new AuthenticatedUser(addUserWithRoles(roles));
    }

    protected ResultActions performRequestWithAuth(MockHttpServletRequestBuilder requestBuilders) throws Exception {
        String token = authentication.getUser().getToken();
        return mockMvc.perform(requestBuilders.header(AUTH_HEADER_NAME, token).principal(authentication));
    }


    protected byte[] toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(o);
    }
}
