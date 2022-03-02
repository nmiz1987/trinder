package iob.logic.instances;

import java.util.Date;
import java.util.List;

import iob.Location;
import iob.boundaries.InstanceBoundary;
import iob.boundaries.InstanceIdBoundary;

public interface InstancesServiceWithPagination extends InstancesService {

	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail, int size, int page,
			boolean sortAscending, String[] sortBy);

	public List<InstanceBoundary> getInstancesByName(String userDomain, String userEmail, String name, int size,
			int page, boolean sortAscending, String[] sortBy);

	public List<InstanceBoundary> getInstancesByType(String userDomain, String userEmail, String type, int size,
			int page, boolean sortAscending, String[] sortBy);

	public List<InstanceBoundary> getInstancesByTypeAndAttr(String domain, String email, String type, String attrName,
			String attrVal, int size, int page, boolean sortAscending, String[] sortBy);

	public List<InstanceBoundary> getInstancesByLocation(String domain, String email, Location location,
			double distance, int size, int page, boolean sortAscending, String[] sortBy);

	public List<InstanceBoundary> getInstancesByDate(String domain, String email, Date date, int size, int page,
			boolean sortAscending, String[] sortBy);

	public void addInstanceChildtoInstanceParent(String domain, String email, String instanceDomain, String instanceId,
			InstanceIdBoundary instanceIdBoundary);

	public List<InstanceBoundary> getArrayInstanceParent(String domain, String email, String instanceDomain,
			String instanceId, int size, int page, boolean sortAscending, String[] sortBy);

	public List<InstanceBoundary> getAllChildrenOfExistingParent(String domain, String email, String instanceDomain,
			String instanceId, int size, int page, boolean sortAscending, String[] sortBy);

	public List<InstanceBoundary> getInstancesByTypeAndAttribute(String domain, String email, String type,
			String attrName, String attrVal, int size, int page, boolean sortAscending, String[] sortBy);
}
