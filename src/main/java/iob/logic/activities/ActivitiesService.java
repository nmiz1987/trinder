package iob.logic.activities;

import java.util.List;

import iob.boundaries.ActivityBoundary;

public interface ActivitiesService {

	public Object invokeActivity(ActivityBoundary activity);

	@Deprecated
	public List<ActivityBoundary> getAllActivities(String adminDomain, String adminEmail);

	public void deleteAllActivities(String adminDomain, String adminEmail);
}
