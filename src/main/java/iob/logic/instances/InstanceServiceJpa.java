package iob.logic.instances;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.InstanceId;
import iob.Location;
import iob.boundaries.*;
import iob.data.InstanceEntity;
import iob.exceptions.UnimplementedOperationException;
import iob.exceptions.UserNotFoundException;

@Service
public class InstanceServiceJpa implements InstancesServiceWithPagination {
	private InstanceConverter converter;
	private InstanceDao instanceDao;
	static private int idCounter = 1; // counter for creation of new instances

	@Autowired
	public void setConverter(InstanceConverter converter) {
		this.converter = converter;
	}

	@Autowired
	public void setInstanceDao(InstanceDao instanceDao) {
		this.instanceDao = instanceDao;
	}

	private InstanceEntity readInstanceFromDb(InstanceId instanceId) {
		return this.instanceDao.findById(instanceId)
				.orElseThrow(() -> new UserNotFoundException("Could not find instance with id:" + instanceId));
	}

	@Override
	@Transactional // (readOnly = false)
	@InstancePermission(type = "create")
	public InstanceBoundary createInstance(String userDomain, String userEmail, InstanceBoundary instance) {
		System.err.println("alkjaslkd");
		// convert InstanceBoundary to entity
		InstanceEntity entity = this.converter.convertToEntity(userDomain, userEmail, instance);
		InstanceId temp = new InstanceId(entity.getInstanceId().getDomain(), String.valueOf(idCounter++));
		entity.setInstanceId(temp);

		// store entity to DB
		entity = this.instanceDao.save(entity);
		// convert entity to InstanceEntity and return it
		return this.converter.convertToBoundary(entity);
	}

	@Override
	@Transactional
	@InstancePermission(type = "update")
	public InstanceBoundary updateInstance(String userDomain, String userEmail, String instanceDomain,
			String instanceId, InstanceBoundary update) {

		// Get instance data from DB
		InstanceBoundary existingInstance = this.getSpecificInstance(userDomain, userEmail, instanceDomain, instanceId);

		// Update instance details in DB
		if (update.getInstanceId() != null) {
			existingInstance.setInstanceId(existingInstance.getInstanceId());
		}

		if (update.getType() != null)
			existingInstance.setType(update.getType());

		if (update.getName() != null)
			existingInstance.setName(update.getName());

		if (update.getActive() != null)
			existingInstance.setActive(update.getActive());

		if (update.getLocation() != null)
			existingInstance.setLocation(update.getLocation());

		if (update.getInstanceAttributes() != null)
			existingInstance.setInstanceAttributes(update.getInstanceAttributes());

		// update DB
		InstanceEntity in = this.converter.convertToEntity(userDomain, userEmail, existingInstance);
		in = this.instanceDao.save(in);

		return this.converter.convertToBoundary(in);
	}

