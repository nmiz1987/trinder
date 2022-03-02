package iob.ops;

import org.springframework.stereotype.Component;

import iob.boundaries.ActivityBoundary;
import iob.exceptions.AccessDeniedException;

@Component("command not found")
public class DefaultCommandInvoker implements CommandInvoker {

	@Override
	public Object invokeCommand(ActivityBoundary command) {
		throw new AccessDeniedException("This command is not available");
	}
}
