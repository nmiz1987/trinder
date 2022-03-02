package iob.boundaries;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import iob.PurchaseId;
import iob.UserID;

public class PurchaseBoundary {
	private PurchaseId purchaseId;
	private UserID userId;
	private Date createdTimestamp;
	private Map<String, Object> purchaseAttributes;

	public PurchaseBoundary() {
		this.purchaseAttributes = new HashMap<String, Object>();
	}

	public PurchaseBoundary(PurchaseId purchaseId, UserID userId, Date createdTimestamp) {
		this.purchaseId = purchaseId;
		this.userId = userId;
		this.createdTimestamp = createdTimestamp;
		this.purchaseAttributes = new HashMap<String, Object>();
	}

	public PurchaseId getPurchaseId() {
		return purchaseId;
	}

	public UserID getUserId() {
		return userId;
	}

	public void setUserId(UserID userId) {
		this.userId = userId;
	}

	public void setPurchaseId(PurchaseId purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Map<String, Object> getPurchaseAttributes() {
		return purchaseAttributes;
	}

	public void setPurchaseAttributes(Map<String, Object> purchaseAttributes) {
		this.purchaseAttributes = purchaseAttributes;
	}
}
