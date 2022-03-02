package iob.logic.activities;

import java.util.List;

import iob.boundaries.ActivityBoundary;

public interface ActivitiesServiceWithPagination extends ActivitiesService {

	public List<ActivityBoundary> getAllActivities(String adminDomain, String adminEmail, int size, int page,
			boolean sortAscending, String[] sortBy);
}
