[TOC]



# IO + File

[全面解析Java-IO流](https://www.bilibili.com/video/BV1n3411Q7gi/?spm_id_from=333.337.search-card.all.click&vd_source=c01240addcba226237f3c4781490fbae)



# 一、 File

**File： 表示系统中的文件或者文件夹的路径。**

利用File我们可以获取文件信息（大小，文件名，修改时间）、判断文件的类型、创建文件/文件夹、删除文件/文件夹等

File类只能对文件本身进行操作，不能读写文件里面存储的数据。







# 二、IO流  



## 2.0 IO流介绍



**存储和读取数据的解决方案**

  用于读写文件中的数据(可以读写文件，或网络中的数据...)

​     写 - output

​     读 - input

 这个读和写的参照物是以“程序”为参照物，或者说以“内存”



**作用：**

​    用于读写数据(本地文件，网络)



**IO流按照流向可以分类哪两种流？**

​      输出流： 程序  - > 文件

​      输入流：  文件 -》 程序



**IO流按照操作文件的类型可以分类哪两种流？**

​        字节流：可以操作所有类型的文件

​        字符流： 只能操作纯文本文件

![image-20230507135002497](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507135002497.png)

**纯文本文件**： Windows自带的记事本打开能正常显示（不是乱码）



![image-20230507141046845](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507141046845.png)







## 2.1 字节流



### 2.1.1 字节输出流  -  FileOutputStream  



#### 2.1.1.1  write方法

| 方法名称                                   | 说明                         |
| ------------------------------------------ | ---------------------------- |
| void write(int b)                          | 一次写一个字节数据           |
| void write(byte[ ] b)                      | 一次写一个字节数组数据       |
| void write(byte[ ] b , int off , int len ) | 一次写一个字节数组的部分数据 |



#### 2.1.1.2 字节输出流细节



**① 创建字节输出流对象**

- **创建FileOutputStream对象时，参数可以使字符串表示的路径，也可以是File类型文件。**

​          如果我们传入的参数是字符串表示的路径，底层也会帮我们new一个File类型的文件。



-    **如果文件不存在会创建一个新的文件，但是要保证父级路径是存在的**



- ​    **如果文件已经存在，则会清空文件内容，若不想清空，则在创建对象时添加“true”参数**



**② 写数据**

- **write方法的参数是整数，但是实际写到本地文件中的是整数在ASCII上对应 的字符**

     如 91  -> a , 100 ->d

  



**③ 释放资源**

关闭流，释放资源。



**如果我们不关闭流**，我们删除文件的时候会有一个提示，“操作无法完成，因为文件在..中打开，请重新尝试.....”

![image-20230507143619916](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507143619916.png)









#### 2.1.1.3代码实现



```java
public class FileOutputStreamTest {
    public static void main(String[] args) {
        FileOutputStream fos = null;
        try {
            /*为什么会加true？ 在原文件的基础上追加内容，不加true的话，会将原文件清空然后再追加内容*/
            /*在此路径下，如果文件不存在，会自己创建一个*/
             fos = new FileOutputStream("E:\\IDEA\\Java\\StudentTwo\\src\\IOTest\\JavaTest.txt",true);
             byte[] bytes ={97,98,99,10};
             /*fos.write(bytes); 将bytes数组整组的写入*/
            /*下面这段代码是将数组中的部分内容写入*/
            fos.write(bytes,0,3);
 
            String s = "我是中国人！";
            /*将字符串转换成byte数组的形式，然后写入*/
            bytes = s.getBytes();
            fos.write(bytes);
            /*写完之后，一定记得刷新！*/
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```





#### 2.1.1.4  换行与续写



- **换行写：**

**写出一个换行符即可**

​          **Windows： \r\n  **  意思就是先回车再换行，这是之前系统的问题，然后Windows就延续下来了（之前回车是将光标放到这一行的前面，换行是将光标移动到下一行）

​          Linux： \n 

​          Mac:  \r

​       在Windows操作系统当中，java对回车换行进行了优化。虽然完整的是\r\n，但是我们写其中一个\r或者\n，java也可以实现换行，因为java在底层会补全，当然也建议不要省略，尽量写全。

 

```java
    String wrap = "\r\n";
    byte[] bytes = wrap.getBytes();
    fos.write(bytes);
```





- **续写**

在创建对象时将续写开关打开

```java
fos = new FileOutputStream("文件路径",true);
```



### 2.1.2 字节输入流  -  FileInputStream 



#### 2.1.2.1 read()方法



|            方法名称            |          说明          |
| :----------------------------: | :--------------------: |
|       public int read()        |  一次读取一个字节数据  |
| public int read(byte[] buffer) | 一次读一个字节数组数据 |



#### 2.1.2.2 字节输入流细节



  **① 创建字节输入流对象**

​    文件不存在，直接报错



  **② 读数据**

- **fis.read() 方法一次读一个字节，读出来的数据是ASCII上对应的数字**

  

- **读到文件末尾了，read方法返回-1**

  ​      比如我们文件中的数据是“abcde”，刚开始fis.read()默认指向第一个数据"a",调用方法后将"a"读取，并且指针移动到"b",再调用fis.read()方法后读取"b"，并且指针移动到"c",........最终fis.read()方法执行“e”之后，什么也没有了，此时数据为“-1”，表示文件末尾，没有数据了

​             但是每次读取一个字节，速度会非常的慢！！！



-  **一次读一个字节数组的数据，每次读取会尽可能把数组填满**

​           public int read(byte[] buffer)此方法即可。

​           虽然数组长度越大读取速度越快，但是不要忘了数组会占用内存空间。

​            我们在创建byte数组的时候一般会创建1024的整数倍，比如1024×1024×5，即每次读取大小为5M的数据。

​            **1024个字节就是1Kb，1024个1Kb就是1M，再×5，就是5M**



- **”把数组填满“，这句话挺重要** 

  假如文件中有数据"abcde"五个字符，我们byte数组的长度是2，则每次读取两个字符

  **第一次**读取的时候，len=2，读取到"ab"，并且byte数组中存储的是“ab”对应的字节，此时read指针指向"c"

  **第二次**读取的时候，len=2，读取到"cd",并且byte数组中存储的是"cd"对应的字节，将之前存储的“ab”所对应的字节给覆盖掉，此时read指针执行“e”

  **第三次**读取的时候，只剩下“e”这么一个字符了，len=1，读取到"e",并存储到数组当中，将上一次读取“cd”中的“c"给覆盖掉，即现在获取的是"ed"

  **第四次**读取的时候，文件中已经没有内容了，就读取到"-1",代表文件读取完成

![image-20230507170553840](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507170553840.png)



所以我们在读取的时候应该按照下面

![image-20230507171954432](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507171954432.png)



**③  释放资源**

   同上



 #### 2.1.2.3 代码实现



操作本地文件的字节输入流，可以把本地文件中的数据读取到程序中来

```java
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
 
/*FileInputStreamTest 文件字节输入流 任何文件都可以采用这个流读取*/
/*字节输入流,是从硬盘向内存中读入*/
public class FileInputStreamTest {
    public static void main(String[] args) {
        /*为什么在这个地方声明？ 为了方便将流关闭*/
        FileInputStream fis =null;
        try {
            fis =  new FileInputStream("E:\\IDEA\\Java\\StudentTwo\\src\\IOTest\\JavaTest.txt");
            /*用字节数组，减少了内存和硬盘的交互，提高了运行效率
               一次最多读取bytes.length个字节*/
            byte[] bytes = new byte[4];
            int readCount=0;
            /*readCount是-1时，说明已经读完了*/
            /* fis.read(bytes)的返回值是读取到的字节的数量，而且返回的是字节的ascll码*/
            while(  (readCount= fis.read(bytes)) !=-1){
                /*读到多少个，就转换输出多少个，不会出现重复读取的问题*/
                System.out.println( new String(bytes,0,readCount));
                /*String(byte[] bytes, int offset, int length)
                 构建了一种新的 String通过解码指定的字节数组使用平台的默认字符集。*/
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis !=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```





## 2.2  字节流 - 拷贝文件



**注意：先开的流，最后再关闭**



```java
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
 
/*利用字节输入输出流完成文件的拷贝*/
public class CopyTest01 {
    public static void main(String[] args) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
 
        try {
            fis = new FileInputStream("E:\\IDEA\\Java\\StudentTwo\\src\\IOTest\\JavaTest.txt");
            /*这个地方是将文件复制到的目的地*/
            fos = new FileOutputStream("D:\\JavaTest.txt");
            /*1024是1kb  1024*1024是1M   这里的意思是，一次性读取1M的内容*/
             byte[] bytes = new byte[1024*1024];
            
             int readCount=0;
            
             while ( (readCount = fis.read(bytes)) !=-1){
                 /*读多少，写多少 */
                 fos.write(bytes,0,readCount);
             }
            fos.flush();//*因为可能有一些数据还在管道里面33*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```







## 2.3 字符流

**字符流底层其实就是字节流**

字符流 = 字节流 + 字符集



**对于纯文本文件进行读写操作**



**特点**

​    **输入流Reader：** 一次读一个字节，遇到中文时，一次读多个字节（具体读取多个和字符集有关）

​    **输出流Writer：** 底层会把数据按照指定的编码方式进行编码，编程字节再写到文件中



### 2.3.1 字符输入流 - FileReader	

#### 2.3.1.1 细节

**① 创建字符输入流对象**

|              构造方法              |            说明            |
| :--------------------------------: | :------------------------: |
|    public FileReader(File file)    | 创建字符输入流关联本地文件 |
| public FileReader(String pathname) | 创建字符输入流关联本地文件 |

   如果文件不存在便报错



**②读取数据**

|            成员方法            |             说明             |
| :----------------------------: | :--------------------------: |
|       public int read()        |   读取数据，读到末尾返回-1   |
| public int read(char[] buffer) | 读取多个数据，读到末尾返回-1 |

按字节进行读取，遇到中文，一次读多个字节，读取后解码，返回一个整数

读到文件末尾了，read方法返回-1



**read()方法**

在读取之后，方法的底层还会进行解码并转成十进制，最终把这个十进制的数据作为返回值，这个十进制的数据也表示在字符集上的数字。

 比如文件中一个英文字符的二进制数据“0110 0001”，read方法进行读取，解码并转成十进制97。

比如文件中一个中文字读的二进制数据"11100110 10110001 10001001",read方法进行读取，解码转成十进制“27721”。

**所以我们不能将char，我们应该new String进行转化**



 **read(char[] buffer)方法**

```java
char[] chars = new char[4];
 
fr.read(chars);
 
for(char c: chars){
 System.out.println(c);
 
}
```

![img](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/ce5e791679044e4e8a3c72f4a40fdffb.png)





然后我们发现如果read方法是无参的话，读出来的数据如果不强转就是int类型数字，强转为char后才是对应的字符，但是read方法是有参，却能把数据完完全全输出，并不需要强转。



**说明：**

   **空参read方法： 一次读取一个字节，遇到中文一次读多个字节，把字节码并转成十进制返回**

   **有参read方法： 把读取字节，解码，强转三步合并了，强转之后的字符放到数组中**



**③ 释放资源**



#### 2.3.1.2 代码实现

```java
/*文件字符输入流  只能读取普通文本
*  读取文本内容的时候，比较方便和便捷*/
public class FileReaderTest {
    public static void main(String[] args) {
        FileReader fr = null;
        try {
            /*读入的文件的目录，这个文件只能是普通的文本文件*/
            fr = new FileReader("E:\\IDEA\\Java\\StudentTwo\\src\\IOTest\\JavaTest.txt");
            /*一次读入4个字符*/
            char[] chars = new char[4];
            
            int readCount =0;
 
            while(  (readCount = fr.read(chars)) !=-1){
                System.out.println( new String(chars,0,readCount));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fr!=null){
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```





#### 2.3.1.3 底层原理

在我们创建对象从数据源(UTF-8)中读取内容到内存时，**内存中会有一个长度为8192的字节数组作为缓冲区**

**read()方法先从缓冲区中读取，如果缓冲区中没有就从文件中读取内容并尽可能的填满缓冲区，这样效率更高，减少了频繁从硬盘中读取数据的过程。**下面的例子“a我”是四个字节，所以缓冲区中此时只有四个字节。

但是**第一次**以read()方法是读取的第一个字节。

**第二次**read()方法依然是遵照这样，先从缓冲区读取，发现缓冲区中存在数据，便直接从缓冲区中读取。注意，第二次读取时中文，一次性读取三个字节，并按照中文的形式进行解码。

**第三次**read()方法从缓存区中读取，发现缓存区已经读完了，再读取文件，发现文件中的内容也读完了，便返回-1



**说明：**

   **空参read方法： 一次读取一个字节，遇到中文一次读多个字节，把字节码并转成十进制返回**

   **有参read方法： 把读取字节，解码，强转三步合并了，强转之后的字符放到数组中**



![image-20230508143531389](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230508143531389.png)



**另外说明： 字节流是没有缓冲区的！！！！！**





### 2.3.2 字符输出流 - FileWriter

#### 2.3.2.1 细节



**① 创建对象**

|                     构造方法                      |              说明               |
| :-----------------------------------------------: | :-----------------------------: |
|           public FileWriter(File file)            |   创建字符输出流关联本地文件    |
|        public FileWriter(String pathname)         |   创建字符输出流关联本地文件    |
|    public FileWriter(File file,boolean append)    | 创建字符输出流关联本地文件,续写 |
| public FileWriter(String pathname,boolean append) | 创建字符输出流关联本地文件,续写 |



**② write方法**

![image-20230507215418385](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507215418385.png)



如果write方法的参数是整数，但是实际上写到本地文件中的是整数在字符集上对应的字符



**③关闭流**



#### 2.3.2.2 代码实现

```java
import java.io.FileWriter;
import java.io.IOException;
/*利用字符输出流向文件中输入某些文本内容   只能输出普通的文本内容*/
public class FileWriterTest {
    public static void main(String[] args) {
        FileWriter fw =null;
        try {
            /*如果这个构造方法上加上true的话，就表示在原文件的基础上再追加内容*/
            fw = new FileWriter("E:\\IDEA\\Java\\StudentTwo\\src\\IOTest\\JavaTest.txt");
            char[] chars=  {'我','是','一','个','大','帅','哥'};
            fw.write(chars);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fw!=null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```







#### 2.3.2.3 底层原理

**在创建对象的时候，底层也创建了一个8192的字节数组，也就是缓冲区。**

也就是说我们调用write方法后并没有直接将内容写入到文件中，而是写入了字节数组(缓冲区)

**那什么时候才会写入到文件？**

  ①  缓冲区满了

  ②  手动调用flush方法，将缓冲区中的数据刷新到文件中

  ③    调用close()方法，在断开连接之前，会检查缓冲区中是否有数据，有的话就刷新到本地文件中





|      成员方法       |                说明                |
| :-----------------: | :--------------------------------: |
| public void flush() | 将缓冲区中的数据，刷新到本地文件中 |
| public void close() |           释放资源/关流            |

flush：刷新之后，还可以继续往文件中写出数据

close关流：断开通道，无法再往文件中写出数据







## 2.4 缓冲流

字符流已经有缓冲区了，字符缓冲流提高的效率不是很明显

但是字节流没有缓冲区，字节缓冲流提高的效率很明显

![image-20230508220206596](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230508220206596.png)





### 2.4.1  字节缓冲流

**字节缓冲流：**

​    **原理：** 底层自带了长度8192的缓冲器提高性能（与字符流一样）



**缓冲流是对基本流做的包装**

|                   方法名称                   |                   说明                   |
| :------------------------------------------: | :--------------------------------------: |
|  public BufferedInputStream(InputStream is)  | 把基本流包装成高级流，提高读取数据的性能 |
| public BufferedOutputStream(OutputStream os) | 把基本流包装成高级流，提高读取数据的性能 |



#### 2.4.1.1 拷贝文件

```java
public class BufferedStreamDemo1 {
    public static void main(String[] args) throws IOException {
//      TODO 创建缓冲流对象   BufferedInputStream 第二个参数可以指定缓冲区大小
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("D:/杂/test/test.txt"));
//      BufferedOutputStream 的第二个参数可以指定缓冲区大小
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:/杂/test/testDemo.txt"));

//      TODO 循环读取并写到目的地
//       一次读取一个字节        
//        int b=0;
//        while ( (b=bis.read()) !=-1){
//            bos.write(b);
//        }
//      TODO 一次读写多个字节
        byte[] bytes = new byte[1024 * 1024]; //1M
        int len ;
        while ((len = bis.read(bytes)) != -1) {
            bos.write(bytes,0,len);
        }

//      TODO 不需要关闭基本流，底层帮我们关了
        bos.close();
        bis.close();
    }
}
```





#### 2.4.1.2 读写原理

**读取：**基本流读取数据源中的数据，然后交给缓冲输入流，准确的来说是将数据放入到缓冲区（默认8192个字节，一次性读取8192个字节）

​         之后read() 方法 以及 read(byte[]) 方法都是从缓冲区读取，缓冲区没有之后再从文件中读取，直到-1为止



**写出：**由缓冲输出流(缓冲区默认大小也是8192)交给基本流，最终还是由基本流写入文件



**注意： 缓冲输入流和缓冲输出流都有一个缓存区，但是不是一个东西！！！！**

![image-20230508223302680](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230508223302680.png)



**速度是快在哪个地方？**

**减少了与硬盘的交互次数**

  下面蓝色的框框是在内存中读取的，倒手的时间非常快。节省的时间是读和写与硬盘交互的时间。如果将下面int b 换成字节数组，倒手的速度会更快

![image-20230508223442786](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230508223442786.png)



### 2.4.2 字符缓冲流



#### 2.4.2.0 字符缓冲流细节

底层自带长度8192的缓冲区提高性能，和字符流缓冲区是相同的。（**这个地方的字符缓冲区是8192大小的字符数组不是字节，我们之前缓冲区都是字节**）

|            方法名称             |        说明        |
| :-----------------------------: | :----------------: |
| public BufferedReader(Reader r) | 把基本流变成高级流 |
| public BufferedWriter(Writer r) | 把基本流变成高级流 |



但是字符缓冲流提供了两个方法，我们会经常使用

|  字符缓冲输入流特有方法  |                    说明                    |
| :----------------------: | :----------------------------------------: |
| public String readLine() | 读取一行数据，如果没有数据可读，会返回null |

readLine()方法在读取的时候一次读取一整行，遇到回车换行结束，但是他不会把回车换行读到内存当中



| 字符缓冲输出流特有方法 |     说明     |
| :--------------------: | :----------: |
| public void newLine()  | 跨平台的换行 |



**缓冲流有几种？**

    *  字节缓冲输入流 BufferedInputStream
    *  字节缓冲输出流 BufferedOutputStream
    *  字符缓冲输入流  BufferedReader
    *  字符缓冲输出流  BufferedWriter



**缓冲流为什么能提高性能？**

*  字符缓冲流底层会创建大小为8192的字符数组，一个字符在java中占用两个字节，那这样就是一个16K的一个缓冲区
*  显著提高字节流的读写性能
*  对于字符流提升不明显，对于服务缓冲流而言关键点是两个特有的方法





#### 2.4.2.1 字符缓冲输入流

```java
//      TODO 创建字符缓冲输入流对象
        BufferedReader br = new BufferedReader(new FileReader("D:/test.txt"));

//      TODO 读取数据
        String line ;
        while ( (line = br.readLine()) !=null){
            System.out.println(line);
        }
//      TODO 释放资源
        br.close();
```



#### 2.4.2.2 字符缓冲输出流

```java
//      TODO 创建对象
        BufferedWriter bw = new BufferedWriter(new FileWriter("D:/test.txt",true));

//      TODO 写出数据
        bw.newLine();
        bw.write("你长得很漂亮");
        bw.newLine();  // 跨平台换行
        bw.write("精神小伙！");

//      TODO 关闭流
        bw.flush();
        bw.close();
```





## 2.5 转换流

### 2.5.0 介绍

   转换流属于字符流，是字符流与字节流之间的桥梁。

​     

​    **InputStreamReader 读取数据**我们需要一个数据源，把数据源中的数据读取到内存当中，当我们创建转换流对象的时候，我们需要创建一个字节输入流，在我们包装之后，字节流就变成字符流了。

​      此时也具有了字符流的特性： 读取数据不会乱码了、根据字符集一次读取多个字节

 

  **OutputStreamWriter 写出数据**需要一个目的地，将字符流转换成字节流，与读取数据相反。在我们的目的地当中就是一个又一个的字节



![image-20230509113341262](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230509113341262.png)



### 2.5.1 案例 - 利用转换流按照指定字符编码读取

​    需求1 ：  手动创建GBK（ANSI）文件，把文件中的中文读取到内存中，不能出现乱码

​    需求2：   把一段中文按照GBK的方式写到本地文件

​    需求3：    将本地文件中的GBK文件，转成UTF-8



#### 2.5.1.1 按照指定编码读取数据



这个方案被淘汰了，了解一下

```java
//      TODO 创建对象并指定字符编码     第二个参数就是指定字符编码
        InputStreamReader isr = new InputStreamReader(new FileInputStream("D:/testANSI.txt"), "GBK");

//      TODO 读取数据  完全可以按照字符流的形式读取
        int ch;
        while( (ch = isr.read()) !=-1){
            System.out.print( (char) ch);
        }
        isr.close();
```



​      **JDK11**后有更好的方案， **FileReader的父类是InputStreamReader(转换流)，InputStreamReader的父类是Reader。**

​    **有了这层关系后新增加了一个构造方法：  public FileReader (File file,Charset charset)**,在这个构造方法里面后调用父类的构造方法创建对象 super(new FileInputStream(file) , charset),其实就是我们上面淘汰代码的写法

![image-20230509115353168](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230509115353168.png)



```java
      FileReader fr = new FileReader( fileName: "myiollgbkfile.txt",    Charset.forName("GBK"));//2.读取数据
     int ch;
     while ((ch = fr.read()) != -1)(System.out.print((char)ch);
//    3.释放资源
     fr.close();
```





#### 2.5.1.2  按照指定字符编码写出

这种方式同样是被淘汰了，与 2.5.1.1类似

```java
//      TODO
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("D:/testANSI.txt"),"GBK");
//      TODO 写出数据
        osw.write("八嘎呀路");
//      TODO 释放资源
        osw.close();
```



新的代码：

```java
      FileWriter fw  = new FileWriter("D:/testANSI.txt",Charset.forName("GBK"));
     
      fw.write("八嘎呀路");
      fw.close();
```





#### 2.5.1.3 将本地文件中的GBK文件转成UTF-8

 JDK 11 之前： 转换流

```java
    InputStreamReader isr = new InputStreamReader(new FileInputStream("D:/testANSI.txt"), "GBK");
    OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("D:/testANSI.txt"),"UTF-8");  //默认UTF-8，不指定也可以
    
     int b;
     while( (b=isr.read())!=-1){
         osw.write(b);
     }
     osw.close();
     isr.close();
     
       
```



JDK 11

```java
      FileReader fr = new FileReader( fileName: "myiollgbkfile.txt",    Charset.forName("GBK"));

      FileWriter fw  = new FileWriter("D:/testANSI.txt",Charset.forName("UTF-8")); //不指定也行，默认UTF-8

     int b;
     while( (b=fr.read())!=-1){
         fw.write(b);
     }
     osw.close();
     isr.close();
```





#### 2.5.1.4  读取文件中的数据，每次读取一整行不能出现乱码

①字节流在读取中文 的时候是会出现乱码的，但是字符流可以

②字节流里面是没有读取一整行的方法的，但是字符缓冲流可以







## 2.6 序列化流 与 反序列化流

### 2.6.0 介绍

属于字节流的一种

![image-20230509143100190](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230509143100190.png)

 

**小细节**

​    使用对象输出流将对象保存到文件时会出现NotSerializableException异常

​    解决方案： 需要让JavaBean类实现Serializable接口

   Serializable接口接口中没有抽象方法，标记型接口。表示此类可以被序列化



**序列化流：** 也叫做对象操作输出流，可以把java中的对象写到本地文件中

|                  构造方法                   |         说明         |
| :-----------------------------------------: | :------------------: |
| public ObjectOutputStream(OutputStream out) | 把基本流包装成高级流 |



|                 成员方法                  |             说明             |
| :---------------------------------------: | :--------------------------: |
| public final void writeObject(Object obj) | 把对象序列化（写出）到文件中 |





**反序列化流：**对象操作输入流，可以把序列化到本地文件中的对象读取到程序中

|                 构造方法                  |        说明        |
| :---------------------------------------: | :----------------: |
| public ObjectInputStream(InputStream out) | 把基本流变成高级流 |



|          成员方法          |                   说明                   |
| :------------------------: | :--------------------------------------: |
| public Object readObject() | 把序列化到本地文件中的对象，读取到程序中 |





**使用场景：**

​      游戏存档，将数据存起来，但是用户看不懂无法修改





### 2.6.1 序列化流

```java
//      TODO 创建对象
        Student student = new Student("张三",23);

//      TODO 创建序列化流对象/对象操作输出
        ObjectOutputStream  oops = new ObjectOutputStream(new FileOutputStream("D:/test.txt"));

//      TODO 写出数据
        oops.writeObject(student);

//      TODO 关闭流
        oops.close();
```



```java
@Data
public class Student implements Serializable {
    private String name;
    private int age;
    
    public Student(String name, int age) {
       this.name = name;
       this.age = age;
    }
}
```





### 2.6.2 反序列化流

```java
//      TODO  创建发序列化流对象
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("D:/test.txt"));

//      TODO 读取数据
        Object o = ois.readObject();

//       打印
        System.out.println(o);

        ois.close();
```





### 2.6.3 使用细节

​      当我们实现了Serializable接口后，会根据我们类中的变量、方法生成一个Long类型的序列号（版本号）



比如我们将代码运行后Student类序列号为1，写入本地文件

此后我们**又修改了Student类，然后利用反序列化流读取本地文件就会出错，这是为什么呢？**

​     因为我们反序列读取本地文件的时候序列号为1，但是我们Student类因为修改了文件序列号变成了2，两个版本号不一样，报错。即文件中的版本号与JavaBean中的版本号不匹配。

![image-20230509152858156](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230509152858156.png)



**解决方案：**

   将版本号固定，并且版本号的名称只能叫“serialVersionUID”

![image-20230509153101126](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230509153101126.png)



但是手动添加太麻烦，我们可以借助IDEA工具，如下所示。当我们勾选上之后，如果bean实现了序列化接口但是没写序列化号会给我们提示。



![image-20230509154147090](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230509154147090.png)







**某个字段不想序列化到本地文件怎么办？**

​    transient: 瞬态关键字

```java
  private  transient  String  address;
```



**总结：**

   ①  使用序列化流将对象写到文件时，需要让JavaBean类实现Serivlizable接口，否则会出现NotSerivlizableException



  ②   序列化流写到文件中的数据是不能修改的，一旦修改就无法再读回来



  ③    序列化对象后，修改了Javabean类，再次反序列化，会不会有问题？

​             会有问题，会抛出InvalidClassException异常。

​             解决方案： 给JavaBean类添加serialVersionUID（序列号、版本号）



 ④    如果一个对象中的某个成员变量的值不想被序列化，可以使用transient关键字

  





## 2.7 打印流

   **打印流不能读，只能写**

   PrintStream、PrintWriter两个类

**特点**

*  **打印流只操作目的地，不操作数据源**



*  **特有的写出方法**可以实现，数据原样写出

​          打印“97” ， 文件中“97”

​          打印“true”  文件中“true”



* **特有的写出方法**，可以实现自动刷新，自动换行

​         打印一次数据 = 写出 + 换行 + 刷新

 

![image-20230509165959266](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230509165959266.png)



### 2.7.1 字节打印流 - PrintStream

|                           构造方法                           |                      说明                      |
| :----------------------------------------------------------: | :--------------------------------------------: |
|         public PrintStream(OutputStream/File/String)         |          关联字节输出流/文件/文件路径          |
|     public PrintStream(String fileName,Charset charset)      |                  指定字符编码                  |
|   public PrintStream(OutputStream out , boolean autoFlush)   | 自动刷新（字节流底层没有缓冲区，开不开一个样） |
| public PrintStream(OutputStream out , boolean autoFlush ,String encoding) |             指定字符编码且自动刷新             |





|                     成员方法                      |                    说明                     |
| :-----------------------------------------------: | :-----------------------------------------: |
|             public void write(int b)              |  常规方法： 规则和之前相同，将指定字节写出  |
|            public void println(Xxx xx)            | 特有方法： 打印任意数据，自动刷新，自动换行 |
|             public void print(Xxx xx)             |       特有方法： 打印任意数据，不换行       |
| public void printf (String format,Object... args) |   特有方法： 带有占位符的打印语句，不换行   |





```java
//      TODO 打印流
//         第一个参数  关联字节输出流   第二个参数 是否自动刷新   第三个参数 指定编码 或者也能用Charset.forName("UTF-8")
        PrintStream ps =new PrintStream(new FileOutputStream("D:/test.txt"),true,"UTF-8");

//      TODO 写出数据1
        ps.println(97);  // 包括三个功能： 写出+自动刷新+自动换行

        ps.print(true);

        ps.printf("%s 爱上了 %s","阿珍","阿强");

//      TODO 释放资源
        ps.close();
```



**占位符：**

| 占位符 |      含义      |
| :----: | :------------: |
|   %n   |      换行      |
|   %s   |     字符串     |
|   %c   | 把字符换成大写 |
|   %b   |    布尔类型    |
|   %d   |  小数的占位符  |
|  ...   |      ...       |
|        |                |
|        |                |
|        |                |





### 2.7.2 字符打印流 - PrintWriter

**字符打印流底层有缓冲区，想要自动刷新需要开启**



|                           构造方法                           |             说明             |
| :----------------------------------------------------------: | :--------------------------: |
|            public PrintWriter(Write/File/String)             | 关联字节输出流/文件/文件路径 |
|     public PrintWriter(String fileName,Charset charset)      |         指定字符编码         |
|        public PrintWriter(Write w,boolean autoFlush)         |           自动刷新           |
| public PrintWriter(OutputStream out,boolean autoFlush,Charset charset) |    指定字符编码且自动刷新    |



|                     成员方法                      |                    说明                     |
| :-----------------------------------------------: | :-----------------------------------------: |
|              public void write(...)               |  常规方法： 规则和之前相同，将指定字节写出  |
|            public void println(Xxx xx)            | 特有方法： 打印任意数据，自动刷新，自动换行 |
|             public void print(Xxx xx)             |       特有方法： 打印任意数据，不换行       |
| public void printf (String format,Object... args) |   特有方法： 带有占位符的打印语句，不换行   |



```java
        PrintWriter pw = new PrintWriter(new FileWriter("D:/test.txt"),true);

        pw.println("今天我很帅");
        pw.print("您好");
        pw.printf("%s 爱上了 %s","阿珍","阿强");
//      开启自动刷新后，这个地方不刷新也行
//      TODO 释放资源
        pw.close();
```



### 2.7.3 输出 - System.out.println 

System是final修饰，是最终类，不能有子类

```java
public final class System
```



out是System的一个静态变量，而且是一个流

```java
public final static PrintStream out = null;
```



拆分出来是下面这个样子。

​    获取打印流对象，此打印流在虚拟机启动的时候，由虚拟机创建，默认只想控制台

​    **特殊的打印流，系统中的标准输出流，不能关闭，因为在系统中是唯一的**

```java
PrintStream ps = System.out;

ps.println(123); //123
```



调用打印流中的方法 println,写出数据，自动换行，自动刷新

```java
System.out.println(123);
```





## 2.8 解压缩流 与 压缩流

注意！在Java文件中只能识别“zip”结尾的压缩文件



### 2.8.1 解压缩流

**解压本质：** 压缩包中的把每一个文件在Java中是一个ZipEntry对象，按照层级拷贝到本地另一个文件夹中



```java
//      解压的本质：把压缩包里面的每一个文件或者文件夹读取出来，按照层级拷贝到目的地当中
//      TODO 创建一个解压缩流来读取压缩包中的数据
        ZipInputStream zip =new ZipInputStream(new FileInputStream(src));
//      TODO 获取到压缩包里面每一个zipEntry对象
        ZipEntry entry = zip.getNextEntry();
        System.out.println(entry);  //test2/java.txt

        ZipEntry entry2 = zip.getNextEntry();
        System.out.println(entry2);  // test2/test03/

        ZipEntry entry3 = zip.getNextEntry();
        System.out.println(entry3);  //  test2/test03/tttttt.txt

        ZipEntry entry4 = zip.getNextEntry();
        System.out.println(entry4);   // null

//      TODO 经过我们上面尝试，getNextEntry可以获取到压缩包中的文件、文件夹以及子文件中的文件与文件夹

//      但是我们不用上面那个写法,用这个
        ZipEntry zipEntry ;

        while( (zipEntry = zip.getNextEntry()) !=null){
            System.out.println(zipEntry);
        }


        zip.close();
```





下面可以正式编写代码：

```java
public class ZipStreamDemo2 {
    public static void main(String[] args) throws IOException {
//      TODO 创建一个File表示要解压的压缩包
        File src = new File("E:/test2.zip");

//      TODO 创建一个File表示解压的目的地
        File dest = new File("D:/");

//
        unZip(src, dest);
    }

    /**
     * @param src  要解压的压缩包
     * @param dest 解压的目的地
     */
    public static void unZip(File src, File dest) throws IOException {
//      解压的本质：把压缩包里面的每一个文件或者文件夹读取出来，按照层级拷贝到目的地当中
//      TODO 创建一个解压缩流来读取压缩包中的数据
        ZipInputStream zip = new ZipInputStream(new FileInputStream(src));
//      TODO 获取到压缩包里面每一个zipEntry对象
//            表示当前在压缩包中获取到的文件或者文件夹
        ZipEntry entry;

        while ((entry = zip.getNextEntry()) != null) {
            System.out.println(entry);

            if (entry.isDirectory()) {
                //文件夹： 需要在目的地dest处创建一个同样的文件夹
                // 第一个参数： 父级路径   第二个参数： 子级路径
                File file = new File(dest,entry.toString()); // 比如 D:/test2/java.txt
                file.mkdirs();
            } else {
                //文件： 读取压缩包中的文件，并且按照层级目录存放到目的地dest文件夹中

                int b; // 一个字节一个字节的读
                FileOutputStream fos = new FileOutputStream( new File(dest,entry.toString()));
                while ( (b= zip.read()) != -1){
                    fos.write(b);
                }
                fos.close();

//              TODO 表示在压缩包中的一个文件处理完毕
                zip.closeEntry();
            }
        }

        zip.close();
    }
}
```





### 2.8.2 压缩流

**压缩包里面的每一个文件或者文件夹都是ZipExtry对象**



**压缩本质：**把每一个(文件/文件夹)看成ZipEntry对象放到压缩包中



#### 2.8.2.1 压缩单个文件

```java
public class ZipStreamDemo3 {
    public static void main(String[] args) throws IOException {
//      TODO 创建File对象表示要压缩的文件
        File src = new File("E:/java.txt");

//      TODO 创建File对象表示压缩包的位置
        File dest = new File("D:/");

//      TODO 调用方法压缩
        toZip(src,dest);
    }

    /**
     * @param src  要压缩的文件
     * @param dest 要压缩的目的地
     */
    public static void toZip(File src,  File dest) throws IOException {
//      TODO 压缩流关联压缩包
//      new File(dest,"a.zip") ， 父级路径dest， 子级路径a.zip,相当于在磁盘中创建了一个D:/a.zip 压缩文件
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(dest,"a.zip")));

//      TODO 创建ZipEntry对象，表示压缩包里面的每一个文件和文件夹
        ZipEntry entry = new ZipEntry("a.txt");

//      TODO 把ZipEntry对象放到压缩包当中
        zos.putNextEntry(entry);

//      TODO 把src文件中的数据写到压缩包当中
        FileInputStream fis = new FileInputStream(src);

        int b;
        while ( (b=fis.read()) !=-1){
            zos.write(b);
        }

//      TODO 关闭
        zos.closeEntry();
        zos.close();
    }
}
```







#### 2.8.2.2 压缩文件夹

ZipEntry里面的参数表示在压缩文件中的路径

```java
public class ZipStreamDemo4 {
    public static void main(String[] args) throws IOException {
//      TODO 要压缩的文件夹
        File src = new File("E:/test2");

//      TODO 创建File对象表示压缩包的路径(压缩包的父级路径)
        File destParent = src.getParentFile(); // destParent = E:\

//       src.getName() =test2
//      TODO 创建File对象表示压缩包的路径
        File dest = new File(destParent, src.getName() + ".zip"); //dest = E:\test2.zip

//     TODO 创建压缩流关联压缩包
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dest));

//     TODO 获取src里面的每一个文件，变成ZipEntry对象，放入到压缩包当中
        toZip(src,zos,src.getName() );
//     TODO 释放资源
        zos.close();
    }

    /**
     * 作用：
     * 获取src里面的每一个文件，变成ZipEntry对象，放入到压缩包当中
     *
     * @param src  数据源
     * @param zos  压缩流，已经关联好了压缩文件
     * @param name 在压缩包内部的路径
     */
    public static void toZip(File src, ZipOutputStream zos, String name) throws IOException {
//      TODO 进入src
        File[] listFiles = src.listFiles();

//      TODO 遍历数组
        for (File file : listFiles) {
            if (file.isFile()) {
//             文件，编程ZipEntry对象，放入到压缩包当中
//               这个地方参数不能直接填入file，因为file是一个完整的路径（绝对路径），并不一定压缩包中的路径
                ZipEntry zipEntry = new ZipEntry(name+"/"+file.getName());

                zos.putNextEntry(zipEntry);

//              TODO 读取文件中的数据到压缩包
                FileInputStream fis = new FileInputStream(file);
                int b;
                while ( (b=fis.read()) !=-1){
                    zos.write(b);
                }

                fis.close();
                zos.closeEntry();

            }else {
//             文件夹，递归
                toZip(file,zos,name+"/"+file.getName());
            }
        }
        zos.close();
    }
}
```









# 三、 综合练习

**字节流**： **拷贝**任意类型的文件

**字符流**：  读取纯文本文件中的数据、往纯文本文件中写出数据



## 3.1  拷贝文件夹

**需要考虑子文件夹**

我们现在有两个文件夹test1（里面还存在其他文件夹）、test2，我们需要将test1文件夹中的内容拷贝到test2文件夹之下

```java
public class Test01 {
    public static void main(String[] args) throws IOException {
//      TODO  数据源 test1文件夹
        File src = new File("D:\\test1");
//      TODO 目标文件夹
        File dest = new File("D:/test2");
//      TODO 调用方法开始拷贝
        copyDir(src,dest);
    }

    /**
     * 拷贝文件夹
     * @param src  数据源
     * @param dest  目的地
     */
    private static void copyDir(File src, File dest) throws IOException {
//      文件夹有可能不存在，我们创建出来
        dest.mkdirs();  //如果已经存在那么久创建失败，但是不会报错

//      TODO 进入数据源
        File[] files = src.listFiles(); // 文件夹及文件以数组的方式返回

//      TODO 遍历数组（数组中文件夹及文件以数组的方式返回）
        for(File file: files){
            if(file.isFile()){
//              TODO 判断文件 - 拷贝
                FileInputStream fis = new FileInputStream(file);
//              TODO  文件开始，文件结束  new File(dest,file.getName()) 表示父级名用dest，文件名用file.getName()
                FileOutputStream fos = new FileOutputStream(new File(dest,file.getName()));

                byte[] bytes = new byte[1024];

                int len;
                while ( (len = fis.read(bytes)) !=-1){
                    fos.write(bytes,0,len);
                }

                fos.close();
                fis.close();

            }else {
//                TODO 判断文件夹 - 递归
                copyDir(file,new File(dest,file.getName()));
            }
        }
    }
}
```







## 3.2  文件加密/解密

为了保证文件安全性，需要对原始文件进行加密存储，再使用的时候再对其进行解密处理

**加密原理：** 对原始文件中的每一个字节数据进行修改，然后将改正以后的数据存储到新的文件中

**解密原理：**读取加密文件之后，按照加密的规则反向操作，变成原始文件。



>  ^： 异或
>
> ​       两边相同:   false
>
> ​       两边不同： true
>
> ```java
> System.out.println(true ^ true);   // false
> System.out.println(true ^ false);  // true
> System.out.println(100 ^ 10 );     // 110
> ```
>
> **为什么  “100 ^ 10”  结果是 “110”？**
>
>   “100”二进制 “1100100”， “10” 二进制 “1010”
>
>    1100100  ^ 1010   将两串数字右边对齐(没有数字的地方用0补齐)。0代表false，1代表true
>
> ​     1 1 0 0 1 0 0
>
> ​               1 0 1 0
>
> ————————
>
> ​     1 1 0 1 1 1 0      -->  十进制就是110
>
> **那“ 110 ^ 10” 的结果是多少？ **
>
> ​     答案是 “100”  ，将结果又还原回来了
>
> **我们就可以利用这个特性进行加密和解密**



### 3.2.1  加密

```java
//      TODO 创建对象关联原始文件
        FileInputStream fis = new FileInputStream("D:/杂/hello.jpg");
//      TODO 创建对象关联加密文件
        FileOutputStream fos = new FileOutputStream("D:/杂/helloTest.jpg");

//      TODO 加密处理
        int b ;
        while ((b = fis.read()) !=-1){
            fos.write(b^2);
        }

        fos.close();
        fis.close();
```



### 3.2.2 解密

```java
//      TODO 创建对象关联原始文件(加密之后的文件)
        FileInputStream fis = new FileInputStream("D:/杂/helloTest.jpg");
//      TODO 创建对象关联解密文件
        FileOutputStream fos = new FileOutputStream("D:/杂/helloTestJIeMi.jpg");

//      TODO 加密处理
        int b ;
        while ((b = fis.read()) !=-1){
            fos.write(b^2);
        }

        fos.close();
        fis.close();
    }
```





## 3.3 修改文件中数据

文本文件中有以下数据： 2-1-9-4-7-8

将文件中的数据进行排序，变成以下的数据： 1-2-4-7-8-9

如果文件数据有换行后就不能下面这样方式，因为数据后面会有"/r/n"

```java
public class Test03 {
    public static void main(String[] args) throws IOException {
//      TODO 读取数据
        FileReader fr = new FileReader("D:/杂/test/test.txt");
        StringBuilder stringBuilder = new StringBuilder();

        int ch;
        while ((ch = fr.read()) != -1) {
            stringBuilder.append((char) ch);
        }
        fr.close();
        System.out.println(stringBuilder); //2-1-9-4-7-8

//      TODO 排序
        String str = stringBuilder.toString();
        String[] split = str.split("-");

        ArrayList<Integer> list = new ArrayList<>();

        for (String s : split){
            int i = Integer.parseInt(s);
            list.add(i);
        }
        System.out.println(list);  //[2, 1, 9, 4, 7, 8]

        Collections.sort(list);

//      TODO 写出
        FileWriter fw = new FileWriter("D:/杂/test/test.txt");
        for(int i=0 ; i<list.size() ; i++){
            if( i == list.size() -1){
                fw.write(list.get(i).toString());  //一定要toString , 如果不写String类型，会写出整数型数字对应的字符
            }else {
//              不是最后一个，需要补充“-”
                fw.write(list.get(i).toString()+"-");
            }
        }
        fw.flush();
        fw.close();
    }
}
```







## 3.4 读写多个对象

​     将多个自定义对象序列化到文件中，但是由于对象个数不确定，反序列化流如何读取？



### 3.4.1 序列化多个对象

```java
Student s1 = new Student("zhangsan",23);
Student s2 = new Student("lisi",23);
Student s3 = new Student("wangwu",23);
Student s4 = new Student("zhaoliu",23);

ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D:/test.txt"));
oos.writeObject(s1);
oos.writeObject(s2);
oos.writeObject(s3);
oos.writeObject(s4);
oos.close();
```



   但是我们一般不这样写，因为我们在读取的时候会很麻烦。

   如果我们读到文件的末尾了，会返回一个异常并不是-1， 异常：EOFException，表示读到文件末尾，但是我们不能在出现异常的时候停止，所以要修改一下代码。

   

ArrayList类本身实现了序列化接口，所以可以这样做

```java
Student s1 = new Student("zhangsan",23);
Student s2 = new Student("lisi",23);
Student s3 = new Student("wangwu",23);
Student s4 = new Student("zhaoliu",23);

ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D:/test.txt"));

ArrayList<Student> list = new ArrayList<>();
list.add(s1);
list.add(s2);
list.add(s3);
list.add(s4);

oos.writeObject(list);

oos.close();
```



### 3.4.2 反序列化多个对象

```java
//      TODO 创建反序列化流
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("D:/test.txt"));
//      TODO 读取数据
        ArrayList<Student> list =(ArrayList<Student>) ois.readObject();
        System.out.println(list); //[Student{name='zhangsan', age=23}, Student{name='lisi', age=23}, Student{name='wangwu', age=23}, Student{name='zhaoliu', age=23}]

        ois.close();
```











# 四、 字符集详解



## 4.1 计算机的存储规则



-  **ASCII 字符集**

**在计算机中，任意数据都是以二进制的形式存储的**

8bit  -> 如 0 0 1 1 1 0 0 0  -> 1K , 即可以存储2^8，256个数据。

1K,也就是一个字节。**字节是计算机最小的存储单元**

**存储英文，一个字节就可以**，这是为什么呢？  这就和字符集相关了



>   可以阅读一下下面的文章
>
> [ 计算机基础——二进制、八进制、十六进制以及相互转换_我爱布朗熊的博客-CSDN博客](https://blog.csdn.net/weixin_51351637/article/details/129996423?spm=1001.2014.3001.5502)





- **GBK字符集**

​      这三个字符意思是"国标扩展"。

​     收录21003个汉字包含国家标准GB13000-1中的全部中日韩汉字，和BIG5编码中的所有汉字。

​     windows系统默认使用的就是GBK。系统显示 ANSI。



- **Unicode字符集**

​       Unicode字符集:国际标准字符集，它将世界各种语言的每个字符定义一个唯一的编码，以满足跨语言、跨平台的文本信息转换。





## 4.2 ASCII 字符集

也叫做编码表，一共有128个数据（字符），对应[0-127]。

1个字节最多能表示256个数据，那这就能得出一个结论，存储英文，一个字节即可。



比如我们要存储英文字母"a"，查询ASCII，对应97（110 0001），我们不能存储这个二进制数字，因为计算机最小的存储单位是字节，“110 0001”不足一个字节，所以我们要补充为"0110 0001",即补齐8位。

解码就是将上面的顺序反过来，“0110 0001”解码为97，然后查询ASCII表为"a"，进而读取到

![image-20230507180144987](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507180144987.png)







## 4.3 GBK 字符集



### 4.3.1 英文字母存储

**和ASCII没有什么区别**

其中：英文用一个字节存储，完全兼容ASCII



依然是 “a”字符，查询GBK，获取对应的数据"97",转成二进制"110 0001"，补充为一个字节"0110 0001"（不足8位，前面补零）





### 4.3.2 中文汉字存储

比如"汉"，查询GBK为”47802“，转换成二进制数字为"10111010 10111010",发现为两个字节，此时二进制数据不需要任何的改动，直接存储到硬盘当中就可以了。

因为一个字节只能记录256个数据，这几个数据对中文来说实在太少了，所以采用**两个字节来记录汉字**，完全够用。

![image-20230507184815905](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507184815905.png)

**对于两个字节，前面的一个字节是高位字节，后面的字节叫做低位字节。**

**对于高位字节二进制一定是以1开头，转成十进制之后是一个负数。**



 **为什么要是一个负数呢？**

​    因为我们要和英文区分开，当我们存储英文的时候，位数不够前面是补0

![image-20230507184522605](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507184522605.png)





## 4.4 Unicode 字符集 - 万国码

 这个"万国码"是一个虚词

 **在UTF-8字符编码下，英文占1个字符，中文占三个字符**

 **UTF-8不是字符集，他是Unicode的一种编码方式**

![image-20230507185536765](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507185536765.png)





![image-20230507185630081](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507185630081.png)





![image-20230507185730547](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507185730547.png)



![image-20230507190145053](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507190145053.png)









## 4.5 乱码



### 4.5.1 乱码产生原因



- **读取数据时未读完整个汉字**

 比如字节流，一次只能读取一个字节，读取英文的时候是没什么问题的。

但是当读取utf-8形式文件的时候，中文占三个字节，那我们用字节流读取文件很显然后产生乱码，因为没有读取完整个汉字





- **编码和解码时的方式不同意统一**

  如下所示，编码和解码方式是统一的，所以能正常展示，如果换成GBK或者其他方式解码，就会带来错误。

![image-20230507190703293](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507190703293.png)





### 4.5.2 如何不产生乱码？

不要用字节流读取文本文件，使用字符流

编码和解码使用同一个码表，我们现在一般采用UTF-8







### 4.5.3 扩展

**字节流读取中文会乱码，但是为什么拷贝不会乱码呢？**

因为拷贝的时候，字节码该怎么排序就怎么排序，还是正常的。但是我们读取的时候却是按照某个定长读取，这个定长可能正好在某个中文的三个字节中的某一个字节位置，从而导致乱码。

![image-20230507204952025](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230507204952025.png)





## 4.6 Java中编码

|                String类方法                |         说明         |
| :----------------------------------------: | :------------------: |
|          public byte[] getBytes()          | 使用默认方式进行编码 |
| public byte[] getBytes(String charsetName) | 会用指定当时进行编码 |
|                                            |                      |

```java
//      TODO 编码1  使用默认编码
        String str = "ai你呦";  // 采用默认编码方式，即右下角显示的“UTF-8”，一个中文三个字节，一个英文一个字节
        byte[] bytes1 = str.getBytes();
        System.out.println(Arrays.toString(bytes1));//[97, 105, -28, -67, -96, -27, -111, -90]  8个字节

//      TODO 编码2  指定编码
        byte[] bytes2 = str.getBytes("GBK");//大写小写都可以
        System.out.println(Arrays.toString(bytes2));//[97, 105, -60, -29, -33, -49]
```



## 4.7 Java中解码

|              String类方法               |         说明         |
| :-------------------------------------: | :------------------: |
|          String(byte[] bytes)           | 使用默认方式进行解码 |
| String(byte[] bytes,String charsetName) | 使用指定方式进行解码 |





```java
//     TODO 解码1  默认解码
        String str2 = new String(bytes1);
        System.out.println(str2);  //ai你呦

//     TODO 解码2  指定解码
        String str3 = new String(bytes2,"GBK");
        System.out.println(str3);  //ai你呦
```







# 五、Commons-io 工具包



Commons-io是apache开源基金组织提供的一组有关IO操作的**开源工具包**。

**作用： 提高IO流的开发效率**

![image-20230510151019766](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230510151019766.png)



## 5.2 Maven坐标

```xml
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.11.0</version>
</dependency>
```





## 5.3 Commons-io 常见方法

### 5.3.1 **与文件夹/文件相关的方法**

![image-20230510151340151](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230510151340151.png)



**拷贝文件**

```java
File src = new File("E:/java.txt");
File dest = new File("D:/java.txt");
FileUtils.copyFile(src,dest);
```



**拷贝目录**

 直接拷贝，就是我们理解的拷贝

```java
File src = new File("E:/test2");
File dest = new File("D:/test2");

FileUtils.copyDirectory(src,dest);
```



这个与上面拷贝文件夹的不同时，现在D:/test2的文件夹中创建一一个test2目录，然后在这个test2目录中添加文件

```java
File src = new File("E:/test2");
File dest = new File("D:/test2");

FileUtils.copyDirectoryToDirectory(src,dest);
```







### 5.3.2 **与IO流相关**

![image-20230510151426495](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230510151426495.png)









# 六、 hutool

http://hutool.cn/docs/#/   官方网站

https://apidoc.gitee.com/dromara/hutool/   帮助文档



![image-20230510171818835](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230510171818835.png)





|      相关类       |             说明              |
| :---------------: | :---------------------------: |
|      IoUtil       |         流操作工具类          |
|     FileUtil      |    文件读写和操作的工具类     |
|   FileTypeUtil    |      文件类型判断工具类       |
|   WatchMonitor    |        目录、文件监听         |
| ClassPathResource | 针对ClassPath中资源的访问封装 |
|    FileReader     |         封装文件读取          |
|    FileWriter     |         封装文件写入          |



##  6.1 Maven 坐标

```xml
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.8.12</version>
</dependency>
```







## 6.2 FileUtil 类常用静态方法



- **file :  根据参数创建一个file对象**

```java
//      file() 根据参数创建一个file对象，和我们之前说的File对象一模一样，而且多了几个构造方法
//      下面这个构造方式是之前没有的
        File file = FileUtil.file("D:/", "aaa", "bb", "test.txt");//D:/aaa/bb/test.txt 文件
        System.out.println(file); //D:\aaa\bb\test.txt
```

![image-20230510215703072](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230510215703072.png)





- **touch:  根据参数创建文件，如果父级路径不存在会一块创建**

  FileUtil.touch 语句运行完成之后文件直接创建

```java
File touch = FileUtil.touch("D:/aaa/bb/test.txt");
System.out.println(touch);  //D:\aaa\bb\test.txt
```





-  **writeLines:  把集合中的数据写出到文件中，覆盖模式**

```java
        ArrayList<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("aaa");
        list.add("aaa");
        FileUtil.writeLines(list,"E:/java.txt","UTF-8",true);
//写入文件的内容
//aaa
//aaa
//aaa
```





-  **addpendLines: 把集合中的数据写出到文件中，续写模式**

```java
ArrayList<String> list = new ArrayList<>();
list.add("aaa");
list.add("aaa");
list.add("aaa");
File file = FileUtil.appendLines(list, "E:/java.txt", "UTF-8");
System.out.println(file); //E:\java.txt
```





* **readLines: 指定字符编码，把文件中的数据读到集合中**

```java
        ArrayList<String> strings = FileUtil.readLines("E:/java.txt", "UTF-8", new ArrayList<String>());

//      这个方法的底层会帮我们创建
        List<String> stringList = FileUtil.readLines("E:/java.txt", "UTF-8");
//      一行数据，就是集合中的一条元素 
        System.out.println(stringList);  //[张靖奇你长得很帅, aaa, aaa, aaa, aaa, aaa, aaa]
```





-  **readUtf8Lines: 按照UTF-8的形式，把文件中的数据，读到集合中**





*  **copy: 拷贝文件或者文件夹**







