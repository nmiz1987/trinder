package iob.logic.users;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.UserID;
import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;
import iob.data.UserEntity;
import iob.exceptions.UnimplementedOperationException;
import iob.exceptions.UserNotFoundException;

@Service
public class UsersServiceJpa implements UsersServiceWithPagination {
	private UsersDao usersDao;
	private UsersConverter converter;

	@Autowired
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}

	@Autowired
	public void setConverter(UsersConverter converter) {
		this.converter = converter;
	}

	@Override
	@Transactional
	public UserBoundary createUser(NewUserBoundary user) {

		// convert NewUserBoundary to entity
		UserEntity entity = this.converter.convertToEntity(user);

		// store entity to DB
		entity = this.usersDao.save(entity);

		// convert entity to UserBoundary and return it
		return this.converter.convertToBoundary(entity);
	}

	private UserEntity readUserFromDb(UserID userId) {
		return this.usersDao.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("Could not find user " + userId));
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userDomain, String userEmail) {
		UserID userId = new UserID(userDomain, userEmail);

		// Get user data from DB
		// Convert data to Boundary and return it
		return this.converter.convertToBoundary(this.readUserFromDb(userId));
	}

	@Override
	@Transactional
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		UserID userId = new UserID(userDomain, userEmail);

		// Get user data from DB
		UserEntity entity = this.readUserFromDb(userId);

		// Update user details in DB
		if (update.getRole() != null)
			entity.setRole(update.getRole());

		if (update.getUsername() != null)
			entity.setUsername(update.getUsername());

		if (update.getAvatar() != null)
			entity.setAvatar(update.getAvatar());

		// update DB
		entity = this.usersDao.save(entity);

		return this.converter.convertToBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	@Deprecated
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		throw new UnimplementedOperationException("This operation is deprecated.");
	}

	@Override
	@Transactional
	@UserPermission(type = "deleteUser")
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		this.usersDao.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	@UserPermission(type = "allUsers")
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail, int size, int page,
			boolean sortAscending, String[] sortBy) {

		Direction direction = sortAscending ? Direction.ASC : Direction.DESC;

		// User found in DB and is ADMIN
		// Get all user boundaries from database
		return StreamSupport
				.stream(this.usersDao.findAll(PageRequest.of(page, size, direction, sortBy)).spliterator(), false) // Page<UserEntity>
				.map(this.converter::convertToBoundary).collect(Collectors.toList()); // List<UserBoundary>
	}
}
