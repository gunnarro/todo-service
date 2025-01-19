package org.gunnarro.microservice.todoservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.exception.ApplicationException;
import org.gunnarro.microservice.todoservice.repository.UserAccountRepository;
import org.gunnarro.microservice.todoservice.repository.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;

import java.util.regex.Pattern;

@Slf4j
public class LocalUserDetailsServiceImpl implements UserDetailsService {

    //@Autowired
    private UserAccountRepository userAccountRepository;

    // @Autowired
    // private LoginAttemptServiceImpl loginAttemptService;

    // @Autowired
    // private HttpServletRequest request;

    /**
     * default constructor
     */
    public LocalUserDetailsServiceImpl() {
        super();
    }

    /**
     * e
     * <p>
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        log.debug("get username: {}", userName);
        // final String ip = request.getRemoteAddr();
        // if (loginAttemptService.isBlocked(ip)) {
        // throw new
        // ApplicationException("Blocked, exceeded number of attempts!");
        // }
        if (ObjectUtils.isEmpty(userName)) {
            throw new ApplicationException("validation error, userName is not valid!");
        } else if (!Pattern.compile("[a-zA-Z]*").matcher(userName).matches()) {
            throw new ApplicationException("validation error, userName is not valid!");
        }
        UserAccount userAccount = userAccountRepository.getUser(userName);
        if (userAccount == null) {
            log.debug("User not found!, username: {}", userName);
            throw new UsernameNotFoundException("User not found!");
        }
       // log.debug("return user: {}", user);
        //return user;
        return null;
    }

    // public final Collection<? extends GrantedAuthority> getAuthorities(final
    // Collection<Role> roles) {
    // return getGrantedAuthorities(getPrivileges(roles));
    // }
    //
    // private final List<String> getPrivileges(final Collection<Role> roles) {
    // final List<String> privileges = new ArrayList<String>();
    // final List<Privilege> collection = new ArrayList<Privilege>();
    // for (final Role role : roles) {
    // collection.addAll(role.getPrivileges());
    // }
    // for (final Privilege item : collection) {
    // privileges.add(item.getName());
    // }
    // return privileges;
    // }
    //
    // private final List<GrantedAuthority> getGrantedAuthorities(final
    // List<String> privileges) {
    // final List<GrantedAuthority> authorities = new
    // ArrayList<GrantedAuthority>();
    // for (final String privilege : privileges) {
    // authorities.add(new SimpleGrantedAuthority(privilege));
    // }
    // return authorities;
    // }
}
