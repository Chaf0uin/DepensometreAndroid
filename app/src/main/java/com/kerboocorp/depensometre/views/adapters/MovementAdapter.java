package com.kerboocorp.depensometre.views.adapters;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.model.entities.Movement;

public class MovementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;

    private SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private DecimalFormat df = new DecimalFormat("##.00");
    private List<Movement> movementList;
    private Context context;

    public MovementAdapter(Context context) {
        this.movementList = new ArrayList<Movement>();
        this.context = context;
    }

    public List<Movement> getMovementList() {
        return movementList;
    }

    public void addMovementList(List<Movement> movementList) {
        this.movementList.addAll(movementList);
        notifyDataSetChanged();
    }

    public void addMovement(Movement movement) {
        this.movementList.add(0, movement);
        notifyDataSetChanged();
    }

    public void updateMovement(Movement movement) {
        int pos = -1;
        for (Movement m : movementList) {
            if (m.getId().equals(movement.getId())) {
                pos = movementList.indexOf(m);
            }
        }

        if (pos != -1) {
            movementList.set(pos, movement);
            notifyDataSetChanged();
        }

    }

    public void clearMovementList() {
        movementList.clear();
        notifyDataSetChanged();
    }

    public double getTotal() {
        double total = 0;
        for (Movement movement : movementList) {
            if (movement.getMovementType()) {
                total += Double.parseDouble(movement.getAmount());
            } else {
                total -= Double.parseDouble(movement.getAmount());
            }
        }
        return total;
    }

    public void removeMovement(Movement movement) {
        movementList.remove(movement);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    	
    	View v = null;
    	
    	if (viewType == TYPE_ITEM) {
    		v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_card_movement, viewGroup, false);
    		return new MovementViewHolder(v);
        }
    	
    	throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
    	
    	if (viewHolder instanceof MovementViewHolder) {
    		
    		MovementViewHolder movementViewHolder = (MovementViewHolder) viewHolder;
    		Movement movement = movementList.get(i);

    		//movementViewHolder.date.setText(fullDateFormat.format(movement.getDate()));
    		movementViewHolder.name.setText(movement.getName());
    		
    		if (movement.getMovementType()) {
    			//movementViewHolder.amount.setText("+" + df.format(movement.getAmount())  + " €");
    			movementViewHolder.amount.setText("-" + movement.getAmount()  + " €");
    			movementViewHolder.amount.setTextColor(context.getResources().getColor(R.color.red));
    		} else {
    			//movementViewHolder.amount.setText("-" + df.format(movement.getAmount())  + " €");
    			movementViewHolder.amount.setText("+" + movement.getAmount()  + " €");
    			movementViewHolder.amount.setTextColor(context.getResources().getColor(R.color.green));
    		}
    		

    		movementViewHolder.context = context;
    		movementViewHolder.movement = movement;
            movementViewHolder.adapter = this;
    	}	

    }

    @Override
    public int getItemCount() {
        return (movementList == null ? 0 : movementList.size());
    }
    
    @Override
    public int getItemViewType(int position) {
        if (position == (movementList.size()))
            return TYPE_FOOTER;

        return TYPE_ITEM;
    }

    public static class MovementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {
        public TextView date;
        public TextView name;
        public TextView amount;
        public Context context;
        public Movement movement;
        public MovementAdapter adapter;
        //private DeleteMovementTask deleteMovementTask;

        public MovementViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            date.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.name);
            name.setOnClickListener(this);
            amount = (TextView) itemView.findViewById(R.id.amount);
            amount.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(context, MovementActivity.class);
//            intent.putExtra("movement", (Parcelable) movement);
//            intent.putExtra("action", "update");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            ((Activity)context).startActivityForResult(intent, 1);
        }

        @Override
        public boolean onLongClick(View v) {
            //adapter.removeMovement(movement);
            //deleteMovementTask = new DeleteMovementTask();
            //deleteMovementTask.setId(movement.getId());
            //deleteMovementTask.execute();
            return true;
        }
    }
    
    public static class HeaderListViewHolder extends RecyclerView.ViewHolder {

        public HeaderListViewHolder(View itemView) {
            super(itemView);
        }
    }
}