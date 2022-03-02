package iob.logic.activities;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import iob.ActivityId;
import iob.InvokedBy;
import iob.UserID;
import iob.boundaries.ActivityBoundary;
import iob.data.ActivityEntity;

//@Service
public class ActivitiesServiceMockup implements ActivitiesService {

	private Map<ActivityId, ActivityEntity> activityMockDatabase;
	private ActivitiesConverter converter;

	@Autowired
	public ActivitiesServiceMockup(ActivitiesConverter converter) {
		super();
		this.converter = converter;
	}

	@Autowired
	public void setConverter(ActivitiesConverter converter) {
		this.converter = converter;
	}

	@PostConstruct
	public void init() {
		this.activityMockDatabase = Collections.synchronizedMap(new HashMap<ActivityId, ActivityEntity>());
	}

	@Override
	public Object invokeActivity(ActivityBoundary activity) {

		ActivityId invalidId = new ActivityId();
		if (activity.getActivityId() != null && !invalidId.equals(activity.getActivityId())) {
			return null;
		}

		UserID userId = activity.getInvokedBy().getUserId();
		String id_str = userId.getEmail() + activity.getCreatedTimestamp().toString();
		ActivityId id = new ActivityId(userId.getDomain(), id_str);

		activity.setActivityId(id);
		ActivityEntity entity = converter.convertToEntity(activity);
		activityMockDatabase.put(activity.getActivityId(), entity);

		return activity;
	}

	@Override
	public List<ActivityBoundary> getAllActivities(String adminDomain, String adminEmail) {
		InvokedBy activityFilter = new InvokedBy(new UserID(adminDomain, adminEmail));

		/*
		 * stream over database (map) Entries filter entires based on values
		 * (ActvityEntity) InvokedBy map the new entries to ActivityBoundary Collect the
		 * data into a list
		 */

		List<ActivityBoundary> activtiesBoundary = activityMockDatabase.values().stream()
				.filter(v -> v.getInvokedBy().equals(activityFilter)).map(v -> converter.convertToBoundary(v))
				.collect(Collectors.toList());

		return activtiesBoundary;
	}

	@Override
	public void deleteAllActivities(String adminDomain, String adminEmail) {
		InvokedBy activityFilter = new InvokedBy(new UserID(adminDomain, adminEmail));

		/*
		 * stream over database filter all entries not equal to activityFilter convert
		 * stream back to map
		 */

		activityMockDatabase = activityMockDatabase.entrySet().stream()
				.filter(e -> !e.getValue().getInvokedBy().equals(activityFilter))
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}
}
