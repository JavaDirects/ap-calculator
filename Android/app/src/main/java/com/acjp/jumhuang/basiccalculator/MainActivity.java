package com.acjp.jumhuang.basiccalculator;

import android.app.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.*;

public class MainActivity extends Activity 
{
	private EditText n1,n2;
	private RadioGroup tg;
	private Button submit;

	private String type;

	// CONNECTION_TIMEOUT 和 READ_TIMEOUT 是以毫秒为单位
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		n1 = (EditText)findViewById(R.id.n1);
		n2 = (EditText)findViewById(R.id.n2);
		tg = (RadioGroup)findViewById(R.id.tg);
		submit = (Button)findViewById(R.id.submit);

		tg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int id)
				{
					switch (id)
					{
						case R.id.t1:
							type = "1";
							break;
						case R.id.t2:
							type = "2";
							break;
						case R.id.t3:
							type = "3";
							break;
						case R.id.t4:
							type = "4";
							break;
					}
				}
			});

		submit.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					//启动AsyncTask
					new AsyncLogin().execute(n1.getText().toString().trim(), n2.getText().toString().trim(), type);
				}
			});
    }

    private class AsyncLogin extends AsyncTask<String, String, String>
	{
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
		{
            super.onPreExecute();

            //这里的代码会在主线程中运行
            pdLoading.setMessage("\n加载中...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params)
		{
            try
			{
                // 请求数据的网址
				url = new URL("http://192.168.1.100/index.php");
            }
			catch (MalformedURLException e)
			{
                e.printStackTrace();
                return e.toString();
            }
            try
			{
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                //conn.setRequestMethod("GET");
				conn.setRequestMethod("POST");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);
				conn.setDoInput(true);

				conn.setUseCaches(false);

				conn.setRequestProperty("Charset", "UTF-8"); 

				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; zh-CN; HUAWEI TAG-TL00 Build;/HUAWEITAG-TL00) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.2.0.715 U3/0.8.0 Mobile Safari/534.30");
				conn.setRequestProperty("Connection", "Keep-Alive"); 

				// Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
					.appendQueryParameter("n1", params[0])
					.appendQueryParameter("n2", params[1])
					.appendQueryParameter("t", params[2]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            }
			catch (IOException e)
			{
                e.printStackTrace();
                return e.toString();
            }

            try
			{
                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK)
				{
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null)
					{
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());
                }
				else
				{
                    return ("unsuccessful");
                }

            }
			catch (IOException e)
			{
                e.printStackTrace();
                return e.toString();
            }
			finally
			{
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result)
		{
            //this method will be running on UI thread
            Toast.makeText(MainActivity.this, "结果：" + result, Toast.LENGTH_SHORT).show();

			pdLoading.dismiss();
        }
    }
}
