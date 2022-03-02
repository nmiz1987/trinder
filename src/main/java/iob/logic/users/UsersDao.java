package iob.logic.users;

import org.springframework.data.repository.PagingAndSortingRepository;

import iob.UserID;
import iob.data.UserEntity;

public interface UsersDao extends PagingAndSortingRepository<UserEntity, UserID> {
}