package com.microservice.auth;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(
      Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)){
            return false;
        }
        
        return hasPrivilege(auth, targetDomainObject.toString(), permission.toString().toUpperCase());
    }
 
    @Override
    public boolean hasPermission(
      Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, targetType.toUpperCase(), 
          permission.toString().toUpperCase());
    }
    
    private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
        for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
        	if (!grantedAuth.getAuthority().startsWith("permission")) {
        		continue;
        	}
        	String[] parts = grantedAuth.getAuthority().split("_");
        	if (!parts[1].equals(targetType)) {
        		continue;
        	}
        	if (parts[2].equals("all")) {
        		return true;
        	}
        	
        	String[] permissions = parts[2].split(";");
        	for(String p : permissions) {
        		if (p.equalsIgnoreCase(permission)) {
        			return true;
        		}
        	}
        }
        return false;
    }
}