package io.webguru.fieldpickup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.webguru.fieldpickup.DocketView;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.R;

/**
 * Created by yatin on 21/01/17.
 */

public class PendingDocketAdapter extends RecyclerView.Adapter<PendingDocketAdapter.ViewHolder> {

    ArrayList<Docket> dockets;
    Context context;
    public PendingDocketAdapter(ArrayList<Docket> dockets, Context context){
        this.dockets = dockets;
        this.context = context;
    }

    @Override
    public PendingDocketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listing_cardview, parent, false);
        return new PendingDocketAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PendingDocketAdapter.ViewHolder holder, int position) {
        final Docket docket = dockets.get(position);
        holder.mDocketNumber.setText(docket.getDocketNumber());
        holder.mContact.setText(docket.getCustoumerContact());
        holder.mLocation.setText(docket.getCustoumerAddress());
        holder.mContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + docket.getCustoumerContact()));
                context.startActivity(intent);
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(docket.isPending()){
                    Intent intent = new Intent(context, DocketView.class);
                    intent.putExtra("Docket", docket);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dockets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDocketNumber;
        public final TextView mContact;
        public final ImageButton mContactButton;
        public final TextView mLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mDocketNumber = (TextView) itemView.findViewById(R.id.docket_number);
            mContact = (TextView) itemView.findViewById(R.id.contact_number);
            mContactButton = (ImageButton) itemView.findViewById(R.id.call_user_button);
            mLocation = (TextView) itemView.findViewById(R.id.docket_location);
        }
    }
}
