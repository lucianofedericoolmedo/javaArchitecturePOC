package ar.com.mobeats.consolidar.backend.model;

public enum Genero {

	MASCULINO('M'), FEMENINO('F');
	
	private Character value;
	
	private Genero(Character value) {
		this.value = value;
	}
	
	public Character getValue() {
		return value;
	}
}
