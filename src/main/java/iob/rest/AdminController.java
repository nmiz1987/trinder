package iob.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iob.boundaries.ActivityBoundary;
import iob.boundaries.UserBoundary;
import iob.logic.activities.ActivitiesServiceWithPagination;
import iob.logic.instances.InstancesService;
import iob.logic.users.UsersServiceWithPagination;

@RestController
public class AdminController {
	private UsersServiceWithPagination admin;
	private ActivitiesServiceWithPagination activitiesService;
	private InstancesService instanceService;

	@Autowired
	public AdminController(UsersServiceWithPagination admin, ActivitiesServiceWithPagination activitiesService,
			InstancesService instanceService) {
		this.admin = admin;
		this.activitiesService = activitiesService;
		this.instanceService = instanceService;
	}

	// Delete all users in the domain
	@RequestMapping(path = "/iob/admin/users/{userDomain}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteUsers(@PathVariable("userDomain") String domain, @PathVariable("userEmail") String email) {

		this.admin.deleteAllUsers(domain, email);
	}

	// Delete all instances in the domain
	@RequestMapping(path = "/iob/admin/instances/{userDomain}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteInstances(@PathVariable("userDomain") String domain, @PathVariable("userEmail") String email) {

		this.instanceService.deleteAllInstances(domain, email);
	}

	// Delete all activities in the domain
	@RequestMapping(path = "/iob/admin/activities/{userDomain}/{userEmail}", method = RequestMethod.DELETE)

	public void deleteAllFromDatabase(@PathVariable("userDomain") String domain,
			@PathVariable("userEmail") String email) {

		this.activitiesService.deleteAllActivities(domain, email);

	}

	// Export all users
	@RequestMapping(path = "/iob/admin/users/{userDomain}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getAllUsersBoundaries(@PathVariable("userDomain") String domain,
			@PathVariable("userEmail") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		boolean sortAscending = true;
		String[] sortBy = new String[] { "username", "userId" };
		UserBoundary[] users = this.admin.getAllUsers(domain, email, size, page, sortAscending, sortBy)
				.toArray(new UserBoundary[0]);
		if (users == null)
			throw new RuntimeException("Users not found.");
		else
			return users;
	}

	// Export all activities
	@RequestMapping(path = "/iob/admin/activities/{userDomain}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ActivityBoundary[] getAllActivityBoundaries(@PathVariable("userDomain") String domain,
			@PathVariable("userEmail") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		boolean sortAscending = true;
		String[] sortBy = new String[] { "createdTimestamp" };
		ActivityBoundary[] acts = this.activitiesService
				.getAllActivities(domain, email, size, page, sortAscending, sortBy).toArray(new ActivityBoundary[0]);
		if (acts == null)
			throw new RuntimeException("Activities not found.");
		else
			return acts;
	}
}
