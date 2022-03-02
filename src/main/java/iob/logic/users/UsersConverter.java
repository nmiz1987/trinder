package iob.logic.users;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import iob.UserID;
import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;
import iob.data.UserEntity;

@Component
public class UsersConverter {
	private String projectName;

	@Value("${spring.application.name}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public UserEntity convertToEntity(NewUserBoundary input) {
		UserEntity entity = new UserEntity();

		entity.setUserId(new UserID(this.projectName, input.getEmail()));
		entity.setUsername(input.getUsername());
		entity.setRole(input.getRole());
		entity.setAvatar(input.getAvatar());

		return entity;
	}

	public UserBoundary convertToBoundary(UserEntity entity) {
		UserBoundary boundary = new UserBoundary();

		boundary.setUserId(entity.getUserId());
		boundary.setUsername(entity.getUsername());
		boundary.setRole(entity.getRole());
		boundary.setAvatar(entity.getAvatar());

		return boundary;
	}
}
