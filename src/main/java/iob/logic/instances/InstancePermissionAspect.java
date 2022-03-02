package iob.logic.instances;

import java.lang.annotation.Annotation;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import iob.UserID;
import iob.boundaries.InstanceBoundary;
import iob.data.UserEntity;
import iob.data.UserRole;
import iob.exceptions.AccessDeniedException;
import iob.exceptions.UserNotFoundException;
import iob.logic.users.UsersDao;

@Component
@Aspect
public class InstancePermissionAspect {
	private UsersDao usersDao;

	@Autowired
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}

	@Around("@annotation(iob.logic.instances.InstancePermission)")
	public Object checkUserPermissionsBeforeMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object originalRV;
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
		Annotation anno = methodSignature.getMethod().getAnnotation(InstancePermission.class);
		// Getting method type
		String type = ((InstancePermission) anno).type();

		// Getting the arguments from the method, args[0] == userDomain, args[1]
		// ==userEmail,
		Object[] args = proceedingJoinPoint.getArgs();
		UserEntity user = this.usersDao.findById(new UserID((String) args[0], (String) args[1]))
				.orElseThrow(() -> new UserNotFoundException("Could not find user "));

		// If User is admin continue else check the instance
		if (user.getRole().equals(UserRole.ADMIN.name())) {
			try {
				originalRV = proceedingJoinPoint.proceed();
				return originalRV;
			} catch (Throwable e) {
				throw e;
			}
		} else if (user.getRole().equals(UserRole.MANAGER.name())) {
			// Checking which method type
			if (type.equals("specific") || type.equals("allInstances") || type.equals("create")) {
				try {
					originalRV = proceedingJoinPoint.proceed();
					return originalRV;
				} catch (Throwable e) {
					throw e;
				}

			} else if (type.equals("update")) {
				try {
					originalRV = proceedingJoinPoint.proceed();
					return originalRV;
				} catch (Throwable e) {
					throw e;
				}
			}
		} else { // PLAYER

			// Checking which method type
			if (type.equals("specific")) {
				try {
					originalRV = proceedingJoinPoint.proceed();
					// Checking if Active
					if (((InstanceBoundary) originalRV).getActive())
						throw new AccessDeniedException("Access Denied.");
				} catch (Throwable e) {
					throw e;
				}
			} else if (type.equals("allInstances")) {
				try {
					originalRV = proceedingJoinPoint.proceed();
					// Checking if Active
					@SuppressWarnings("unchecked")
					List<InstanceBoundary> originalRVInstance = (List<InstanceBoundary>) originalRV;
					for (InstanceBoundary inctanse : originalRVInstance) {
						// If instance is active, remove it
						if (inctanse.getActive())
							originalRVInstance.remove(inctanse);
					}
					return originalRVInstance;
				} catch (Throwable e) {
					throw e;
				}
			} else {
				throw new AccessDeniedException("Access Denied.");
			}
		}
		return null;
	}
}
