package meetingscheduler.com.companymeetingscheduler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    public static final String TAG="MainActivity";
    private String URLstring ="http://fathomless-shelf-5846.herokuapp.com/api/schedule?date=";

    private List<Model> scheduleList;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    //private Button btn_prev;
    //  private TextView txtResponse;
    private LinearLayoutManager linearLayoutManager;
    public static String inputDate;
    private Button btn_prev,btn_next,btn_scheduleMeeting;
    private TextView tv_date,error;
     SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
    Calendar c = Calendar.getInstance();
   private int counter1=1;
   private int counter2=-1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);


       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);*/
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        btn_prev=(Button)findViewById(R.id.btn_prev);
        btn_next=(Button)findViewById(R.id.btn_next);
        tv_date=(TextView)findViewById(R.id.tv_date);
        error=(TextView)findViewById(R.id.error);
        btn_scheduleMeeting=(Button)findViewById(R.id.btn_scheduleMeeting);


        scheduleList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        // mList.setAdapter(adapter);


        inputDate = sdfDate.format(new Date());

        Log.d(TAG,"inputDate :"+inputDate);
        //btn_prev=(Button)findViewById(R.id.btn_prev);

        Log.d(TAG,"Inside on create");
        requestJSON();

        btn_scheduleMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(MainActivity.this,ScheduleMeetingActivity.class);
                startActivity(i1);
            }
        });





        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //decrementDateByOne(new Date());
                scheduleList.clear();
                inputDate= sdfDate.format( decrementDateByOne(new Date()));
                Log.d(TAG,"Inside prev :"+inputDate);
                requestJSON();
                tv_date.setText(inputDate);
            }
        });



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // incrementDateByOne(new Date());
                scheduleList.clear();
                inputDate=sdfDate.format(incrementDateByOne(new Date()));
                Log.d(TAG,"Inside next :"+inputDate);
                requestJSON();
                tv_date.setText(inputDate);
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Date incrementDateByOne(Date date) {
     //   Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.add(Calendar.DATE, counter1);
        counter1++;
        counter2++;
        Date nextDate = c.getTime();
        return nextDate;
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public Date decrementDateByOne(Date date) {
        //Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, counter2);
        counter2--;
        counter1--;
        Date previousDate = c.getTime();
        return previousDate;
    }
    private void requestJSON(){
        Log.d(TAG,"Inside on requestJson()");
       /* final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, URLstring+inputDate,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "Response :" + response.toString());
                         if(response.length()>0){
                             error.setVisibility(View.GONE);
                        for (int i = 0; i < response.length(); i++) {
                            Model model = new Model();
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String startTime = jsonObject.getString("start_time");
                                Log.d(TAG, "startTime :" + startTime);


                                model.setStart_time(jsonObject.getString("start_time"));
                                model.setEnd_time(jsonObject.getString("end_time"));
                                model.setDescription(jsonObject.getString("description"));
                                JSONArray arrObj = jsonObject.getJSONArray("participants");
                                List<String> participants = new ArrayList<>();
                                for (int j = 0; j < arrObj.length(); j++) {
                                    String participant = arrObj.getString(j);
                                    participants.add(participant.toString());
                                }
                                model.setParticipants(participants);
//                                Log.d(TAG,"arrObj :"+arrObj);
                              /*  Gson gson = new Gson();
                                String[] output = gson.fromJson(arrObj , String[].class)*/
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //progressDialog.dismiss();
                            }
                            scheduleList.add(model);
                        }
                        adapter = new RecyclerAdapter(MainActivity.this, scheduleList);

                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                        // progressDialog.dismiss();
                        else
                        {
                          error.setVisibility(View.VISIBLE);
                          error.setText("No meeting Scheduled");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs

                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue

        requestQueue.add(stringRequest);

    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_prev) {
            //process your onClick here
            return true;
        }
        if (id == R.id.action_date) {
            //process your onClick here
            return true;
        }
        if (id == R.id.action_next) {
            //process your onClick here
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
