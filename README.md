# zcy-hadoop
测试hadoop

一：本地测试链接远程服务器的hadoop服务器，需要增加本地配置文件config.xml
该配置文件和远程服务器的core-site.xml一样
二：同时需要本地拷贝NativiIO，修改564行，直接返回true
如果将该项目放置在远程linux服务器的hadoop里面则不需要上面两步
