package io.ghama.security.sample;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	/**
	 * 로그인, 로그아웃 설정.
	 * 제거된 설정: csrf 설정, x-frame-option 설정
	 * 		- csrf 설정: 사이트간 요청 위조. 한번 인증된 세션을 통해, 동일하게 구성된 페이지로 요청을 보내서 공격하는 것.
	 * 		- x-frame-option 설정: <frame>, <iframe>, <object> 태그 사용여부.
	 * 특정 URL에 ADMIN만 권한 부여 설정. 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().headers().frameOptions().sameOrigin().and().authorizeRequests()
		.and()
		.formLogin().loginPage("/login").failureUrl("/login?error").permitAll()
		.and()
		.logout().permitAll()
		.and()
		.authorizeRequests().antMatchers("/h2-console/*").hasAnyRole("ADMIN");
	}

	/**
	 * DB연동.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(this.dataSource);
	}
	
}
