# easyExcel



# 一、 了解

开发中经常会涉及到Excel的处理，如导出Excel，导入Excel到数据库中



 **引用场景：**

​      将用户信息导出为Excel表格

​      将Excel表中的信息录入到网站数据库(习题上传...)，大大减轻网站录入量。（大概意思就是将内容写在表格中，然后通过表格一次性的录入数据库，不用一个一个的在网页的表格中输入）



目前最流行的就是 **Apache POI **和阿里巴巴的 **easyExcel**



##  1.1 Apache POI

比较麻烦

官网： https://poi.apache.org/



开放源码函式库，提供API给Java程序对Microsoft Office格式档案读和写的功能。



结构:
HSSF         提供读写MicrosoftExcel各式档案的功能。（03 版Excel，最多65535行）

XSSF          提供读写MicrosoftExcel OOXML各式档案的功能。（07 版Excel，没有限制多少行）

HWPF        提供读写MicrosofttWord各式档案的功能。（Word）

HSLF          提供读写MicrosofPowerPoint林式档案的功能（幻灯片）

HDGF         提供读写Microsoft Visio式档案的功能。







## 1.2 easyExcel

easyExcel 官网地址 ： https://github.com/alibaba/easyexcel



Apache POI 比较消耗内存，对于easyExcel，不管数据量多发都不会出现内存溢出。



内存问题：  

​    对于POI，  POI=100w  即100w条数据先加载到内存，此时内存就会溢出

​    对于EasyExcel，会一行一行的写，本质就是时间和空间的转换



在帮助文档中“关于EasyExcel”中，**文件解压文件读取通过文件形式**

