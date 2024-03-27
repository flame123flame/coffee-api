package framework.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatatableRequest implements Serializable {

	private static final long serialVersionUID = -2101666256658867880L;
	int page;
	int length;
	List<DatatableSort> sort = new ArrayList<DatatableSort>();
	List<DatatableFilter> filter = new ArrayList<DatatableFilter>();

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List<DatatableSort> getSort() {
		return sort;
	}

	public void setSort(List<DatatableSort> sort) {
		this.sort = sort;
	}

	public List<DatatableFilter> getFilter() {
		return filter;
	}

	public void setFilter(List<DatatableFilter> filter) {
		this.filter = filter;
	}
	
	public boolean isSort() {
		return !this.sort.isEmpty();
	}
}
