import java.util.Scanner;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Main {

    public static   Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        Scanner in =new Scanner(System.in);
        int n=in.nextInt();
        System.out.printf("%.2f",Math.pow(n,1.0/3)*3);
    }

    public static void main10(String[] args) {

        int count = 0;

        while (in.hasNextInt()) { // 注意 while 处理多个 case
            if (count ==0){
                count++;
                in.nextInt();
                continue;
            }

            int a = in.nextInt();
            int b = in.nextInt();
            System.out.println(a + b);
//       hasNext()方法用于检测下一个标记是否存在，而hasNextLine()方法用于检测下一行是否存在，并移动指针到下一行的开头
        }
    }


    public static void main3(String[] args) {
        int a;
        int b;
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        if(in.hasNextInt()){
            int n = in.nextInt();
            System.out.println(n);
            for(int i=0;i< n;i++){
                a = in.nextInt();
                b = in.nextInt();
                System.out.println(a+b);
            }
        }
    }


    public static void main1(String[] args) {
//
//        while (true){
//            System.out.println(in.nextInt());
//        }

    }

    public static void main2(String[] args) {

        // 注意 hasNext 和 hasNextLine 的区别
//      调用next()方法从输入流中读取的内容。它返回一个布尔值，表示是否存在匹配的标记。这个方法可以用于检测输入流中是否还有内容可供读取，但它不会移动指针，也不会消耗输入
//      hasNextLine()方法用于检测输入流中是否还有更多的行可供读取。它返回一个布尔值，表示是否存在下一行。这个方法会移动指针到下一行，并且消耗掉这一行的内容，将指针定位到下一行的开头。换句话说，它会读取并忽略当前行的剩余内容
        while (in.hasNextInt()) { // 注意 while 处理多个 case
            int a = in.nextInt();
            int b = in.nextInt();
            System.out.println(a + b);
//       hasNext()方法用于检测下一个标记是否存在，而hasNextLine()方法用于检测下一行是否存在，并移动指针到下一行的开头
        }
    }
}