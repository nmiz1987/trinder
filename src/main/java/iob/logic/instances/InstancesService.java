package iob.logic.instances;

import java.util.List;

import iob.boundaries.InstanceBoundary;

public interface InstancesService {

	public InstanceBoundary createInstance(String userDomain, String userEmail, InstanceBoundary instance);

	public InstanceBoundary updateInstance(String userDomain, String userEmail, String instanceDomain,
			String instanceId, InstanceBoundary update);

	@Deprecated
	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail);

	public InstanceBoundary getSpecificInstance(String userDomain, String userEmail, String instanceDomain,
			String instanceId);

	public void deleteAllInstances(String userDomain, String userEmail);
}
