package kr.top2blue.boot3.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		// h2-console 사용 가능(2 번째 프레임셑 사용 가능)
		http.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
		//--------------------------------------------------------------------------------------
		http.authorizeHttpRequests((authorize) -> authorize
					.shouldFilterAllDispatcherTypes(false)
                    .requestMatchers("/", "/home","/index").permitAll()
                    .requestMatchers("/css/**", "/js/**", "/images/**","/upload/**").permitAll()
                    // h2-console 사용 가능(1 번째 권한 설정)
    				.requestMatchers(PathRequest.toH2Console()).permitAll()
    				.requestMatchers("/welcome").hasAnyRole("USER", "ADMIN")
    				.requestMatchers("/getEmployees").hasAnyRole("USER", "ADMIN")
    				.requestMatchers("/addNewEmployee").hasAnyRole("ADMIN")
    				.anyRequest().authenticated() // 그 외 모든 요청은 인증된 사용자만 접근 가능
	    );
		http.formLogin((form) -> 
			form.loginPage("/login").permitAll()
			.usernameParameter("username")
			.passwordParameter("password")
			.loginProcessingUrl("/login_proc") 
			.successHandler(new LoginSuccessHandler())
			.defaultSuccessUrl("/")
		);
		http.logout((logout) -> 
			logout.permitAll()
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true)
			.logoutSuccessUrl("/")
		);
		return http.build();
	}

	@Autowired
	private DataSource dataSource;
	
	//Enable jdbc authentication
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
        .jdbcAuthentication()
        .passwordEncoder(new BCryptPasswordEncoder())
        .dataSource(dataSource)
        .authoritiesByUsernameQuery("select Username,role from roles where Username = ?")
        .usersByUsernameQuery("select Username, Password, Enabled from Users where Username = ?")
        ;
    }
    /*
	@Bean
    public UserDetailsService getUerDetailsService() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.builder()
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
    */
}