![img](https://cdn.nlark.com/yuque/0/2020/png/553000/1584449986458-75c79796-c987-4b0f-92cd-82cd1855384e.png?x-oss-process=image%2Fresize%2Cw_772%2Climit_0)













# 二、 准备工作

## 2.1 Maven坐标

```xml
<!--xls(03版)-->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>3.9</version>
</dependency>
<!--xls(07版)-->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>3.9</version>
</dependency>
<!--日期格式化工具-->
<dependency>
    <groupId>joda-time</groupId>
    <artifactId>joda-time</artifactId>
    <version>2.10.1</version>
</dependency>
```



## 2.2 Excel讲解

**03版本与07版本的区别：**

① 首先是文件结尾不同，03版本Excel文件结尾是".xls"，07版本Excel文件结尾是".xlsx"

②03版本最多65536行数据，而07版本的数据量没有限制，理论是无线的

③03版本与07版本对应的工具类不同，在我们上面的左面就看出来了



**03版本与07版本的相同处：**

工作簿、工作表、行、列。一会也是按照这个操作进行读和写。





# 三、 Excel基本写操作（导出Excel）



**创建工作簿对象：**其中org.apache.poi.ss.usermodel.Workbook是一个接口，有三个实现类，分别是HSSFWorkbook（Excel 03 版本）、SXSSFWorkbook（可以加快速度的处理07版本Excel）、XSSFWorkbook(Excel 07 版本，处理速度会慢一点)





## 3.1 03 版本Excel导出操作

```java
@SpringBootTest
class EasyExcelApplicationTests {

//  路径
    String PATH = "D:/";


    @Test
    void contextLoads() {
//      TODO 创建一个工作薄
        Workbook workbook = new HSSFWorkbook(); // 03版本Excel

//      TODO 创建一个工作表  工作表就是打开Excel表下面的sheet1、sheet2、sheet3
        Sheet sheet = workbook.createSheet("张靖奇测试表1");

//      TODO 创建第一行
        Row row1 = sheet.createRow(0);  //第一行

//      TODO 创建一个单元格（在二维坐标里面我们叫做[0,0]或者叫做[1,1],也就是第一行的第一个单元格，即第一行第一列交叉处)
//      [1,1]
        Cell cell11 = row1.createCell(0);
        cell11.setCellValue("今日新增观众");
//      [1,2]
        Cell cell12 = row1.createCell(1);
        cell12.setCellValue("666");

//      TODO 创建第二行
        Row row2 = sheet.createRow(1);
//      [2,1]
        Cell cell21 = row2.createCell(0);
        cell21.setCellValue("统计时间");

        Cell cell22 = row2.createCell(1);
        String time = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        cell22.setCellValue(time);

//      TODO IO 流生成一张表
        FileOutputStream fileOutputStream =null;
        try {
            fileOutputStream = new FileOutputStream(PATH + "张靖奇测试"+".xls");//注意是03版本结尾
//          TODO 写出
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//          TODO 关闭流
            if (fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Excel表导出完毕！");
    }

}
```





## 3.2 07版本Excel导出操作

下面的实现，Workbook接口的实现类是XSSFWorkbook，IO流输出的文件的名称结尾是".xlsx"，除此之外其他的基本相同。

```java
  @Test
    void contextLoads07() {
//      TODO 创建一个工作薄
        Workbook workbook = new XSSFWorkbook(); // 07版本Excel

//      TODO 创建一个工作表  工作表就是打开Excel表下面的sheet1、sheet2、sheet3
        Sheet sheet = workbook.createSheet("张靖奇测试表1");

//      TODO 创建第一行
        Row row1 = sheet.createRow(0);  //第一行

//      TODO 创建一个单元格（在二维坐标里面我们叫做[0,0]或者叫做[1,1],也就是第一行的第一个单元格，即第一行第一列交叉处)
//      [1,1]
        Cell cell11 = row1.createCell(0);
        cell11.setCellValue("今日新增观众");
//      [1,2]
        Cell cell12 = row1.createCell(1);
        cell12.setCellValue("666");

//      TODO 创建第二行
        Row row2 = sheet.createRow(1);
//      [2,1]
        Cell cell21 = row2.createCell(0);
        cell21.setCellValue("统计时间");

        Cell cell22 = row2.createCell(1);
        String time = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        cell22.setCellValue(time);

//      TODO IO 流生成一张表
        FileOutputStream fileOutputStream =null;
        try {
            fileOutputStream = new FileOutputStream(PATH + "张靖奇测试"+".xlsx");//注意是07版本结尾
//          TODO 写出
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//          TODO 关闭流
            if (fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Excel表导出完毕！");
    }
```





## 3.3 大数据量的导出（数据批量导入到磁盘）

**大文件写HSSF：** 

​    缺点： 最多只能处理65536行，超出则抛异常

   优点： 过程中写入缓存，不操作磁盘，最后一次性写入磁盘 



**大文件写XSSF：**

​    缺点： 写数据速度非常慢，非常消耗内存，也会发生内存溢出，如100万条

​    优点：可以写较大的数据量，如20万条



**大文件写SXSSF：**

   优点： 可以写入非常大的数据量，如100万条甚至更多条，写数据速度快，占用更少的内存

   **注意：**

​      过程中会产生临时文件，需要清理临时文件

​      默认100条记录被保存在内存中，如果超过这个数量，则最前面的数据被写入临时文件

​       如果想自定义内存中数据的数量，可以使用 new SXSSFWorkbook(数量)



  **SXSSFWorkbook-来至官方的解释:**实现"BigGridDemo"策略的流式XSSFWorkbook版本。这允许写入非常大的文件而不会耗尽内存，因为任何时候只有可配置的行部分被保存在内存中。
          请注意，仍然可能会消耗大量内存，这些内存基于您正在使用的功能，例如合并区域，注释...仍然只存储在内存中，因此如果广泛使用，可能需要大量内存。

```java
    @Test
    void testWrite07BigDataSupper() throws IOException {
//      TODO 创建一个簿
        Workbook workbook = new SXSSFWorkbook();
//      TODO 创建表
        Sheet sheet = workbook.createSheet();
//      TODO 写入数据
        for (int rowNum =0; rowNum<100000; rowNum++){
//          TODO 行
            Row row = sheet.createRow(rowNum);
            for (int cellNum=0;cellNum<10;cellNum++){
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(cellNum);
            }
        }

//     TODO 导出Excel
        FileOutputStream outputStream = new FileOutputStream(PATH+"超大数据测试"+".xlsx");
        workbook.write(outputStream);
//      TODO 清理临时文件,因为SXSSFWorkbook写出时会生成临时文件，但是占用内存不是很大
        ((SXSSFWorkbook) workbook).dispose();
        outputStream.close();
    }
```







# 四、Excel基本读取及注意



## 4.1 03版本Excel文件读取

```java
    @Test
    void testRead03() throws IOException {
//      TODO 获取文件流,读取工作簿
        FileInputStream fileInputStream = new FileInputStream("D:/明细表.xls");

//      TODO 创建一个工作薄.Excel能操作的，此工作簿对象都能操作
        Workbook workbook = new HSSFWorkbook(fileInputStream); // 03版本Excel

//      TODO 得到表
        Sheet sheet = workbook.getSheetAt(0);//可以通过名字获取表，也可以通过下标，下面这个形式就是通过下标

//      TODO 取出行
        Row row = sheet.getRow(1);

//
//      TODO 取出列  读取值的时候一定要注意类型
//           字符串类型数据
//        Cell cell = row.getCell(0);
//        System.out.println(cell.getStringCellValue());

//           数字类型数据
        Cell cell = row.getCell(0);
        System.out.println(cell.getNumericCellValue());

//      TODO 关闭流
        fileInputStream.close();

    }
```





## 4.2 07版本Excel文件读取

文件名后缀不同，Workbook实现类不同

```java
    @Test
    void testRead07() throws IOException {
//      TODO 获取文件流,读取工作簿
        FileInputStream fileInputStream = new FileInputStream("D:/明细表.xlsx");

//      TODO 创建一个工作薄.Excel能操作的，此工作簿对象都能操作
        Workbook workbook = new XSSFWorkbook(fileInputStream); // 03版本Excel

//      TODO 得到表
        Sheet sheet = workbook.getSheetAt(0);//可以通过名字获取表，也可以通过下标，下面这个形式就是通过下标

//      TODO 取出行
        Row row = sheet.getRow(1);

//
//      TODO 取出列  读取值的时候一定要注意类型
//           字符串类型数据
//        Cell cell = row.getCell(0);
//        System.out.println(cell.getStringCellValue());

//           数字类型数据
        Cell cell = row.getCell(0);
        System.out.println(cell.getNumericCellValue());

//      TODO 关闭流
        fileInputStream.close();

    }
```





## 4.3 难点 — 读取不同类型的数据





# 五、 计算公式





# 六、 EasyExcel 使用



