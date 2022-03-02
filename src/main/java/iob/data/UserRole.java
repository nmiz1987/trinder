package iob.data;

public enum UserRole {
	PLAYER, MANAGER, ADMIN;

	public static boolean contains(String s) {
		for (UserRole choice : values())
			if (choice.name().equalsIgnoreCase(s))
				return true;
		return false;
	}
}
