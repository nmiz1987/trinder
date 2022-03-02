package iob.ops;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import iob.boundaries.ActivityBoundary;

@Component("list commands")
public class ListCommandsInvoker implements CommandInvoker {
	private ApplicationContext applicationContext;

	@Autowired
	public ListCommandsInvoker(ApplicationContext applicationContext) {
		super();
		this.applicationContext = applicationContext;
	}

	@Override
	public Object invokeCommand(ActivityBoundary command) {
		Map<String, Object> rv = new HashMap<>();
		String[] commands = this.applicationContext.getBeanNamesForType(CommandInvoker.class);
		rv.put("commands", commands);

		return rv;
	}
}
