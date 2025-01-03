package com.example.firebaseauth;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
public class RecyclerAdapterr extends FirebaseRecyclerAdapter<DataModel,RecyclerAdapterr.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecyclerAdapterr(@NonNull FirebaseRecyclerOptions<DataModel> options) {
        super(options);
    }

    //item manage by recyclerView data
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull DataModel model) {
        holder.name.setText(model.getName());
        holder.mobile.setText(model.getMobile());

        //update data from firebasedatabase real time by clicking update button
        holder.updt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(v.getContext());
                dialog.setContentView(R.layout.add_item);
                Button button;
                TextView textView;
                TextInputEditText nameee,mobileee;
                textView=dialog.findViewById(R.id.text);
                textView.setText("UPDATE DETAILS");
                nameee=dialog.findViewById(R.id.name);
                mobileee=dialog.findViewById(R.id.mobile);
                button=dialog.findViewById(R.id.add);
                button.setText("UPDATE");
                nameee.setText(model.getName());
                mobileee.setText(model.getMobile());
                //sucessfully updated
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map=new HashMap<>();
                        map.put("name",nameee.getText().toString());
                        map.put("mobile",mobileee.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("task").child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.name.getContext(), "sucess",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.name.getContext(), "error"+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        //delete data by clicking delete button and setup with alertdialog
        holder.delt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a=new AlertDialog.Builder(holder.name.getContext());
                a.setTitle("Are you sure - ");
                a.setMessage("Deleted data can't be Undo!");
                a.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("task").child(getRef(position).getKey())
                                .removeValue();
                        Toast.makeText(holder.name.getContext(), "Removed", Toast.LENGTH_SHORT).show();
                    }
                });
                a.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.name.getContext(), "Canclled", Toast.LENGTH_SHORT).show();
                    }
                });
                a.show();
            }
        });
    }

    //recyclerView data layout setup in recyclerView
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewitem,parent,false);
        ViewHolder v=new ViewHolder(view);
        return v;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,mobile;
        Button updt,delt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            mobile=itemView.findViewById(R.id.mobile);
            updt=itemView.findViewById(R.id.updatebtn);
            delt=itemView.findViewById(R.id.deletebtn);
        }
    }
}
