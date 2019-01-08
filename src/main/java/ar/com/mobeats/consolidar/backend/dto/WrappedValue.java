package ar.com.mobeats.consolidar.backend.dto;


public class WrappedValue<T> {

    private T value;
    
    public WrappedValue() { }
    
    public WrappedValue(T value) {
        this.setValue(value);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
    
}