	@Override
	@Transactional(readOnly = true)
	@Deprecated
	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail) {
		throw new UnimplementedOperationException("This operation is deprecated.");
	}

	@Override
	@Transactional(readOnly = true)
	@InstancePermission(type = "specific")
	public InstanceBoundary getSpecificInstance(String userDomain, String userEmail, String instanceDomain,
			String instanceId) {

		InstanceId instanceIdToCheck = new InstanceId(instanceDomain, instanceId);
		InstanceEntity instanceEntity = this.readInstanceFromDb(instanceIdToCheck);

		return this.converter.convertToBoundary(instanceEntity);
	}

	@Override
	@Transactional
	@InstancePermission(type = "deleteInstances")
	public void deleteAllInstances(String userDomain, String userEmail) {
		this.instanceDao.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	@InstancePermission(type = "allInstances")
	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail, int size, int page,
			boolean sortAscending, String[] sortBy) {

		Direction direction = sortAscending ? Direction.ASC : Direction.DESC;

		return this.instanceDao.findAll(PageRequest.of(page, size, direction, sortBy)) // Page<InstanceEntity>
				.stream() // Stream<InstanceEntity>
				.map(this.converter::convertToBoundary) // Stream<InstanceBoundary>
				.collect(Collectors.toList()); // List<InstanceBoundary>
	}

	@Override
	@Transactional
	@InstancePermission(type = "allInstances")
	public List<InstanceBoundary> getInstancesByName(String userDomain, String userEmail, String name, int size,
			int page, boolean sortAscending, String[] sortBy) {

		Direction direction = sortAscending ? Direction.ASC : Direction.DESC;

		return this.instanceDao.findAllByName(name, PageRequest.of(page, size, direction, sortBy)) // Page<InstanceEntity>
				.stream() // Stream<InstanceEntity>
				.map(this.converter::convertToBoundary) // Stream<InstanceBoundary>
				.collect(Collectors.toList()); // List<InstanceBoundary>
	}

	@Override
	@Transactional
	@InstancePermission(type = "allInstances")
	public List<InstanceBoundary> getInstancesByType(String userDomain, String userEmail, String type, int size,
			int page, boolean sortAscending, String[] sortBy) {

		Direction direction = sortAscending ? Direction.ASC : Direction.DESC;

		return this.instanceDao.findAllByType(type, PageRequest.of(page, size, direction, sortBy)) // Page<InstanceEntity>
				.stream() // Stream<InstanceEntity>
				.map(this.converter::convertToBoundary) // Stream<InstanceBoundary>
				.collect(Collectors.toList()); // List<InstanceBoundary>
	}

	@Override
	@Transactional
	@InstancePermission(type = "allInstances")
	public List<InstanceBoundary> getInstancesByTypeAndAttr(String domain, String email, String type, String attrName,
			String attrVal, int size, int page, boolean sortAscending, String[] sortBy) {
		return null;
	}

	@Override
	@Transactional
	@InstancePermission(type = "allInstances")
	public List<InstanceBoundary> getInstancesByLocation(String domain, String email, Location location,
			double distance, int size, int page, boolean sortAscending, String[] sortBy) {

		Direction direction = sortAscending ? Direction.ASC : Direction.DESC;

		return StreamSupport.stream(this.instanceDao.findAll(Sort.by(direction, sortBy)).spliterator(), false)
				.filter(i -> location.isNear(i.getLocation(), distance)).skip(size * page).limit(size)
				.map(this.converter::convertToBoundary).collect(Collectors.toList());
	}

	@Override
	@Transactional
	@InstancePermission(type = "allInstances")
	public List<InstanceBoundary> getInstancesByDate(String domain, String email, Date date, int size, int page,
			boolean sortAscending, String[] sortBy) {

		Direction direction = sortAscending ? Direction.ASC : Direction.DESC;

		return this.instanceDao.findAllByCreatedTimestamp(date, PageRequest.of(page, size, direction, sortBy)) // Page<InstanceEntity>
				.stream() // Stream<InstanceEntity>
				.map(this.converter::convertToBoundary) // Stream<InstanceBoundary>
				.collect(Collectors.toList()); // List<InstanceBoundary>
	}

	@Override
	@Transactional
	@InstancePermission(type = "allInstances")
	public List<InstanceBoundary> getInstancesByTypeAndAttribute(String domain, String email, String type,
			String attrName, String attrVal, int size, int page, boolean sortAscending, String[] sortBy) {

		Direction direction = sortAscending ? Direction.ASC : Direction.DESC;

		return this.instanceDao
				.findAllByTypeAndAttributes(type, attrName, attrVal, PageRequest.of(page, size, direction, sortBy)) // Page<InstanceEntity>
				.stream() // Stream<InstanceEntity>
				.map(this.converter::convertToBoundary) // Stream<InstanceBoundary>
				.collect(Collectors.toList()); // List<InstanceBoundary>
	}

	@Override
	@Transactional
	public void addInstanceChildtoInstanceParent(String domain, String email, String instanceDomain, String instanceId,
			InstanceIdBoundary instanceIdBoundary) {

		// get parent entity(current)
		InstanceId instanceIdParent = new InstanceId(instanceDomain, instanceId);
		InstanceEntity parent = this.readInstanceFromDb(instanceIdParent);

		// get child entity
		InstanceId instanceIdChild = new InstanceId(instanceIdBoundary.getDomain(), instanceIdBoundary.getId());
		InstanceEntity child = this.readInstanceFromDb(instanceIdChild);

		parent.addChild(child);
		child.setParent(parent);

		this.instanceDao.save(child);
	}

	@Override
	@Transactional(readOnly = true)
	public List<InstanceBoundary> getArrayInstanceParent(String domain, String email, String instanceDomain,
			String instanceId, int size, int page, boolean sortAscending, String[] sortBy) {

		// get child entity(current)
		InstanceId instanceIdChild = new InstanceId(instanceDomain, instanceId);
		this.readInstanceFromDb(instanceIdChild); // make sure parent exists

		Direction direction = sortAscending ? Direction.ASC : Direction.DESC;

		// list of all parents(for now: 1 or null)
		List<InstanceEntity> parents = this.instanceDao.findAllByChildren_InstanceId(instanceIdChild,
				PageRequest.of(page, size, direction, sortBy));

		return parents.stream().map(this.converter::convertToBoundary) // Stream<InstanceBoundary>
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<InstanceBoundary> getAllChildrenOfExistingParent(String domain, String email, String instanceDomain,
			String instanceId, int size, int page, boolean sortAscending, String[] sortBy) {

		// get parent entity(current)
		InstanceId instanceIdParent = new InstanceId(instanceDomain, instanceId);
		this.readInstanceFromDb(instanceIdParent); // make sure parent exists

		Direction direction = sortAscending ? Direction.ASC : Direction.DESC;

		List<InstanceEntity> children = this.instanceDao.findAllByParent_InstanceId(instanceIdParent,
				PageRequest.of(page, size, direction, sortBy));

		return children.stream().map(this.converter::convertToBoundary) // Stream<InstanceBoundary>
				.collect(Collectors.toList());
	}
}
