package audu.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import audu.app.R;
import audu.app.common;
import audu.app.models.Capitulo_Class;
import audu.app.utils.IViewHolderClick;

public class capituloAdapter extends RecyclerView.Adapter<capituloAdapter.ViewHolder>{

    private Context _context;
    private ArrayList<Capitulo_Class> _items;
    private IViewHolderClick _listener;

    public capituloAdapter( Context context, ArrayList<Capitulo_Class> items, IViewHolderClick listener )
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
        View view = LayoutInflater.from( _context ).inflate( R.layout.item_capitulo, parent, false );
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
    public void onBindViewHolder( ViewHolder holder, int position ) {
        Capitulo_Class item = _items.get(position);

        holder.setIndex( position );

        holder.getNombreCapitulo().setText(item.get_nombreCapitulo());
        holder.getSubtituloCapitulo().setText(item.get_subtitulo());
        //holder.getDuracionCapitulo().setText(item.ge);



    }







    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView _iconView;
        private int _index;
        private IViewHolderClick _listener;
        private View _parent;

        private TextView nombreCapitulo;

        public TextView getNombreCapitulo() {
            return nombreCapitulo;
        }

        public void setNombreCapitulo(TextView nombreCapitulo) {
            this.nombreCapitulo = nombreCapitulo;
        }

        public TextView getSubtituloCapitulo() {
            return subtituloCapitulo;
        }

        public void setSubtituloCapitulo(TextView subtituloCapitulo) {
            this.subtituloCapitulo = subtituloCapitulo;
        }

        public TextView getDuracionCapitulo() {
            return duracionCapitulo;
        }

        public void setDuracionCapitulo(TextView duracionCapitulo) {
            this.duracionCapitulo = duracionCapitulo;
        }

        public ImageButton getPlayCapitulo() {
            return playCapitulo;
        }

        public void setPlayCapitulo(ImageButton playCapitulo) {
            this.playCapitulo = playCapitulo;
        }

        private TextView subtituloCapitulo;
        private TextView duracionCapitulo;
        private ImageButton playCapitulo;


        public ViewHolder( View view, IViewHolderClick listener )
        {
            super( view );

            _parent = view;
            view.setOnClickListener( this );
            _iconView = (ImageView) view.findViewById( R.id.imgIcon );
            _listener = listener;
            subtituloCapitulo = view.findViewById(R.id.rowSubtitulo);
            nombreCapitulo = view.findViewById(R.id.rowCapitulo);
        }

        public ImageView getIconView()
        {
            return _iconView;
        }


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