package bzh.zomzog.synchronizer.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.anyExchange
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain




//@EnableWebSecurity
@Configuration
class SecurityConfig /** : WebSecurityConfigurerAdapter() **/{
    @Bean
    fun securitygWebFilterChain(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain {
        return httpSecurity.authorizeExchange().anyExchange().permitAll().and().build()
    }

//    @Throws(Exception::class)
//    override fun configure(http: HttpSecurity) {
//        http
//            .csrf()
//            .disable()
//            .authorizeRequests()
//            .anyRequest().authenticated()
//            .and()
//            .httpBasic()
//            .and()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//    }
//
//    @Throws(Exception::class)
//    override fun configure(auth: AuthenticationManagerBuilder?) {
//        auth!!.inMemoryAuthentication()
//            .withUser("user")
//            .password("password")
//            .roles("USER")
//            .and()
//            .withUser("manager")
//            .password("password")
//            .credentialsExpired(true)
//            .accountExpired(true)
//            .accountLocked(true)
//            .authorities("WRITE_PRIVILEGES", "READ_PRIVILEGES")
//            .roles("MANAGER")
//    }
}