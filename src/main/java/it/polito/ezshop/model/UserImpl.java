package it.polito.ezshop.model;



import it.polito.ezshop.data.*;

public class UserImpl implements User, Storable{
	Integer id;
	String username;
	String password;
	String role;


	public UserImpl(Integer id, String username, String password, String role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	public UserImpl() {
		super();
	}

	/*
	 * Franco: added this method, it gets a variable number of strings (the roles), it returns true if this.role
	 * contains one of them
	 */
	public boolean checkRole(String ...requiredRole) {
		for(String req : requiredRole) {
			if(req.equals(this.role)) {
				return true;
			}
		}
		return false;
	}
	

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getRole() {
		return role;
	}

	@Override
	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String getCSV() {
		return id + "," + username + "," + password + "," + role;
	}

}
