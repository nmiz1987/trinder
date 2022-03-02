package iob.logic.activities;

import iob.boundaries.ActivityBoundary;

public interface ActivitiesServiceWithFunctionality extends ActivitiesServiceWithPagination {

	public Object doSomething(ActivityBoundary command);
}
