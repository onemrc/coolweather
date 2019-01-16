# coolweather
《第一行代码》天气项目 代码

## 设计思路

APP数据来源自网上提供的API，
用户打开APP选择想要查询的城市天气，当选择的省份是用户首次选择的，则把从API查到的省份下一级城市信息存近数据库，当用户之后再次选择该省份时，直接从本地数据库查询。
同理对城市下一级的区级也是如此。
当用户选择区级时就能打开一个包含该地区的天气及其他信息的界面。

## 相关技术

数据存储：android内置的SQLite数据库

网络请求工具类：OkHttp

解析JSON格式数据工具类：JSONObject


## 运行效果图：

### 天气显示界面

![image](https://github.com/onemrc/coolweather/blob/master/image/%E5%A4%A9%E6%B0%94%E7%95%8C%E9%9D%A21.png)

![image](https://github.com/onemrc/coolweather/blob/master/image/%E5%A4%A9%E6%B0%94%E7%95%8C%E9%9D%A22.png)

### 选择地区界面

![image](https://github.com/onemrc/coolweather/blob/master/image/%E6%9F%A5%E8%AF%A2%E7%9C%81%E4%BB%BD.png)

![image](https://github.com/onemrc/coolweather/blob/master/image/%E6%9F%A5%E8%AF%A2%E5%9F%8E%E5%B8%82.png)

![image](https://github.com/onemrc/coolweather/blob/master/image/%E6%9F%A5%E8%AF%A2%E5%9C%B0%E5%8C%BA.png)
