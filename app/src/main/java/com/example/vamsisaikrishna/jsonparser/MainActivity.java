package com.example.vamsisaikrishna.jsonparser;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button bttn;
    TextView txt;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);

//     bttn = (Button)findViewById(R.id.bttn);
//        txt =(TextView)findViewById(R.id.txt);
//
//        bttn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new MyAsync().execute("http://services.hanselandpetal.com/feeds/flowers.json");
//            }
//        });

    }

   public class MyAsync extends AsyncTask<String, String, List<Model>> {

       private BufferedReader reader = null;
       private HttpURLConnection urlConnection = null;

       @Override
        protected List<Model> doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    InputStream stream = urlConnection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line=reader.readLine()) != null){
                        buffer.append(line + "\n");
                    }

                    String finalJson = buffer.toString();
                    StringBuffer sBuffer = new StringBuffer();


                    try {
                        Model model = new Model();
                        List<Model> modelList = new ArrayList<>();
                        JSONArray jArray = new JSONArray(finalJson);
                        for(int i = 0; i < jArray.length(); i++){

                            JSONObject finalObject = jArray.getJSONObject(i);

//                            String category = finalObject.getString("category");
//                            double price = finalObject.getDouble("price");
//                            int productId = finalObject.getInt("productId");
//                           // String instructions = finalObject.getString("instructions");
//                            String name = finalObject.getString("name");
//                            //String photo = finalObject.getString("photo");
//                            sBuffer.append(("category: " +category + " \n "+ "price: "+ price + " \n " +"productId: " +productId + " \n " + "name :"+ name + " \n "));

                            model.setProductId(finalObject.getInt("productId"));
                            model.setPrice(finalObject.getDouble("price"));
                            model.setName(finalObject.getString("name"));
                            model.setCategory(finalObject.getString("category"));
                            modelList.add(model);

                        }
                        return modelList;


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
           finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
           if(reader !=null){
               try {
                   reader.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }


                return null;
        }

        @Override
        protected void onPostExecute(List<Model> models) {
            super.onPostExecute(models);
           // txt.setText(models);
            ModelAdapter adapter = new ModelAdapter(getApplicationContext(), R.layout.row, models);
            listView.setAdapter(adapter);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            new MyAsync().execute("http://services.hanselandpetal.com/feeds/flowers.json");
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
    public class ModelAdapter extends ArrayAdapter{


        ViewHolder holder = new ViewHolder();


        //Context context;
        private int resource;
        private LayoutInflater inflater;
        private List<Model> modelList;
        public ModelAdapter(Context context, int resource, List<Model> objects) {
          super(context, resource, objects);
            //this.context  = context;
            this.resource = resource;
            modelList = objects;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           if(convertView == null){
               convertView = inflater.inflate(resource, null);
              holder.pId = (TextView)convertView.findViewById(R.id.product_txt);
               holder.name = (TextView)convertView.findViewById(R.id.name_txt);
               holder.price = (TextView)convertView.findViewById(R.id.price_txt);
               holder.category = (TextView)convertView.findViewById(R.id.category_txt);
               convertView.setTag(holder);
           }else{
               holder = (ViewHolder)convertView.getTag();
           }


            holder.pId.setText("productId: " +modelList.get(position).getProductId());
            holder.name.setText(modelList.get(position).getName());
            holder.price.setText("price: " +modelList.get(position).getPrice());
            holder.category.setText(modelList.get(position).getCategory());

            return convertView;
        }


        class ViewHolder{
            private TextView pId;
            private TextView name;
            private TextView price;
            private TextView category;

        }
    }

}


