# Spring Security Sample

이 가이드는 Spring Security를 이용하여, URL별 접근권한을 설정하는 방법에 대해서 다룹니다.

## 목표

부여된 권한에 따라서, 접근할 수 있는 REST API URL을 확인할 수 있습니다.

## Project 구성

프로젝트 구성은 다음과 같습니다.

* REST API URL
* 사용자 관리를 위한 DBMS

## Maven 설정

`pom.xml`
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.skcc</groupId>
	<artifactId>cna-security-sample</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>cna-security-sample</name>
	<description>Demo project for Spring Security</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.4.5.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

## REST API Application 구현

`@RestController`를 사용하여 Log-in한 사용자 정보를 반환하는 API를 생성합니다.
사용자 정보는 Spring Security의 Filter를 통해서, SecurityContext에 저장되어 있으며, SecurityContextHolder를 통해서 접근 가능합니다. (단 Anonymous 사용자의 경우, Log-in전에는 Security Context에 저장된 정보가 없으므로, 그 값은 Null이 될 수 있습니다.)

* `GET /anonymous`: 누구나 접근 가능한 URL
* `GET /admin`: 시스템 사용자 중 관리자 권한을 갖고 있는 사용자만 접근할 수 있는 URL
* `GET /user`: 시스템 사용자만 접근할 수 있는 URL

```java
@RestController
public class Controller {
	
	@RequestMapping("/anonymous")
	public String helloAnonymous() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		} else {
			return principal.toString();
		}
	}
	
	@RequestMapping("/admin")
	public Principal helloAdmin(Principal principal) {
		return principal;
	}
	
	@RequestMapping("/user")
	public Principal helloUser(Principal principal) {
		return principal;
	}
	
}
```

## Spring Security 적용

Security 설정을 위한 Class를 생성하고, `WebSecurityConfigurerAdapter`를 상속 받습니다.

```java
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
}
```

HttpSecurity 설정을 추가합니다.

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
	http.authorizeRequests()
	.and()
	.formLogin().loginPage("/login").failureUrl("/login?error").permitAll()
	.and()
	.logout().permitAll();
}
```

설정된 항목은 다음과 같습니다.

* Log-in 및 Log-in FailureURL 전체 권한 허용
* Log-out에 대해 전제 권한 허용

## 사용자 정보 구성

H2 DB를 이용하여 Users, Authorities Table을 구성합니다.(`data.sql`과 `schema.sql` 참고)

DB에 저장된 사용자 정보를 통해서, 인증/권한 처리를 할 수 있도록 설정을 추가합니다.

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.jdbcAuthentication().dataSource(this.dataSource);
}
```

## Security 권한 적용을 위한 설정

Security Configuration 클래스에 Spring Security의 설정을 Annotation으로 적용하기 위해 `@EnableGlobalMethodSecurity`를 추가합니다.(Pre/PreAuthorize사용을 위해 prePostEnabled=true로 설정합니다.)
또한, 기본 Spring Security의 설정들을 유지한 상태에서, URL 접근 규칙만 변경하기 위해서 `@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)`를 추가합니다.

```java
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	...
}
```

## URL별 권한 적용

`@PreAuthorize`를 통해서 REST URL에 대한 접근 권한을 설정합니다.

```java
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public Principal helloAdmin(Principal principal) {
	return principal;
}
@PreAuthorize("hasRole('USER')")
@RequestMapping("/user")
public Principal helloUser(Principal principal) {
	return principal;
}
```

> Spring Security를 권한 값의 Prefix로 `ROLE_`를 사용하고 있습니다. 따라서, `hasRole('USER')`로 검색을 진행할 때, 실제로 조회되는 값은 `ROLE_USER`입니다.

## Spring Security Application 실행

Application을 실행합니다.
`/anonymous`는 인증/권한 처리 없이 모든 사용자가 접근 가능합니다. 인증과정을 거치지 않았다면, `anonymousUser`를 확인할 수 있습니다. 반대로 인증과정이 통과되었다면, 인증처리된 사용자 정보를 확인할 수 있습니다.

`/admin`는 ADMIN 권한을 갖고 있는 사용자에 한해서 접근 가능하며, 인증처리를 통과한 사용자 정보를 확인할 수 있습니다. 단 User 권한을 갖고 있는 사용자가 접근할 경우, 접근 제한 에러가 나타납니다.

`/user`는 USER 권한을 갖고 있는 사용자와 ADMIN 권한을 갖고 있는 사용자가 접근 가능 합니다.