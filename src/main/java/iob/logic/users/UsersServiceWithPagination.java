package iob.logic.users;

import java.util.List;

import iob.boundaries.UserBoundary;

public interface UsersServiceWithPagination extends UsersService {

	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail, int size, int page,
			boolean sortAscending, String[] sortBy);
}
