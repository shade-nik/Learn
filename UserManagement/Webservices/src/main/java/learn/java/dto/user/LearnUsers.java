package learn.java.dto.user;

import java.util.List;

public class LearnUsers {

	private List<LearnUser> users;

	public LearnUsers() {
	}

	public LearnUsers(List<LearnUser> users) {
		this.users = users;
	}

	public List<LearnUser> getUsers() {
		return users;
	}

	public void setUsers(List<LearnUser> users) {
		this.users = users;
	}
}
