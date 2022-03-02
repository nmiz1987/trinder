package iob.logic.users;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;

import iob.UserID;
import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;
import iob.data.UserEntity;
import iob.data.UserRole;
import iob.exceptions.UserNotAdminException;
import iob.exceptions.UserNotFoundException;

//@Service
public class UsersServiceMockup implements UsersService {
	private Map<UserID, UserEntity> userDataBaseMockup;
	private UsersConverter converter;

	@Autowired
	public UsersServiceMockup(UsersConverter converter) {
		super();
		this.converter = converter;
	}

	@Autowired
	public void setConverter(UsersConverter converter) {
		this.converter = converter;
	}

	@PostConstruct
	public void init() {
		// thread safe Map
		this.userDataBaseMockup = Collections.synchronizedMap(new HashMap<>());
	}

	@Override
	public UserBoundary createUser(NewUserBoundary input) {

		// convert NewUserBoundary to entity
		UserEntity entity = this.converter.convertToEntity(input);

		// store entity to DB
		this.userDataBaseMockup.put(entity.getUserId(), entity);

		// convert entity to UserBoundary and return it
		return this.converter.convertToBoundary(entity);
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {

		UserID userId = new UserID(userDomain, userEmail);

		// Return user data from DB
		UserEntity entity = this.userDataBaseMockup.get(userId);

		if (entity == null)
			throw new UserNotFoundException("Could not find user with id: " + userId);

		// Convert data to Boundary and return it
		return this.converter.convertToBoundary(entity);
	}

	@Override
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {

		UserID userId = new UserID(userDomain, userEmail);

		// Return user data from DB
		UserEntity existing = this.userDataBaseMockup.get(userId);

		if (existing == null)
			throw new UserNotFoundException("Could not find user with id: " + update.getUserId());

		// update relevant fields
		boolean dirtyFlag = false;

		// Update user details
		if (update.getRole() != null) {
			dirtyFlag = true;
			existing.setRole(update.getRole());
		}

		if (update.getUsername() != null) {
			dirtyFlag = true;
			existing.setUsername(update.getUsername());
		}

		if (update.getAvatar() != null) {
			dirtyFlag = true;
			existing.setAvatar(update.getAvatar());
		}

		// store entity to DB if needed
		if (dirtyFlag)
			this.userDataBaseMockup.put(existing.getUserId(), existing);

		return this.converter.convertToBoundary(existing);

	}

	@Override
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {

		UserID userId = new UserID(adminDomain, adminEmail);

		// Return user data from DB
		UserEntity entity = this.userDataBaseMockup.get(userId);

		if (entity == null)
			throw new UserNotFoundException("Could not find user with id: " + userId);

		// is ADMIN
		if (!entity.getRole().equals(UserRole.ADMIN.name()))
			throw new UserNotAdminException("User ID " + entity.getUserId() + " is not ADMIN!");

		// User found in DB and is ADMIN
		// Get all user boundaries from database
		return this.userDataBaseMockup.values().stream().map(this.converter::convertToBoundary)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteAllUsers(String adminDomain, String adminEmail) {

		UserID userId = new UserID(adminDomain, adminEmail);

		// Return user data from DB
		UserEntity entity = this.userDataBaseMockup.get(userId);

		if (entity == null)
			throw new UserNotFoundException("Could not find user with id: " + userId);

		// is ADMIN
		if (!entity.getRole().equals(UserRole.ADMIN.name()))
			throw new UserNotAdminException("User ID " + entity.getUserId() + " is not ADMIN!");

		// User found in DB and is ADMIN
		// Delete all users that are not ADMIN
		userDataBaseMockup = userDataBaseMockup.entrySet().stream()
				.filter(u -> !u.getValue().getRole().equals(UserRole.ADMIN.name()))
				.collect(Collectors.toMap(u -> u.getKey(), u -> u.getValue()));
	}
}
