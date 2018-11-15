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
import audu.app.utils.IViewHolderClick;

public class optionsAdapter extends RecyclerView.Adapter<optionsAdapter.ViewHolder>
{
    private Context _context;
    private ArrayList<General_class> _items;
    private IViewHolderClick _listener;

    public optionsAdapter( Context context, ArrayList<General_class> items, IViewHolderClick listener )
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
        View view = LayoutInflater.from( _context ).inflate( R.layout.item_options, parent, false );
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
        General_class item = _items.get( position );



        holder.getIconView().setImageResource( item.get_res_id() );
        holder.getDescView().setText(item.get_name());
        holder.setIndex( position );
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView _iconView;
        private int _index;
        private IViewHolderClick _listener;
        private TextView _desc;

        public ViewHolder( View view, IViewHolderClick listener )
        {
            super( view );

            view.setOnClickListener( this );
            _iconView = (ImageView) view.findViewById( R.id.item_options_img );
            _desc = (TextView) view.findViewById(R.id.item_options_desc);
            _listener = listener;
        }

        public ImageView getIconView()
        {
            return _iconView;
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
