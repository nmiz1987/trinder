package iob.logic.users;

import java.lang.annotation.Annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import iob.UserID;
import iob.data.UserEntity;
import iob.data.UserRole;
import iob.exceptions.AccessDeniedException;
import iob.exceptions.UserNotFoundException;

@Component
@Aspect
public class UserPermissionAspect {
	private UsersDao usersDao;

	@Autowired
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}

	@Around("@annotation(iob.logic.users.UserPermission)")
	public Object checkUserPermissionsBeforeMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object originalRV;
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
		Annotation anno = methodSignature.getMethod().getAnnotation(UserPermission.class);

		// Getting method type
		String type = ((UserPermission) anno).type();

		// Getting the arguments from the method, args[0] == userDomain, args[1]
		// ==userEmail
		Object[] args = proceedingJoinPoint.getArgs();
		UserEntity user = this.usersDao.findById(new UserID((String) args[0], (String) args[1]))
				.orElseThrow(() -> new UserNotFoundException("Could not find user "));

		// If User is admin continue
		if (user.getRole().equals(UserRole.ADMIN.name())) {
			// Checking which method type
			if (type.equals("deleteUser") || type.equals("allUsers") || type.equals("deleteActivity")
					|| type.equals("allActivities")) {
				try {
					originalRV = proceedingJoinPoint.proceed(); // call original method
					return originalRV;
				} catch (Throwable e) {
					throw e;
				}
			} else {
				throw new AccessDeniedException("Access Denied - user is not a admin.");
			}
		}
		return null;
	}
}
