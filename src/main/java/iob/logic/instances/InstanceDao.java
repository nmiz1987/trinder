package iob.logic.instances;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import iob.InstanceId;
import iob.data.InstanceEntity;

public interface InstanceDao extends PagingAndSortingRepository<InstanceEntity, InstanceId> { 
	
	List<InstanceEntity> findAllByName(@Param("name")String name, Pageable pageable);
	
	List<InstanceEntity> findAllByType(@Param("type")String type, Pageable pageable);
	
	@Query("SELECT i FROM InstanceEntity i WHERE i.createdTimestamp >= createdTimestamp")
	List<InstanceEntity> findAllByCreatedTimestamp(@Param("createdTimestamp")Date createdTimestamp, Pageable pageable);

	List<InstanceEntity> findAllByChildren_InstanceId(@Param("instanceIdChild")InstanceId instanceIdChild, Pageable pageable);
	
	List<InstanceEntity> findAllByParent_InstanceId(@Param("instanceIdParent")InstanceId instanceIdParent, Pageable pageable);
	
	@Query(value = "SELECT i FROM InstanceEntity i WHERE i.type = type AND i.instanceAttributes LIKE '%attrName%:%attrVal%'")
	List<InstanceEntity> findAllByTypeAndAttributes(String type, String attrName, String attrVal, Pageable of);
}

