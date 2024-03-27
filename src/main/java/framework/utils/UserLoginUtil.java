package framework.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import framework.model.UserDetailModel;

public class UserLoginUtil {

	public static UserDetailModel getCurrentUserBean() {
		UserDetailModel userBean = null;

		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String username = "";
			if (principal instanceof UserDetails) {
				username = ((UserDetails)principal).getUsername() ;
			}
			userBean = new UserDetailModel(username);
		} else {
			String username = "NO LOGIN";
			userBean = new UserDetailModel(username);
		}
		return userBean;
	}

	public static String getUsername() {
		return UserLoginUtil.getCurrentUserBean().getUsername();
	}
}
