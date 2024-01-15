package kr.top2blue.boot3.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/*
		http
		.exceptionHandling() //  예외처리 기능이 작동
		// 인증실패시 처리
		.authenticationEntryPoint(new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					org.springframework.security.core.AuthenticationException authException)
					throws IOException, ServletException {
				  response.sendRedirect("/login");
			}
		})
		// 인가실패시 처리
		.accessDeniedHandler( new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response,
                        AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        response.sendRedirect("/accessDenied");
            }
		});
		*/
		http.csrf(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests((authorize) -> authorize
					.shouldFilterAllDispatcherTypes(false)
                    .requestMatchers("/", "/home","/index").permitAll()
                    .requestMatchers("/css/**", "/js/**", "/images/**","/upload/**").permitAll()
    				.requestMatchers("/hello").hasAnyRole("USER","ADMIN")
    				.requestMatchers("/admin").hasRole("ADMIN")
    				// h2-console 사용 가능(1 번째 권한 설정)
    				.requestMatchers(PathRequest.toH2Console()).permitAll()
    				.anyRequest().authenticated() // 그 외 모든 요청은 인증된 사용자만 접근 가능
	    );
		// h2-console 사용 가능(2 번째 프레임셑 사용 가능)
		http.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
		
		http.formLogin((form) -> form.loginPage("/login").permitAll());
		http.logout((logout) -> logout.permitAll().logoutSuccessUrl("/"));
		return http.build();
	}
	/*
	// org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration 발생
	@Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    	auth
          .inMemoryAuthentication()
          .withUser("user")
          .password(encoder.encode("123456"))
          .roles("USER")
          .and()
          .withUser("admin")
          .password(encoder.encode("123456"))
          .roles("USER", "ADMIN");
	}
	*/
	@Bean
    UserDetailsService getUerDetailsService() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = 
        		// User.withDefaultPasswordEncoder() // deprecated 되었다.
        		User.builder()
                .username("user")
                .password(encoder.encode("123456"))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
        		.username("admin")
        		.password(encoder.encode("123456"))
        		.roles("USER", "ADMIN")
        		.build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}