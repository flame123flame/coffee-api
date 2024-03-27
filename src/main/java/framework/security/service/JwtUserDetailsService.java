package framework.security.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String data) throws UsernameNotFoundException {
		List<String> list = Arrays.asList(data.split("∆∆∆"));
		String username = list.get(0);
		return new User(username, "", new ArrayList<>());
	}

}
