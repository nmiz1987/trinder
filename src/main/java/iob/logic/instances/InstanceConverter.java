package iob.logic.instances;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import iob.CreatedBy;
import iob.InstanceId;
import iob.UserID;
import iob.boundaries.InstanceBoundary;
import iob.data.InstanceEntity;

@Component
public class InstanceConverter {
	private String projectName;
	
	@Value("${spring.application.name}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}	

	public InstanceEntity convertToEntity(String userDomain, String userEmail, InstanceBoundary instance) {
		InstanceEntity entity = new InstanceEntity();
		
		if (instance.getInstanceId() == null)
			entity.setInstanceId(new InstanceId(this.projectName, ""));
		else
			entity.setInstanceId(new InstanceId(this.projectName, instance.getInstanceId().getId()));		
		entity.setType(instance.getType());
		entity.setName(instance.getName());
		entity.setActive(instance.getActive());
		entity.setCreatedTimestamp(new Date());
		entity.setCreatedBy(new CreatedBy(new UserID(userDomain, userEmail)));
		entity.setLocation(instance.getLocation());
		entity.setInstanceAttributes(instance.getInstanceAttributes());
		
		return entity;
	}
	
	public InstanceBoundary convertToBoundary( InstanceEntity entity) {
		InstanceBoundary boundary = new InstanceBoundary();
		
		boundary.setInstanceId(entity.getInstanceId());		
		boundary.setType(entity.getType());
		boundary.setName(entity.getName());
		boundary.setActive(entity.getActive());
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		boundary.setCreatedBy(entity.getCreatedBy());
		boundary.setLocation(entity.getLocation());
		boundary.setInstanceAttributes(entity.getInstanceAttributes());
		
		return boundary;
	}
}
