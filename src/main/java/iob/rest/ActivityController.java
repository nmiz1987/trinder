package iob.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iob.boundaries.ActivityBoundary;
import iob.logic.activities.ActivitiesServiceWithFunctionality;

@RestController
public class ActivityController {
	private ActivitiesServiceWithFunctionality activitiesService;

	public ActivityController(ActivitiesServiceWithFunctionality activitiesService) {
		this.activitiesService = activitiesService;
	}

	// Invoke an instance activity
	@RequestMapping(path = "/iob/activities", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object invokeActivity(@RequestBody ActivityBoundary input) {
		return this.activitiesService.invokeActivity(input);
	}
}
