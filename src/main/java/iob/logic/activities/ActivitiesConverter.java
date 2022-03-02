package iob.logic.activities;

import org.springframework.stereotype.Component;
import iob.Instance;
import iob.boundaries.ActivityBoundary;
import iob.data.ActivityEntity;

@Component
public class ActivitiesConverter {

	public ActivityEntity convertToEntity(ActivityBoundary activityBoundery) {
		ActivityEntity activityEntity = new ActivityEntity();

		activityEntity.setActivityId(activityBoundery.getActivityId());
		activityEntity.setType(activityBoundery.getType());
		activityEntity.setInstanceId(activityBoundery.getInstance().getInstanceId());
		activityEntity.setCreatedTimestamp(activityBoundery.getCreatedTimestamp());
		activityEntity.setInvokedBy(activityBoundery.getInvokedBy());
		activityEntity.setActivityAttributes(activityBoundery.getActivityAttributes());

		return activityEntity;
	}

	public ActivityBoundary convertToBoundary(ActivityEntity activityEntity) {
		ActivityBoundary activityBoundary = new ActivityBoundary();

		activityBoundary.setActivityId(activityEntity.getActivityId());
		activityBoundary.setType(activityEntity.getType());
		activityBoundary.setInstance(new Instance(activityEntity.getInstanceId()));
		activityBoundary.setCreatedTimestamp(activityEntity.getCreatedTimestamp());
		activityBoundary.setInvokedBy(activityEntity.getInvokedBy());
		activityEntity.setActivityAttributes(activityEntity.getActivityAttributes());

		return activityBoundary;
	}
}
