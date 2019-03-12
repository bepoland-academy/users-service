package pl.betse.beontime.users.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.stream.Collectors;

public class RoleVerifier {

    private static final String ROLE_ADMINISTRATION = "ROLE_ADMINISTRATION";
    private static final String ROLE_CONSULTANT = "ROLE_CONSULTANT";
    private static final String ROLE_MANAGER = "ROLE_MANAGER";

    private boolean isAdministration() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).contains(ROLE_ADMINISTRATION);
    }

    private boolean isConsultant() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).contains(ROLE_CONSULTANT);
    }

    private boolean isManager() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).contains(ROLE_MANAGER);
    }

}
