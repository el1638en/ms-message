package com.syscom.api.config.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.syscom.dto.UserDTO;
import com.syscom.mapper.UserMapper;
import com.syscom.service.UserService;
import com.syscom.utils.Fonctions;

@Service
public class AuthUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDTO userDTO = userMapper.beanToDto(userService.findWithRoleAndFonctions(username));
		if (userDTO != null) {
			List<GrantedAuthority> grantedAuthorities = userDTO.getRole().getFonctions().stream()
					.map(fonction -> new SimpleGrantedAuthority(Fonctions.ROLE_PREFIX + fonction.getCode()))
					.collect(Collectors.toList());
			return new org.springframework.security.core.userdetails.User(userDTO.getMail(), userDTO.getPassword(),
					grantedAuthorities);
		} else {
			throw new UsernameNotFoundException("could not find the user '" + username + "'");
		}
	}

}
