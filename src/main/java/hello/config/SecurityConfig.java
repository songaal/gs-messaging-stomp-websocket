package hello.config;

import hello.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private CustomAuthenticationProvider authProvider;

  @Autowired
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth.inMemoryAuthentication().withUser("kim").password("kim").roles("USER");
//    auth.inMemoryAuthentication().withUser("song").password("song").roles("USER");
//    auth.inMemoryAuthentication().withUser("chris").password("chris").roles("USER");
      auth.authenticationProvider(authProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
//      http.authorizeRequests().anyRequest().permitAll(); // 권한제한 없는 모드
//      http.authorizeRequests().anyRequest().authenticated().and().httpBasic(); //팝업 로그인창 나오는 모드. 테스트용도.
        http.csrf().disable()
            .formLogin()
              .loginPage("/login.html")
              .loginProcessingUrl("/dologin")  //여기 설정되는 url은 자동으로 동작하여 사용자정보가 스프링 세션에 자동으로 들어간다.
                //즉, 외부에서는 이 url로 호출만 해주면 세션처리가 완료된다.
                .usernameParameter("user").passwordParameter("password") //login.html의 폼에서 name을 정해준다.
                .defaultSuccessUrl("/index.html")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/dologout")
                .logoutSuccessUrl("/login.html")
                .permitAll()
                .and()
            .authorizeRequests()
                .antMatchers("/webjars/**", "/assets/**").permitAll()
                .anyRequest().authenticated()
            ;

  }




}