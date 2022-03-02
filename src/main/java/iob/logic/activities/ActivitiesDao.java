package iob.logic.activities;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import iob.ActivityId;
import iob.InvokedBy;
import iob.data.ActivityEntity;

public interface ActivitiesDao extends PagingAndSortingRepository<ActivityEntity, ActivityId> {

	List<ActivityEntity> findByInvokedBy(@Param("invokedBy") InvokedBy invokedBy);

	void deleteByInvokedBy(@Param("invokedBy") InvokedBy invokedBy);
}
