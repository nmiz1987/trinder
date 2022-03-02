package iob.ops;

import iob.boundaries.ActivityBoundary;

public interface CommandInvoker {
	public Object invokeCommand(ActivityBoundary command);
}
