#  hutool工具类实现SM2与SM4

通过调用`getEncoded()`方法，可以获取私钥的字节数组表示形式，它是使用特定编码规则（如DER编码）将私钥转换为字节数组。这个字节数组可以被保存、传输或序列化，以便在需要时重新还原为私钥对象。

获取私钥的字节数组表示形式对于存储私钥或将其传输到其他系统或组件中都很有用。例如，你可以将私钥字节数组保存在文件中，以便在以后的时间点重新加载并还原私钥对象。或者，你可以将私钥字节数组通过网络传输给其他系统，让其在远程位置上进行加密、解密或签名等操作。

注意，私钥字节数组的具体格式和编码取决于所使用的非对称加密算法和相关的编码标准。不同的算法和标准可能采用不同的表示形式和编码方案。

以下是一个示例，展示了如何获取私钥的字节数组并将其保存到文件



# 一、SM2加密与解密

非对称加密SM2

```java
public class MD2Test {

    public static void main(String[] args) {
//      生成用于非对称加密的公钥和私钥，仅用于非对称加密
//      将公钥和私钥相关联，以便在加密、解密、签名和验证等操作中使用
//      参数"SM2"表示使用SM2（椭圆曲线密码学）算法生成密钥对
        KeyPair pair = SecureUtil.generateKeyPair("SM2");

        System.out.println("私钥对象 ； "+ pair.getPrivate());
        System.out.println("公钥对象 ； "+ pair.getPublic());

//      密钥对通常用于非对称加密算法
//      公钥用于加密数据
//      调用getEncoded()方法，可以获取私钥的字节数组表示形式，使用特定编码规则将私钥转换为字节数组
        byte[] privateKey = pair.getPrivate().getEncoded();
//      而私钥用于解密数据或签名
        byte[] publicKey = pair.getPublic().getEncoded();


//      私钥 - encodeToString将字节数组转换为Base64编码格式的字符串
        String privateKeyStr= Base64.getEncoder().encodeToString(privateKey);
        System.out.println("私钥："+privateKeyStr);

//      公钥
        String publicKeyStr=Base64.getEncoder().encodeToString(publicKey);
        System.out.println("公钥："+publicKeyStr);


//      加密
        // 公钥加密，私钥解密
        SM2 sm2 = SmUtil.sm2(null, publicKey);
        String content = "我是Hanley.";
        String encryptStr = sm2.encryptBcd(content, KeyType.PublicKey);
        System.out.println(encryptStr);


//      公钥加密，私钥解密
        SM2 sm22 = SmUtil.sm2(privateKeyStr, null);
        String decryptStr = StrUtil.utf8Str(sm22.decryptFromBcd(encryptStr, KeyType.PrivateKey));
        System.out.println("解密后数据 "+decryptStr);

    }
}
```



![image-20230830134120207](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230830134120207.png)





# 二、SM4加密与加密

对称加密 sm4













