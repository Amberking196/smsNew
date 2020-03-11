package com.server.module.zfb_trade.paramconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix="h5URLConfig")
public class H5URLConfig {

	public String buyEntry;
	public String loginPage;
	public String weightH5Page;
    public String weightH5PageForFree;
    public String replenishH5Page;
	public String signUrl;
	public String signReturnUrl;
	public String vmListPage;
	public String vmListVerifyPage;
	public String phoneVerifyPage;
	public String realNamePage;
	public String address;
	public String skipUrl;

    public String getWeightH5PageForFree() {
        return weightH5PageForFree;
    }

    public void setWeightH5PageForFree(String weightH5PageForFree) {
        this.weightH5PageForFree = weightH5PageForFree;
    }

    public String getSkipUrl() {
		return skipUrl;
	}
	public void setSkipUrl(String skipUrl) {
		this.skipUrl = skipUrl;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRealNamePage() {
		return realNamePage;
	}
	public void setRealNamePage(String realNamePage) {
		this.realNamePage = realNamePage;
	}
	public String getBuyEntry() {
		return buyEntry;
	}
	public void setBuyEntry(String buyEntry) {
		this.buyEntry = buyEntry;
	}
	public String getLoginPage() {
		return loginPage;
	}
	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}
	public String getWeightH5Page() {
		return weightH5Page;
	}
	public void setWeightH5Page(String weightH5Page) {
		this.weightH5Page = weightH5Page;
	}
	public String getReplenishH5Page() {
		return replenishH5Page;
	}
	public void setReplenishH5Page(String replenishH5Page) {
		this.replenishH5Page = replenishH5Page;
	}
	public String getSignUrl() {
		return signUrl;
	}
	public void setSignUrl(String signUrl) {
		this.signUrl = signUrl;
	}
	public String getSignReturnUrl() {
		return signReturnUrl;
	}
	public void setSignReturnUrl(String signReturnUrl) {
		this.signReturnUrl = signReturnUrl;
	}
	public String getVmListPage() {
		return vmListPage;
	}
	public void setVmListPage(String vmListPage) {
		this.vmListPage = vmListPage;
	}
	public String getVmListVerifyPage() {
		return vmListVerifyPage;
	}
	public void setVmListVerifyPage(String vmListVerifyPage) {
		this.vmListVerifyPage = vmListVerifyPage;
	}
	public String getPhoneVerifyPage() {
		return phoneVerifyPage;
	}
	public void setPhoneVerifyPage(String phoneVerifyPage) {
		this.phoneVerifyPage = phoneVerifyPage;
	}

}
