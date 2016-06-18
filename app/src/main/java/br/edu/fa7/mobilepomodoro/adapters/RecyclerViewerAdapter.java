package br.edu.fa7.mobilepomodoro.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import br.edu.fa7.mobilepomodoro.MainActivity;
import br.edu.fa7.mobilepomodoro.R;
import br.edu.fa7.mobilepomodoro.activity.NewTaskActivity;
import br.edu.fa7.mobilepomodoro.business.PomoTaskBusiness;
import br.edu.fa7.mobilepomodoro.model.PomoTask;
import br.edu.fa7.mobilepomodoro.repository.PomoTaskDao;
import br.edu.fa7.mobilepomodoro.service.BoundCountDownService;

/**
 * Created by erinaldo.souza on 07/06/2016.
 */
public class RecyclerViewerAdapter extends RecyclerView.Adapter<RecyclerViewerAdapter.RecyclerViewHolder> implements Serializable{

    private List<PomoTask> tasks;
    private LayoutInflater mLayoutInflater;
    private PomoTaskBusiness business;
    private Context context;
    private static boolean playing = false;

    public RecyclerViewerAdapter(List<PomoTask> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.business = new PomoTaskBusiness(new PomoTaskDao(context));
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.card_view_tasks, parent, false);
        return  new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        final PomoTask task = tasks.get(position);
        holder.taskName.setText("#" + task.getId() + " - " + task.getName());
        holder.taskDetail.setText(" " + task.getDetail() + "\n Pomodoros: " + task.getQuantity());
        task.setHolder(holder)
        ;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!playing || v.getId() != R.id.btnStartTask)) {
                    buttonClicked(v.getId(), position, task);
                } else {
                    Toast.makeText(context, "Já existe uma atividade em execução!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        holder.mBtnStartTask.setOnClickListener(listener);
        holder.mBtnEditTask.setOnClickListener(listener);
        holder.mBtnDeleteTask.setOnClickListener(listener);
        holder.mBtnConcluir.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    private void buttonClicked(int btnId, int position, PomoTask task) {
        Intent in;
        switch (btnId) {
           case R.id.btnStartTask :
                /*in = new Intent(context, MainActivity.class);
                in.putExtra("task", tasks.get(position));
                context.startActivity(in);*/

               MainActivity.startTask(tasks.get(position));
               playing = true;
               break;
            case R.id.btnEditTask :
                if(playing) {
                    Toast.makeText(context, "Conclua a tarefa corrente antes de editar ou cadastrar uma nova tarefa!", Toast.LENGTH_SHORT).show();
                } else {
                    in = new Intent(context, NewTaskActivity.class);
                    in.putExtra("task", tasks.get(position));
                    context.startActivity(in);
                }
                break;
            case R.id.btnDeleteTask:
                if(playing) {
                    Toast.makeText(context, "Conclua a tarefa corrente antes de excluir uma tarefa!", Toast.LENGTH_SHORT).show();
                } else {
                    business.delete(task.getId());
                    tasks.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeRemoved(position, tasks.size()/2);
                    Log.i("CardView", "Task id: " + task.getId() + " deleted!");
                    Toast.makeText(context, "Tarefa #" + task.getId() + " deletada com sucesso!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnConcluir:
                ((CardView)tasks.get(position).getHolder().itemView).setCardBackgroundColor(Color.GREEN);
                BoundCountDownService.stopTask();
                Toast.makeText(context, "Tarefa #" + task.getId() + " concluída com sucesso!", Toast.LENGTH_SHORT).show();
                playing = false;
                break;
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements Serializable {

        private TextView taskName;
        private TextView taskDetail;
        private ImageButton mBtnStartTask;
        private ImageButton mBtnEditTask;
        private ImageButton mBtnDeleteTask;
        private Button mBtnConcluir;
        public View itemView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            this.taskName = (TextView) itemView.findViewById(R.id.cardTxtTaskName);
            this.taskDetail = (TextView) itemView.findViewById(R.id.cardTxtTaskDetail);

            this.mBtnStartTask = (ImageButton) itemView.findViewById(R.id.btnStartTask);
            this.mBtnEditTask = (ImageButton) itemView.findViewById(R.id.btnEditTask);
            this.mBtnDeleteTask = (ImageButton) itemView.findViewById(R.id.btnDeleteTask);
            this.mBtnConcluir = (Button) itemView.findViewById(R.id.btnConcluir);

            this.itemView = itemView;
        }
    }

    public static boolean isPlaying() {
        return playing;
    }

    public static void setPlaying(boolean playing) {
        RecyclerViewerAdapter.playing = playing;
    }
}