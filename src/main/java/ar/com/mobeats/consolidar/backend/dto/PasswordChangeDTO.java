package ar.com.mobeats.consolidar.backend.dto;

import ar.com.mobeats.consolidar.backend.model.BackEndObject;

public class PasswordChangeDTO extends BackEndObject {

	private static final long serialVersionUID = -6879676791650952453L;

	private String oldPassword;
	private String newPassword;
	
	public String getOldPassword() {
		return oldPassword;
	}
	
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
}
