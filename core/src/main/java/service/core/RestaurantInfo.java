package service.core;

import java.util.HashMap;

public class RestaurantInfo {

	public RestaurantInfo(String name, String licenseNumber, int openTime, int closeTime, HashMap<Integer, Dish> menu) {
		this.name = name;
		this.licenseNumber = licenseNumber;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.menu = menu;
	}

	public RestaurantInfo() {}

	/**
	 * Public fields are used as modern best practice argues that use of set/get
	 * methods is unnecessary as (1) set/get makes the field mutable anyway, and
	 * (2) set/get introduces additional method calls, which reduces performance.
	 */
	public String name;
	public String licenseNumber;
	public int openTime;
	public int closeTime;
	public HashMap<Integer, Dish> menu;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public int getOpenTime() {
		return openTime;
	}

	public void setOpenTime(int openTime) {
		this.openTime = openTime;
	}

	public int getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(int closeTime) {
		this.closeTime = closeTime;
	}

	public HashMap<Integer, Dish> getMenu() {
		return menu;
	}

	public void setMenu(HashMap<Integer, Dish> menu) {
		this.menu = menu;
	}
}
