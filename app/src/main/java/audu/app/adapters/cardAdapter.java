package audu.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import audu.app.R;
import audu.app.models.General_class;
import audu.app.models.card_info;
import audu.app.utils.IViewHolderClick;

public class cardAdapter extends RecyclerView.Adapter<cardAdapter.ViewHolder>
{
    private Context _context;
    private ArrayList<card_info> _items;
    private IViewHolderClick _listener;

    public cardAdapter( Context context, ArrayList<card_info> items, IViewHolderClick listener )
    {
        _context = context;
        _items = items;
        _listener = listener;
    }



    @Override
    public int getItemCount()
    {
        return _items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType )
    {
        View view = LayoutInflater.from( _context ).inflate( R.layout.item_card, parent, false );
        ViewHolder viewHolder = new ViewHolder( view, new IViewHolderClick()
        {
            @Override
            public void onClick( int position )
            {
                if( _listener != null )
                    _listener.onClick( position );
            }
        } );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position )
    {
        card_info item = _items.get( position );

        holder.getDescView().setText(item.get_card_number());
        holder.setIndex( position );
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private int _index;
        private IViewHolderClick _listener;
        private TextView _desc;

        public ViewHolder( View view, IViewHolderClick listener )
        {
            super( view );

            view.setOnClickListener( this );
            _desc = (TextView) view.findViewById(R.id.item_card_desc);
            _listener = listener;
        }

        public TextView getDescView() {return  _desc;}
        public int getIndex()
        {
            return _index;
        }
        public void setIndex( int index )
        {
            _index = index;
        }

        @Override
        public void onClick(View v)
        {
            if( _listener != null )
                _listener.onClick( _index );
        }
    }


}
