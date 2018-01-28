package com.saurabh.hackathon18;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    ListView listView;
    EditText Post;
    Button btn;
    String Id;
    ShareHolder shareHolder;
    ArrayList Name, Date, Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        progressDialog = new ProgressDialog(CommentActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        getSupportActionBar().setTitle("Message");
        shareHolder = new ShareHolder(CommentActivity.this);
        listView = findViewById(R.id.CommentListView);
        Name = new ArrayList();
        Date = new ArrayList();
        Text = new ArrayList();

        Id = getIntent().getExtras().getString("post_id");

        GetInfo();

        Post = findViewById(R.id.CommentEdit);
        btn = findViewById(R.id.CommentButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaunchComment();
            }
        });


    }

    public class CustomAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return Name.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.comment_each , null);
            TextView NameView , DateView , TextVieW;
            NameView = view.findViewById(R.id.CommentName);
            DateView = view.findViewById(R.id.CommentDate);
            TextVieW = view.findViewById(R.id.CommentText);

            NameView.setText(Name.get(i).toString());
            DateView.setText(Date.get(i).toString());
            TextVieW.setText(Text.get(i).toString());

            return view;
        }
    }

    private void GetInfo() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://crummy-stuff.000webhostapp.com/Fake/getComment.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    Name = new ArrayList();
                    Date =  new ArrayList();
                    Text = new ArrayList();

                    JSONArray array =  new JSONArray(response);
                    for (int i=0 ; i<array.length() ; i++){
                        JSONArray inner = array.getJSONArray(i);
                        Name.add(inner.getString(0));
                        Date.add(inner.getString(1));
                        Text.add(inner.getString(2));
                    }
                } catch (JSONException e) {
                }

                listView.setAdapter(new CustomAdapter());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("local", Id);
                return map;
            }
        };
        MySending.getInstance(CommentActivity.this).addToRequestQueue(stringRequest);
    }


    private void LaunchComment() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://crummy-stuff.000webhostapp.com/Fake/CommentLaunch.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                GetInfo();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("local", Id);
                if (shareHolder.getName().equals("")){
                    map.put("name", "Anonymous");
                }else{
                    map.put("name",shareHolder.getName());
                }
                map.put("date", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()));
                map.put("text",Post.getText().toString());
                return map;
            }
        };
        MySending.getInstance(CommentActivity.this).addToRequestQueue(stringRequest);
    }


}
