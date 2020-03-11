package com.server.module.trade.web.bean;


        import lombok.Data;

        @Data
        public class HuaFaGoods {

        //商品id
        private Integer itemId;
        //商品名称
        private String itemName;
        //商品价格
        private Double price;
        //商品数量
        private Integer num;
        //商品图片
        private String pic;
        private Long orderId;

        }
