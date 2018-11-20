package audu.app.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import audu.app.R;
import audu.app.models.Categoria_Class;
import audu.app.utils.IViewHolderClick;

public class preferenciasAdapter extends RecyclerView.Adapter<preferenciasAdapter.ViewHolder> {


    private Context _context;
    private ArrayList<Categoria_Class> _items;
    private IViewHolderClick _listener;


    public preferenciasAdapter( Context context, ArrayList<Categoria_Class> items, IViewHolderClick listener )
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
    public preferenciasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType )
    {
        View view = LayoutInflater.from( _context ).inflate( R.layout.item_preferencias, parent, false );
        preferenciasAdapter.ViewHolder viewHolder = new preferenciasAdapter.ViewHolder( view, new IViewHolderClick()
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
    public void onBindViewHolder(preferenciasAdapter.ViewHolder holder, int position )
    {
        Categoria_Class item = _items.get( position );

        holder.get_desc().setText(item.get_name());
        holder.set_index(position);

        if (item.is_selected())
        {
            holder.get_iconView() .setVisibility(View.VISIBLE);

        }

        else
        {
            holder.get_iconView().setVisibility(View.GONE);

        }

    }




    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView _iconView;
        private int _index;
        private IViewHolderClick _listener;
        private TextView _desc;


        public ImageView get_iconView() {
            return _iconView;
        }

        public void set_iconView(ImageView _iconView) {
            this._iconView = _iconView;
        }

        public int get_index() {
            return _index;
        }

        public void set_index(int _index) {
            this._index = _index;
        }

        public IViewHolderClick get_listener() {
            return _listener;
        }

        public void set_listener(IViewHolderClick _listener) {
            this._listener = _listener;
        }

        public TextView get_desc() {
            return _desc;
        }

        public void set_desc(TextView _desc) {
            this._desc = _desc;
        }

        public ViewHolder(View view, IViewHolderClick listener )
        {
            super( view );

            view.setOnClickListener( this );
            _iconView = (ImageView) view.findViewById( R.id.item_preferencias_check);
             _desc = (TextView) view.findViewById(R.id.item_preferencias_desc);
             _listener = listener;
        }



        @Override
        public void onClick(View v)
        {
            if( _listener != null )
                _listener.onClick( _index );
        }

    }

    }
