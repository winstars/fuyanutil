package models;

public class UserPerformance {
    private int id;

    private int syear;

    private int smonth;

    private int userid;

    private double saleValue;

    private double saleValueSum = 0.0;

    private double saleNum;

    private double saleNumSum = 0.0;

    private int completeStatus;

    private double extractValue;

    private double extraValue;

    private double givenValue;

    private double sale1;

    private double sale2;

    private double exchange;

    private double ungivenValue = 0;

    public void set(int id, int syear, int smonth, int userid,
                    double saleValue, double saleNum, int completeStatus, double extractValue,
                    double extraValue, double givenValue, double sale1, double sale2,
                    double exchange) {
        this.id = id;
        this.syear = syear;
        this.smonth = smonth;
        this.userid = userid;
        this.saleValue = saleValue;
        this.saleNum = saleNum;
        this.completeStatus = completeStatus;
        this.extractValue = extractValue;
        this.extraValue = extraValue;
        this.givenValue = givenValue;
        this.sale1 = sale1;
        this.sale2 = sale2;
        this.exchange = exchange;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSyear() {
        return syear;
    }

    public void setSyear(int syear) {
        this.syear = syear;
    }

    public int getSmonth() {
        return smonth;
    }

    public void setSmonth(int smonth) {
        this.smonth = smonth;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public double getSaleValue() {
        return saleValue;
    }

    public void setSaleValue(double saleValue) {
        this.saleValue = saleValue;
    }

    public double getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(double saleNum) {
        this.saleNum = saleNum;
    }

    public int getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(int completeStatus) {
        this.completeStatus = completeStatus;
    }

    public double getExtractValue() {
        return extractValue;
    }

    public void setExtractValue(double extractValue) {
        this.extractValue = extractValue;
    }

    public double getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(double extraValue) {
        this.extraValue = extraValue;
    }

    public double getGivenValue() {
        return givenValue;
    }

    public void setGivenValue(double givenValue) {
        this.givenValue = givenValue;
    }

    public double getSale1() {
        return sale1;
    }

    public void setSale1(double sale1) {
        this.sale1 = sale1;
    }

    public double getSale2() {
        return sale2;
    }

    public void setSale2(double sale2) {
        this.sale2 = sale2;
    }

    public double getExchange() {
        return exchange;
    }

    public void setExchange(double exchange) {
        this.exchange = exchange;
    }

    public double getSaleValueSum() {
        return saleValueSum;
    }

    public void setSaleValueSum(double saleValueSum) {
        this.saleValueSum = saleValueSum;
    }

    public double getSaleNumSum() {
        return saleNumSum;
    }

    public void setSaleNumSum(double saleNumSum) {
        this.saleNumSum = saleNumSum;
    }

    public double getUngivenValue() {
        return ungivenValue;
    }

    public void setUngivenValue(double ungivenValue) {
        this.ungivenValue = ungivenValue;
    }
}