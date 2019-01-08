package ar.com.mobeats.consolidar.backend.util;

public class Tupla {
	
	private String firstMember;
	private String secondMember;
	
	public Tupla(String firstMember, String secondMember) {
		super();
		this.firstMember = firstMember;
		this.secondMember = secondMember;
	}
	
	public String getFirstMember() {
		return firstMember;
	}
	public void setFirstMember(String firstMember) {
		this.firstMember = firstMember;
	}
	public String getSecondMember() {
		return secondMember;
	}
	public void setSecondMember(String secondMember) {
		this.secondMember = secondMember;
	}
	
	
}
