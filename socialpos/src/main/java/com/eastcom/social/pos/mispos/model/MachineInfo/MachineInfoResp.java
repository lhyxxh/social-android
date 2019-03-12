package com.eastcom.social.pos.mispos.model.MachineInfo;

public class MachineInfoResp {
    String code;
    String errMsg;
    String cRetResv;   //POSSN、PSAM、精度、纬度；XX 字节，左对齐，不足部分补空格
                        //cRetResv 中变量为 possn|psam|经度|纬度|


}
