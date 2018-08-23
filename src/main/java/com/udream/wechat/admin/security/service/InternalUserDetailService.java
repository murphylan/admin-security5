package com.udream.wechat.admin.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udream.wechat.admin.security.entity.UserDO;
import com.udream.wechat.admin.security.entity.UserPrincipal;
import com.udream.wechat.admin.security.repository.UserRepository;

@Service
@Transactional
public class InternalUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDO user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Unknown User");
		}
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNotExpired = true;
		boolean accountNonLocked = true;
		UserPrincipal principal = new UserPrincipal(user, enabled, accountNonExpired, credentialsNotExpired,
				accountNonLocked, getAuthorities(user.getRoles()));
		return principal;
	}

	private List<GrantedAuthority> getAuthorities(List<String> roles) {
		return roles.stream().map(r -> new SimpleGrantedAuthority(r)).collect(Collectors.toList());
	}
}