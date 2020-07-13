# uscinema

UsCinema是一个基于院线电影进行评分、评论的网站，个人独立完成网站设计、后端开发、前端开发、数据采集爬虫开发及项目上线工作。后端使用Spring Boot开发，实现了电影、用户、评论、认证、搜索五个业务模块的30余个Restful API接口，并构建了一套简单的权限管理系统、异常统一返回接口、FTP文件上传接口等；项目使用MySQL进行数据持久化存储，并进行了索引优化和慢查询优化；使用Redis进行热点数据缓存，并实现了缓存命中率统计；使用RabbitMQ实现了评分的异步更新；使用Vue和BootStrap完成前端开发；使用Scrapy构建爬虫爬取了豆瓣四十余万条数据作为网站的测试数据；使用Nginx实现反向代理及静态资源代理。

# 项目地址

# 网站访问：http://47.105.137.128/
# 后端开源：https://github.com/ushill/uscinema
# 前端开源：https://github.com/ushill/uscinema-vue
