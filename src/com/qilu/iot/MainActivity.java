package com.qilu.iot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.Test;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;
public class MainActivity extends Activity {
	private EditText send_data_edittext;
	private TextView data_show;
	private ServerSocket serversocket;
	private Handler mhander;
	private Socket socket;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mhander=new Handler(){
			public void handleMessage(Message msg) {
				Bundle bundle=msg.getData();
				String message=bundle.getString("message");
				data_show.setText(message);
			};
		};
		send_data_edittext = (EditText) findViewById(R.id.send_data_edittext);
		data_show = (TextView) findViewById(R.id.data_show);
		init();
	}
	@Test
	private void init() {
		// TODO Auto-generated method stub
		System.out.println("客户端正在连接。。。。");
		try {
			serversocket = new ServerSocket(1234);
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						// TODO Auto-generated method stub
						try {
							socket = serversocket.accept();
							if(socket!=null){
							System.out.println("客户端连接成功。。。");
							InputStream s=socket.getInputStream();
							BufferedReader buffer = new BufferedReader(
									new InputStreamReader(
										s,"GBK"));
							System.out.println("客户端数据接收成功。。。");
						    String strinfo=null;
 					    	while(!((strinfo=buffer.readLine())==null)){
							System.out.println("客户端数据接收成功，接收的数据为：" + strinfo);						
							Message message=new Message();
							Bundle bundle=new Bundle();
							bundle.putString("message",strinfo);
							message.setData(bundle);
							mhander.sendMessage(message);
 					    	}
					    	buffer.close();
							}
						 } catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}