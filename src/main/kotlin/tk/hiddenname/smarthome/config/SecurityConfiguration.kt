package tk.hiddenname.smarthome.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter


@Configuration
open class SecurityConfiguration: WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.addFilter(digestAuthenticationFilter())
                .exceptionHandling().authenticationEntryPoint(digestEntryPoint())
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
    }

    private fun digestAuthenticationFilter(): DigestAuthenticationFilter {
        val digestAuthenticationFilter = DigestAuthenticationFilter()
        digestAuthenticationFilter.userDetailsService = userDetailsServiceBean()
        digestAuthenticationFilter.setAuthenticationEntryPoint(digestEntryPoint())
        return digestAuthenticationFilter
    }

    @Bean
    open override fun userDetailsServiceBean(): UserDetailsService {
        val inMemoryUserDetailsManager = InMemoryUserDetailsManager()
        inMemoryUserDetailsManager.createUser(User.withUsername("zone")
                .password("{noop}password").roles("USER").build())
        return inMemoryUserDetailsManager
    }

    @Bean
    open fun digestEntryPoint(): DigestAuthenticationEntryPoint {
        @Suppress("SpellCheckingInspection")
        val bauth = DigestAuthenticationEntryPoint()
        bauth.realmName = "Digest WF Realm"
        bauth.key = "MySecureKey"
        return bauth
    }

    @Bean
    open fun customAuthenticationManager(): AuthenticationManager? = authenticationManager()
}