package com.yen.SpringBootPart1.config;

// https://www.youtube.com/watch?v=GGWMK2BJs7E&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=9
// https://www.youtube.com/watch?v=uUpNr3PzNsY&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=10

import com.yen.SpringBootPart1.bean.Pet;
import com.yen.SpringBootPart1.bean.User;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *  tell spring boot, this is a config class. same as config file
 *
 *  1) we can use @Bean
 *  2) component instance is a singleton (單例) by default
 *  3) conf class (MyConfig) is also a component
 *  4) we have
 *          Full (proxyBeanMethods=true)
 *          Lite (proxyBeanMethods=false)
 *      mode in config setting
 *      -> for dealing with component dep.
 *
 *      -> so
 *            if (component) dependents on other components ->  Full (proxyBeanMethods=true)
 *            else -> Full (proxyBeanMethods=false)  (make spring init faster)
 *
 *   5) @Import can  (e.g. @Import({User.class, DataBuffer.class}))
 *      -> import classes' and init their instances
 *      -> default component name is class name
 *
 */

@Import({User.class})
@Configuration(proxyBeanMethods=true)  // proxyBeanMethods default = true
public class MyConfig {

    /**
     *  1) add component to container, use method name as component id
     *  2) returned type is component type
     *  3) returned value is component instance in container
     *  4) no matter how many times we grab component instance in app,
     *      -> it ALWAYS uses the SAME registered one instance (due to singleton (單例))
     */
    @Bean
    public User user01(){
        return new User("kyo", 18);
    }

    @Bean("tom") // we can modify the component id via "@Bean("tom")"
    public Pet tomcatPer(){
        return new Pet("tomcat");
    }
}
