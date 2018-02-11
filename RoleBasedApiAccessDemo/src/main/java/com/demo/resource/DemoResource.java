package com.demo.resource;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.demo.application.UserRoles;

@Path("/plag")
public class DemoResource {
	
	
	//use this base64 encoded value as authorization key:"Authorization" , value:  "Basic c3RhZmY6cGFzc3dvcmQ="
    @GET
    @Produces("text/plain")
    @RolesAllowed({UserRoles.ADMIN_VALUE})
    @Path("/say-hello-as-admin")
    public String sayHelloAsAdmin() 
    {
    		return "Hello From Admin";
    }
    
    //use this base64 encoded value as authorization key:"Authorization" , value:  "Basic c3RhZmY6cGFzc3dvcmQ="
    @GET
    @Produces("text/plain")
    @RolesAllowed({UserRoles.STAFF_VALUE})
    @Path("/say-hello-as-staff")
    public String sayHelloAsStaff()
    {
    		return "Hello From Staff";
    }
    
    @GET
    @Produces("text/plain")
    @RolesAllowed({UserRoles.STAFF_VALUE,UserRoles.ADMIN_VALUE})  //or else can use @PermitAll
    @Path("/say-hello")
    public String sayHelloAsBothAdminAndStaff()
    {
    		return "Hello From Admin and Staff";
    }
    
    @GET
    @Produces("text/plain")
    @PermitAll
    @Path("/say-hello-to-all")
    public String sayHelloToAll()
    {
    		return "Hello All";
    }
	
}
