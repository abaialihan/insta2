package com.abai.insta2.config;

import com.abai.insta2.domain.User;
import com.abai.insta2.repo.UserDetailsRepo;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.time.LocalDateTime;

    /*Spring Security это Java/JavaEE framework, предоставляющий
    механизмы построения систем аутентификации и авторизации,
    а также другие возможности обеспечения безопасности для корпоративных
    приложений, созданных с помощью Spring Framework.*/


@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login", "/js**", "/error")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().logout().logoutSuccessUrl("/").permitAll()
                .and()
                .csrf().disable();

    }

    /*@Bean - это не что иное, как самый обычный объект.
    Разница лишь в том, что бинами принято называть те объекты,
    которые управляются Spring-ом и живут внутри его DI-контейнера.*/

    @Bean
    public PrincipalExtractor principalExtractor(UserDetailsRepo userDetailsRepo){
        return map -> {
            String id = (String) map.get("sub");

            // ищем пользователя в базе данных
            User user = userDetailsRepo.findById(id).orElseGet(() ->{

                // если не находим создаем нового пользователя
                User newUser = new User();

                newUser.setId(id);
                newUser.setName((String) map.get("name"));
                newUser.setEmail((String) map.get("email"));
                newUser.setGender((String) map.get("gender"));
                newUser.setLocal((String) map.get("locale"));
                newUser.setAvatar((String) map.get("picture"));
                //возвращаем нового пользователя
                return newUser;
            });
            //устанавливаем дату последнего визита полбзователю
            user.setLastVisit(LocalDateTime.now());
            //и сохраняем пользователя
           return userDetailsRepo.save(user);
        };
    }
}
