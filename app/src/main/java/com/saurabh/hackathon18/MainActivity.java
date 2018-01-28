package com.saurabh.hackathon18;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FloatingActionButton floatingActionButton;
    ListView NewsFeed;
    ShareHolder shareHolder;
    ProgressDialog progressDialog;
    ArrayList<String> post_id, image, up, down, date, username, title, text, link;

    @Override
    protected void onResume() {
        post_id = new ArrayList<>();
        date = new ArrayList<>();
        username = new ArrayList<>();
        image = new ArrayList<>();
        title = new ArrayList<>();
        text = new ArrayList<>();
        link = new ArrayList<>();
        up = new ArrayList<>();
        down = new ArrayList<>();

        if (shareHolder.getId().equals("")) {
            progressDialog.show();
            StringRequest AndroidId = new StringRequest(Request.Method.POST, "https://crummy-stuff.000webhostapp.com/Fake/CreateUser.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray array = new JSONArray(response);
                        shareHolder.setId(array.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                    getInfo("https://crummy-stuff.000webhostapp.com/Fake/getData.php");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map map = new HashMap();
                    map.put("Android", Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));
                    return map;
                }
            };
            MySending.getInstance(MainActivity.this).addToRequestQueue(AndroidId);
        } else {
            getInfo("https://crummy-stuff.000webhostapp.com/Fake/getData.php");
        }

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        post_id = new ArrayList<>();
        date = new ArrayList<>();
        username = new ArrayList<>();
        image = new ArrayList<>();
        title = new ArrayList<>();
        text = new ArrayList<>();
        link = new ArrayList<>();
        up = new ArrayList<>();
        down = new ArrayList<>();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(true);

        setContentView(R.layout.activity_main);
        shareHolder = new ShareHolder(MainActivity.this);
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setTitle("CounterFeed");
        actionBarDrawerToggle.syncState();
        floatingActionButton = findViewById(R.id.LaunchPost);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.navigation);
        NewsFeed = findViewById(R.id.MainFeed);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.AboutUs:
                        startActivity(new Intent(MainActivity.this , AboutUs.class));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.AppInfo:
                        startActivity(new Intent(MainActivity.this , AboutAppActivity.class));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.Trending:
                        post_id = new ArrayList<>();
                        date = new ArrayList<>();
                        username = new ArrayList<>();
                        image = new ArrayList<>();
                        title = new ArrayList<>();
                        text = new ArrayList<>();
                        link = new ArrayList<>();
                        up = new ArrayList<>();
                        down = new ArrayList<>();
                        getInfo("https://crummy-stuff.000webhostapp.com/Fake/trending.php");
                        getSupportActionBar().setTitle("Trending");
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.Home:
                        post_id = new ArrayList<>();
                        date = new ArrayList<>();
                        username = new ArrayList<>();
                        image = new ArrayList<>();
                        title = new ArrayList<>();
                        text = new ArrayList<>();
                        link = new ArrayList<>();
                        up = new ArrayList<>();
                        down = new ArrayList<>();
                        getInfo("https://crummy-stuff.000webhostapp.com/Fake/getData.php");
                        getSupportActionBar().setTitle("CounterFeed");
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shareHolder.getName().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    final View LoginView = getLayoutInflater().inflate(R.layout.create_user, null);
                    builder.setView(LoginView);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    TextView Skip;
                    final EditText Name, Email, Mob;
                    Button Next;

                    Next = LoginView.findViewById(R.id.LoginNext);
                    Name = LoginView.findViewById(R.id.LoginName);
                    Email = LoginView.findViewById(R.id.LoginEmail);
                    Mob = LoginView.findViewById(R.id.LoginMob);
                    Skip = LoginView.findViewById(R.id.LoginText);

                    Skip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            startActivity(new Intent(MainActivity.this, NewsLaunch.class));
                        }
                    });

                    Next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            progressDialog.show();
                            StringRequest sendData = new StringRequest(Request.Method.POST, "https://crummy-stuff.000webhostapp.com/Fake/SetDetails.php", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    shareHolder.setAccount(Name.getText().toString(), Email.getText().toString(), Mob.getText().toString());
                                    startActivity(new Intent(MainActivity.this, NewsLaunch.class));
                                    progressDialog.dismiss();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map map = new HashMap();
                                    map.put("Android",  Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));
                                    map.put("Name", Name.getText().toString());
                                    map.put("Email", Email.getText().toString());
                                    map.put("Mob", Mob.getText().toString());
                                    return map;
                                }
                            };
                            MySending.getInstance(MainActivity.this).addToRequestQueue(sendData);
                            dialog.dismiss();
                        }
                    });
                } else {
                    startActivity(new Intent(MainActivity.this, NewsLaunch.class));
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

        }

        return super.onOptionsItemSelected(item);
    }

    public void getInfo(String URL) {
        progressDialog.show();
        StringRequest GetInformation = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONArray inner = array.getJSONArray(i);
                        post_id.add(inner.getString(8));
                        username.add(inner.getString(0));
                        date.add(inner.getString(1));
                        image.add(inner.getString(2));
                        title.add(inner.getString(3));
                        text.add(inner.getString(5));
                        link.add(inner.getString(4));
                        up.add(inner.getString(6));
                        down.add(inner.getString(7));
                    }
                } catch (JSONException e) {
                }
                NewsFeed.setAdapter(new CustomAdapter());
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySending.getInstance(MainActivity.this).addToRequestQueue(GetInformation);

    }

    private void RealFakeFunction(Button real, Button fake, String val, String post_id, int i, TextView upV, TextView downV) {
        real.setBackground(getResources().getDrawable(R.drawable.real_unchnage));
        real.setTextColor(getResources().getColor(R.color.Real));
        fake.setBackground(getResources().getDrawable(R.drawable.fake_unchange));
        fake.setTextColor(getResources().getColor(R.color.Fake));

        if (val.equals("real")) {
            if (real.getTag().equals("") && fake.getTag().equals("")) {
                real.setTag("real");
                fake.setTag("");
                shareHolder.SetButton(post_id, "1");
                upV.setText("Real : " + String.valueOf(Integer.valueOf(up.get(i)) + 1));
                up.set(i, String.valueOf(Integer.valueOf(up.get(i)) + 1));
            } else if (fake.getTag().equals("real")) {
                real.setTag("real");
                fake.setTag("");
                shareHolder.SetButton(post_id, "1");
                upV.setText("Real : " + String.valueOf(Integer.valueOf(up.get(i)) + 1));
                up.set(i, String.valueOf(Integer.valueOf(up.get(i)) + 1));
                downV.setText("Fake : " + String.valueOf(Integer.valueOf(down.get(i)) - 1));
                down.set(i, String.valueOf(Integer.valueOf(down.get(i)) - 1));
            }
        } else {
            if (real.getTag().equals("") && fake.getTag().equals("")) {
                fake.setTag("real");
                real.setTag("");
                shareHolder.SetButton(post_id, "0");
                downV.setText("Fake : " + String.valueOf(Integer.valueOf(down.get(i)) + 1));
                down.set(i, String.valueOf(Integer.valueOf(down.get(i)) + 1));
            } else if (real.getTag().equals("real")) {
                fake.setTag("real");
                real.setTag("");
                shareHolder.SetButton(post_id, "0");
                upV.setText("Real : " + String.valueOf(Integer.valueOf(up.get(i)) - 1));
                up.set(i, String.valueOf(Integer.valueOf(up.get(i)) - 1));
                downV.setText("Fake : " + String.valueOf(Integer.valueOf(down.get(i)) + 1));
                down.set(i, String.valueOf(Integer.valueOf(down.get(i)) + 1));
            }

        }
        ChangeData(post_id, up.get(i).toString(), down.get(i).toString());
    }

    private void ChangeData(final String post_id, final String up, final String down) {
        StringRequest sendData = new StringRequest(Request.Method.POST, "https://crummy-stuff.000webhostapp.com/Fake/ChnageValue.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("post_id", post_id);
                map.put("up", up);
                map.put("down", down);
                return map;
            }
        };

        MySending.getInstance(MainActivity.this).addToRequestQueue(sendData);
    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return post_id.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.feed_layout, null);
            ImageView imageView = view.findViewById(R.id.FeedImage);
            final TextView Name, Date, Title, Text, upView, downView , Link;
            final Button real, fake, comment;


            Name = view.findViewById(R.id.FeedName);
            Date = view.findViewById(R.id.FeedDate);
            Title = view.findViewById(R.id.FeedTitle);
            Text = view.findViewById(R.id.FeedText);
            Link = view.findViewById(R.id.FeedLink);

            upView = view.findViewById(R.id.RealCount);
            downView = view.findViewById(R.id.FakeCount);

            if (!image.get(i).equals("null")){
                Glide.with(MainActivity.this).load("http://crummy-stuff.000webhostapp.com/Fake/" + image.get(i)).into(imageView);
            }else{
                imageView.setVisibility(View.GONE);
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Name.setText(username.get(i));
            Date.setText(date.get(i));
            Title.setText(title.get(i));
            Text.setText(text.get(i));
            Link.setText(link.get(i));

            Link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this , LinkActivity.class);
                    intent.putExtra("Link" , link.get(i).toString());
                    startActivity(intent);
                }
            });

            upView.setText("Real : " + up.get(i));
            downView.setText("Fake : " + down.get(i));

            real = view.findViewById(R.id.RealButton);
            fake = view.findViewById(R.id.FakeButton);
            comment = view.findViewById(R.id.CommentButton);

            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this ,CommentActivity.class).putExtra("post_id",post_id.get(i)));
                }
            });

            if (shareHolder.getCheckButton(post_id.get(i)).equals("")) {
                real.setTag("");
                fake.setTag("");
            } else if (shareHolder.getCheckButton(post_id.get(i)).equals("1")) {
                real.setTag("real");
                fake.setTag("");
                real.setBackground(getResources().getDrawable(R.drawable.real_clicked));
                real.setTextColor(getResources().getColor(R.color.white));
            } else if (shareHolder.getCheckButton(post_id.get(i)).equals("0")) {
                real.setTag("");
                fake.setTag("real");
                fake.setBackground(getResources().getDrawable(R.drawable.fake_click));
                fake.setTextColor(getResources().getColor(R.color.white));
            }

            real.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RealFakeFunction(real, fake, "real", post_id.get(i).toString(), i, upView, downView);
                    real.setBackground(getResources().getDrawable(R.drawable.real_clicked));
                    real.setTextColor(getResources().getColor(R.color.white));
                }
            });

            fake.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RealFakeFunction(real, fake, "fake", post_id.get(i).toString(), i, upView, downView);
                    fake.setBackground(getResources().getDrawable(R.drawable.fake_click));
                    fake.setTextColor(getResources().getColor(R.color.white));
                }
            });

            return view;
        }
    }
}
