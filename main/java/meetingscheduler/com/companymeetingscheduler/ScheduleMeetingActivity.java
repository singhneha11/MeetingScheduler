package meetingscheduler.com.companymeetingscheduler;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ScheduleMeetingActivity extends AppCompatActivity {
    DatePicker picker;
    private DatePickerDialog mDatePickerDialog;
  //  TimePickerDialog timepicker;
    Button displayDate,btn_back;
    TextView textview1,tv_description;
    EditText et_startTime,et_endTime,et_Date;
    private String startTime,endTime;
    Date date1,date2,time1,time2;
    private String    myString;
    long elapsed;
    public static final String TAG="ScheduleMeetingActivity";
    boolean Validate= false;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
   private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_meeting);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

            textview1=(TextView)findViewById(R.id.textView1);
            picker=(DatePicker)findViewById(R.id.datePicker);
            displayDate=(Button)findViewById(R.id.button1);
            et_endTime=(EditText)findViewById(R.id.et_EndTime);
            et_startTime=(EditText)findViewById(R.id.et_StartTime);
            et_Date=(EditText)findViewById(R.id.et_Date);
            btn_back=(Button)findViewById(R.id.btn_back) ;
            tv_description=(TextView)findViewById(R.id.tv_description);
            et_Date.setText(MainActivity.inputDate);
tv_description.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        tv_description.setFocusable(true);
        myString = tv_description.getText().toString();

    }
});


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }

        };

        et_Date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] parts = MainActivity.inputDate.split("/");
                int month = Integer.parseInt(parts[1]);
                Log.d(TAG,"month :"+month);
                int day = Integer.parseInt(parts[0]);
                Log.d(TAG,"day :"+day);
                int yearT = Integer.parseInt(parts[2]);
                Log.d(TAG,"year :"+yearT);
                // TODO Auto-generated method stub
               /* new DatePickerDialog(ScheduleMeetingActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/
                new DatePickerDialog(ScheduleMeetingActivity.this, date,yearT,month-1,day).show();

            }
        });



       btn_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i2=new Intent(ScheduleMeetingActivity.this,MainActivity.class);
               startActivity(i2);
           }
       });

    displayDate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            textview1.setText("Change Date: " + getCurrentDate());

            try {
                date1 = format.parse(MainActivity.inputDate);
                date2 = format.parse(getCurrentDate());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if ((startTime == null || startTime.isEmpty()) && (endTime == null || endTime.isEmpty())) {
                Toast.makeText(ScheduleMeetingActivity.this, "Select StartTime and EndTime", Toast.LENGTH_LONG).show();
            }

            if (time1 != null && time2 != null) {
            elapsed = time2.getTime() - time1.getTime();
        }

            if ((date1.before(new Date()) || date2.before(new Date()))&& elapsed<=0) {
                AlertDialog alertDialog = new AlertDialog.Builder(ScheduleMeetingActivity.this).create(); //Read Update
                alertDialog.setTitle("Error ");
                alertDialog.setMessage("Meeting cannot be scheduled for the dates before Today's date and time");

                alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                    }
                });

                alertDialog.show();  //<-- See This!
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(ScheduleMeetingActivity.this).create(); //Read Update
                alertDialog.setTitle("Meeting Confirmation ");
                alertDialog.setMessage("Thanks for scheduling the meeting\n Meeting scheduled for Date-" + MainActivity.inputDate + "\n StartTime-" + startTime + "\n EndTime-" + endTime);

                alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                    }
                });

                alertDialog.show();  //<-- See This!
            }
        }


    });




        et_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_startTime.setFocusable(true);
               // et_startTime.setEnabled(true);
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ScheduleMeetingActivity.this, android.R.style.Theme_Holo_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_startTime.setText("Start Time :"+selectedHour + ":" + selectedMinute);
                        startTime=Integer.toString(selectedHour)+":"+Integer.toString(selectedMinute);
                        try {

                            time1=sdf.parse(startTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, hour, minute,true);//Yes 24 hour time

              //  TimePickerDialog timePickerDialog = new TimePickerDialog(ScheduleMeetingActivity.this, android.R.style.Theme_Holo_Light_Dialog, onTimeSetListener, hour, minute, is24Hour);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        et_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_endTime.setFocusable(true);
              //  et_endTime.setEnabled(true);
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ScheduleMeetingActivity.this,android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_endTime.setText("End Time :"+selectedHour + ":" + selectedMinute);
                        endTime=Integer.toString(selectedHour)+":"+Integer.toString(selectedMinute);
                        try {

                            time2=sdf.parse(endTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });



        }
        public String getCurrentDate(){
            StringBuilder builder=new StringBuilder();;
          //month is 0 based
            builder.append(picker.getDayOfMonth()+"/");
            builder.append((picker.getMonth() + 1)+"/");
            builder.append(picker.getYear());
            return builder.toString();
        }

    private void updateLabel() {
       // String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdfnew = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        et_Date.setText(sdfnew.format(myCalendar.getTime()));
    }

    }
