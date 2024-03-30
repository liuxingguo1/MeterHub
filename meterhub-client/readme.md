
####

添加TCP连接
```json
{
  "name": "保信子站",
  "code": "BXZZ",
  "host": "192.168.5.53",
  "port": 2404,
  "commProtocol": "TCP",
  "dataProtocol": "IEC104"
}
```

添加KAFKA连接
```json
{
  "name": "综自",
  "code": "ZZ",
  "host": "192.168.3.100",
  "port": 9092,
  "commProtocol": "KAFKA",
  "dataProtocol": "JSON",
  "topic": "duhv_signal_metric",
  "group": "meterhubclient"
}
```