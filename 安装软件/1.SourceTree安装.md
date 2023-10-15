# SourceTree安装

1. 双击SourceTree安装包
2. 看到如下的页面后直接关闭，别安装

![image-20230902213354684](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230902213354684.png)

3. 创建accounts.json文件

```json
[
  {
    "$id": "1",
    "$type": "SourceTree.Api.Host.Identity.Model.IdentityAccount, SourceTree.Api.Host.Identity",
    "IsDefault": false,
    "Authenticate": true,
    "HostInstance": {
      "$id": "2",
      "$type": "SourceTree.Host.Atlassianaccount.AtlassianAccountInstance, SourceTree.Host.AtlassianAccount",
      "Host": {
        "$id": "3",
        "$type": "SourceTree.Host.Atlassianaccount.AtlassianAccountHost, SourceTree.Host.AtlassianAccount",
        "Id": "atlassian account"
      },
      "BaseUrl": "https://id.atlassian.com/"
    },
    "Credentials": {
      "$id": "4",
      "$type": "SourceTree.Api.Account.Basic.BasicAuthCredentials, SourceTree.Api.Account.Basic",
      "Username": "",
      "Email": null,
      "AvatarURL": null,
      "AuthenticationScheme": {
        "$type": "SourceTree.Api.Account.Basic.BasicAuthAuthenticationScheme, SourceTree.Api.Account.Basic",
        "Value": "用户名/密码",
        "Name": "Basic",
        "Description": "密码",
        "HeaderValuePrefix": "Basic",
        "UsernameIsRequired": true
      },
      "Id": "",
      "EmailHash": null,
      "DisplayName": null
    }
  },
  {
    "$id": "5",
    "$type": "SourceTree.Model.ScmAccount, SourceTree.Api.Host.Scm",
    "IsDefault": false,
    "Authenticate": true,
    "HostInstance": {
      "$id": "6",
      "$type": "SourceTree.Host.GitHub.GitHubInstance, SourceTree.Host.GitHub",
      "Host": {
        "$id": "7",
        "$type": "SourceTree.Host.GitHub.GitHubHost, SourceTree.Host.GitHub",
        "Id": "github"
      },
      "BaseUrl": "https://github.com/",
      "Protocol": "HTTPS"
    },
    "Credentials": {
      "$id": "8",
      "$type": "SourceTree.Api.Account.OAuth.TwoZero.OAuthTwoZeroCredentials, SourceTree.Api.Account.OAuth.TwoZero",
      "AuthenticationScheme": {
        "$type": "SourceTree.Api.Account.OAuth.TwoZero.OAuthTwoZeroBearerAuthenticationScheme, SourceTree.Api.Account.OAuth.TwoZero",
        "Name": "OAuth",
        "Description": "OAuth 令牌",
        "HeaderValuePrefix": "Bearer",
        "UsernameIsRequired": false
      },
      "Id": "e9da1255-9bf0-4ed6-807b-c01f28ec8e31",
      "Username": "fuyongCSDN",
      "DisplayName": null,
      "Email": null,
      "AvatarURL": "https://avatars2.githubusercontent.com/u/44149226?v=4",
      "EmailHash": null
    }
  }
]
```

4. 将此json文件放至sourcetree目录

   一般是在%LocalAppData%\Atlassian\SourceTree\ 

   我的是在C:\Users\jd\AppData\Local\Atlassian\SourceTree

![image-20230902215356884](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230902215356884.png)



5. 打开SourceTree软件

如果有Git的话，选择本地的Git即可

![image-20230902220042708](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230902220042708.png)

其他的缺少什么环境安装即可

为了方便可以添加一个SSH

![image-20230902220305925](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230902220305925.png)