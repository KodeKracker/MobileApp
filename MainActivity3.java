package com.example.random;

import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.util.*;
import java.util.ArrayList;

import java.net.InetAddress;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity3 extends Activity {

	private String message = null;
	private String message2 = null;
	private String str = null;
	private String imei = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity3);
		Button button=(Button)findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				// get the mobile number
				EditText et = (EditText) findViewById(R.id.editText1);
				mb = et.getText().toString();

				// execute the mobile number process method
			    new ProcessMobileNumber().execute(mb);
			}
		});

	}


	private class ProcessMobileNumber extends AsyncTask<String, Void, String>
	{

		/*Integer value;*/

		public Map<String, String> sendHttpPostRequest(String url, ArrayList<NameValuePair> payload){
			Map<String, String> response = new Map<String, String>();
			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				// set payload as entity and send
				httpPost.setEntity(new UrlEncodedFormEntity(payload,"UTF-8"))
				HttpResponse httpResponse = httpClient.execute(httpPost)

				// get the response status
				StatusLine statusLine = httpResponse.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK){
					// get response entity
					HttpEntity httpEntity = httpResponse.getEntity()

					// get reponse stream to read from response entity
					BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), "UTF-8"));

					String line;
					String[] res;

					// read response line by line
					while(line = reader.readLine()) != null){
						// trim and split the line in Key-Value pair
						line = line.trim();
						res = line.split(":",2);

						// add the key-value pair in response
						response.put(res[0].trim(),res[1].trim()));
					}
				}else{
	                //Closes the connection.
	                Log.w("HTTP1:",statusLine.getReasonPhrase());
	                httpResponse.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }

			}catch (ClientProtocolException e) {
	            Log.w("HTTP2:",e );
	        } catch (IOException e) {
	            Log.w("HTTP3:",e );
	        }catch (Exception e) {
	            Log.w("HTTP4:",e );
	        }

	        return response;
		}


		@Override
		protected String doInBackground(String mbNumber)
		{
			//prepare to make Http request
        	String url = "http://localhost:9080/New/MyServlet";

        	//add name value pair for the request body
	        ArrayList<NameValuePair> payload = new ArrayList<NameValuePair>();

	        // add content(i.e key-value pair) in the payload
	        payload.add(new BasicNameValuePair("Cycle","1"));
	        payload.add(new BasicNameValuePair("Data",mbNumber));

	        // get the response in key-value pair
	        Map<String, String> response= sendHttpPostRequest(url,payload);

	        if(response.containsKey("Acknowledgement")){
	        	data = response.get("Message");
	        	dataBase64 = Base64.encodeToString(data, Base64.DEFAULT);
	        	payload.clear();
	        	payload.add(new BasicNameValuePair("Cycle","2"));
	        	payload.add(new BasicNameValuePair("Data",dataBase64.toString()));
	        }else{
	        	raise Exception("No Acknowledgement Found");
	        }

			}
			catch(Exception e)
			{
				Toast.makeText(MainActivity3.this, "Incorrect pin", Toast.LENGTH_LONG).show();
			}
			return message;
	}

	@Override
	protected void onPostExecute(String result)
	{
		Toast.makeText(MainActivity3.this, result, Toast.LENGTH_LONG).show();
		et = (EditText) findViewById(R.id.editText1);
		et.setText("");
	}

				URL url = new URL("http://10.210.8.168:8080/New/MyServlet");
				URLConnection urlconnection = url.openConnection();

				urlconnection.setDoInput(true);
				urlconnection.setDoOutput(true);

				OutputStreamWriter out = new OutputStreamWriter(
						urlconnection.getOutputStream());
				TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

				imei = tm.getDeviceId();

				out.write(imei + "\n");// sending IMEI no. to server
				out.flush();
				out.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(
						urlconnection.getInputStream()));
				message = in.readLine();//Acknowledgment  sent by server
				str = in.readLine();// reading the string sent by server
				System.out.println(str);
				in.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return message;

		}

		protected void onPostExecute(String result) {

			Toast.makeText(MainActivity3.this, result, Toast.LENGTH_LONG)
					.show();

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity3, menu);
		return true;
	}
//This will receive a string ,digitally sign it and send it back to Servlet1 on button click
	private class Sendbase64 extends AsyncTask<Void, Void, String> {
		protected String doInBackground(Void... params) {

			try {
				URL url1 = new URL("http://10.210.8.168:8080/Base64/Servlet1");
				URLConnection urlconnection = url1.openConnection();

				urlconnection.setDoOutput(true);
				urlconnection.setDoInput(true);
				KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");// returns
																			// a
																			// keypair
																			// generator
																			// object
																			// that
																			// generates
																			// public/private
																			// key
																			// pair
																			// for
																			// the
																			// specified
																			// algorithm
				kpg.initialize(1024);
				KeyPair kp = kpg.generateKeyPair();
				PrivateKey priKey = kp.getPrivate();
				PublicKey pubKey = kp.getPublic();
				Signature instance = Signature.getInstance("SHA1withRSA");
				instance.initSign(priKey);// initialize this object for signing
				instance.update(str.getBytes());// updates the data to be signed
												// or verified,using the
												// specified array of bytes

				byte[] signature = instance.sign();

				byte[] publickeybytes = pubKey.getEncoded();

				MyEntity obj = new MyEntity();
				obj.setStr(str);
				obj.setPublickeybytes(publickeybytes);
				obj.setSignature(signature);
				Gson gson = new Gson();
				String json = gson.toJson(obj);//converting java object into json representation
				OutputStreamWriter out1 = new OutputStreamWriter(
						urlconnection.getOutputStream());
				out1.write(json);

				System.out.println(str);
				System.out.println(new String(signature));
				System.out.println(new String(publickeybytes));
				out1.flush();
				out1.close();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						urlconnection.getInputStream()));
				message2 = in.readLine();//Acknowledgment sent by server on verifying the signature

			} catch (Exception e) {
				e.printStackTrace();
			}
			return message2;
		}

		protected void onPostExecute(String result) {
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
					.show();
		}

	}

}
