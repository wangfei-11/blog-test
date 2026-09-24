# blog-test —— 个人博客系统（blog）测试作品

基于 SpringBoot 3 + MyBatis Plus + MySQL + JWT 的个人博客系统全流程测试（功能 / 接口 / UI 自动化 / 性能）。

## 测试成果

| 测试类型 | 数量 | 结果 |
|---|---|---|
| 功能测试用例（手工） | 82 条 | 全部通过 |
| 接口测试用例（Postman） | 81 条 | 全部通过 |
| UI 自动化脚本（Java + Selenium） | 25 条 | 全部通过 |
| 性能测试（JMeter） | 3 接口，100 并发 | 无性能瓶颈 |
| 缺陷 | 10 个（严重 3 个） | 全部修复并回归通过 |

## 目录结构

```
blog-test/
├── docs/                      # 测试用例与总结报告
│   ├── 测试用例.xlsx           # 功能82 + 接口81 + 缺陷10 + UI25 四个 sheet
│   └── 测试总结报告.md         # 完整测试总结报告
├── ui_test/                   # UI 自动化工程（Selenium 4 + JUnit 4，JDK 17）
│   ├── pom.xml
│   └── src/test/java/com/blog/
│       ├── common/            # BaseTest、UiHelper（BASE_URL=8081）
│       ├── smoke/             # SmokeTest 冒烟 6 条
│       ├── login/             # LoginTest 登录 7 条
│       ├── list/              # ListTest 列表 3 条
│       ├── detail/            # DetailTest 详情 3 条（含权限）
│       ├── add/               # AddTest 新增 3 条
│       └── update/            # UpdateTest 更新 3 条（含越权）
└── jmeter/
    └── blog-压测.jmx           # 登录/列表/详情三接口压测脚本（100 并发）
```

## 报告入口

- 📄 [测试总结报告](docs/测试总结报告.md)
- 📊 测试用例明细：`docs/测试用例.xlsx`

## 被测系统

个人博客系统（SpringBoot 3.2.9 + MyBatis Plus + MySQL + JWT Token 鉴权，端口 8081），提供用户登录、博客增删改查、作者信息查询共 8 个接口。
