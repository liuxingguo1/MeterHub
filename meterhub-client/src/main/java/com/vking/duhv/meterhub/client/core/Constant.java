package com.vking.duhv.meterhub.client.core;

public class Constant {

    public static final String METER_JSON_TEMP = """
            {
                "from":"{}",
                "protocol":"{}",
                "data":"{}"
            }
            """;

    public static final String COMM_PROTOCOL_KAFKA = "KAFKA";

    public static final String COMM_PROTOCOL_TCP = "TCP";

    public static final String DATA_PROTOCOL_JSON = "JSON";

    public static final String DATA_PROTOCOL_IEC000 = "IEC000";

    public static final String DATA_PROTOCOL_IEC103 = "IEC103";

    public static final String DATA_PROTOCOL_IEC104 = "IEC104";



}
