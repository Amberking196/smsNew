package com.server.module.zfb_trade.controller;

import lombok.Data;


@Data
public class FaceInitResult {
    String retCode;
    String retMessage;
    FaceInitResultDetail result;
}
