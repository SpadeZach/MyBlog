// 上传一个文件到HDFS中
import  java.net.URI
import org.apache.hadoop.fs.FileSystem

public class HdfsClientDemo{
    public static void main(String[] args) throws Exception{
        //new Configuration();会从classpath中加载core-default.xml hdfs-default.xml core-site.xml 
        Configuration conf = new Configuration();
        //指定本客户端上传文件到hdfs时需要保持的副本数为：2
        conf.set("dfs.replication","2");
        //指定客户端上传到hdfs时切块的规格大小：64MB
        conf.set("dfs.blocksize","64m");
        //构造一个访问hdfs系统的客户端对象，参数1.hdfs系统的url 参数2.客户端特别指定参数，参数3.客户端的身份
        FileSystem.get(new URI("hdfs://192.168.219.11:9000/"),conf,"root");

        //上传一个文件到HDFS中
        fs.copyFromLocalFile(new Path("D:/install-pkgs/hbase-1.2.1-bin.tar.gz"),new Path("/aaa/"));
        fs.close();
    }
}


// 下载文件

//前置方法
@Before
FileSystem fs = null;
public void  init() throws Exception{
    Configuration conf = new Configuration();
    conf.set("dfs.replication","2");
    conf.set("dfs.blocksize","64m");
    FileSystem.get(new URI("hdfs://192.168.219.11:9000/"),conf,"root");
}
/** 从HDFS中下载到本地磁盘 */
@Test
public void testGet() throws IlleagelArgumentException, IOException{
    //参数1.hdfs路径 参数2.目标路径
    fs.copyToLocalFile(new Path("/hdp.txt"),new Path("f:/"));
    fs.close();
}

/** 从HDFS中内部移动文件 */
@Test
public void testRename() throws IlleagelArgumentException, IOException{
    //参数1.hdfs路径 参数2.目标路径
    fs.copyToLocalFile(new Path("/hdp.txt"),new Path("/aaa/in.text"));
    fs.close();
}
/** 从HDFS中内部创建文件夹 */
@Test
public void testMkdir() throws IlleagelArgumentException, IOException{
    fs.mkdirs(new Path("/xx/yy/zz"));
    fs.close();
}
/** 从HDFS中内部删除文件夹/文件 */
@Test
public void testRm() throws IlleagelArgumentException, IOException{
    fs.delete(new Path("/xx/yy/zz"),true);
    fs.close();
}
/** 查询HDFS指定目录下的信息 */
@Test
public void testLs() throws Exception{
    //fs.listFiles(new Path("/"),false);    //参数2.要不要把子目录文件查出来
    RemoteIterator<LocatedFileStatus> iter = fs.listFiles(new Path("/"),true);

    while(iter.hasNext){
        LocatedFileStatus status = iter.next();
        System.out.println("块大小:"+status.getBlocksize());
        System.out.println("文件长度:"+status.getLen());
        System.out.println("副本数量:"+status.getReplication());
        System.out.println("块信息:"+Arrays.toString(status.getBlockLocations()));
        System.out.println("块路径:"+Arrays.toString(status.getPath()));
        System.out.println("------------------------");
    }
    fs.close();
}
/** 查询HDFS指定文件和文件夹信息 */
@Test
public void testLs2() throws Exception{
   FileStatus[] listStatus = fs.listStatus(new Path("/"));
   for(FileStatus status:listStatus){
        System.out.println("文件路径:"+Arrays.toString(status.getPath()));
        System.out.println(status.isDirectory()?"这是文件夹":"这是文件");
        System.out.println("块大小:"+status.getBlocksize());
        System.out.println("文件长度:"+status.getLen());
        System.out.println("副本数量:"+status.getReplication());
        System.out.println("------------------------");
    }
    fs.close();
}

/** 读取hdfs中的文件的内容 */
@Test
public void testReadData() throws  IlleagelArgumentException, IOException{
    //获取数据流
    FSDataInputStream in  = fs.open(new Path("/test.text"));
    //缓冲字节流
    BufferedReader br = new bufferedReader(new InputStreamReader(in,"utf-8"));

    String line = null;
    while((line=br.readLine())!=null){
        //一次读一行
        System.out.println(new String(line));
    }
    br.close();
    in.close();
    fs.close();

}

/** 读取hdfs中指定偏移量的文件的内容 */
@Test
public void testReadData() throws  IlleagelArgumentException, IOException{
    //获取数据流
    FSDataInputStream in  = fs.open(new Path("/xx.dat"));
    
    //将读取的起始位置进行指定
    in.seek(12);
    //读取字节 --- 另一种写法
    byte[] buf = new byte[16];
    in.read(buf);
    System.out.println(new String(buf));
    in.close();
    fs.close();
}

/** hdfs中写数据  */
@Test
public void testWriteData() throws  IlleagelArgumentException, IOException{
    //1.路径2.是否覆盖，不覆盖为追加
    FSDataOutputStream out =  fs.creat(new Path("/yy.jpg"),false);

    byte[] buf = new byte[1024];

    new FileInputStream in = new FileInputStream("D:/images/11.jpg");
    int read = 0;
    while(read=in.read(buf)!=-1){
        out.write(buf,0,read);
    }
    in.close(); 
    out.close();
    fs.close();

}
