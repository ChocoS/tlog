package com.pwawrzyniak.tlog.backend.service.security;

import com.pwawrzyniak.tlog.backend.dto.UserDto;
import com.pwawrzyniak.tlog.backend.entity.Privilege;
import com.pwawrzyniak.tlog.backend.entity.Role;
import com.pwawrzyniak.tlog.backend.entity.User;
import com.pwawrzyniak.tlog.backend.repository.PrivilegeRepository;
import com.pwawrzyniak.tlog.backend.repository.RoleRepository;
import com.pwawrzyniak.tlog.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PrivilegeRepository privilegeRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findOneByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User with username '" + username + "' was not found");
    }

    return new UserAwareUserDetails(user, getAuthorities(user.getRoles()));
  }

  public User registerNewUserAccount(String username, String password, String firstName, String lastName, String... roles) {
    if (userExists(username)) {
      return null;
    }
    User user = new User();
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setPassword(passwordEncoder.encode(password));
    user.setUsername(username);
    if (roles != null) {
      user.setRoles(Arrays.stream(roles).map(roleRepository::findByName).collect(Collectors.toList()));
    }
    user.setEnabled(true);
    return userRepository.save(user);
  }

  public UserDto getLoggedInUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      UserAwareUserDetails userAwareUserDetails = (UserAwareUserDetails) authentication.getPrincipal();
      if (userAwareUserDetails != null) {
        return UserDto.builder().firstName(userAwareUserDetails.getUser().getFirstName())
            .lastName(userAwareUserDetails.getUser().getLastName())
            .username(userAwareUserDetails.getUsername()).build();
      }
    }

    return UserDto.builder().firstName("anonymous").lastName("anonymous").username("anonymous").build();
  }

  public Privilege createPrivilegeIfNotFound(String name) {
    Privilege privilege = privilegeRepository.findByName(name);
    if (privilege == null) {
      privilege = new Privilege();
      privilege.setName(name);
      privilegeRepository.save(privilege);
    }
    return privilege;
  }

  public Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
    Role role = roleRepository.findByName(name);
    if (role == null) {
      role = new Role();
      role.setName(name);
      role.setPrivileges(privileges);
      roleRepository.save(role);
    }
    return role;
  }

  private List<String> getPrivileges(Collection<Role> roles) {
    List<String> privileges = new ArrayList<>();
    List<Privilege> collection = new ArrayList<>();
    for (Role role : roles) {
      collection.addAll(role.getPrivileges());
    }
    for (Privilege item : collection) {
      privileges.add(item.getName());
    }
    return privileges;
  }

  private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (String privilege : privileges) {
      authorities.add(new SimpleGrantedAuthority(privilege));
    }
    return authorities;
  }

  private Collection<? extends GrantedAuthority> getAuthorities(
      Collection<Role> roles) {

    return getGrantedAuthorities(getPrivileges(roles));
  }

  private boolean userExists(String email) {
    return userRepository.findOneByUsername(email) != null;
  }
}