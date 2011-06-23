package models;

public class UserPerformance {
	private int id;

	private int syear;

	private int smonth;

	private int userid;

	private double saleValue;

	private double saleNum;

	private double extractValue;

	private double extraValue;

	private double givenValue;

	private double sale1;

	private double sale2;

	private double exchange;

	public void set(int id, int syear, int smonth, int userid,
			double saleValue, double saleNum, double extractValue,
			double extraValue, double givenValue, double sale1, double sale2,
			double exchange) {
		this.id = id;
		this.syear = syear;
		this.smonth = smonth;
		this.userid = userid;
		this.saleValue = saleValue;
		this.saleNum = saleNum;
		this.extractValue = extractValue;
		this.extraValue = extraValue;
		this.givenValue = givenValue;
		this.sale1 = sale1;
		this.sale2 = sale2;
		this.exchange = exchange;
	}

	public int getId() {
		return this.id;
	}

	public int getSyear() {
		return this.syear;
	}

	public int getSmonth() {
		return this.smonth;
	}

	public int getUserid() {
		return this.userid;
	}

	public double getSaleValue() {
		return this.saleValue;
	}

	public double getSaleNum() {
		return this.saleNum;
	}

	public double getExtractValue() {
		return this.extractValue;
	}

	public double getExtraValue() {
		return this.extraValue;
	}

	public double getGivenValue() {
		return this.givenValue;
	}

	public double getSale1() {
		return this.sale1;
	}

	public double getSale2() {
		return this.sale2;
	}

	public double getExchange() {
		return this.exchange;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSyear(int syear) {
		this.syear = syear;
	}

	public void setSmonth(int smonth) {
		this.smonth = smonth;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public void setSaleValue(double saleValue) {
		this.saleValue = saleValue;
	}

	public void setSaleNum(double saleNum) {
		this.saleNum = saleNum;
	}

	public void setExtractValue(double extractValue) {
		this.extractValue = extractValue;
	}

	public void setExtraValue(double extraValue) {
		this.extraValue = extraValue;
	}

	public void setGrivenValue(double givenValue) {
		this.givenValue = givenValue;
	}

	public void setSale1(double sale1) {
		this.sale1 = sale1;
	}

	public void setSale2(double sale2) {
		this.sale2 = sale2;
	}

	public void setExchange(double exchange) {
		this.exchange = exchange;
	}
}