package ar.com.mobeats.consolidar.backend.pagination;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import ar.com.mobeats.consolidar.backend.model.BackEndObject;
import ar.com.mobeats.consolidar.backend.util.Tupla;

public class PageRequest extends BackEndObject {

    private static final long serialVersionUID = -1103944103272599761L;

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 20;

    private final Integer pageNumber;
    private final Integer pageSize;

    public PageRequest(final Integer pageNumber, final Integer pageSize) {
        this.pageNumber = (pageNumber == null) ? DEFAULT_PAGE_NUMBER : pageNumber;
        this.pageSize = (pageSize == null) ? DEFAULT_PAGE_SIZE : pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public org.springframework.data.domain.PageRequest convert() {
        return new org.springframework.data.domain.PageRequest(pageNumber, pageSize);
    }

    public org.springframework.data.domain.PageRequest convert(Direction dir, String field) {
        return new org.springframework.data.domain.PageRequest(pageNumber, pageSize, dir, field);
    }

    public org.springframework.data.domain.PageRequest convertWithSort(Tupla... tuplas) {
        for (Tupla tupla : tuplas) {
            if (tupla.getSecondMember() != null) {
                return new org.springframework.data.domain.PageRequest(pageNumber, pageSize, Sort.Direction.ASC,
                        tupla.getFirstMember());
            }
        }
        return new org.springframework.data.domain.PageRequest(pageNumber, pageSize, Sort.Direction.DESC,
                "fechaCreacion");
    }

    /**
     * Devuelve el page request con el sort del criterio y direccion especificados.
     * @param criterio : Un string representando al campo por el que se desee sortear.
     * @param direccion : Un string (asc/desc) representando la direccion del sorting.
     * @return Un @PageRequest con el criterio y direccion especificados. 
     */
    public org.springframework.data.domain.PageRequest convertWithSpecificSort(String criterio, String direccion) {
        Sort.Direction dir = direccion.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC; 
        return new org.springframework.data.domain.PageRequest(pageNumber, pageSize, dir, criterio);
    }

    public org.springframework.data.domain.PageRequest convertWithFieldsSort(String[] criterios, String direccion) {
        Sort.Direction dir = direccion.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC; 
        return new org.springframework.data.domain.PageRequest(pageNumber, pageSize, dir, criterios);
    }

}
