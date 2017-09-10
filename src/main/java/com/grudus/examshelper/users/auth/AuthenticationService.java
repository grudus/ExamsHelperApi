package com.grudus.examshelper.users.auth;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.exceptions.CannotFindUserException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public long getCurrentLoggedUserId() {
        Authentication authentication = getAuthentication();
        if (authentication instanceof AuthenticatedUser)
            return ((AuthenticatedUser) authentication).getUser().getId();

        throw new CannotFindUserException("Cannot obtain current user id");
    }
}
