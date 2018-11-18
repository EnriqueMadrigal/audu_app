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
import audu.app.models.General_class;
import audu.app.models.Pregunta_class;
import audu.app.utils.IViewHolderClick;

public class preguntasAdapter extends RecyclerView.Adapter<preguntasAdapter.ViewHolder>
{
    private Context _context;
    private ArrayList<Pregunta_class> _items;
    private IViewHolderClick _listener;


    public preguntasAdapter( Context context, ArrayList<Pregunta_class> items, IViewHolderClick listener )
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
    public preguntasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType )
    {
        View view = LayoutInflater.from( _context ).inflate( R.layout.item_preguntas, parent, false );
        preguntasAdapter.ViewHolder viewHolder = new preguntasAdapter.ViewHolder( view, new IViewHolderClick()
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
    public void onBindViewHolder(preguntasAdapter.ViewHolder holder, int position )
    {
        Pregunta_class item = _items.get( position );



        holder.getNameView().setText(item.get_name());
        holder.get_desc().setText(item.get_desc());

        holder.setIndex( position );

        Drawable drawable1 = _context.getResources().getDrawable(R.drawable.ico_faq_plus);
        Drawable drawable2 = _context.getResources().getDrawable(R.drawable.ico_faq_minus);

        if (item.is_expanded())
        {
            holder.getIconView().setImageDrawable(drawable2);
            holder.get_layout().setVisibility(View.VISIBLE);
        }

        else
        {
            holder.getIconView().setImageDrawable(drawable1);
            holder.get_layout().setVisibility(View.GONE);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView _iconView;
        private int _index;
        private IViewHolderClick _listener;
        private TextView _name;
        private TextView _desc;
        private LinearLayout _layout;

        public ViewHolder( View view, IViewHolderClick listener )
        {
            super( view );

            view.setOnClickListener( this );
            _iconView = (ImageView) view.findViewById( R.id.item_preguntas_img);
            _name = (TextView) view.findViewById(R.id.item_preguntas_name);
            _desc = (TextView) view.findViewById(R.id.item_preguntas_desc);
            _layout = (LinearLayout) view.findViewById(R.id.item_preguntas_layout);
            _listener = listener;
        }

        public ImageView getIconView()
        {
            return _iconView;
        }
        public TextView getNameView() {return  _name;}
        public int getIndex()
        {
            return _index;
        }
        public void setIndex( int index )
        {
            _index = index;
        }

        public TextView get_desc() {
            return _desc;
        }

        public LinearLayout get_layout() {
            return _layout;
        }

        @Override
        public void onClick(View v)
        {
            if( _listener != null )
                _listener.onClick( _index );
        }
    }

}
