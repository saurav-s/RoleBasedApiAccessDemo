package com.demo.application;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.demo.resource.DemoResource;
 
@Component
public class JerseyConfig extends ResourceConfig
{
    public JerseyConfig()
    {
        register(SecurityFilter.class);
        register(DemoResource.class);
    }
}