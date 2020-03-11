package com.server.module.trade.web.balance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.server.client.MachinesClient;
import com.server.module.commonBean.ItemInfoDto;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.payRecordItem.PayRecordItemBean;
import com.server.redis.RedisService;

/**
 * 用于计算验证商品
 * @author hebiting
 *
 */
public class BalanceUtil {

	private final static Logger log = LogManager.getLogger(BalanceUtil.class);
	@Autowired
	private MachinesClient machinesClient;
	@Autowired
	private RedisService redisService;
	
	
	int n, y, tsum, max;
	int[] ans;
	int[] stardard;
	int realApproximage = 0;
	int[] approximateArray;
	int sub = Integer.MAX_VALUE;
	
	public int[] getApproximateArray(){
		return approximateArray;
	}

	private void retrack(int i) {
		int k;
		if (i == n) {
			return;
		}
		int l = y / stardard[i] + 1;
		for (k = 0; k <= l; ++k) {
			int t = k * stardard[i];
			if (tsum + t <= max && tsum + t >= 0) {
				tsum += t;
				ans[i] = k;
				int abs = Math.abs(tsum - y);
				if (sub > abs) {
//					System.out.println("curr min = " + tsum);
					sub = abs;
					realApproximage = tsum;
					approximateArray = ans.clone();
				}
				retrack(i + 1);
				tsum -= t;
				ans[i] = 0;
			} else
				continue;
		}
	}

	public BigDecimal calculate(List<ItemInfoDto> itemList,Integer currWeight) {
		BigDecimal result = BigDecimal.ZERO;
		n = itemList.size();
		stardard = new int[n];
		ans = new int[n];
		approximateArray = new int[n];
		for (int cc = 0; cc < n; ++cc){
			stardard[cc] = itemList.get(cc).getUnitWeight();
		}
		y = Math.abs(currWeight);
		max = y + stardard[0];
		retrack(0);
		log.info("pause:商品质量" + realApproximage);
		for (int j = 0;j<n;j++) {
			log.info((j+1)+"号商品数量="+approximateArray[j]);
			result = result.add(itemList.get(j).getUnitPrice().multiply(new BigDecimal(approximateArray[j])));
		}
		return result.setScale(2, BigDecimal.ROUND_DOWN);
	}

	public List<BigDecimal> calculate(List<ItemInfoDto> itemList,Integer currWeight,List<PayRecordItemBean> payRecordList,MachineInfo machinesInfo) {
		List<BigDecimal> bList=Lists.newArrayList();
		BigDecimal result = BigDecimal.ZERO;
		n = itemList.size();
		stardard = new int[n];
		ans = new int[n];
		approximateArray = new int[n];
		for (int cc = 0; cc < n; ++cc){
			stardard[cc] = itemList.get(cc).getUnitWeight();
		}
		y = Math.abs(currWeight);
		max = y + stardard[0];
		retrack(0);
		log.info("pause:商品质量" + realApproximage);
		int sum = 0 ;
		int payRecordSum=0;
		for (int j = 0;j<n;j++) {
			sum=sum+approximateArray[j];
		}
		if(payRecordList!=null && payRecordList.size()>0) {
			for (int j = 0;j<payRecordList.size();j++) {
				payRecordSum=payRecordSum+payRecordList.get(j).getNum();
			}
		}
		log.info("原数量-新数量:"+payRecordSum+"-"+sum);
		if(payRecordSum != sum) {
			bList.add(new BigDecimal(0));
		}else {
			bList.add(new BigDecimal(1));
		}
		
		for (int j = 0;j<n;j++) {
			if(payRecordList!=null && payRecordList.size()>0) {
				approximateArray[j]=approximateArray[j]-payRecordList.get(j).getNum();
			}
			log.info((j+1)+"号商品数量="+approximateArray[j]);
			result = result.add(itemList.get(j).getUnitPrice().multiply(new BigDecimal(approximateArray[j])));
		}
		bList.add(result.setScale(2, BigDecimal.ROUND_DOWN));
		return bList;
	}

	public static void main(String[] args) {
		BalanceUtil cas = new BalanceUtil();
		List<ItemInfoDto> itemList = new ArrayList<ItemInfoDto>();
		ItemInfoDto item1 = new ItemInfoDto();
		item1.setUnitPrice(new BigDecimal("8.9"));
		item1.setUnitWeight(5180);
		itemList.add(item1);
		
		ItemInfoDto item2 = new ItemInfoDto();
		item2.setUnitPrice(new BigDecimal("10.9"));
		item2.setUnitWeight(4866);
		itemList.add(item2);
//		
//		ItemInfoDto item3 = new ItemInfoDto();
//		item3.setUnitPrice(new BigDecimal("15.9"));
//		item3.setUnitWeight(7633);
//		itemList.add(item3);
		Integer currWeight = 5180;
		System.out.println(cas.calculate(itemList,currWeight));
		for(int a :cas.approximateArray){
			System.out.println(a);
		}
		
	}
	

}
