package iob.rest;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iob.Location;
import iob.boundaries.InstanceBoundary;
import iob.boundaries.InstanceIdBoundary;
import iob.logic.instances.InstancesServiceWithPagination;

@RestController
public class InstanceController {
	private InstancesServiceWithPagination instanceService;

	private Date convertCreationToTimeStamp(String creationWindow) {
		long epochs = -1;

		if (creationWindow.equalsIgnoreCase("LAST_HOUR")) {
			epochs = 60 * 60;
		} else if (creationWindow.equalsIgnoreCase("LAST_24_HOUR")) {
			epochs = 60 * 60 * 24;
		} else if (creationWindow.equalsIgnoreCase("LAST_7_DAYS")) {
			epochs = 60 * 60 * 24 * 7;
		} else if (creationWindow.equalsIgnoreCase("LAST_30_DAYS")) {
			epochs = 60 * 60 * 24 * 30;
		} else {
			return null;
		}

		long timestamp = java.time.Instant.now().getEpochSecond();

		return new Date(timestamp - epochs);
	}

	@Autowired
	public InstanceController(InstancesServiceWithPagination instanceService) {
		this.instanceService = instanceService;
	}

	// Create an instance
	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary createInstance(@PathVariable("userDomain") String domain,
			@PathVariable("userEmail") String email, @RequestBody InstanceBoundary input) {
		return this.instanceService.createInstance(domain, email, input);
	}

	// Update an instance
	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateInstance(@PathVariable("userDomain") String domain, @PathVariable("userEmail") String email,
			@PathVariable("instanceDomain") String intanceDomain, @PathVariable("instanceId") String instanceId,
			@RequestBody InstanceBoundary instanceBoundary) {

		this.instanceService.updateInstance(domain, email, intanceDomain, instanceId, instanceBoundary);
	}

	// Retrieve instance
	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary getInstance(@PathVariable("userDomain") String domain,
			@PathVariable("userEmail") String email, @PathVariable("instanceDomain") String intanceDomain,
			@PathVariable("instanceId") String instanceId) {

		return this.instanceService.getSpecificInstance(domain, email, intanceDomain, instanceId);
	}

	// Get all instances
	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}?size={size}&page={page}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary[] getAllInstances(@PathVariable("userDomain") String domain,
			@PathVariable("userEmail") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		boolean sortAscending = true;
		String[] sortBy = new String[] { "createdTimestamp" };
		return this.instanceService.getAllInstances(domain, email, size, page, sortAscending, sortBy)
				.toArray(new InstanceBoundary[0]);
	}

	// get instances by name
	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}/search/byName/{name}?size={size}&page={page}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary[] getInstancesByName(@PathVariable("userDomain") String domain,
			@PathVariable("userEmail") String email, @PathVariable("name") String name,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		boolean sortAscending = true;
		String[] sortBy = new String[] { "createdTimestamp" };
		return this.instanceService.getInstancesByName(domain, email, name, size, page, sortAscending, sortBy)
				.toArray(new InstanceBoundary[0]);
	}

	// get instances by type
	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}/search/byType/{type}?size={size}&page={page}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary[] getInstancesByType(@PathVariable("userDomain") String domain,
			@PathVariable("userEmail") String email, @PathVariable("type") String type,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		boolean sortAscending = true;
		String[] sortBy = new String[] { "createdTimestamp" };
		return this.instanceService.getInstancesByType(domain, email, type, size, page, sortAscending, sortBy)
				.toArray(new InstanceBoundary[0]);
	}

	// get instances by type and attr
	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}/search/byType/{type}/{attrName}/{attrVal}?&size={size}&page={page}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary[] getInstancesByTypeAndAttr(@PathVariable("userDomain") String domain,
			@PathVariable("userEmail") String email, @PathVariable("type") String type,
			@PathVariable("attrName") String attrName, @PathVariable("attrVal") String attrVal,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		boolean sortAscending = true;
		String[] sortBy = new String[] { "createdTimestamp" };
		return this.instanceService
				.getInstancesByTypeAndAttr(domain, email, type, attrName, attrVal, size, page, sortAscending, sortBy)
				.toArray(new InstanceBoundary[0]);
	}

	// get instances by location
	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}/search/near/{lat}/{lng}/{distance}?size={size}&page={page}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary[] getInstancesByLocation(@PathVariable("userDomain") String domain,
			@PathVariable("userEmail") String email, @PathVariable("lat") double lat, @PathVariable("lng") double lng,
			@PathVariable("distance") double distance,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		Location location = new Location(lat, lng);
		boolean sortAscending = true;
		String[] sortBy = new String[] { "createdTimestamp" };
		return this.instanceService
				.getInstancesByLocation(domain, email, location, distance, size, page, sortAscending, sortBy)
				.toArray(new InstanceBoundary[0]);
	}

	 //get instances by creation
	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}/search/created/{creationWindow}?size={size}&page={page}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary [] getInstancesByCreation(@PathVariable("userDomain") String domain,
											          @PathVariable("userEmail") String email,
											          @PathVariable("creationWindow") String creation,
											          @RequestParam(name = "size", required = false, defaultValue = "10") int size,
											          @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		boolean sortAscending = true;
		String[] sortBy = new String[]{"createdTimestamp"};

		Date date = this.convertCreationToTimeStamp(creation);
		
		if (date == null) 
			throw new RuntimeException("invalid creation window");

		return this.instanceService
			.getInstancesByDate(domain, email, date, size, page, sortAscending, sortBy)
			.toArray(new InstanceBoundary[0]);
	}

	// Add (Instance)child to existing (Instance)parent
	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}/children", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addInstanceChildtoInstanceParent(@PathVariable("userDomain") String domain,
			@PathVariable("userEmail") String email, @PathVariable("instanceDomain") String intanceDomain,
			@PathVariable("instanceId") String instanceId, @RequestBody InstanceIdBoundary instanceIdBoundary) {

		this.instanceService.addInstanceChildtoInstanceParent(domain, email, intanceDomain, instanceId,
				instanceIdBoundary);
	}

	// Get (Instance)parent
	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}/parents", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary[] getArrayInstanceParent(@PathVariable("userDomain") String domain,
			@PathVariable("userEmail") String email, @PathVariable("instanceDomain") String instanceDomain,
			@PathVariable("instanceId") String instanceId,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		boolean sortAscending = true;
		String[] sortBy = new String[] { "createdTimestamp" };

		return this.instanceService
				.getArrayInstanceParent(domain, email, instanceDomain, instanceId, size, page, sortAscending, sortBy)
				.toArray(new InstanceBoundary[0]);
	}

	// get all children of (Instance)parent
	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}/children", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary[] getAllChildrenOfExistingParent(@PathVariable("userDomain") String domain,
			@PathVariable("userEmail") String email, @PathVariable("instanceDomain") String instanceDomain,
			@PathVariable("instanceId") String instanceId,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		boolean sortAscending = true;
		String[] sortBy = new String[] { "createdTimestamp" };

		return this.instanceService.getAllChildrenOfExistingParent(domain, email, instanceDomain, instanceId, size,
				page, sortAscending, sortBy).toArray(new InstanceBoundary[0]);
	}
}
