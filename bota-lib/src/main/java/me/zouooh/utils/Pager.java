package me.zouooh.utils;
/**
 * 分页
 * @author zouooh
 *
 */
public class Pager {
	
	private int pageNo = 1;
	private int pageSize = 20;
	private int total = -1;

	public int getPs() {
		return pageSize;
	}
	public void setPs(int ps) {
		this.pageSize = ps;
	}
	public int getPn() {
		return pageNo;
	}
	public void setPn(int pn) {
		this.pageNo = pn;
	}
	/**
	 * 移动到下一页
	 */
	public void nextPage(){
		pageNo ++;
	}
	
	/**
	 * 移动到下一页
	 */
	public void prePage(){
		pageNo --;
		if (pageNo<1) {
			pageNo = 1;
		}
	}
	
	/**
	 * 重置
	 */
	public void reset(){
		this.total = -1;
		this.pageNo = 1;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}

	public boolean hasMore() {
		return pageNo*pageSize < total;
	}
	
	public boolean isUnkown(){
		return total == -1;
	}
}
