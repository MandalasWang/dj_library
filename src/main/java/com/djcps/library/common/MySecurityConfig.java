package com.djcps.library.common;


/*@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter{
		@Override
		public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("dj_library/admin/adminLogin", "dj_library/user/useradmin", "/image/**", "/css/**",
						"/upload/**", "/scripts/**", "favicon.ico").permitAll()
			.antMatchers("dj_library/admin/**").hasRole("admin")
				.antMatchers("dj_library/user/**").hasAnyAuthority("admin","user");
			http.formLogin();
		}

}*/