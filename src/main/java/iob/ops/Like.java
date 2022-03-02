package iob.ops;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

import iob.UserID;
import iob.boundaries.ActivityBoundary;
import iob.boundaries.InstanceBoundary;
import iob.boundaries.UserBoundary;
import iob.data.UserRole;
import iob.logic.instances.InstanceServiceJpa;
import iob.logic.users.UsersServiceJpa;

@Component("Like")
public class Like implements CommandInvoker {
	private InstanceServiceJpa instanceServiceJpa;
	private UsersServiceJpa usersServiceJpa;

	@Autowired
	public Like(InstanceServiceJpa instanceServiceJpa, UsersServiceJpa usersServiceJpa) {
		super();
		this.instanceServiceJpa = instanceServiceJpa;
		this.usersServiceJpa = usersServiceJpa;
	}

	@Override
	public Object invokeCommand(ActivityBoundary command) {
		String likeAttributeKey = "like";
		UserID userId = command.getInvokedBy().getUserId();

		String userDomain = userId.getDomain();
		String userEmail = userId.getEmail();

		String instanceDomain = command.getInstance().getInstanceId().getDomain();
		String instanceId = command.getInstance().getInstanceId().getId();

		/* Temporal change to manager role to update instance attribute */
		UserBoundary userBondary = usersServiceJpa.login(userDomain, userEmail);
		userBondary.setRole(UserRole.MANAGER.name());
		usersServiceJpa.updateUser(userDomain, userEmail, userBondary);

		InstanceBoundary instanceBoundary = instanceServiceJpa.getSpecificInstance(userDomain, userEmail,
				instanceDomain, instanceId);

		Map<String, Object> instanceAttributes = instanceBoundary.getInstanceAttributes();
		if (instanceAttributes.containsKey(likeAttributeKey)) {
			Integer attributeVal = (Integer) instanceAttributes.get(likeAttributeKey);
			attributeVal++;

			instanceAttributes.put(likeAttributeKey, attributeVal);
			;
		} else {
			Integer initValue = 1;
			instanceAttributes.put(likeAttributeKey, initValue);
		}

		instanceBoundary.setInstanceAttributes(instanceAttributes);
		instanceBoundary = instanceServiceJpa.updateInstance(userDomain, userEmail, instanceDomain, instanceId,
				instanceBoundary);

		userBondary.setRole(UserRole.PLAYER.name());
		usersServiceJpa.updateUser(userDomain, userEmail, userBondary);

		return instanceBoundary;
	}
}
