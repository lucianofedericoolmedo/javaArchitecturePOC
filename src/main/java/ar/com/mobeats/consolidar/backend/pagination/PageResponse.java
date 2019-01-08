package ar.com.mobeats.consolidar.backend.pagination;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import ar.com.mobeats.consolidar.backend.model.BackEndObject;

public class PageResponse<T> extends BackEndObject {

	private static final long serialVersionUID = 9149715934242637196L;

	private List<T> items;
	private Integer pageNumber;
	private Integer pageSize;
	private Long totalSize;
	
	public PageResponse() {
	}
	
	public PageResponse(List<T> items, Integer pageNumber, Integer pageSize, Long totalSize) {
		this.items = items;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalSize = totalSize;
	}
	
	public static <T> PageResponse<T> fromPage(Page<T> page) {
		return new PageResponse<T>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
	}

	public boolean addItem(T item){
		if (items == null){
			items = new ArrayList<T>();
		}
		return items.add(item);
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTotalSize() {
		return totalSize;
	}
	
	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}
	
}
