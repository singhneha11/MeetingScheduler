package meetingscheduler.com.companymeetingscheduler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private List<Model> scheduleList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView startTime,endTime, description, participants;

        public MyViewHolder(View view) {
            super(view);
            startTime = (TextView) view.findViewById(R.id.tv_startTime);
            endTime = (TextView) view.findViewById(R.id.tv_endTime);
            description = (TextView) view.findViewById(R.id.tv_description);
            participants = (TextView) view.findViewById(R.id.tv_participants);
        }
    }







    public RecyclerAdapter(Context context,List<Model> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Model model = scheduleList.get(position);
        holder.startTime.setText(model.getStart_time() +" - "+model.getEnd_time());
      //  holder.endTime.setText("End_Time : "+model.getEnd_time());
        holder.description.setText(model.getDescription());
      /* String participantArray=model.getParticipants();
       participantArray.replace("[","");
       participantArray.replace("]","")*/
        //holder.participants.setText("Participants : "+model.getParticipants());

    }



    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

}
