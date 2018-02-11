package com.demo.application;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

@Provider
public class SecurityFilter implements javax.ws.rs.container.ContainerRequestFilter {
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Basic";
	private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED).build();
	private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN).build();
	private static final Response SERVER_ERROR = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	Logger logger = Logger.getLogger(SecurityFilter.class);

	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) {
		try {
			logger.info("I am going in to check security . Hang On..!!");
			Method method = resourceInfo.getResourceMethod();
			if (!method.isAnnotationPresent(PermitAll.class)) {
				if (method.isAnnotationPresent(DenyAll.class)) {
					requestContext.abortWith(ACCESS_FORBIDDEN);
					return;
				}

				final MultivaluedMap<String, String> headers = requestContext.getHeaders();

				final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

				if (authorization == null || authorization.isEmpty()) {
					requestContext.abortWith(ACCESS_DENIED);
					return;
				}

				final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

				String usernameAndPassword = null;
				try {
					usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword));
				} catch (Exception e) {
					requestContext.abortWith(SERVER_ERROR);
					logger.error(e.getMessage());
					return;
				}

				// Split username and password tokens
				final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
				final String username = tokenizer.nextToken();
				final String password = tokenizer.nextToken();

				// Verifying Username and password
				// TODO: Make it from DB
				if (!(username.equalsIgnoreCase("staff") && password.equalsIgnoreCase("password"))
						&& !(username.equalsIgnoreCase("sanket") && password.equalsIgnoreCase("test123"))) {
					requestContext.abortWith(ACCESS_DENIED);
					return;
				}

				// Verify user access
				if (method.isAnnotationPresent(RolesAllowed.class)) {
					RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
					Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

					// Is user valid?
					if (!isUserHavingSufficientPermissions(username, password, rolesSet)) {
						requestContext.abortWith(ACCESS_DENIED);
						return;
					}
				}
			}
		} catch (Exception e) {
			logger.error("error occured in filter(): " + e.getMessage());
		}
	}

	private boolean isUserHavingSufficientPermissions(final String username, final String password,
			final Set<String> rolesSet) {
		boolean isAllowed = false;

		String userRole = null;
		//TODO: Get roles from db and verify
		if (username.equals("sanket"))
			userRole = "ADMIN";
		else
			userRole = "STAFF";
		// Step 2. Verify user role
		if (rolesSet.contains(userRole)) {
			isAllowed = true;
		}
		return isAllowed;
	}
}