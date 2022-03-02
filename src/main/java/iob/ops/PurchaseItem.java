package iob.ops;

import org.springframework.stereotype.Component;
import java.util.Date;

import iob.PurchaseId;
import iob.boundaries.ActivityBoundary;
import iob.boundaries.PurchaseBoundary;

@Component("PurchaseItem")
public class PurchaseItem implements CommandInvoker {
	private static int id = 1;

	@Override
	public Object invokeCommand(ActivityBoundary command) {
		PurchaseId purchaseId = new PurchaseId(command.getActivityId().getDomain(), String.valueOf(PurchaseItem.id++));

		PurchaseBoundary purchase = new PurchaseBoundary(purchaseId, command.getInvokedBy().getUserId(), new Date());

		purchase.setPurchaseAttributes(command.getActivityAttributes());

		return purchase;
	}
}
