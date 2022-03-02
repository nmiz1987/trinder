package iob.logic.activities;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.ops.CommandInvoker;
import iob.ActivityId;
import iob.InstanceId;
import iob.UserID;
import iob.boundaries.ActivityBoundary;
import iob.data.ActivityEntity;
import iob.data.InstanceEntity;
import iob.data.UserEntity;
import iob.data.UserRole;
import iob.exceptions.UnimplementedOperationException;
import iob.exceptions.UserNotFoundException;
import iob.logic.instances.InstanceDao;
import iob.logic.users.UserPermission;
import iob.logic.users.UsersDao;

@Service
public class ActivitiesServiceJpa implements ActivitiesServiceWithFunctionality {

	private UsersDao usersDao;
	private ActivitiesDao activitiesDao;
	private InstanceDao instanceDao;
	private ActivitiesConverter activityConverter;
	static private int idCounter = 1; // counter for creation of new instances
	private ApplicationContext applicationContext;
	private String projectName;

	@Value("${spring.application.name}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Autowired
	public ActivitiesServiceJpa(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Autowired
	public void setInstanceDao(InstanceDao instanceDao) {
		this.instanceDao = instanceDao;
	}

	@Autowired
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}

	@Autowired
	public void setActivitiesDao(ActivitiesDao activitiesDao) {
		this.activitiesDao = activitiesDao;
	}

	@Autowired
	public void setActivityConverter(ActivitiesConverter converter) {
		this.activityConverter = converter;
	}

	@Override
	@Transactional
	public Object invokeActivity(ActivityBoundary activity) {

		// VALIDATE ACITIVTY IS GOOD
		ActivityId invalidId = new ActivityId();

		if (activity.getActivityId() != null && !invalidId.equals(activity.getActivityId())) {
			throw new RuntimeException("invalid ActivityId error");
		}

		// Update timestamp to NOW
		activity.setCreatedTimestamp(java.util.Calendar.getInstance().getTime());
		UserID userId = activity.getInvokedBy().getUserId();

		// Get user data from DB and check if PLAYER
		UserEntity userRole = this.readUserFromDb(userId);
		if (!userRole.getRole().equals(UserRole.PLAYER.name()))
			throw new RuntimeException("User's role is not a player");

		// VALIDATE INSTANCE ACTIIVTY IS ACTED UPON EXIST AND ACTIVE
		String instanceId = activity.getInstance().getInstanceId().getId();
		String instanceDomain = activity.getInstance().getInstanceId().getDomain();
		InstanceId instanceIdToCheck = new InstanceId(instanceDomain, instanceId);

		InstanceEntity instanceEntity = this.instanceDao.findById(instanceIdToCheck)
				.orElseThrow(() -> new UserNotFoundException("Could not find instance with id: " + instanceId));

		instanceEntity.getActive();
		if (instanceEntity.getActive() == false)
			throw new RuntimeException("Instance is not active");

		// SAVE ACITIVTY TO DB
		ActivityId id = new ActivityId(this.projectName, String.valueOf(idCounter++));
		activity.setActivityId(id);

		ActivityEntity entity = activityConverter.convertToEntity(activity);
		entity = this.activitiesDao.save(entity);

		/* RETURN ACTIVITY RESPONSE */
		return doSomething(activity);
	}

	private UserEntity readUserFromDb(UserID userId) {
		return this.usersDao.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("Could not find user " + userId));
	}

	@Override
	@Transactional(readOnly = true)
	@Deprecated
	public List<ActivityBoundary> getAllActivities(String adminDomain, String adminEmail) {
		throw new UnimplementedOperationException("This operation is deprecated.");
	}

	@Override
	@Transactional
	@UserPermission(type = "deleteActivity")
	public void deleteAllActivities(String adminDomain, String adminEmail) {
		this.activitiesDao.deleteAll();
	}

	@Override
	@Transactional
	@UserPermission(type = "allActivities")
	public List<ActivityBoundary> getAllActivities(String adminDomain, String adminEmail, int size, int page,
			boolean sortAscending, String[] sortBy) {

		Direction direction = sortAscending ? Direction.ASC : Direction.DESC;

		// User found in DB and is ADMIN
		// Get all user boundaries from database
		return StreamSupport
				.stream(this.activitiesDao.findAll(PageRequest.of(page, size, direction, sortBy)).spliterator(), false) // Page<ActivityEntity>
				.map(e -> this.activityConverter.convertToBoundary(e)).collect(Collectors.toList()); // List<ActivityBoundary>
	}

	@Override
	@Transactional
	public Object doSomething(ActivityBoundary command) {
		String defaultBeanName = "command not found";

		Object operation = command.getType();

		// when operation is not properly defined
		if (operation == null || !(operation instanceof String)) {
			// echo
			operation = defaultBeanName;
		}

		CommandInvoker commandInvokerBean = null;
		try {
			commandInvokerBean = this.applicationContext.getBean(operation.toString(), CommandInvoker.class);
		} catch (BeansException e) {
			commandInvokerBean = this.applicationContext.getBean(defaultBeanName, CommandInvoker.class);
		}

		return commandInvokerBean.invokeCommand(command);
	}
}
